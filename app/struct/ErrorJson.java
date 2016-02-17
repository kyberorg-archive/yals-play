package struct;

import com.google.gson.annotations.Since;
import play.Logger;
import play.data.validation.Error;
import play.data.validation.Validation;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * This struct of JSON send in case of message
 *
 * @since 1.0
 */
@Since(1.0)
public class ErrorJson {

    @Since(1.0)
    private final List<InternalErrorStruct> errors = new ArrayList<>(1);

    @Since(1.0)
    private String message;

    @Since(1.0)
    private String errorId;

    public static ErrorJson init(@NotNull String error) {
        ErrorJson errorJson = new ErrorJson();
        errorJson.message = error;
        return errorJson;
    }

    public static ErrorJson init() {
        ErrorJson errorJson = new ErrorJson();
        errorJson.collect();
        return errorJson;
    }

    public ErrorJson error(@NotNull String error) {
        this.message = error;
        return this;
    }

    public ErrorJson errorId(@NotNull String id) {
        this.errorId = id;
        return this;
    }

    public String getMessage() {
        return this.message;
    }

    public List<InternalErrorStruct> getErrors() {
        return errors;
    }

    public String getErrorId() {
        return errorId;
    }

    private void collect() {
        List<Error> errorList = Validation.errors();
        int howManyErrorsCollected = errorList.size();
        Logger.debug("Collected %d errors", howManyErrorsCollected);
        for (Error error : errorList) {
            Logger.debug("KEY: " + error.getKey());
            String[] keyParts = error.getKey().split("\\.");

            if (keyParts.length != 0) {
                errors.add(InternalErrorStruct.create()
                        .field(keyParts[keyParts.length - 1])
                        .text(error.message())
                );
            }
        }

        if (howManyErrorsCollected == 0) {
            message = "Validation failed. No errors reported";
        } else if (howManyErrorsCollected == 1) {
            message = errorList.get(0).message();
        } else {
            message = "Validation failed";
        }
    }

    @Since(1.0)
    static class InternalErrorStruct {
        @Since(1.0)
        String field;
        @Since(1.0)
        String error;

        static InternalErrorStruct create() {
            return new InternalErrorStruct();
        }

        InternalErrorStruct field(String field) {
            this.field = field;
            return this;
        }

        InternalErrorStruct text(String errorText) {
            this.error = errorText;
            return this;
        }
    }
}
