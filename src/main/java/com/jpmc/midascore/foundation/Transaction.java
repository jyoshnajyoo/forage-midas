package com.jpmc.midascore.foundation;

public class Transaction {

    private Long senderId;
    private Long recipientId;
    private float amount;

    public Transaction() {}

    public Transaction(Long senderId, Long recipientId, float amount) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.amount = amount;
    }

    public Long getSenderId() {
        return senderId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public float getAmount() {
        return amount;
    }
}