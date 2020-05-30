package com.leoni.document_uploader_example.services.implementation;

import com.leoni.document_uploader_example.entities.models.Document;
import com.leoni.document_uploader_example.entities.repositories.DocumentRepository;
import com.leoni.document_uploader_example.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;

public class DocumentServiceImplementation implements DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    @Override
    public Document create(Document document) {
        return documentRepository.save(document);
    }
}
