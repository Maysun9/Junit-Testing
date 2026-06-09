package com.example.redthreadgame;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "OPEN_AI_KEY=test",
        "ELEVEN_LABS_KEY=test",
        "TWILIO_ACCOUNT_SID=test",
        "TWILIO_AUTH_TOKEN=test",
        "TWILIO_WHATSAPP_NUMBER=test",
        "SPRING_MAIL_USERNAME=test",
        "SPRING_MAIL_PASSWORD=test"
})
class RedThreadGameApplicationTests {

    @Test
    void contextLoads() {
    }

}
