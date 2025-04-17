package com.example.controller;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
 
import java.util.List;
import java.util.Optional;
 
@RestController
@RequestMapping("/")
public class SocialMediaController {
 
    @Autowired
    private AccountService accountService;
 
    @Autowired
    private MessageService messageService;
 
    /**
    * Endpoint: POST /register
    */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
 
        if (username == null || username.isBlank() || password == null || password.length() < 4) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
 
        if (accountService.findByUsername(username).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
 
        Account savedAccount = accountService.save(account);
        return ResponseEntity.ok(savedAccount);
    }
 
    /**
    * Endpoint: POST /login
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Account account) {
        Optional<Account> optionalAccount = accountService.findByUsername(account.getUsername());
 
        if (optionalAccount.isPresent()) {
            Account existing = optionalAccount.get();
            if (existing.getPassword().equals(account.getPassword())) {
                return ResponseEntity.ok(existing);
            }
        }
 
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
 
    /**
    * POST /messages
    * Create a new message
    */
    @PostMapping("/messages")
    public ResponseEntity<?> createMessage(@RequestBody Message message) {
        String text = message.getMessageText();
        Integer userId = message.getPostedBy();
 
        if (text == null || text.isBlank() || text.length() > 255 || userId == null || !accountService.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
 
        Message saved = messageService.save(message);
        return ResponseEntity.ok(saved);
    }
 
    /**
    * GET /messages
    * Retrieve all messages
    */
    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.findAll());
    }
 
    /**
    * GET /messages/{messageId}
    * Retrieve a message by ID
    */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<Message> getMessageById(@PathVariable int messageId) {
        return messageService.findById(messageId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(null));
    }
 
    /**
    * DELETE /messages/{id}
    * Delete a message by ID
    */
    @DeleteMapping("/messages/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable int id) {
        boolean deleted = messageService.deleteById(id);
        if (deleted) {
            return ResponseEntity.ok("1");
        } else {
            return ResponseEntity.ok().build();
        }
    }

 
    /**
    * PATCH /messages/{id}
    * Update message text
    */
    @PatchMapping("/messages/{id}")
    public ResponseEntity<String> updateMessageText(@PathVariable int id, @RequestBody Message messageBody) {
        String newText = messageBody.getMessageText();

        // Invalid message text
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid message text");
        }

        // Update message text
        Message updated = messageService.updateText(id, newText);

        if (updated != null) {
            return ResponseEntity.ok("1");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Message not found");
        }
    }
 
    /**
    * GET /accounts/{accountId}/messages
    * Get all messages by a specific user
    */
    @GetMapping("/accounts/{accountId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUser(@PathVariable int accountId) {
        return ResponseEntity.ok(messageService.findByAccountId(accountId));
    }
}
