/*
 * CriarSala.java
 *
 * Created on 25 de Outubro de 2002, ??:??
 */

package ServidorAplicacao.Servico.sop;

/**
 * Serviço CriarSala.
 *
 * @author tfc130
 **/
import DataBeans.InfoRoom;
import Dominio.ISala;
import Dominio.Sala;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

public class CriarSala implements IServico {

	private static CriarSala _servico = new CriarSala();
	/**
	 * The singleton access method of this class.
	 **/
	public static CriarSala getService() {
		return _servico;
	}

	/**
	 * The actor of this class.
	 **/
	private CriarSala() {
	}

	/**
	 * Devolve o nome do servico
	 **/
	public final String getNome() {
		return "CriarSala";
	}

	public Object run(InfoRoom infoSala) throws FenixServiceException {

		ISala sala = null;
		boolean result = false;

		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			sala =
				new Sala(
					infoSala.getNome(),
					infoSala.getEdificio(),
					infoSala.getPiso(),
					infoSala.getTipo(),
					infoSala.getCapacidadeNormal(),
					infoSala.getCapacidadeExame());
			try {
				sp.getISalaPersistente().lockWrite(sala);
				result = true;
			} catch (ExistingPersistentException ex) {
				throw new ExistingRoomServiceException(ex);
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex.getMessage());
		}

		return new Boolean(result);
	}



	public class ExistingRoomServiceException extends FenixServiceException {

		/**
		 * 
		 */
		private ExistingRoomServiceException() {
			super();
		}

		/**
		 * @param cause
		 */
		ExistingRoomServiceException(Throwable cause) {
			super(cause);
		}

	}

}