package com.bt.demo.service;

import com.bt.demo.handler.BucketNotFoundException;
import com.bt.demo.handler.ObjectNotFoundException;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;
import software.amazon.awssdk.utils.IoUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

    private final S3Template s3Template;


    public String createBucket(String bucketName) {
        if (isBucketExists(bucketName)) {
            throw BucketAlreadyExistsException.builder().message("bucket already exist " + bucketName).build();
        } else {
            String bucket = s3Template.createBucket(bucketName);
            log.info("bucket uri " + bucket);
            return "bucket created name of bucket is: " + bucket;
        }
    }

    public String uploadFileOnS3(String bucketName, MultipartFile file) throws BucketNotFoundException {
        boolean bucketExists = isBucketExists(bucketName);
        if (!bucketExists) {
            throw new BucketNotFoundException("given bucket " + bucketName + " is not present in S3");

        } else {
            String fileName = file.getOriginalFilename();
            if (fileName != null) {
                InputStream fileObject = null;
                try {
                    fileObject = new ByteArrayInputStream(file.getBytes());
                } catch (IOException e) {
                    log.error("problem to process the file " + e.getCause());
                }

                var upload = s3Template.upload(bucketName, fileName, fileObject);

                return "File uploaded on bucket " + upload.getFilename();
            }
        }
        return "";
    }

    public byte[] downloadFile(String bucketName, String fileName) {
        S3Resource download = s3Template.download(bucketName, fileName);
        try {
            InputStream inputStream = download.getInputStream();
            return IoUtils.toByteArray(inputStream);
        } catch (IOException e) {
            log.error("problem to download file " + e.getCause());
        }
        return new byte[0];
    }


    public void deleteBucket(String name) {
        if (isBucketExists(name)) {
            s3Template.deleteBucket(name);
            log.info("bucket " + name + " is deleted successfully");
        } else {
            throw new IllegalArgumentException("Bucket does not exist " + name);
        }
    }

    public String deleteFileFromS3Bucket(String bucketName, String fileName) throws ObjectNotFoundException {
        if (isBucketExists(bucketName) && isObjectExists(bucketName, fileName)) {
            s3Template.deleteObject(bucketName, fileName);
        } else {
            throw new ObjectNotFoundException("Object Not exist in s3 bucket");
        }
        return fileName + "  deleted from bucket " + bucketName;
    }


    private boolean isBucketExists(String bucketName) {
        return s3Template.bucketExists(bucketName);
    }

    private boolean isObjectExists(String bucketName, String fileName) {
        return s3Template.objectExists(bucketName, fileName);
    }


}
