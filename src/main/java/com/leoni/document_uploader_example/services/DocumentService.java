package com.leoni.document_uploader_example.services;

import com.leoni.document_uploader_example.entities.models.Document;
import org.springframework.stereotype.Component;

@Component
public interface DocumentService {
    Document create(Document document);
}
