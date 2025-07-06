package org.gn.ebankbackend.web;

import lombok.AllArgsConstructor;
import org.gn.ebankbackend.dtos.AccountHistoryDTO;
import org.gn.ebankbackend.dtos.AccountOperationDTO;
import org.gn.ebankbackend.dtos.BankAccountDTO;
import org.gn.ebankbackend.exceptions.BankAccountNotFoundException;
import org.gn.ebankbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")

public class BankAccountRestController {

    private BankAccountService bankAccountService;

    @GetMapping("accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }
    @GetMapping("accounts")
    public List<BankAccountDTO> BankAccounts() throws BankAccountNotFoundException {
        return bankAccountService.listBankAccount();
    }

    @GetMapping("accounts/{accountId}/operations")
    public List<AccountOperationDTO> accountHistory(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.accountHistory(accountId);
    }


    @GetMapping("accounts/{accountId}/pageOperations")
    public AccountHistoryDTO accountAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page" ,defaultValue = "0")int page,
            @RequestParam(name = "size" ,defaultValue = "5")int size
    ) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

}
