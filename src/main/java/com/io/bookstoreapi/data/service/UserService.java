package com.io.bookstoreapi.data.service;

import com.io.bookstoreapi.data.repositories.UserRepository;
import com.io.bookstoreapi.domain.User;

import com.io.bookstoreapi.exceptions.UserAlreadyExistsException;
import com.io.bookstoreapi.exceptions.UserIsNullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

   public UserService(){}

   public UserService(UserRepository repository,PasswordEncoder encoder){userRepository=repository; passwordEncoder=encoder;}
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Optional<User> getUser(int id){
        return userRepository.findById(id);
    }


    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) {
        if(user!=null){
        if(userExists(user)) throw new UserAlreadyExistsException();

        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );
        return userRepository.save(user);}
        else{
            throw new UserIsNullException();
        }
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return userRepository.findByUsername(s);
    }

    public boolean userExists(User user){
        return userRepository.existsByEmailAddress(user.getEmailAddress()) ||
                userRepository.existsByUsername(user.getUsername());
    }


}
