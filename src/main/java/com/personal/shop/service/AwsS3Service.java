package com.personal.shop.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class AwsS3Service {

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;
    private final S3Presigner s3Presigner;
    private final S3Client s3Client;

    public String createPreSignedUrl(String path) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucket)
                .key(path)
                .build();

        PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(5)) // The Url will expire in 5 minutes
                .putObjectRequest(putObjectRequest)
                .build();
        log.info("Testing AwsS3Service");
        return s3Presigner.presignPutObject(preSignRequest).url().toString();
    }

    public boolean deleteFile(String fileUrl) {
        try {
            String key = extractKeyFromUrl(fileUrl);
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(bucket) // 필드 변수로 저장된 버킷 이름
                    .key(key) // extractKeyFromUrl 메서드에서 추출한 키
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
            log.info("File successfully deleted from S3: {}", key);
            return true;

        } catch (Exception e) {
            log.error("Error deleting file from S3: {}", fileUrl, e);
            return false;
        }
    }

    public String extractKeyFromUrl(String fileUrl) {
        try {
            URI uri = new URI(fileUrl);
            String path = uri.getPath();

            // 첫 번째 '/'를 제거 (버킷 이름 다음의 '/')
            return path.substring(path.indexOf('/', 1) + 1);

        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Invalid S3 URL", e);
        }
    }
}
