package learn.fomo_nomo.domain;

import learn.fomo_nomo.data.UserRepository;
import learn.fomo_nomo.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {this.repository = repository;}

    public Result<User> add(User user){
        Result<User> result = validate(user);
        if(!result.isSuccess()){
            return result;
        }

        if (user.getUserId() != 0){
            result.addMessage("UserId cannot be set for add operation",ResultType.INVALID);
            return result;
        }

        user = repository.add(user);
        result.setPayload(user);
        return result;
    }
    
    public Result<User> update(User user){
        Result<User> result = validate(user);
        if(!result.isSuccess()){
            return result;
        }

        if (user.getUserId() <= 0){
            result.addMessage("UserId must be set for update operation",ResultType.INVALID);
            return result;
        }

        if(!repository.update(user)){
            String msg = String.format("UserId: %s, not found",user.getUserId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }
        return result;
    }

    public boolean deleteById(int userId){
        return repository.deleteById(userId);
    }

    private Result<User> validate (User user){
        Result<User> result = new Result<>();
        if (user == null){
            result.addMessage("User cannot be null",ResultType.INVALID);
        }

        if(user.getFirstName() == null || user.getFirstName().isEmpty()){
            result.addMessage("User first name is required",ResultType.INVALID);
        }

        if(user.getLastName() == null || user.getLastName().isEmpty()){
            result.addMessage("User last name is required",ResultType.INVALID);
        }

        if(user.getLastName() == null || user.getLastName().isEmpty()){
            result.addMessage("User last name is required",ResultType.INVALID);
        }

        if(user.getEmail() == null || user.getEmail().isEmpty()){
            result.addMessage("User email is required.",ResultType.INVALID);
        }

        return result;
    }
}
