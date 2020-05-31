package com.leoni.document_uploader_example.services.implementation;

import com.leoni.document_uploader_example.entities.models.Document;
import com.leoni.document_uploader_example.entities.repositories.DocumentRepository;
import com.leoni.document_uploader_example.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class DocumentServiceImplementation implements DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    @Override
    public Document create(Document document) {
        return this.documentRepository.save(document);
    }

    @Override
    public List<Document> filterType(String type, String initialDateString, String finalDateString) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Date initialDate = null;
        Date finalDate = null;

        if(initialDateString != null){
            try {
                initialDate = formatter.parse(initialDateString);
            } catch (ParseException e) {
            }
        }

        if(finalDateString != null){
            try {
                finalDate = formatter.parse(finalDateString);
            } catch (ParseException e) {
            }
        }

        if(initialDate != null && finalDate != null && initialDate.compareTo(finalDate) > 0 ){
            return null;
        }

        return this.documentRepository.findFile(type, initialDate, finalDate);
    }
}
