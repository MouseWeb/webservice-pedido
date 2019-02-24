package br.com.mouseweb.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.mouseweb.services.exceptions.FileException;

// Seriviço responsavel por fornecer funcionalidades de imagem. 
@Service
public class ImageService {

	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		String ext = FilenameUtils.getExtension(uploadedFile.getOriginalFilename());

		// Testa a extensão do arquivo, vai rejeita a requisição
		if (!"png".equals(ext) && !"jpg".equals(ext)) {
			throw new FileException("Somente imagens PNG e JPG são permitidas");
		}

		try {

			// Ler o Arquivo
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());

			// Se o arquivo for (PNG) converte para (JPG)
			if ("png".equals(ext)) {
				img = pngToJpg(img);
			}

			return img;

		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");

		}

	}

	// Função que converte o arquivo
	public BufferedImage pngToJpg(BufferedImage img) {
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);

		return jpgImage;

	}

	// Função = ela retorna o InputStream que é o objeto que encapsula a leitura
	// apartir de (BufferedImage).
	public InputStream getInputStream(BufferedImage img, String extension) {

		try {

			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);

			return new ByteArrayInputStream(os.toByteArray());

		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");

		}

	}

	// Função que recorta a imagem (CROPAR)
	public BufferedImage cropSquare(BufferedImage sourceImg) {
		// Pega o minumo altura ou largura. 
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth();
		// Recorta a imagem
		return Scalr.crop(
				sourceImg,
				// Passa as codernadas de tamanho de corte.
				(sourceImg.getWidth() / 2) - (min / 2),
				(sourceImg.getHeight() / 2) - (min / 2),
				// Recorta o minimo para a altura e largura.
				min,
				min);

	}

	//  Função para redimensionar uma imagem
	public BufferedImage resize(BufferedImage sourceImg, int size) {

		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);

	}

}
