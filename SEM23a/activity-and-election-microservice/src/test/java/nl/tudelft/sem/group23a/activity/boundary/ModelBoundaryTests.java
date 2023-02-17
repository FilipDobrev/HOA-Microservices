package nl.tudelft.sem.group23a.activity.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import nl.tudelft.sem.group23a.activity.models.ActivityCreationRequestModel;
import nl.tudelft.sem.group23a.activity.models.NotificationRequestModel;
import nl.tudelft.sem.group23a.commons.ActivityType;
import org.junit.jupiter.api.Test;


class ModelBoundaryTests {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    NotificationRequestModel model;
    String[] usernames;

    void setup() {
        model = new NotificationRequestModel(null, null, null);
        usernames = new String[2];
        usernames[0] = "Name1";
        usernames[1] = "Name2";
    }

    @Test
    public void boundaryLengthSubject() {
        setup();
        model.setUsernames(usernames); // name is too short
        model.setSubject("q".repeat(64)); // age is too small
        model.setContent("Content");

        Set<ConstraintViolation<NotificationRequestModel>> violations = validator.validate(model);
        assertEquals(0, violations.size());
    }

    @Test
    public void boundaryLengthSubjectOver() {
        setup();
        model.setUsernames(usernames); // name is too short
        model.setSubject("q".repeat(65)); // age is too small
        model.setContent("Content");

        Set<ConstraintViolation<NotificationRequestModel>> violations = validator.validate(model);
        assertEquals(1, violations.size());
    }

    @Test
    public void boundaryLengthContent() {
        setup();
        model.setUsernames(usernames); // name is too short
        model.setSubject("Subject"); // age is too small
        model.setContent("C".repeat(640));

        Set<ConstraintViolation<NotificationRequestModel>> violations = validator.validate(model);
        assertEquals(0, violations.size());
    }

    @Test
    public void boundaryLengthContentOver() {
        setup();
        model.setUsernames(usernames); // name is too short
        model.setSubject("Subject"); // age is too small
        model.setContent("C".repeat(641));

        Set<ConstraintViolation<NotificationRequestModel>> violations = validator.validate(model);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidHoaId() {
        ActivityCreationRequestModel model = new ActivityCreationRequestModel(0, "desc", ActivityType.GATHERING, 7);
        Set<ConstraintViolation<ActivityCreationRequestModel>> violations = validator.validate(model);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidDescription() {
        ActivityCreationRequestModel model = new ActivityCreationRequestModel(1, "d".repeat(641), ActivityType.GATHERING, 7);
        Set<ConstraintViolation<ActivityCreationRequestModel>> violations = validator.validate(model);
        assertEquals(1, violations.size());
    }

    @Test
    public void testJustAboutValidDescription() {
        ActivityCreationRequestModel model = new ActivityCreationRequestModel(1, "d".repeat(640), ActivityType.GATHERING, 7);
        Set<ConstraintViolation<ActivityCreationRequestModel>> violations = validator.validate(model);
        assertEquals(0, violations.size());
    }

    @Test
    public void testInvalidDays() {
        ActivityCreationRequestModel model = new ActivityCreationRequestModel(1, "d".repeat(1), ActivityType.GATHERING, -1);
        Set<ConstraintViolation<ActivityCreationRequestModel>> violations = validator.validate(model);
        assertEquals(1, violations.size());
    }

    @Test
    public void testInvalidAll() {
        ActivityCreationRequestModel model = new ActivityCreationRequestModel(
                0, "d".repeat(641), ActivityType.GATHERING, -1);
        Set<ConstraintViolation<ActivityCreationRequestModel>> violations = validator.validate(model);
        assertEquals(3, violations.size());
    }
}