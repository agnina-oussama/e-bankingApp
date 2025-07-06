package org.gn.ebankbackend;

import org.gn.ebankbackend.dtos.BankAccountDTO;
import org.gn.ebankbackend.dtos.CurrentBankAccountDTO;
import org.gn.ebankbackend.dtos.CustomerDTO;
import org.gn.ebankbackend.dtos.SavingBankAccountDTO;
import org.gn.ebankbackend.exceptions.BalanceNotSufficentException;
import org.gn.ebankbackend.exceptions.BankAccountNotFoundException;
import org.gn.ebankbackend.exceptions.CustomerNotFoundException;
import org.gn.ebankbackend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner start(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("oussama","ali","youness").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentAccount(Math.random()*10000,Math.random()*5000,customer.getId());
                    bankAccountService.saveSavingAccount(Math.random()*10000,5+Math.random()*10,customer.getId());


                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });

            List<BankAccountDTO> bankAccounts = bankAccountService.listBankAccount();
            for (BankAccountDTO bankAccount:bankAccounts){
                for (int i = 0; i <10 ; i++) {
                    String accountId;
                    if(bankAccount instanceof SavingBankAccountDTO){
                        accountId=((SavingBankAccountDTO) bankAccount).getId();
                    } else{
                        accountId=((CurrentBankAccountDTO) bankAccount).getId();
                    }
                    bankAccountService.credit(accountId,10000+Math.random()*120000,"Credit");
                    try {
                        bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
                    } catch (BalanceNotSufficentException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }
}
