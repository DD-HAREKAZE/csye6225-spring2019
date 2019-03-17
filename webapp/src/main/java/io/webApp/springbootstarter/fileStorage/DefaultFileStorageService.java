package io.webApp.springbootstarter.fileStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import io.webApp.springbootstarter.exception.FileStorageException;
import io.webApp.springbootstarter.exception.MyFileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Profile("default")
public class DefaultFileStorageService implements FileStorageService {

	private final Path fileStorageLocation;

	@Autowired
	public DefaultFileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

		try {
			Files.createDirectories(this.fileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Creation of the directory is not possible",
					ex);
		}
	}

	public String storeFile(MultipartFile file) {
		
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		try {
			
			if (fileName.contains("..")) {
				throw new FileStorageException(" Please try again as the file name includes invalid path " + fileName);
			}


			Path targetLocation = this.fileStorageLocation.resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return fileName;
		} catch (IOException ex) {
			throw new FileStorageException(" Storage of the file is not possible " + fileName + " . ", ex);
		}
	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}
	
	public boolean DeleteFile(String file) {
		
		Path targetLocation = Paths.get(file);
		try {
			return Files.deleteIfExists(targetLocation);
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public Path getFileStorageLocation() {
		return fileStorageLocation;
	}

	
}
