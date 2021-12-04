package hm.edu.dako.auditLogServer;

import hm.edu.dako.chatCommon.ExceptionHandler;
import hm.edu.dako.connection.Connection;
import hm.edu.dako.connection.EndOfFileException;
import hm.edu.dako.connection.ServerSocketInterface;
import hm.edu.dako.pdu.AuditLogPDU;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ConnectionWorkerThreadImpl extends Thread{
    private static final Logger log = LogManager.getLogger(ConnectionWorkerThreadImpl.class);
    private final AuditLogPDUMessagesInterface<AuditLogPDU> model;
    private final ServerSocketInterface socket;
    private boolean isFinished = false;
    private Connection connection;
    //private MessagingLoggingWorkerThread messagingLoggingWorker;

    public ConnectionWorkerThreadImpl(AuditLogPDUMessagesInterface<AuditLogPDU> model, ServerSocketInterface socket) {
        this.model = model;
        this.socket = socket;
    }

    @Override
    public void start() {
        while (!Thread.currentThread().isInterrupted() && !socket.isClosed()) {
            isFinished = false;
            //von SimpleChatServerImpl
            try {
                connection = socket.accept();
                log.debug("Neuer Verbindungsaufbauwunsch empfangen");
                while (!isFinished && !Thread.currentThread().isInterrupted()) {
                    //startMessageLoggingWorkerImpl();
                    handleIncomingMessage();
                }
            } catch (Exception e) {
                if (socket.isClosed()) {
                    log.debug("Socket wurde geschlossen");
                } else {
                    log.error("Exception beim Entgegennehmen von Verbindungsaufbauwuenschen: " + e);
                    ExceptionHandler.logException(e);
                }
            }
        }
        stopConnection();
    }

    /*
    private void startMessageLoggingWorkerImpl() {
        if (mes) {

        }
    }*/

    //SimpleChatWorkerThreadImpl
    private void handleIncomingMessage() {
        try {
            AuditLogPDU receivedPDU = (AuditLogPDU) connection.receive();
            log.debug(receivedPDU);
            model.addMessageToList(receivedPDU);
        } catch (EndOfFileException e) {
            log.debug("End of File beim Empfang, vermutlich Verbindungsabbau des Partners");

            isFinished = true;
        } catch (java.net.SocketException e) {
            log.error("Verbindungsabbruch beim Empfang der naechsten Nachricht vom Client");

            isFinished = true;
        } catch (IOException e) {
            log.error("Empfang einer Nachricht fehlgeschlagen");

            isFinished = true;
        } catch (Exception e) {
            ExceptionHandler.logException(e);
        }
    }

    /**
     * Schliesst die Verbindung.
     */
    private void stopConnection() {
        try {
            connection.close();
            log.error("Verbindung " + " geschlossen");
        } catch (Exception e) {
            log.debug("Fehler beim Schliessen der Verbindung zu Client ");
            ExceptionHandler.logException(e);
        }
    }
}
