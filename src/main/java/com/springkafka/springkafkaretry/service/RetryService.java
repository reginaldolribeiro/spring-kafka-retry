package com.springkafka.springkafkaretry.service;

import com.springkafka.springkafkaretry.exception.MyException;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
@EnableRetry
public class RetryService {

    @Retryable(value = MyException.class, maxAttempts = 3, backoff = @Backoff(10000))
    public void retry(int value) {
        if(value == 1){
            System.out.println("\n *** Fazendo RETRY...... " + LocalDateTime.now());
            throw new MyException();
        } else {
            System.out.println("\n **** Sem retry.....");
        }
    }

    public void retry2(int value){
        if(value == 1){
            System.out.println("\n *** Fazendo RETRY......");
//            CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello retry");
//            future.completeOnTimeout("a", 3, TimeUnit.SECONDS);

            TimerTask task = new TimerTask() {
                public void run() {
                    System.out.println("Task performed on: " + new Date() + "n"
                            + "Thread's name: " + Thread.currentThread().getName());
                }
            };
            Timer timer = new Timer("Timer");
            long delay = 5000L;
//            timer.schedule(task, delay);

            timer.scheduleAtFixedRate(task, delay, 30000);

        } else {
            System.out.println("\n **** Sem retry.....");
        }
    }
}
