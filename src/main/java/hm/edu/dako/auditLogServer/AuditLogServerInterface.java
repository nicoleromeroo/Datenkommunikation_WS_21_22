package hm.edu.dako.auditLogServer;

public interface AuditLogServerInterface {
    /**
     * Einheitliche Schnittstelle aller TCP und UDP Server.
     * @author Nicole Romero
     */

    /**
     * Startet einen {@link ConnectionWorkerThreadImpl}.
     */
    void start();

    /**
     * Stoppt den Server.
     *
     * @throws Exception - Fehler beim Beenden aller Threads des Chat-Servers
     */
    void stop() throws Exception;
}
