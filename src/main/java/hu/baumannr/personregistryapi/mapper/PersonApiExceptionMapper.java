package hu.baumannr.personregistryapi.mapper;

import hu.baumannr.personregistryapi.exception.PersonApiException;
import hu.baumannr.personregistryapi.rest.model.ErrorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PersonApiExceptionMapper {

    @Mapping(target = "error", qualifiedByName = "statusCodeToError", source = "statusCode")
    ErrorResponse convert(PersonApiException exception);

    @Named("statusCodeToError")
    default String statusCodeToError(HttpStatus httpStatus) {
        return httpStatus.getReasonPhrase();
    }
}
