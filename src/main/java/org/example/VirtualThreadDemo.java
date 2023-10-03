package org.example;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class VirtualThreadDemo {
    public static void main(String[] args) throws Exception{
        //这个run是可重入的，本质上就是wrap然后run continuation
        Thread.startVirtualThread(() -> {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
            System.out.println("after park " + Thread.currentThread());
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
            System.out.println("after park " + Thread.currentThread());
        }).join();

    }
}
