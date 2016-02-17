package models.internal;

import play.Logger;
import play.data.validation.Error;
import play.data.validation.Validation;
import play.db.jpa.GenericModel;

import javax.persistence.MappedSuperclass;
import java.util.List;

/**
 * Basic JPA Model superclass for this application. Use this when no need for Id
 *
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess") //protected by design
@MappedSuperclass
public class HzGenericModel extends GenericModel {
    @Override
    public boolean validateAndSave() {
        Validation validation = Validation.current();
        Validation.ValidationResult result = validation.valid(this);
        if (result.ok) {
            save();
        } else {
            report();
        }
        return result.ok;
    }

    @Override
    public boolean validateAndCreate() {
        Validation validation = Validation.current();
        Validation.ValidationResult result = validation.valid(this);
        if (result.ok) {
            create();
        } else {
            report();
        }
        return result.ok;
    }

    protected void report() {
        List<Error> errors = Validation.errors();

        Logger.error("Model '" + getClass().getSimpleName() + "' has validation errors: ");
        int seq = 0;

        for (Error error : errors) {
            if (error.getKey().isEmpty()) {
                Logger.error(error.message());
            } else {
                StringBuilder errText = new StringBuilder("Error");
                if (errors.size() > 2) {
                    errText.append(" ").append(seq).append(": ");
                } else {
                    errText.append(". ");
                }
                errText.append("Field: ").append(error.getKey());
                errText.append(" ");
                errText.append("ErrorText: ").append(error.message());
                errText.append(" ");
                errText.append("ObjectRef: ").append(this);

                Logger.error(errText.toString());
            }
            seq++;
        }
    }

    @Override
    public String toString() {
        String className = getClass().getSimpleName();
        String pointer = Integer.toHexString(System.identityHashCode(this));
        return className + "@" + pointer;
    }
}


