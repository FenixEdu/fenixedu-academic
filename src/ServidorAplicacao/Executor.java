package ServidorAplicacao;

import ServidorPersistenteJDBC.SuportePersistente;
import Util.ErrorConstants;

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

	public void doIt(ServicoSeguro ss) throws NotAuthorizeException, NotExecuteException, PersistenceException {
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

/* Created by Nuno Antão */