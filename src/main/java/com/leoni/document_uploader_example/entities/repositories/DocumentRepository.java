package com.leoni.document_uploader_example.entities.repositories;

import com.leoni.document_uploader_example.entities.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query(value="SELECT * FROM document where (TYPE = :type OR :type is NULL) " +
            "AND (DATE_CREATED >= :initialDate OR :initialDate is NULL) " +
            "AND (DATE_CREATED <= :finalDate OR :finalDate is NULL)", nativeQuery=true)
    List<Document> findFile(@Param("type") String type, @Param("initialDate")Date initialDate, @Param("finalDate")Date finalDate);
}