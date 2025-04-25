package io.github.Maike2961.Chamados.service.AWS;


import io.github.Maike2961.Chamados.Enums.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsResponse;

import java.util.HashMap;

@Service
public class awsService {

    private SnsClient snsClient;

    public awsService(SnsClient snsClient) {
        this.snsClient = snsClient;
    }

    @Value("${aws.sns.topic-arn}")
    private String topic;

    public SnsResponse publishChamadoConcluido(StatusEnum status, String title) {

        String mensagem = mensagensChamado.getMensagem(status, title);

        PublishRequest build = PublishRequest.builder()
                .message(mensagem)
                .topicArn(topic)
                .build();

        PublishResponse response = snsClient.publish(build);

        System.out.println("Message ID: " + response.messageId());
        System.out.println("Message: " + mensagem);
        System.out.println("Status code: " + response.sdkHttpResponse().statusCode());

        return response;
    }
}

