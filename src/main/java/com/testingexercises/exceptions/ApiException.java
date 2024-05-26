package com.testingexercises.exceptions;

import java.time.ZonedDateTime;

public record ApiException(String message, ZonedDateTime time) {
}
