package br.com.snapcast.config;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@ApplicationScoped
@NoArgsConstructor
@Getter
public class ClienteS3 {
    @ConfigProperty(name = "aws.regiao", defaultValue = "us-east-1")
    private String regiao;

    @ConfigProperty(name = "aws.bucket.name", defaultValue = "snapcastvideos")
    private String bucket;

    public S3Client pegarS3() {
        return S3Client.builder()
                .region(Region.of(System.getProperty("aws.region", regiao)))
                .credentialsProvider(DefaultCredentialsProvider.builder().build())
                .build();
    }

}
