package br.com.snapcast.domain.port.s3;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@ApplicationScoped
@NoArgsConstructor
public class ClienteS3 {
    public final String REGIAO = "us-east-1";
    public final S3Client client = S3Client.builder()
            .region(Region.of(System.getProperty("aws.region", REGIAO)))
            .credentialsProvider(DefaultCredentialsProvider.builder().build())
            .build();

    public final String BUCKET_NAME = "snapcastvideos";

}
