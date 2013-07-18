package net.sourceforge.fenixedu.persistenceTier;

import pt.ist.fenixframework.pstm.Transaction;

public class SuportePersistenteOJB implements ISuportePersistente {
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

}
