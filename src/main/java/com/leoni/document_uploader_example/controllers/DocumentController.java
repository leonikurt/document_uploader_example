package com.leoni.document_uploader_example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leoni.document_uploader_example.entities.factories.DocumentFactory;
import com.leoni.document_uploader_example.entities.models.Document;
import com.leoni.document_uploader_example.entities.models.requests.FilterRequest;
import com.leoni.document_uploader_example.services.DocumentService;
import com.leoni.document_uploader_example.utils.MimeTypesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping({"/document"})
public class DocumentController {

    @Autowired
    DocumentFactory documentFactory;

    @Autowired
    DocumentService documentService;

    @Value("${root.path}")
    private String rootPath;

    @RequestMapping(value = "/filter", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> filter(@RequestBody (required=false)FilterRequest filterRequest){
        List<Document> documentList;

        documentList = this.documentService.filterType(filterRequest.getType(), filterRequest.getInitialDate(),
                filterRequest.getFinalDate());

        if(documentList == null){
            return ResponseEntity.status(500).body("Initial Date can't be an after date than Final Date");
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonString;

        try {
            jsonString = mapper.writeValueAsString(documentList);
        }catch (JsonProcessingException e){
            return ResponseEntity.status(500).body(e.getMessage());
        }

        return ResponseEntity.ok(jsonString);
    }

    @PostMapping("")
    public ResponseEntity<String> createFile(@RequestParam MultipartFile file) throws Exception{

        String fileExtension = MimeTypesHelper.getDefaultExt(file.getContentType());

        if(!fileExtension.equalsIgnoreCase("pdf") && !fileExtension.equalsIgnoreCase("png") &&
                !fileExtension.equalsIgnoreCase("tiff")){
            return ResponseEntity.status(406).body("Extension " + fileExtension + " not supported.");
        }

        String classification = fileClassification(file.getOriginalFilename());

        if(classification == ""){
            return ResponseEntity.status(406).body("File not supported. Only CNH, RG or CR are allowed");
        }

        String fileName = doUpload(this.rootPath, classification, file, fileExtension);

        this.documentFactory.create(fileName, fileExtension, this.rootPath, classification);

        return ResponseEntity.ok("Upload successful!");
    }

    public static String fileClassification(String fileName){

        if(fileName.toLowerCase().contains("cnh")){
            return "CNH";
        }else if(fileName.toLowerCase().contains("cr")){
            return "CR";
        }else if(fileName.toLowerCase().contains("rg")){
            return "RG";
        }else{
            return "";
        }

    }

    public static String doUpload(String rootPath, String directory, MultipartFile file, String fileExtension) throws Exception {

        Path directoryPath = Paths.get(rootPath, directory);

        int count = 0;

        String fileName = "imagem_"+directory.toLowerCase()+"_"+String.format("%03d", count)+"."+fileExtension;

        Path filePath = directoryPath.resolve("imagem_"+directory.toLowerCase()+"_"+String.format("%03d", count)+"."+fileExtension);

        try{

            while(true){
                if(!Files.exists(filePath)){
                    break;
                }

                count++;
                filePath = directoryPath.resolve("imagem_"+directory.toLowerCase()+"_"+String.format("%03d", count)+"."+fileExtension);
            }

            Files.createDirectories(directoryPath);
            file.transferTo(filePath.toFile());

        }catch(IOException e) {

            throw new Exception("Failed to upload file, try again later");

        }

        return "imagem_"+directory.toLowerCase()+"_"+String.format("%03d", count)+"."+fileExtension;
    }
}
