package com.springkafka.springkafkaretry;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public class Test {
    public static void main(String[] args) {

        ZonedDateTime beforeDate = ZonedDateTime.now().minus(25, ChronoUnit.DAYS);
//        ZonedDateTime beforeDate = ZonedDateTime.now().minus(25, ChronoUnit.HOURS);
        System.out.println("Before date: " + beforeDate);

        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("Now: " + now);

        int compare = now.compareTo(beforeDate);
        System.out.println(compare);

//        Period.between(now, beforeDate);
        Duration duration = Duration.between(now, beforeDate);
        long diffHours = duration.abs().toHours();
        System.out.println(diffHours);

        if(diffHours >= 24){
            System.out.println("Intervalo maior que 24hs, descartar!");
        } else {
            System.out.println("Esta dentro do tempo");
        }


    }
}
