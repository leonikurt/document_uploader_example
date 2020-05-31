package com.leoni.document_uploader_example.entities.factories;

import com.leoni.document_uploader_example.entities.models.Document;
import com.leoni.document_uploader_example.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DocumentFactory {
    @Autowired
    DocumentService documentService;

    public Document create(MultipartFile file, String fileExtension, String rootPath){
        Document  document = new Document();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        document.setName(file.getOriginalFilename());
        document.setExtension(fileExtension);
        document.setPath(rootPath + "/" + file.getOriginalFilename());
        document.setType("CNH");

        try {
            document.setDate_created(formatter.parse(formatter.format(new Date())));
        } catch (ParseException e) {
            document.setDate_created(new Date());
        }

        return documentService.create(document);
    }
}
