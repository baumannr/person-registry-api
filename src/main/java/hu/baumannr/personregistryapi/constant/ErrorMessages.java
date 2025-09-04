package hu.baumannr.personregistryapi.constant;

/**
 * Error message constants.
 */
public final class ErrorMessages {

    private ErrorMessages() {

    }

    public final static String UNEXPECTED_ERROR = "Unexpected error happened";

    public final static String PERSON_NOT_FOUND = "Person with id %d not found";

    public final static String ADDRESS_NOT_FOUND = "Address with id %d not found";

    public final static String CONTACT_INFO_NOT_FOUND = "Contact information with id %d not found";

    public final static String ADDRESS_ALREADY_EXISTS = "Address with type %s already exists for person %d";

    public final static String ADDRESS_DOES_NOT_BELONG_TO_PERSON = "Address with id %d does not belong to person %d";

    public final static String CONTACT_INFO_DOES_NOT_BELONG_TO_PERSON =
            "Contact information with id %d does not belong to person %d";

}
