package ServidorAplicacao.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import DataBeans.InfoExecutionYear;
import DataBeans.util.Cloner;
import Dominio.IExecutionPeriod;
import Dominio.IExecutionYear;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentExecutionPeriod;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadExecutionPeriodsByExecutionYear implements IServico {

	private static ReadExecutionPeriodsByExecutionYear service = new ReadExecutionPeriodsByExecutionYear();
	/**
	 * The singleton access method of this class.
	 **/
	public static ReadExecutionPeriodsByExecutionYear getService() {
		return service;
	}

	/**
	 * @see ServidorAplicacao.IServico#getNome()
	 */
	public String getNome() {
		return "ReadExecutionPeriodsByExecutionYear";
	}

	public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException {

		List result = new ArrayList();
		try {
			ISuportePersistente sp = SuportePersistenteOJB.getInstance();
			IPersistentExecutionPeriod executionPeriodDAO = sp.getIPersistentExecutionPeriod();

			IExecutionYear executionYear = Cloner.copyInfoExecutionYear2IExecutionYear(infoExecutionYear);
			List executionPeriods = executionPeriodDAO.readByExecutionYear(executionYear);

			if (executionPeriods != null) {
				for (int i = 0; i < executionPeriods.size(); i++) {
					result.add(Cloner.copyIExecutionPeriod2InfoExecutionPeriod((IExecutionPeriod) executionPeriods.get(i)));
				}
			}
		} catch (ExcepcaoPersistencia ex) {
			throw new FenixServiceException(ex);
		}

		return result;
	}
}
