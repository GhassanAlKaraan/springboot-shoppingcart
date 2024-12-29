// package com.example.dreamshops.data;

// import com.example.dreamshops.models.Role;
// import com.example.dreamshops.models.User;
// import com.example.dreamshops.repositories.UserRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.boot.context.event.ApplicationReadyEvent;
// import org.springframework.context.ApplicationListener;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Component;
// import org.springframework.transaction.annotation.Transactional;

// import java.util.Set;

// @Transactional
// @Component
// @RequiredArgsConstructor
// public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {
//     private final UserRepository userRepository;
//     private final RoleRepository roleRepository;
//     private final PasswordEncoder passwordEncoder;

//     @Override
//     public void onApplicationEvent(ApplicationReadyEvent event) {
//         Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
//         createDefaultUserIfNotExits();
//         createDefaultRoleIfNotExits(defaultRoles);
//         createDefaultAdminIfNotExits();
//     }

//     private void createDefaultUserIfNotExits() {
//         Role userRole = roleRepository.findByName("ROLE_USER").get();
//         for (int i = 1; i <= 5; i++) {
//             String defaultEmail = "sam" + i + "@email.com";
//             if (userRepository.existsByEmail(defaultEmail)) {
//                 continue;
//             }
//             User user = new User();
//             user.setFirstName("The User");
//             user.setLastName("User" + i);
//             user.setEmail(defaultEmail);
//             user.setPassword(passwordEncoder.encode("123456"));
//             user.setRoles(Set.of(userRole));
//             userRepository.save(user);
//             System.out.println("Default vet user " + i + " created successfully.");
//         }
//     }

//     private void createDefaultAdminIfNotExits() {
//         Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
//         for (int i = 1; i <= 2; i++) {
//             String defaultEmail = "admin" + i + "@email.com";
//             if (userRepository.existsByEmail(defaultEmail)) {
//                 continue;
//             }
//             User user = new User();
//             user.setFirstName("Admin");
//             user.setLastName("Admin" + i);
//             user.setEmail(defaultEmail);
//             user.setPassword(passwordEncoder.encode("123456"));
//             user.setRoles(Set.of(adminRole));
//             userRepository.save(user);
//             System.out.println("Default admin user " + i + " created successfully.");
//         }
//     }

//     private void createDefaultRoleIfNotExits(Set<String> roles) {
//         roles.stream()
//                 .filter(role -> roleRepository.findByName(role).isEmpty())
//                 .map(Role::new).forEach(roleRepository::save);

//     }

// }

package com.example.dreamshops.data;

import com.example.dreamshops.models.Role;
import com.example.dreamshops.models.User;
import com.example.dreamshops.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(@SuppressWarnings("null") ApplicationReadyEvent event) {
        Set<String> defaultRoles = Set.of("ROLE_ADMIN", "ROLE_USER");
        createDefaultUserIfNotExits();
        createDefaultRoleIfNotExits(defaultRoles);
        createDefaultAdminIfNotExits();
    }

    private void createDefaultUserIfNotExits() {
        // Get the user role or create it if it doesn't exist
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole);
                });

        for (int i = 1; i <= 5; i++) {
            String defaultEmail = "sam" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("The User");
            user.setLastName("User" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);
            System.out.println("Default vet user " + i + " created successfully.");
        }
    }

    private void createDefaultAdminIfNotExits() {
        // Get the admin role or create it if it doesn't exist
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_ADMIN");
                    return roleRepository.save(newRole);
                });

        for (int i = 1; i <= 2; i++) {
            String defaultEmail = "admin" + i + "@email.com";
            if (userRepository.existsByEmail(defaultEmail)) {
                continue;
            }
            User user = new User();
            user.setFirstName("Admin");
            user.setLastName("Admin" + i);
            user.setEmail(defaultEmail);
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRoles(Set.of(adminRole));
            userRepository.save(user);
            System.out.println("Default admin user " + i + " created successfully.");
        }
    }

    private void createDefaultRoleIfNotExits(Set<String> roles) {
        for (String roleName : roles) {
            if (!roleRepository.findByName(roleName).isPresent()) {
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
                System.out.println("Role " + roleName + " created successfully.");
            }
        }
    }
}