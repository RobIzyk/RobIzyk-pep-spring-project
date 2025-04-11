package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message save(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public Optional<Message> findById(int id) {
        return messageRepository.findById(id);
    }

    public boolean deleteById(int id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Message> findByAccountId(int accountId) {
        return messageRepository.findByPostedBy(accountId);
    }

    public Message updateText(int id, String newText) {
        Optional<Message> optional = messageRepository.findById(id);
        if (optional.isPresent()) {
            Message message = optional.get();
            message.setMessageText(newText);
            return messageRepository.save(message);
        }
        return null;
    }
}
