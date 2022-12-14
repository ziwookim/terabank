package terafintech.terabank.api;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import terafintech.terabank.dto.ApplyAccountRequest;
import terafintech.terabank.dto.ApplyAccountResponse;
import terafintech.terabank.domain.Account;
import terafintech.terabank.service.AccountService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AccountApiController {

    private final AccountService accountService;

    @Async
    @PostMapping("/api/account")
    public ApplyAccountResponse applyAccount(@RequestBody @Valid ApplyAccountRequest request) {

        Account account = new Account();
        account.setUserId(request.getUserId());

        Long id = accountService.apply(account);

        account = accountService.findOne(id);

        return new ApplyAccountResponse(account.getUserId(), account.getPublicKey(), account.getPrivateKey());
    }

}
