package net.sourceforge.fenixedu.persistenceTier;

import pt.ist.fenixframework.pstm.Transaction;

import pt.utl.ist.berserk.storage.ITransactionBroker;
import pt.utl.ist.berserk.storage.exceptions.StorageException;

public class SuportePersistenteOJB implements ISuportePersistente, ITransactionBroker {
    private static SuportePersistenteOJB _instance = null;

    public static synchronized SuportePersistenteOJB getInstance() {
        if (_instance == null) {
            _instance = new SuportePersistenteOJB();
        }
        return _instance;
    }

    /** Creates a new instance of SuportePersistenteOJB */
    private SuportePersistenteOJB() {
    }

    public void iniciarTransaccao() {
        // commit any current transaction
        if (Transaction.current() != null) {
            Transaction.commit();
        }
        Transaction.begin();
    }

    public void confirmarTransaccao() {
        Transaction.checkpoint();
        Transaction.currentFenixTransaction().setReadOnly();
    }

    public void cancelarTransaccao() {
        Transaction.abort();
        Transaction.begin();
        Transaction.currentFenixTransaction().setReadOnly();
    }

    public void beginTransaction() {
        this.iniciarTransaccao();
    }

    public void commitTransaction() {
        this.confirmarTransaccao();
    }

    public void abortTransaction() throws StorageException {
        this.cancelarTransaccao();
    }

    public void lockRead(java.util.List list) throws StorageException {
    }

    public void lockRead(Object obj) throws StorageException {
    }

    public void lockWrite(Object obj) throws StorageException {
    }
}
