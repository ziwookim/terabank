package terafintech.terabank.service;

import lombok.RequiredArgsConstructor;
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

    private final RemitHistoryRepository remitHistoryRepository;
    private final DepositApiService depositApiService;
    private final WithdrawApiService withdrawApiService;

    public RemitHistory findOne(Long id) {
        return remitHistoryRepository.findOne(id);
    }

    @Transactional
    public Long remit(String receiverPublicKey, String senderPrivateKey, String amount) {

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
//        System.out.println("## depositResultMap: " + depositResultMap.get("resultCode"));
        if(depositResultMap != null) {
            resultCodesMap.put("depositResultCode", depositResultMap.get("resultCode"));
        } else {
            resultCodesMap.put("depositResultCode", null);
        }

        Map<String, String> withdrawResultMap = withdrawApiService.callWithdrawApi(senderPrivateKey, amount);
//        System.out.println("## withdrawResultCode: " + withdrawResultMap.get("resultCode"));
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
