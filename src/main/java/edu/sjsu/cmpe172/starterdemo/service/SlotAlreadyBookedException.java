package edu.sjsu.cmpe172.starterdemo.service;

/**
 * Thrown when a user attempts to book a slot
 * that has already been reserved by another user.
 */
public class SlotAlreadyBookedException extends RuntimeException {
    public SlotAlreadyBookedException(String message) {
        super(message);
    }
}
