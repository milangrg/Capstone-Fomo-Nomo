package learn.fomo_nomo.controllers;

import learn.fomo_nomo.domain.Result;
import learn.fomo_nomo.domain.UserService;
import learn.fomo_nomo.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> findAll(){
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable int userId){
        return userService.findById(userId);
    }

    @PostMapping()
    public ResponseEntity<Object> add(@RequestBody User user){
        Result<User> result = userService.add(user);
        if (result.isSuccess()){
            return new ResponseEntity<>(result.getPayload(),HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

}
