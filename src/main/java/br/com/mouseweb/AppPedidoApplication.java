package br.com.mouseweb;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//import br.com.mouseweb.services.S3Service;

@SpringBootApplication
public class AppPedidoApplication implements CommandLineRunner{

	/*@Autowired
	private S3Service s3Service;*/
	
	public static void main(String[] args) {
		SpringApplication.run(AppPedidoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//s3Service.uploadFile("C:\\Douglas\\Spring_Boot_APP_PEDIDOS\\git_Heroku.png");

	}

}
