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
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping({"/document"})
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @Autowired
    DocumentFactory documentFactory;

    @Value("${root.path}")
    private String rootPath;

    @PostMapping("")
    public ResponseEntity<String> createOrUpdate(@RequestParam MultipartFile file) {

        String fileExtension = MimeTypesHelper.getDefaultExt(file.getContentType());
        if(!fileExtension.equalsIgnoreCase("pdf")){
            return ResponseEntity.status(406).body("Extension " + fileExtension + " not supported.");
        }

        return ResponseEntity.ok(null);
    }
}
