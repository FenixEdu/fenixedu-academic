package net.sourceforge.fenixedu.applicationTier;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotExecuteException;
import net.sourceforge.fenixedu.persistenceTierJDBC.SuportePersistente;
import net.sourceforge.fenixedu.util.ErrorConstants;

public class Executor {
    /* Singleton */

    private static Executor _instance = new Executor();

    private Executor() {
    }

    public static Executor getInstance() {
        return _instance;
    }

    private void begin() throws PersistenceException {
        try {
            SuportePersistente.getInstance().iniciarTransaccao();
            //inicia BD Teleponto
            //SuportePersistenteOracle.getInstance().iniciarTransaccao();
        } catch (Exception e) {
            throw new PersistenceException(ErrorConstants.PERSISTENCE_ERROR);
        }
    }

    private void cancel() throws PersistenceException {
        try {
            //cancela transacção da BD do Teleponto
            //SuportePersistenteOracle.getInstance().cancelarTransaccao();

            SuportePersistente.getInstance().cancelarTransaccao();
        } catch (Exception e) {
            throw new PersistenceException(ErrorConstants.PERSISTENCE_ERROR);
        }
    }

    private void end() throws PersistenceException {
        try {
            //termina transacção da BD do Teleponto
            //SuportePersistenteOracle.getInstance().confirmarTransaccao();

            SuportePersistente.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            throw new PersistenceException(ErrorConstants.PERSISTENCE_ERROR);
        }
    }

    public void doIt(ServicoSeguro ss) throws NotExecuteException, PersistenceException {
        try {
            begin();
            ss.authorize();
            ss.execute();
            end();
        } catch (NotExecuteException e) {
            cancel();
            throw e;
            // TODO : this case was never considered.
            //        Does it make sense?
        } catch (Exception e) {
            if (!(e instanceof NotExecuteException)) {
                cancel();
                throw new NotExecuteException(e.getLocalizedMessage());
            }
            throw (NotExecuteException) e;

        }
    }
}

/* Created by Nuno Antão */