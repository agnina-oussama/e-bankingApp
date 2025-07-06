package org.gn.ebankbackend.services;

import org.gn.ebankbackend.dtos.*;
import org.gn.ebankbackend.entities.BankAccount;
import org.gn.ebankbackend.entities.CurrentAccount;
import org.gn.ebankbackend.entities.SavingAccount;
import org.gn.ebankbackend.exceptions.BalanceNotSufficentException;
import org.gn.ebankbackend.exceptions.BankAccountNotFoundException;
import org.gn.ebankbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);
    CurrentBankAccountDTO saveCurrentAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingAccount(double initialBalance , double intrestRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomer();

    List<BankAccountDTO> listBankAccount();
    CustomerDTO getCustomerById(Long customerId) throws CustomerNotFoundException;

    BankAccountDTO getBankAccount(String id) throws BankAccountNotFoundException;
    void debit(String accountId,double amount,String description ) throws BankAccountNotFoundException, BalanceNotSufficentException;
    void credit(String accountId,double amount,String description) throws BankAccountNotFoundException;

    void transfer(String fromAccountId,String toAccountId,double amount) throws BankAccountNotFoundException, BalanceNotSufficentException;

    CustomerDTO updateCustomer(CustomerDTO customer);

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> accountHistory(String accountId);

    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
