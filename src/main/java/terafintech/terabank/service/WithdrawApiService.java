package terafintech.terabank.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import terafintech.terabank.dto.CreateDepositResponse;
import terafintech.terabank.dto.CreateWithdrawResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

@Service
public class WithdrawApiService {

    @Async
    public Future<String> callWithdrawApi(String senderPrivateKey, String amount) {

//        Map<String, String> result = new HashMap<>();
        String result = "";

        RestTemplate restTemplate = new RestTemplate(); // 비동기 전달
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("senderPrivateKey", (Object)senderPrivateKey);
            jsonObject.put("amount", (Object)amount);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpEntity<String> logRequest = new HttpEntity<>(jsonObject.toString(), httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/api/withdraw", logRequest, String.class);

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

        return new AsyncResult<>(result);
    }
}
