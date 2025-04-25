package io.github.Maike2961.Chamados.config.AWS;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

@Configuration
public class awsConfig {

    @Value("${aws.sns.endpoint}")
    private String endpoint;

    @Value("${aws.sns.access-key}")
    private String accessKey;

    @Value("${aws.sns.secret-key}")
    private String secretKey;

    @Bean
    public SnsClient snsClient(){
        return SnsClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .endpointOverride(URI.create(endpoint))
                .build();
    }
}
