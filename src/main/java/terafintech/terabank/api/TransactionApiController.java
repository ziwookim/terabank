package terafintech.terabank.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import terafintech.terabank.service.TransactionHistoryService;

@RestController
@RequiredArgsConstructor
public class TransactionApiController {

    private final TransactionHistoryService transactionHistoryService;


}
