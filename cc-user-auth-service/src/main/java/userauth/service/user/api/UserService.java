package userauth.service.user.api;

import userauth.domain.User;
import userauth.utility.response.ResponseWrapper;

public interface UserService {

    User findUserById(long id);

    User findUserByFullnameAndEmail(String fullname, String email);

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}
