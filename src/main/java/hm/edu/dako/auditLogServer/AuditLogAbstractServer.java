package hm.edu.dako.auditLogServer;

import hm.edu.dako.chatCommon.ExceptionHandler;
import hm.edu.dako.connection.ServerSocketInterface;
import hm.edu.dako.pdu.AuditLogPDU;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 *  ChatServerGUI
 */


public abstract class AuditLogAbstractServer implements AuditLogServerInterface{

    /**
     * Default AuditLog Server Port.
     */
    // Standardvalue for AuditLogServer
    static final int DEFAULT_AUDIT_LOG_SERVER_PORT = 40001;

    /**
     * Default Send Buffer Size fuer Server Port in Bytes.
     */
    // Standard-und Maximal-Puffergroessen in Byte
    static final int DEFAULT_SENDBUFFER_SIZE = 300000;
    /**
     * Default Receive Buffer Size fuer Server Port in Bytes.
     */
    static final int DEFAULT_RECEIVEBUFFER_SIZE = 300000;

    static final Logger log = LogManager.getLogger(AuditLogAbstractServer.class);

    static ServerSocketInterface serverSocket;

    final int serverPort;

    private ConnectionWorkerThreadImpl connectionWorkerThread;
    private AuditLogPDUMessagesInterface<AuditLogPDU> model;

    AuditLogAbstractServer() {
        this(AuditLogAbstractServer.DEFAULT_AUDIT_LOG_SERVER_PORT);
    }
    AuditLogAbstractServer(int serverPort) {
        this.serverPort = serverPort;
        checkAndConfigure();
    }

    AuditLogAbstractServer(int serverPort, AuditLogPDUMessagesInterface<AuditLogPDU> model) {
        this.serverPort = serverPort;
        this.model = model;
    }

    /**
     * Initializiert die Konfiguration fuer log.
     */
    abstract void checkAndConfigure();

    private synchronized AuditLogPDUMessagesInterface<AuditLogPDU> getModel() {
        //if model == null -> new AuditLogPDUMessageImpl
        if (model != null) {
            return model;
        }
        return model = new AuditLogPDUMessagesImpl();
    }

    @Override
    public void start() {
        try {
            if (connectionWorkerThread == null) {
                connectionWorkerThread = new ConnectionWorkerThreadImpl(getModel(), getServerSocket());
                connectionWorkerThread.setName("Connection-Worker-Thread");
            }
            connectionWorkerThread.start();
        } catch (IOException e) {
            log.error("Der Socket kann nicht initialisiert werden.");
            ExceptionHandler.logException(e);
        }

    }

    /**
     * Erstellt neuer Instanz ServerSocketInterface, nur wenn serverSocket = null.
     *
     * @return Instanz von {@link ServerSocketInterface}
     * @throws IOException wenn Instanz nicht erstellt werden kann.
     */
    abstract ServerSocketInterface getServerSocket() throws IOException;
}
