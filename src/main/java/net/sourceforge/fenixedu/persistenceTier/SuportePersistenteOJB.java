package net.sourceforge.fenixedu.persistenceTier;

import pt.ist.fenixWebFramework.services.ServiceManager;
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

    @Override
    public void iniciarTransaccao() {
        // commit any current transaction
        if (Transaction.current() != null) {
            Transaction.commit();
        }
        Transaction.begin();
    }

    @Override
    public void confirmarTransaccao() {
        Transaction.checkpoint();
        Transaction.currentFenixTransaction().setReadOnly();
    }

    @Override
    public void cancelarTransaccao() {
        Transaction.abort();
        Transaction.begin();
        Transaction.currentFenixTransaction().setReadOnly();
    }

    @Override
    public void beginTransaction() {
        if (ServiceManager.isInsideBerserkService()) {
            this.iniciarTransaccao();
        }
    }

    @Override
    public void commitTransaction() {
        if (ServiceManager.isInsideBerserkService()) {
            this.confirmarTransaccao();
        }
    }

    @Override
    public void abortTransaction() throws StorageException {
        if (ServiceManager.isInsideBerserkService()) {
            this.cancelarTransaccao();
        }
    }

    @Override
    public void lockRead(java.util.List list) throws StorageException {
    }

    @Override
    public void lockRead(Object obj) throws StorageException {
    }

    @Override
    public void lockWrite(Object obj) throws StorageException {
    }
}
