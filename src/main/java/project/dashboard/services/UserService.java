package project.dashboard.services;

import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dashboard.models.Role;
import project.dashboard.models.User;
import project.dashboard.models.internals.UpdateUserInfoRequestData;
import project.dashboard.repos.IUserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Autowired
    public UserService (IUserRepository value) {
        userRepository = value;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User myUser = userRepository.findByNickname(username);
        return new org.springframework.security.core.userdetails.User(myUser.getNickname(),
                myUser.getPassword(), mapRolesToAuthorities(myUser.getRoles()));
    }

    private List<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles)
    {
        return roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.name())).collect(Collectors.toList());
    }

    public void addUser(User user) throws Exception
    {
        User userFromDb = userRepository.findByNickname(user.getNickname());
        if (userFromDb != null)
        {
            throw new Exception("user exist");
        }
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
    }

    public User getUser(Long id) {
        var candidate = userRepository.findById(id);
        return candidate.orElse(null);
    }

    public User getUserByName(String name) {
        return userRepository.findByNickname(name);
    }

    @Transactional
    public void updateUserInfo(UpdateUserInfoRequestData data, Long userId) {
        var dbUser = userRepository.findById(userId);
        if (dbUser.isEmpty())
            return;

        var user = dbUser.get();

        user.setNickname(data.getNickname());
        user.setEmail(data.getEmail());
        user.setAdditionalInfo(data.getAbout());

        userRepository.save(user);
    }
}
