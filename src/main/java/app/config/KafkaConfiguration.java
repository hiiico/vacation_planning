//package app.config;
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.TopicBuilder;
//
//@Configuration
//public class KafkaConfiguration {
//
//    // Request topic
//    @Bean
//    public NewTopic userRegisterEventTopic() {
//        return TopicBuilder.name("user-register-event.v1").build();
//    }
//
//    // Reply topic
//    @Bean
//    public NewTopic userRegisterReplyTopic() {
//        return TopicBuilder.name("user-register-reply.v1").build();
//    }
//
//}
