package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import terafintech.terabank.domain.*;
import terafintech.terabank.repository.RemitHistoryRepository;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RemitHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(RemitHistoryService.class);

    private final RemitHistoryRepository remitHistoryRepository;
    private final DepositApiService depositApiService;
    private final WithdrawApiService withdrawApiService;

    public RemitHistory findOne(Long id) {
        return remitHistoryRepository.findOne(id);
    }

    @Async
    @Transactional
    public Long remit(String receiverPublicKey, String senderPrivateKey, String amount) {

        logger.info("called RemitHistoryService remit()");

        /**
         * 입금 api 호출 + 출금 api 호출
         */
        Map<String, Object> resultCodesMap = depositAndWithdrawApiCalls(receiverPublicKey, senderPrivateKey, amount);

        ResultCode depositResultCode = findResultCode((String) resultCodesMap.get("depositResultCode"));
        ResultCode withdrawResultCode = findResultCode((String) resultCodesMap.get("withdrawResultCode"));

        RemitHistory remitHistory = RemitHistory.createTransaction(depositResultCode, withdrawResultCode, receiverPublicKey, senderPrivateKey, amount);

        // 저장
        remitHistoryRepository.save(remitHistory);

        return remitHistory.getId();
    }

    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> depositAndWithdrawApiCalls(String receiverPublicKey, String senderPrivateKey, String amount) {

        Map<String, Object> resultCodesMap = new HashMap<>();

        Map<String, String> depositResultMap = depositApiService.callDepositApi(receiverPublicKey, amount);
        logger.info("## depositResultMap: {}", depositResultMap.get("resultCode"));
        if(depositResultMap != null) {
            resultCodesMap.put("depositResultCode", depositResultMap.get("resultCode"));
        } else {
            resultCodesMap.put("depositResultCode", null);
        }

        Map<String, String> withdrawResultMap = withdrawApiService.callWithdrawApi(senderPrivateKey, amount);
        logger.info("## withdrawResultCode: {}", withdrawResultMap.get("resultCode"));
        if(withdrawResultMap != null) {
            resultCodesMap.put("withdrawResultCode", withdrawResultMap.get("resultCode"));
        } else {
            resultCodesMap.put("withdrawResultCode", null);
        }

        return resultCodesMap;
    }

    public ResultCode findResultCode (String stringResultCode) {

        ResultCode findResult = null;
        switch(stringResultCode) {
            case "SUCCESS" :
                findResult = ResultCode.SUCCESS;
            break;
            case "INVALIDAMOUNT" :
                findResult = ResultCode.INVALIDAMOUNT;
            break;
            case "RECEIVERERROR" :
                findResult = ResultCode.RECEIVERERROR;
            break;
            case "SENDERERROR":
                findResult = ResultCode.SENDERERROR;
            break;
            case "LACKOFMONEY":
                findResult = ResultCode.LACKOFMONEY;
            break;
            case "OTHERPROBLEMS":
                findResult = ResultCode.OTHERPROBLEMS;
                break;
            default:
                break;
        }

        return findResult;
    }
}
