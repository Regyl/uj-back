package com.github.regyl.unfriendlyjarvis.service.impl.s3;

import com.github.regyl.unfriendlyjarvis.configuration.s3.S3ConfigurationProperties;
import com.github.regyl.unfriendlyjarvis.service.s3.S3Service;
import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.Http.Method;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of MinIOService for working with MinIO object storage.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private final MinioClient minioClient;
    private final S3ConfigurationProperties configProps;

    @Override
    public String uploadFile(String bucketName, String objectName, MultipartFile file) {
        try {
            // Ensure bucket exists
            ensureBucketExists(bucketName);

            // Upload file
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(file.getInputStream(), file.getSize(), -1L)
                            .contentType(file.getContentType())
                            .build()
            );

            log.info("File uploaded successfully: {}/{}", bucketName, objectName);
            return objectName;
        } catch (Exception e) {
            log.error("Error uploading file to MinIO: {}/{}", bucketName, objectName, e);
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }

    @Override
    public String getPresignedUrl(String bucketName, String objectName) {
        try {
            GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(configProps.getPresignedUriTtlHours(), TimeUnit.HOURS)
                    .build();

            return minioClient.getPresignedObjectUrl(args);
        } catch (Exception e) {
            log.error("Error generating presigned URL for: {}/{}", bucketName, objectName, e);
            throw new RuntimeException("Failed to generate presigned URL", e);
        }
    }

    @Override
    public InputStream getFile(String bucketName, String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            log.error("Error getting file from MinIO: {}/{}", bucketName, objectName, e);
            throw new RuntimeException("Failed to get file from MinIO", e);
        }
    }

    @Override
    public void deleteFile(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
            log.info("File deleted successfully: {}/{}", bucketName, objectName);
        } catch (Exception e) {
            log.error("Error deleting file from MinIO: {}/{}", bucketName, objectName, e);
            throw new RuntimeException("Failed to delete file from MinIO", e);
        }
    }

    /**
     * Ensure bucket exists, create if not.
     *
     * @param bucketName bucket name
     */
    private void ensureBucketExists(String bucketName) {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Bucket created: {}", bucketName);
            }
        } catch (Exception e) {
            log.error("Error checking/creating bucket: {}", bucketName, e);
            throw new RuntimeException("Failed to ensure bucket exists", e);
        }
    }
}

