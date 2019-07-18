package com.alexkudlick.authentication.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ErrorResponse {

    @JsonProperty("errors")
    @NotEmpty
    private List<String> errors;

    private ErrorResponse() {

    }

    public ErrorResponse(List<String> errors) {
        this.errors = ImmutableList.copyOf(errors);
    }

    public ErrorResponse(String firstError, String... errors) {
        this(
            ImmutableList.<String>builder()
                .add(Objects.requireNonNull(firstError))
                .addAll(Arrays.stream(errors).map(Objects::requireNonNull).collect(Collectors.toList()))
                .build()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ErrorResponse)) return false;
        ErrorResponse that = (ErrorResponse) o;
        return errors.equals(that.errors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(errors);
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
            "errors=" + errors +
            '}';
    }
}
