package com.ashish.accounttransactionservice.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerAccountRepository extends JpaRepository<PlayerAccount, Long> {

}
