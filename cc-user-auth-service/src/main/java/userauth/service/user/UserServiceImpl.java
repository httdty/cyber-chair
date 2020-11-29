package userauth.service.user;

import userauth.domain.User;
import userauth.repository.UserRepository;
import userauth.service.user.api.UserService;
import userauth.utility.response.ResponseGenerator;
import userauth.utility.response.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findUserByFullnameAndEmail(String fullname, String email) {
        return userRepository.findByFullnameAndEmail(fullname, email);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
