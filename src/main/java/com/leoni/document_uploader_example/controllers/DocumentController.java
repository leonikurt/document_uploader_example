package com.leoni.document_uploader_example.controllers;

import com.leoni.document_uploader_example.entities.factories.DocumentFactory;
import com.leoni.document_uploader_example.entities.models.Document;
import com.leoni.document_uploader_example.services.DocumentService;
import com.leoni.document_uploader_example.utils.MimeTypesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping({"/document"})
public class DocumentController {

    @Autowired
    DocumentFactory documentFactory;

    @Value("${root.path}")
    private String rootPath;

    @PostMapping("")
    public ResponseEntity<String> createOrUpdate(@RequestParam MultipartFile file) throws Exception{

        String fileExtension = MimeTypesHelper.getDefaultExt(file.getContentType());

        if(!fileExtension.equalsIgnoreCase("pdf") && !fileExtension.equalsIgnoreCase("png") &&
                !fileExtension.equalsIgnoreCase("tiff")){
            return ResponseEntity.status(406).body("Extension " + fileExtension + " not supported.");
        }

        doUpload(this.rootPath, "CNH", file, fileExtension);

        Document document = this.documentFactory.create(file, fileExtension, this.rootPath);

        return ResponseEntity.ok("Upload successful!");
    }

    public static String doUpload(String raiz, String diretorio, MultipartFile file, String extension) throws Exception {

        Path diretorioPath = Paths.get(raiz, diretorio);

        Path arquivoPath = diretorioPath.resolve(file.getOriginalFilename());

        try{

            Files.createDirectories(diretorioPath);
            file.transferTo(arquivoPath.toFile());

        }catch(IOException e) {

            throw new Exception("Failed to upload file, try again later");

        }

        return file.getOriginalFilename();
    }
}
