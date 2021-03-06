package tk.spotimatch.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tk.spotimatch.api.model.auth.AuthenticationRequest;
import tk.spotimatch.api.model.auth.AuthenticationResponse;
import tk.spotimatch.api.model.auth.RegisterUser;
import tk.spotimatch.api.model.user.User;
import tk.spotimatch.api.model.user.UserDTO;
import tk.spotimatch.api.service.MyUserDetailService;
import tk.spotimatch.api.service.UserService;
import tk.spotimatch.api.util.JwtUtil;

@CrossOrigin
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private UserService userService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        final UserDetails userDetails = userDetailService.loadUserByUsername(
                authenticationRequest.getEmail());

        final String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterUser registerUser) {
        return userService.findByEmail(registerUser.getEmail())
                .map(u -> ResponseEntity.badRequest().build())
                .orElseGet(() -> ResponseEntity.ok(
                        UserDTO.from(userService.create(User.from(registerUser)))));
    }
}
