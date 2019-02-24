package br.com.mouseweb.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.mouseweb.domain.Cidade;
import br.com.mouseweb.domain.Cliente;
import br.com.mouseweb.domain.Endereco;
import br.com.mouseweb.domain.enums.Perfil;
import br.com.mouseweb.domain.enums.TipoCliente;
import br.com.mouseweb.dto.ClienteDTO;
import br.com.mouseweb.dto.ClienteNewDTO;
import br.com.mouseweb.repositories.ClienteRepository;
import br.com.mouseweb.repositories.EnderecoRepository;
import br.com.mouseweb.security.UserSS;
import br.com.mouseweb.services.exceptions.AuthorizationException;
import br.com.mouseweb.services.exceptions.DataIntegrityException;
import br.com.mouseweb.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {
	
	@Autowired
	private BCryptPasswordEncoder pe;

	@Autowired
	private ClienteRepository repo;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	@Value("${img.prefix.client.profile}")
	private String prefix;

	public Cliente find(Integer id) {
		
		// Verifica se o usuario logado é do perfil de admin e não for o cliente do id solicitado, lança uma Exceção. 
		UserSS user = UserService.authenticated();
		
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	

	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;

	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir por que há pedidos relacionadas");
		}
		
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	// Paginação de categoria
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy); 
		return repo.findAll(pageRequest);
	}
	
	// Busca todos os clientes sem os dados
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	// Busca todos os clientes sem os dados, sobrecarga de método.
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), 
				TipoCliente.toEnum(objDto.getTipo()), pe.encode(objDto.getSenha()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), 
									objDto.getBairro(), objDto.getCep(), cli, cid);
		
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());

		if (objDto.getTelefone2()!=null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		
		if (objDto.getTelefone3()!=null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
		
	}
	
	// Pega os dados no banco e Faz update com novos dados da instaciação.
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	// Método para upload de imagem do perfil do cliente
	// grava o Link da imagem do usuario que esta logado no banco de dados.
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		UserSS user = UserService.authenticated();
		if (user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		
		// monta o nome padrão do arquivo = prefix + cód do use + extensão JPG
		String fileName = prefix + user.getId() + ".jpg";
		
		// Obj URI recebe a chamada do método UploadFile
		URI uri =  s3Service.uploadFile(multipartFile);
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
		
		/*// Salva a URI = Link da imagem no banco de dados do cliente que esta logado.
		Cliente cli = find(user.getId());
		cli.setImageUrl(uri.toString());
		repo.save(cli);
		
		return uri;*/
	}

}
