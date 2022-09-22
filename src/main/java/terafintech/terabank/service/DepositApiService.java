package terafintech.terabank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class DepositApiService {

    @Async
    public Future<String> callDepositApi(String receiverPublicKey, String amount) {

//        Map<String, String> result = new HashMap<>();
        String result = "";

        RestTemplate restTemplate = new RestTemplate(); // 비동기 전달
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("receiverPublicKey", (Object)receiverPublicKey);
            jsonObject.put("amount", (Object)amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpEntity<String> logRequest = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/api/deposit", logRequest, String.class);

        ObjectMapper mapper = new ObjectMapper();

        try {
            result = mapper.readTree(responseEntity.getBody()).get("resultCode").asText();
//            resultMap.put("resultCode", result);
        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            result.put("body", responseEntity.getBody());
            result = responseEntity.getBody();
        }

//        result.put("statusCode", responseEntity.getStatusCodeValue());
//        result.put("headers", responseEntity.getHeaders());
//        result.put("body", responseEntity.getBody());

//        return result;
        return new AsyncResult<>(result);
    }
}
