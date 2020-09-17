package fr.moneycore.riskframework.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import fr.moneycore.riskframework.model.ConfirmationToken;
import fr.moneycore.riskframework.model.User;
import fr.moneycore.riskframework.repository.ConfirmationTokenRepository;
import fr.moneycore.riskframework.repository.UserRepository;
import fr.moneycore.riskframework.service.EmailSenderService;

@RestController
public class RegisterController {
	
	
	 @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private ConfirmationTokenRepository confirmationTokenRepository;

	    @Autowired
	    private JavaMailSender emailSenderService;
	    
	    
	    @GetMapping("/register")
	    public String register () {
	    	return "cc";
	    }
	    
	@PostMapping("/register")
	public ResponseEntity<String> getToken(@RequestBody User user){
		if(userRepository.findByEmailIdIgnoreCase(user.getEmailId())!=null){
			return new ResponseEntity<String>("Utilisateur existe", HttpStatus.CONFLICT);
		}
		else {
			userRepository.save(user);

            ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenRepository.save(confirmationToken);
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmailId());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("yassine.oues@gmail.com");
            mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:8082/confirm-account?token="+confirmationToken.getConfirmationToken());

            emailSenderService.send(mailMessage);
        	return new ResponseEntity<String>(confirmationToken.getConfirmationToken(), HttpStatus.OK);

		}
		
	
	}

}
