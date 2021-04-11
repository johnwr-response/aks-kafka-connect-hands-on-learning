package no.responseweb.kafka.utils;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateUtilsTest {

    @Test
    void maxInstant() {
        Instant i1 = ZonedDateTime.now().toInstant();
        Instant i2 = i1.plusSeconds(1);
        assertEquals(i2, DateUtils.maxInstant(i1, i2));
        assertEquals(i2, DateUtils.maxInstant(i2, i1));
    }
}
