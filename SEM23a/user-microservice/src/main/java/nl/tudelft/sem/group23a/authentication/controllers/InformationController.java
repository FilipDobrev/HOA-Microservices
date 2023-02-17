package nl.tudelft.sem.group23a.authentication.controllers;

import nl.tudelft.sem.group23a.authentication.domain.services.InformationService;
import nl.tudelft.sem.group23a.authentication.models.EmailResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InformationController {

    private final transient InformationService informationService;

    @Autowired
    public InformationController(InformationService informationService) {
        this.informationService = informationService;
    }

    /**
     * Get the email of the user by id.
     *
     * @param id the id of the user
     * @return the email of the user if it exists otherwise return empty email
     */
    @GetMapping("/email/{id}")
    public ResponseEntity<EmailResponseModel> email(@PathVariable String id) {
        return ResponseEntity
                .ok()
                .body(new EmailResponseModel(informationService.getEmailById(Long.parseLong(id)).toString()));
    }
}
