package br.com.eduardogfischer.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

        @Autowired
        private IUserRepository userRepository;

        @PostMapping("/")
        public ResponseEntity create(@RequestBody UserModel userModel) {
                var user = this.userRepository.findByUsername(userModel.getUsername());

                if(user != null) {
                        //Mensagem erro
                        //Status Code
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
                }

                var passwordHashed =  BCrypt.withDefaults()
                .hashToString(12, userModel.getPassword().toCharArray());

                userModel.setPassword(passwordHashed);
                var userCreated = this.userRepository.save(userModel);
                return ResponseEntity.status(HttpStatus.OK).body(userCreated);
        }
}
