package com.muller.kedateste.config.listener;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class SqsMessageListener implements ApplicationRunner {

    private final AmazonSQSAsync amazonSQSAsync;

    @Value("${cloud.aws.sqs.inicio.processo}")
    private String queueUrl;

    @Autowired
    public SqsMessageListener(AmazonSQSAsync amazonSQSAsync) {
        this.amazonSQSAsync = amazonSQSAsync;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest()
                .withQueueUrl(queueUrl).withMaxNumberOfMessages(1);
        log.info("Buscando mensagens da fila: {}", queueUrl);
        var messages = amazonSQSAsync.receiveMessage(receiveMessageRequest).getMessages();
        if (messages != null && !messages.isEmpty()) {
            log.info("mensagem finalizada {} - {}", messages.get(0).getBody(), LocalDateTime.now());
            amazonSQSAsync.deleteMessage(queueUrl, messages.get(0).getReceiptHandle());
            Thread.sleep(1000L);
        }
    }
}
