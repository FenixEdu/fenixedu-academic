package ServidorAplicacao;

import java.util.Date;

import ServidorAplicacao.Servico.exceptions.NotAuthorizeException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
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
			System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + ss.getClass().getName());
			begin();
			System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + ss.getClass().getName() + "Finished begin Transaction");
			ss.authorize();
			System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + ss.getClass().getName() + "Finished Authentication");
			ss.execute();
			end();
			System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + ss.getClass().getName() + " finished sucessfully.");
		} catch (NotExecuteException e) {
			System.out.println("LOGTIME= " + new Date() + " : SERVICE= " + ss.getClass().getName() + " aborted.");			
			cancel();
			throw e;
		}
	}
}

/* Created by Nuno Antão */