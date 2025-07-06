package org.gn.ebankbackend.repositories;

import org.gn.ebankbackend.entities.BankAccount;
import org.gn.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
