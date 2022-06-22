package emsi.ma.ebankingbackend.web;

import emsi.ma.ebankingbackend.dtos.*;
import emsi.ma.ebankingbackend.exceptions.BalanceNotSufficientException;
import emsi.ma.ebankingbackend.exceptions.BankAccountNotFoundException;
import emsi.ma.ebankingbackend.exceptions.CustomerNotFoundException;
import emsi.ma.ebankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin("*")
public class BankAccountRestAPI {
    private BankAccountService bankAccountService;

    public BankAccountRestAPI(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/customer-accounts/saveSavingAccount")
    public void saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException{
         bankAccountService.saveSavingBankAccount(initialBalance,interestRate,customerId);
    }
    @PostMapping("/customer-accounts/saveCurrentAccount")
    public void saveCurrentBankAccount(double initialBalance, double overdraft, Long customerId) throws CustomerNotFoundException{
         bankAccountService.saveCurrentBankAccount(initialBalance,overdraft,customerId);
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts(){
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId){
        return bankAccountService.accountHistory(accountId);
    }

    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(@PathVariable String accountId, @RequestParam(name="page",defaultValue = "0") int page , @RequestParam(name="size",defaultValue = "5") int size) throws BankAccountNotFoundException {
        return bankAccountService.getAccountHistory(accountId,page,size);
    }

    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.debit(debitDTO.getAccountId(), debitDTO.getAmount(),debitDTO.getDescription());
    return debitDTO;
    }

    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.credit(creditDTO.getAccountId(), creditDTO.getAmount(),creditDTO.getDescription());
        return creditDTO;
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundException, BalanceNotSufficientException {
        this.bankAccountService.transfer(transferRequestDTO.getAccountSource(),transferRequestDTO.getAccountDestination(), transferRequestDTO.getAmount());
    }
}
