package kz.bitlab.javapro.security.springsecuritydemo.services;

import kz.bitlab.javapro.security.springsecuritydemo.model.Roles;
import kz.bitlab.javapro.security.springsecuritydemo.model.Users;
import kz.bitlab.javapro.security.springsecuritydemo.repository.RoleRepository;
import kz.bitlab.javapro.security.springsecuritydemo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findAllByEmail(username);
        if (user!=null){
            return user;
        } else {
            throw new UsernameNotFoundException("USER NOT FOUND");
        }
    }

    public Users registerUser(Users user){
        Users checkUser = userRepository.findAllByEmail(user.getEmail());
        if(checkUser==null){
            Roles userRole = roleRepository.findByRole("ROLE_USER");
            ArrayList<Roles> roles = new ArrayList<>();
            roles.add(userRole);
            user.setRoles(roles);

            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        return null;
    }

    public Users updatePassword(Users user, String oldPassword, String newPassword){
        Users currentUser = userRepository.findById(user.getId()).orElse(null);
        if(currentUser!=null){
            if(passwordEncoder.matches(oldPassword, currentUser.getPassword())){
                currentUser.setPassword(passwordEncoder.encode(newPassword));
                return userRepository.save(currentUser);
            }
        }
        return null;
    }
}
