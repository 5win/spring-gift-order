package gift.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.KakaoProductMessage;
import gift.properties.KakaoProperties;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoMessageSender {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.create();

    public KakaoMessageSender(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public void sendForMe(String token, KakaoProductMessage message)
        throws JsonProcessingException {

        String json = new ObjectMapper().writeValueAsString(message);
        System.out.println("json = " + json);
        System.out.println(kakaoProperties.getSendMessageForMe());
        System.out.println("token = " + token);

        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", json);

        ResponseEntity<String> res = restClient.post()
            .uri(kakaoProperties.getSendMessageForMe())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + token)
            .body(body)
            .retrieve()
            .toEntity(String.class);

        System.out.println(res.getStatusCode());
        System.out.println(res.getBody());
    }
}
