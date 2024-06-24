package com.personal.shop.controller.rest;

import com.personal.shop.service.AwsS3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageUploadController {

    private final AwsS3Service awsS3Service;

    // S3 버킷에 이미지 파일 저장
    @GetMapping("/presigned-url")
    String getURL(@RequestParam String filename,
                  @RequestParam String type) {
        log.info("type : " + type);

        String path;
        if ("item".equalsIgnoreCase(type)) {
            path = "item/" + filename;

        } else if ("notice".equalsIgnoreCase(type)) {
            path = "notice/" + filename;

        } else {
            throw new IllegalArgumentException("Invalid type parameter");
        }

        String result = awsS3Service.createPreSignedUrl(path);
        log.info("ImageUploadController result : " + result);

        return result;
    }


}
