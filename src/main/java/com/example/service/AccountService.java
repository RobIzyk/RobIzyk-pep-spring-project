package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Optional<Account> findByUsername(String username) {
        return accountRepository.findByUsername(username);
    }

    public Account save(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> findById(int id) {
        return accountRepository.findById(id);
    }

    public boolean existsById(int accountId) {
        return accountRepository.existsById(accountId);
    }
}
