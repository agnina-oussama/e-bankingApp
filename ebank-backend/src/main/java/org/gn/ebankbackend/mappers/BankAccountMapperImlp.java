package org.gn.ebankbackend.mappers;

import com.fasterxml.jackson.databind.util.BeanUtil;
import org.gn.ebankbackend.dtos.AccountOperationDTO;
import org.gn.ebankbackend.dtos.CurrentBankAccountDTO;
import org.gn.ebankbackend.dtos.CustomerDTO;
import org.gn.ebankbackend.dtos.SavingBankAccountDTO;
import org.gn.ebankbackend.entities.AccountOperation;
import org.gn.ebankbackend.entities.CurrentAccount;
import org.gn.ebankbackend.entities.Customer;
import org.gn.ebankbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImlp {
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;

    }
    public Customer toCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingAccount(SavingAccount sa){
        SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(sa, savingBankAccountDTO);
        savingBankAccountDTO.setType(sa.getClass().getSimpleName());
        return savingBankAccountDTO;
    }
    public SavingAccount toSavingAccount(SavingBankAccountDTO saDTO){
        SavingAccount savingAccount = new SavingAccount();
        BeanUtils.copyProperties(saDTO, savingAccount);
        return savingAccount;
    }

    public CurrentBankAccountDTO fromCurrentAccount(CurrentAccount ca){
        CurrentBankAccountDTO currentBankAccountDTO = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(ca, currentBankAccountDTO);
        currentBankAccountDTO.setType(ca.getClass().getSimpleName());

        return currentBankAccountDTO;
    }
    public CurrentAccount toCurrentAccount(CurrentBankAccountDTO caDTO){
        CurrentAccount currentAccount = new CurrentAccount();
        BeanUtils.copyProperties(caDTO, currentAccount);
        return currentAccount;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation ao){
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(ao, accountOperationDTO);
        return accountOperationDTO;
    }

    public AccountOperation toAccountOperation(AccountOperationDTO aoDTO){
        AccountOperation accountOperation = new AccountOperation();
        BeanUtils.copyProperties(aoDTO, accountOperation);
        return accountOperation;
    }
}
