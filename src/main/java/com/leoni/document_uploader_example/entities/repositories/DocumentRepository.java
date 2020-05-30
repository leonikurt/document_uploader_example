package com.leoni.document_uploader_example.entities.repositories;

import com.leoni.document_uploader_example.entities.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}