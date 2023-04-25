package com.cretus.retailbackend.utils;

import com.google.protobuf.Timestamp;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class TimeUtils {

    public Timestamp toGoogleTimestamp(LocalDateTime localDateTime){
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();
        return  timestamp;
    }

    public Date localTimeToDate(LocalDateTime localDateTime){
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        Date date = Date.from(instant);
        return date;
    }

}
