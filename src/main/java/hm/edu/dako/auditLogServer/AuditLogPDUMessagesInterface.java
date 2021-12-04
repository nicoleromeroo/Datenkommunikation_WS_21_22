package hm.edu.dako.auditLogServer;
import java.util.List;

/**
 * Schnittstelle zum AuditLogPDUMessages
 * Interface zur Uebergabe von Daten fuer die Ausgabe im AuditLogPDUMessages.
 */

public interface AuditLogPDUMessagesInterface<Messages>{
    /**
     * @return eine Liste von allen Nachrichten.
     */
    List<Messages> getMessages();

    /**
     *
     * @return wenn der Zaehler mehr als 0 neue Nachrichten zaehlt.
     */
    boolean hasNewMessages();

    /**
     * Wenn neue Nachrichten gekommen sind {@code true}. Der Zaehler wird wieder 0 und eine Liste von hinzugefuegten
     * Nachrichten wird zurueckgegeben.
     *
     * @return eine Liste von neue Nachrichten
     * @throws InterruptedException Wird ausgelöst, wenn ein Thread wartet, schläft oder anderweitig beschäftigt ist
     *                              und der Thread entweder vor oder während der Aktivität unterbrochen wird.
     */
    List<Messages> getNewMessages() throws InterruptedException;

    /**
     * Hier wird die Anzahl der neuen Nachrichten angezeigt.
     * @return Zaehler der neuen Nachrichten.
     */
    int getCounterOfNewMessages();

    /**
     * @param position wenn die Nachricht zurückgegeben werden soll.
     * @return die Nachricht an der angegebenen Position in dieser Liste
     */
    Messages getMessage(int position);

    /**
     *
     * @param message wenn die vorhandene Nachricht aus der Liste geloescht werden soll.
     */
    void delete(Messages message);

    /**
     * Diese Methode fügt der Liste eine Nachricht hinzu.
     * @param message die an die Liste angehängt werden soll.
     */
    void addMessageToList(Messages message);

}
