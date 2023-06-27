package com.sms_help_server.services.user_service;

import com.sms_help_server.entities.base.Status;
import com.sms_help_server.entities.role.RoleName;
import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.repo.RoleRepository;
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
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public SmsHelpUser register(SmsHelpUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(List.of(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow()));
        user.setStatus(Status.ACTIVE);
        return userRepository.save(user);
    }

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
                .orElseThrow(NoSuchElementException::new);
    }
}
