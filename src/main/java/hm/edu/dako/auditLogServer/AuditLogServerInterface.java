package hm.edu.dako.auditLogServer;

public interface AuditLogServerInterface {
    /**
     * Einheitliche Schnittstelle aller TCP und UDP Server.
     * @author Nicole Romero
     * Startet einen {@link ConnectionWorkerThreadImpl}.
     */
    void start();
}
