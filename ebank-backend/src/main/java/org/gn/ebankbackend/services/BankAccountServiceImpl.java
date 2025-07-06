package org.gn.ebankbackend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gn.ebankbackend.dtos.*;
import org.gn.ebankbackend.entities.*;
import org.gn.ebankbackend.enums.OperationType;
import org.gn.ebankbackend.exceptions.BalanceNotSufficentException;
import org.gn.ebankbackend.exceptions.BankAccountNotFoundException;
import org.gn.ebankbackend.exceptions.CustomerNotFoundException;
import org.gn.ebankbackend.mappers.BankAccountMapperImlp;
import org.gn.ebankbackend.repositories.AccountOperationRepository;
import org.gn.ebankbackend.repositories.BankAccountRepository;
import org.gn.ebankbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
        private  CustomerRepository customerRepository;
        private  BankAccountRepository bankAccountRepository;
        private  AccountOperationRepository accountOperationRepository;
        private BankAccountMapperImlp bankingMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customer) {
        log.info("Saving customer " + customer);
        Customer c =  bankingMapper.toCustomer(customer);
        Customer saved = customerRepository.save(c);
        return bankingMapper.fromCustomer(saved);
    }

    @Override
    public CurrentBankAccountDTO saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {

        Customer customerById = customerRepository.findById(customerId).orElse(null);
        if(customerById == null){
            throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
        }
        CurrentAccount currentBankAccount = new CurrentAccount();
        currentBankAccount.setId(UUID.randomUUID().toString());
        currentBankAccount.setBalance(initialBalance);
        currentBankAccount.setCreatedAt(new Date());
        currentBankAccount.setOverDraft(overDraft);
        currentBankAccount.setCustomer(customerById);
        CurrentAccount ca = bankAccountRepository.save(currentBankAccount);
        CurrentBankAccountDTO caDTO = bankingMapper.fromCurrentAccount(ca);
        return caDTO;
    }

    @Override
    public SavingBankAccountDTO saveSavingAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {

        Customer customerById = customerRepository.findById(customerId).orElse(null);
        if(customerById == null){
            throw new CustomerNotFoundException("Customer with id " + customerId + " not found");
        }
        SavingAccount sa = new SavingAccount();
        sa.setId(UUID.randomUUID().toString());
        sa.setBalance(initialBalance);
        sa.setCreatedAt(new Date());
        sa.setInterestRate(interestRate);
        sa.setCustomer(customerById);
        SavingAccount ssa = bankAccountRepository.save(sa);
        return bankingMapper.fromSavingAccount(ssa);
    }

    @Override
    public List<CustomerDTO> listCustomer() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTO> collect = customers.stream()
                .map(cust -> bankingMapper.fromCustomer(cust))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<BankAccountDTO> listBankAccount() {
            List<BankAccount> bankAccounts = bankAccountRepository.findAll();
            List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
                if (bankAccount instanceof SavingAccount) {
                    SavingAccount savingAccount = (SavingAccount) bankAccount;
                    return bankingMapper.fromSavingAccount(savingAccount);
                } else {
                    CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                    return bankingMapper.fromCurrentAccount(currentAccount);
                }
            }).collect(Collectors.toList());
            return bankAccountDTOS;
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) throws CustomerNotFoundException {
        Customer c = customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("customer not found"));
        return bankingMapper.fromCustomer(c);
    }

    @Override
    public BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException());
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount= (SavingAccount) bankAccount;
            return bankingMapper.fromSavingAccount(savingAccount);
        } else {
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return bankingMapper.fromCurrentAccount(currentAccount);
        }
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficentException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException());
        if(bankAccount.getBalance()<amount)
            throw new BalanceNotSufficentException("balance not suffecient");
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException());
        AccountOperation accountOperation=new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }


    @Override
    public void transfer(String fromAccountId, String toAccountId, double amount) throws BankAccountNotFoundException, BalanceNotSufficentException {
            debit(fromAccountId,amount,"Transfer to " + toAccountId);
            credit(toAccountId,amount,"Transfer from " + fromAccountId);

    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customer) {
        log.info("updating customer " + customer);
        Customer c =  bankingMapper.toCustomer(customer);
        Customer saved = customerRepository.save(c);
        return bankingMapper.fromCustomer(saved);
    }

    @Override
    public void deleteCustomer(Long customerId)  {
        customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> aos = accountOperationRepository.findByBankAccountId(accountId);
        List<AccountOperationDTO> aosDto = aos.stream().map(acc->bankingMapper.fromAccountOperation(acc)).collect(Collectors.toList());
        return aosDto;
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        BankAccount ba = bankAccountRepository.findById(accountId).orElse(null);

        if(ba == null) throw new BankAccountNotFoundException();

        Page<AccountOperation> pao = accountOperationRepository.findByBankAccountId(accountId, PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO = new AccountHistoryDTO();
        accountHistoryDTO.setAccountId(accountId);
        accountHistoryDTO.setBalance(ba.getBalance());
        accountHistoryDTO.setAccountOperationDTOList(pao.getContent().stream().map(acc->bankingMapper.fromAccountOperation(acc)).collect(Collectors.toList()));
        accountHistoryDTO.setTotalPages(pao.getTotalPages());
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setCurrentPage(page);

        return accountHistoryDTO;
    }
}
