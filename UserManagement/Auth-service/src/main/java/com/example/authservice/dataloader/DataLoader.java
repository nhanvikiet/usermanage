package com.example.authservice.dataloader;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository repo;
    @Autowired private PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            repo.save(new User(null,"admin", encoder.encode("123456"),"ROLE_ADMIN","admin123@gmail.com"));
            repo.save(new User(null,"user",encoder.encode("123456"),"ROLE_USER","user123@gmail.com"));
        }
    }
}
