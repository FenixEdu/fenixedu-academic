/*
 * Created on 18/07/2003
 *
 */
package ServidorAplicacao.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz & Sara Ribeiro
 *
 */
public class ReadPublicExecutionPeriods implements IServico {

	private static ReadPublicExecutionPeriods service = new ReadPublicExecutionPeriods();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadPublicExecutionPeriods getService() {
		return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadPublicExecutionPeriods";
	}

	public List run()
		throws FenixServiceException {

		List result = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionPeriod executionPeriodDAO =
				sp.getIPersistentExecutionPeriod();

			List executionPeriods =
				executionPeriodDAO.readPublic();

			if (executionPeriods != null) {
				for (int i = 0; i < executionPeriods.size(); i++) {
					result.add(
						Cloner.copyIExecutionPeriod2InfoExecutionPeriod(
							(IExecutionPeriod) executionPeriods.get(i)));
				}
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return result;
	}
}
