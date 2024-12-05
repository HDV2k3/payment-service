package com.example.payment.utility;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class CustomInstantDeserializer extends JsonDeserializer<Instant> {

    // Danh sách các format date có thể xử lý
    private static final List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            DateTimeFormatter.ISO_INSTANT,                    // 2024-11-16T03:35:25.642Z
            DateTimeFormatter.ISO_DATE_TIME,                 // 2024-11-16T03:35:25
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),  // 2024-11-16 03:35:25
            DateTimeFormatter.ISO_DATE                       // 2024-11-16
    );

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateStr = p.getValueAsString();

        // Handle null/empty cases
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }

        // Try each formatter
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                // Handle different date-time formats
                if (formatter.equals(DateTimeFormatter.ISO_INSTANT)) {
                    return Instant.parse(dateStr);
                } else if (formatter.equals(DateTimeFormatter.ISO_DATE)) {
                    // Convert date-only string to start of day in UTC
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr + "T00:00:00");
                    return dateTime.toInstant(ZoneOffset.UTC);
                } else {
                    // Parse as LocalDateTime and convert to Instant
                    LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
                    return dateTime.toInstant(ZoneOffset.UTC);
                }
            } catch (DateTimeParseException e) {
                // Continue to next formatter if parsing fails
                continue;
            }
        }

        // If all formatters fail, throw exception
        throw new IOException("Unable to parse date: " + dateStr +
                ". Supported formats are: ISO_INSTANT, ISO_DATE_TIME, yyyy-MM-dd HH:mm:ss, ISO_DATE");
    }
}