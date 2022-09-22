package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import terafintech.terabank.domain.*;
import terafintech.terabank.repository.RemitHistoryRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    @Transactional
    public Long remit(String receiverPublicKey, String senderPrivateKey, String amount) throws ExecutionException, InterruptedException {

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
    public Map<String, Object> depositAndWithdrawApiCalls(String receiverPublicKey, String senderPrivateKey, String amount) throws InterruptedException, ExecutionException {

        Map<String, Object> resultCodesMap = new HashMap<>();
        resultCodesMap.put("depositResultCode", null);
        resultCodesMap.put("withdrawResultCode", null);

        logger.info("RemitHistoryService depositAndWithdrawApiCalls... asyncTime...");
        logger.info("callDepositApi");
        Future<String> depositResult = depositApiService.callDepositApi(receiverPublicKey, amount);
        logger.info("callWithdrawApi");
        Future<String> withdrawResult = withdrawApiService.callWithdrawApi(senderPrivateKey, amount);

        while(true) {
            if(depositResult.isDone()) {
                logger.info("## depositResultMap: {}", depositResult.get());
                resultCodesMap.replace("depositResultCode", depositResult.get());
                break;
            }
            if(depositResult.isCancelled()) {
                logger.info("### depositApi didn't work well. ###");
                break;
            }
        }

        while(true) {
            if(withdrawResult.isDone()) {
                logger.info("## withdrawResultCode: {}", withdrawResult.get());
                resultCodesMap.replace("withdrawResultCode", withdrawResult.get());
                break;
            }
            if (withdrawResult.isCancelled()) {
                logger.info("### withdrawApi didn't work well. ###");
                break;
            }
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
