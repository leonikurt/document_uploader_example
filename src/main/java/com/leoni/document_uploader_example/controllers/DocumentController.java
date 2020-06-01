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

        doUpload(this.rootPath, "CNH", file);

        this.documentFactory.create(file, fileExtension, this.rootPath);

        return ResponseEntity.ok("Upload successful!");
    }

    public static String doUpload(String rootPath, String directory, MultipartFile file) throws Exception {

        Path diretorioPath = Paths.get(rootPath, directory);

        Path arquivoPath = diretorioPath.resolve(file.getOriginalFilename());

        try{

            Files.createDirectories(diretorioPath);
            file.transferTo(arquivoPath.toFile());

        }catch(IOException e) {

            throw new Exception("Failed to upload file, try again later");

        }

        return file.getOriginalFilename();
    }
}
