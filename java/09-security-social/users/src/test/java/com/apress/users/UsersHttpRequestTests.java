package com.apress.users;


import com.apress.users.model.User;
import com.apress.users.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsersHttpRequestTests {

    @Value("${local.server.port}")
    private int port;

    private final String BASE_URL = "http://localhost:";
    private final String USERS_PATH = "/users";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void indexPageShouldReturnHeaderOneContent() throws Exception {
        assertThat(this.restTemplate.withBasicAuth("ximena@email.com","aw2s0me").getForObject(BASE_URL + port,
                String.class)).contains("Simple Users Rest Application");
    }

    @Test
    public void usersEndPointShouldReturnCollectionWithTwoUsers() throws Exception {
        Collection<User> response = this.restTemplate.withBasicAuth("ximena@email.com","aw2s0me").
                getForObject(BASE_URL + port + USERS_PATH, Collection.class);

        assertThat(response.size()).isGreaterThan(1);
    }

    @Test
    public void userEndPointPostNewUserShouldReturnUser() throws Exception {
        User user =  new User("dummy@email.com","Dummy",
                "https://www.gravatar.com/avatar/23bb62a7d0ca63c9a804908e57bf6bd4?d=wavatar",
                "aw2s0me", Arrays.asList(UserRole.USER),true);
        User response =  this.restTemplate.withBasicAuth("ximena@email.com","aw2s0me").postForObject(BASE_URL + port + USERS_PATH,user, User.class);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo(user.getEmail());

        Collection<User> users = this.restTemplate.withBasicAuth("ximena@email.com","aw2s0me").
                getForObject(BASE_URL + port + USERS_PATH, Collection.class);

        assertThat(users.size()).isGreaterThanOrEqualTo(2);

    }

    @Test
    public void userEndPointDeleteUserShouldReturnVoid() throws Exception {
        this.restTemplate.delete(BASE_URL + port + USERS_PATH + "/norma@email.com");

        Collection<User> users = this.restTemplate.withBasicAuth("ximena@email.com","aw2s0me").
                getForObject(BASE_URL + port + USERS_PATH, Collection.class);

        assertThat(users.size()).isLessThanOrEqualTo(2);
    }

    @Test
    public void userEndPointFindUserShouldReturnUser() throws Exception{
        User user = this.restTemplate.withBasicAuth("ximena@email.com","aw2s0me").getForObject(BASE_URL + port + USERS_PATH + "/ximena@email.com",User.class);

        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("ximena@email.com");
    }
}
