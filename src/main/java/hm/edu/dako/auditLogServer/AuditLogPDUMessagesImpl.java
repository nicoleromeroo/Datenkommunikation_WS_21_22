package hm.edu.dako.auditLogServer;

import hm.edu.dako.pdu.AuditLogPDU;

import java.util.ArrayList;
import java.util.List;

/**
 * AuditLog-PDU-Messages-Implementierung.
 */

public class AuditLogPDUMessagesImpl implements AuditLogPDUMessagesInterface<AuditLogPDU>{

    private final List<AuditLogPDU> messages;
    private volatile int counterNewMessages = 0;

    public AuditLogPDUMessagesImpl() {this(new ArrayList<>());}

    public AuditLogPDUMessagesImpl(List<AuditLogPDU> messages) {
        this.messages = messages;
    }

    @Override
    public List<AuditLogPDU> getMessages() {
        return new ArrayList<>(messages);
    }

    @Override
    public synchronized boolean hasNewMessages() {
        return counterNewMessages > 0;
    }

    @Override
    public synchronized List<AuditLogPDU> getNewMessages() throws InterruptedException {
        while (!hasNewMessages()) {
            Thread.onSpinWait();
            wait();
        }
        final int fromIndex = messages.size() - counterNewMessages;
        return new ArrayList<>(messages.subList(fromIndex, messages.size()));
    }

    @Override
    public synchronized int getCounterOfNewMessages() {
        return counterNewMessages;
    }

    @Override
    public AuditLogPDU getMessage(int position) {
        return messages.get(position);
    }

    @Override
    public synchronized void delete(AuditLogPDU message) {
        messages.remove(message);
    }

    @Override
    public synchronized void addMessageToList(AuditLogPDU message) {
        messages.add(message);
        counterNewMessages++;
        notifyAll();
    }
}
