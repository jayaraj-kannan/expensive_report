package com.myapp.myapp.Service;

import com.myapp.myapp.Data.UserRepository;
import com.myapp.myapp.Model.Role;
import com.myapp.myapp.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        ;
    }
    public  User getCurrentUsername(Authentication authentication){
        authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return this.getUserByEmail(userDetails.getUsername()).get();
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void createUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
    }
    public Optional<User> getUserByEmail(String email){
       return userRepository.findByEmail(email);
    }
    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }
    public Map<String,Object> isUserPresent(User user){
       Map<String,Object> map=new HashMap<>();
        map.put("isexists",false);
        Optional<User> existingUserEmail = userRepository.findByEmail(user.getEmail());
        if(existingUserEmail.isPresent()){
            map.put("isexists",true);
            map.put("message","Email Already Present!");
        }
        Optional<User> existingUserMobile = userRepository.findByMobile(user.getMobile());
        if(existingUserMobile.isPresent()){
            map.put("isexists",true);
            map.put("message","Mobile Number Already Present!");
        }
        if (existingUserEmail.isPresent() && existingUserMobile.isPresent()) {
            map.put("isexists",true);
            map.put("message","Email and Mobile Number Both Already Present!");
        }

        System.out.println("existingUserEmail.isPresent() - "+existingUserEmail.isPresent()+"existingUserMobile.isPresent() - "+existingUserMobile.isPresent());
        return map;
    }
    public void deleteUser(String id) {
        userRepository.deleteById(id);
        System.out.println("User Deleted");
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(
                        String.format("USER_NOT_FOUND", email)
                ));
    }

}
