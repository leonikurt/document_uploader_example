package com.leoni.document_uploader_example.controllers;

import com.leoni.document_uploader_example.entities.factories.DocumentFactory;
import com.leoni.document_uploader_example.entities.models.Document;
import com.leoni.document_uploader_example.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;

@RestController
@RequestMapping({"/document"})
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @Autowired
    DocumentFactory documentFactory;

    @Value("${root.path}")
    private String root_path;

    @PostMapping("")
    public ResponseEntity<Response<Document>> createOrUpdate(@RequestParam MultipartFile file) {

        System.out.println(file.getOriginalFilename().split("."));




            //return ResponseEntity.status(401).body(null);//"Você não possui essa autorização"




        return ResponseEntity.ok(null);
    }
}
