package com.sms_help_server.services.user_service;

import com.sms_help_server.entities.base.EntityStatus;
import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.repo.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Log
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<SmsHelpUser> findAll() {
        return userRepository.findAll();
    }

    @Override
    public SmsHelpUser findByEmail(String email) {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("email not found"));
    }

    @Override
    public SmsHelpUser findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("user_actions not found"));
    }

    @Override
    public SmsHelpUser findByIdOrEmail(Long id, String email) {
        if (id == null && email == null) {
            throw new NoSuchElementException("neither id nor email was provided");
        }

        return  (id != null) ?
                this.findById(id) :
                this.findByEmail(email);
    }

    @Override
    public SmsHelpUser updateUserStatus(SmsHelpUser user, EntityStatus newStatus) {
        user.setEntityStatus(newStatus);
        return userRepository.saveAndFlush(user);
    }
}
