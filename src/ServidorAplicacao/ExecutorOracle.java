package ServidorAplicacao;

import ServidorAplicacao.Servico.exceptions.NotAuthorizeException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorPersistenteJDBC.SuportePersistenteOracle;
import Util.ErrorConstants;

/**
 * 
 * @author Fernanda Quitério & Tania Pousão
 * @deprecated
 */
public class ExecutorOracle {
    /* Singleton */

    private static ExecutorOracle _instance = new ExecutorOracle();

    private ExecutorOracle() {
    }

    public static ExecutorOracle getInstance() {
        return _instance;
    }

    private void begin() throws PersistenceException {
        try {
            SuportePersistenteOracle.getInstance().iniciarTransaccao();
        } catch (Exception e) {
            throw new PersistenceException(ErrorConstants.PERSISTENCE_ERROR);
        }
    }

    private void cancel() throws PersistenceException {
        try {
            SuportePersistenteOracle.getInstance().cancelarTransaccao();
        } catch (Exception e) {
            throw new PersistenceException(ErrorConstants.PERSISTENCE_ERROR);
        }
    }

    private void end() throws PersistenceException {
        try {
            SuportePersistenteOracle.getInstance().confirmarTransaccao();
        } catch (Exception e) {
            throw new PersistenceException(ErrorConstants.PERSISTENCE_ERROR);
        }
    }

    synchronized public void doIt(ServicoSeguro ss) throws NotAuthorizeException, NotExecuteException,
            PersistenceException {
        try {
            ss.authorize();
            begin();
            ss.execute();
            end();
        } catch (NotExecuteException e) {
            cancel();
            throw e;
        }
    }
}