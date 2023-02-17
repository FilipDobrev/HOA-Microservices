package nl.tudelft.sem.group23a.activity.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import nl.tudelft.sem.group23a.hoa.models.HoaCreationModel;
import org.junit.jupiter.api.Test;

class HoaCreationModelTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void testInvalidAll() {
        HoaCreationModel model = new HoaCreationModel("n".repeat(129), "c".repeat(129), "f".repeat(129));
        Set<ConstraintViolation<HoaCreationModel>> violations = validator.validate(model);
        assertEquals(3, violations.size());
    }

    @Test
    public void testInvalidName() {
        HoaCreationModel model = new HoaCreationModel("n".repeat(129), "c", "f");
        Set<ConstraintViolation<HoaCreationModel>> violations = validator.validate(model);
        assertEquals(1, violations.size());
    }

    @Test
    public void testValidName() {
        HoaCreationModel model = new HoaCreationModel("n".repeat(128), "c", "f");
        Set<ConstraintViolation<HoaCreationModel>> violations = validator.validate(model);
        assertEquals(0, violations.size());
    }

    @Test
    public void testInvalidCountry() {
        HoaCreationModel model = new HoaCreationModel("n", "c".repeat(129), "f");
        Set<ConstraintViolation<HoaCreationModel>> violations = validator.validate(model);
        assertEquals(1, violations.size());
    }

    @Test
    public void testValidCountry() {
        HoaCreationModel model = new HoaCreationModel("n", "c".repeat(128), "f");
        Set<ConstraintViolation<HoaCreationModel>> violations = validator.validate(model);
        assertEquals(0, violations.size());
    }

    @Test
    public void testInvalidCity() {
        HoaCreationModel model = new HoaCreationModel("n", "c", "f".repeat(129));
        Set<ConstraintViolation<HoaCreationModel>> violations = validator.validate(model);
        assertEquals(1, violations.size());
    }

    @Test
    public void testValidCity() {
        HoaCreationModel model = new HoaCreationModel("n", "c", "f".repeat(128));
        Set<ConstraintViolation<HoaCreationModel>> violations = validator.validate(model);
        assertEquals(0, violations.size());
    }
}