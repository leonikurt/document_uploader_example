package com.leoni.document_uploader_example.entities.repositories;

import com.leoni.document_uploader_example.entities.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
}