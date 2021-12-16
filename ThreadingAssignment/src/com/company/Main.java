package com.company;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

class Semaphoreinit {

    static Semaphore semaphore = new Semaphore(1);

    static class CustomerThread extends Thread {
        private Thread thread;
        String name;
        Ticket ticket;

        public void run() {
            try {
                semaphore.acquire();
                if (!ticket.owned) {
                    ticket.owned = true;
                    System.out.println(name + " has purchased the ticked with ID: " + ticket.id);
                    Thread.sleep(250);
                } else {
                    System.out.println(name + " cannot purchase this ticket with ID: " + ticket.id + " as it is already owned.");
                    ticket = null;
                    Thread.sleep(250);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }

        public void start() {
            System.out.println("The thread" + name + "is queuing up to buy: " + ticket.id);
            if (thread == null) {
                thread = new Thread(this, name);
            }
        }
    }

    static class Ticket {
        int id;
        boolean owned = false;
    }

        public static void main(String[] args) {
            Random rand = new Random();
            Ticket[] tickets = new Ticket[rand.nextInt(100)];
            CustomerThread[] customerThreads = new CustomerThread[tickets.length];
            for (int i = 0; i < tickets.length; i++) {
                tickets[i] = new Ticket();
                tickets[i].id = i;
            }
            for (int i = 0; i < customerThreads.length; i++) {
                customerThreads[i] = new CustomerThread();
                customerThreads[i].ticket = tickets[rand.nextInt(tickets.length)];
                customerThreads[i].name = "c" + i;
                customerThreads[i].start();
            }
            try {
                for (CustomerThread customerThread : customerThreads) {
                    customerThread.run();
                    while (customerThread.ticket == null) {
                        customerThread.ticket = tickets[rand.nextInt(tickets.length)];
                        customerThread.run();
                    }
                }
                System.out.println("All tickets are sold.");
            } catch (Exception e) {
                System.out.println("error");
            }
        }
    }