package bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.beanutils.BeanUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            String password = BeanUtils.getProperty(value, "password");
            String confirmPassword = BeanUtils.getProperty(value, "repeatPassword");
            return password != null && password.equals(confirmPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
