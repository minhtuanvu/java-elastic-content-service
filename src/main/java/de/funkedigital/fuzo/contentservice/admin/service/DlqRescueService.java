package de.funkedigital.fuzo.contentservice.admin.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.Message;

import de.funkedigital.fuzo.contentservice.admin.models.DlqRescueResult;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DlqRescueService {

    private final String mainQueueUrl;
    private final String dlqUrl;
    private final AmazonSQS amazonSQS;

    DlqRescueService(@Value("${queueUrl}") String mainQueueUrl,
                     @Value("${dlqUrl}") String dlqUrl,
                     AmazonSQS amazonSQS) {
        this.mainQueueUrl = mainQueueUrl;
        this.dlqUrl = dlqUrl;
        this.amazonSQS = amazonSQS;
    }

    public DlqRescueResult rescue() {
        List<Message> deadMessages = amazonSQS.receiveMessage(dlqUrl).getMessages();
        AtomicInteger redirectCounter = new AtomicInteger(0);

        deadMessages.forEach(message -> {
            amazonSQS.sendMessage(mainQueueUrl, message.getBody());
            redirectCounter.incrementAndGet();
            amazonSQS.deleteMessage(dlqUrl, message.getReceiptHandle());
        });

        return new DlqRescueResult(deadMessages.size(), redirectCounter.get());
    }

}
