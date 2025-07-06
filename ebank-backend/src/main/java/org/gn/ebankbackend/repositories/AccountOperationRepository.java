package org.gn.ebankbackend.repositories;

import org.gn.ebankbackend.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface AccountOperationRepository extends JpaRepository<AccountOperation, Long> {


    List<AccountOperation> findByBankAccountId(String accountId);
    Page findByBankAccountId(String accountId, Pageable pageable);
}
