package com.ozru.kinesisproducer.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.ozru.kinesisproducer.kinesis.DataProducer;
import com.ozru.kinesisproducer.model.OzruMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class DataUploadController {

    private final DataProducer dataProducer;

    public DataUploadController(DataProducer dataProducer) {
        this.dataProducer = dataProducer;
    }

    @PostMapping(value = "/uploadData")
    ResponseEntity<String> uploadData(@RequestBody OzruMessage ozruMessage) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String data1 = mapper.writeValueAsString(ozruMessage);
            dataProducer.putIntoKinesis( "payload1", data1);
            return ResponseEntity.ok("data uploaded");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed " + e.getMessage());
        }
    }

}
