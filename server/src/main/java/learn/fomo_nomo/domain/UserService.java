package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.UserRepository;
import learn.fomo_nomo.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {this.repository = repository;}

    public Result<User> add(User user){
        return null;
    }
    
    public Result<User> update(User user){
        return null;
    }

    public boolean deleteById(int userId){
        return false;
    }
}
