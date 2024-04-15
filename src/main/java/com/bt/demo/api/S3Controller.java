package com.bt.demo.api;


import com.bt.demo.handler.BucketNotFoundException;
import com.bt.demo.handler.ObjectNotFoundException;
import com.bt.demo.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("/{bucketName}")
    public ResponseEntity<String> createBucket(@PathVariable String bucketName) throws BucketAlreadyExistsException {
        String bucket = s3Service.createBucket(bucketName);
        return new ResponseEntity<>(bucket, HttpStatus.CREATED);
    }

    @PostMapping("/delete/{bucketName}")
    public ResponseEntity<Void> deleteBucket(@PathVariable String bucketName) {
        s3Service.deleteBucket(bucketName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileOnBucket(@RequestParam String bucketName,@RequestParam MultipartFile file  ) throws IOException, BucketNotFoundException {
        String uploaded = s3Service.uploadFileOnS3(bucketName,file);
        return new ResponseEntity<>(uploaded,HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFileOnBucket(@RequestParam String bucketName, @RequestParam String fileName )  {
        byte[] data = s3Service.downloadFile(bucketName, fileName);
        ByteArrayResource resource=new ByteArrayResource(data);
         return ResponseEntity
                .ok()
                .contentLength(data.length)
                .header("Content-type", "application/octet-stream")
                .header("Content-disposition", "attachment; filename=\"" + fileName + "\"")
                .body(resource);
    }

    @DeleteMapping("/delete/file")
    public ResponseEntity<String> uploadFileOnBucket(@RequestParam String bucketName,@RequestParam String fileName  ) throws ObjectNotFoundException {
        String result = s3Service.deleteFileFromS3Bucket(bucketName, fileName);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

}
