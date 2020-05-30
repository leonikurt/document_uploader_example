package com.leoni.document_uploader_example.controllers;

import com.leoni.document_uploader_example.entities.factories.DocumentFactory;
import com.leoni.document_uploader_example.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/document"})
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @Autowired
    DocumentFactory documentFactory;
}
