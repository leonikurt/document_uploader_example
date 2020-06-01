package com.leoni.document_uploader_example.services;

import com.leoni.document_uploader_example.entities.models.Document;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public interface DocumentService {
    Document create(Document document);
    List<Document> filterType(String type, String initialDateString, String finalDateString);
}
