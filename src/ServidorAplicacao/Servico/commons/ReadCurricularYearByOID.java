/*
 * Created on 2003/07/29
 *
 *
 */
package ServidorAplicacao.Servico.commons;

import DataBeans.InfoCurricularYear;
import DataBeans.util.Cloner;
import Dominio.CurricularYear;
import Dominio.ICurricularYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularYear;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 * 
 */
public class ReadCurricularYearByOID implements IServico {

	private static ReadCurricularYearByOID service =
		new ReadCurricularYearByOID();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadCurricularYearByOID getService() {
		return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadCurricularYearByOID";
	}

	public InfoCurricularYear run(Integer oid) throws FenixServiceException {

		InfoCurricularYear result = null;
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentCurricularYear curricularYearDAO =
				sp.getIPersistentCurricularYear();
			ICurricularYear curricularYear =
				(ICurricularYear) curricularYearDAO.readByOID(
					CurricularYear.class,
					oid);
			if (curricularYear != null) {
				result = Cloner.copyICurricularYear2InfoCurricularYear(
						curricularYear);
			}
			else {
				throw new UnexistingCurricularYearException();
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return result;
	}
	
	public class UnexistingCurricularYearException extends FenixServiceException {

		/**
		 * 
		 */
		private UnexistingCurricularYearException() {
			super();
		}

	}

}
