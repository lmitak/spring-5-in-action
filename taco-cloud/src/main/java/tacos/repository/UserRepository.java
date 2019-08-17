package tacos.repository;

import tacos.model.User;

public interface UserRepository {

	User findUser(String username);

	User save(User user);
}
