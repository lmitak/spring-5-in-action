package tacos.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import tacos.model.User;

@Repository
public class JdbcUserRepository implements UserRepository {

	JdbcTemplate jdbc;
	SimpleJdbcInsert userInserter;
	ObjectMapper objectMapper;
	
	@Autowired
	public JdbcUserRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
		userInserter = new SimpleJdbcInsert(jdbc).usingGeneratedKeyColumns("id");
		objectMapper = new ObjectMapper();
	}

	@Override
	public User findUser(String username) {
		return jdbc.queryForObject("SELECT id, username, password, fullname FROM user WHERE username=?", this::mapRowToUser, username);
	}
	
	private User mapRowToUser(ResultSet rs, int row) throws SQLException {
		return new User(rs.getString("username"), rs.getString("password"), rs.getString("fullname"));
	}

	@Override
	public User save(User user) {
		@SuppressWarnings("unchecked")
		Map<String, Object> values = objectMapper.convertValue(user, Map.class);
		userInserter.execute(values);

		return user;
	}

}
