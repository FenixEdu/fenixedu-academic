/*
 * Created on 31/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import DataBeans.InfoDegreeCurricularPlan;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class InsertDegreeCurricularPlan implements IServico {

	private static InsertDegreeCurricularPlan service = new InsertDegreeCurricularPlan();

	public static InsertDegreeCurricularPlan getService() {
		return service;
	}

	private InsertDegreeCurricularPlan() {
	}

	public final String getNome() {
		return "InsertDegreeCurricularPlan";
	}
	

	public void run(InfoDegreeCurricularPlan infoDegreeCurricularPlan) throws FenixServiceException {

		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
			
				ICursoPersistente persistentDegree = persistentSuport.getICursoPersistente();
				ICurso degree = persistentDegree.readByIdInternal(infoDegreeCurricularPlan.getInfoDegree().getIdInternal());	

				IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
				degreeCurricularPlan.setName(infoDegreeCurricularPlan.getName());
				degreeCurricularPlan.setDegree(degree);
				degreeCurricularPlan.setState(infoDegreeCurricularPlan.getState());
				degreeCurricularPlan.setInitialDate(infoDegreeCurricularPlan.getInitialDate());
				degreeCurricularPlan.setEndDate(infoDegreeCurricularPlan.getEndDate());
				degreeCurricularPlan.setDegreeDuration(infoDegreeCurricularPlan.getDegreeDuration());
				degreeCurricularPlan.setMinimalYearForOptionalCourses(infoDegreeCurricularPlan.getMinimalYearForOptionalCourses());
				degreeCurricularPlan.setNeededCredits(infoDegreeCurricularPlan.getNeededCredits());
				degreeCurricularPlan.setMarkType(infoDegreeCurricularPlan.getMarkType());
				degreeCurricularPlan.setNumerusClausus(infoDegreeCurricularPlan.getNumerusClausus());
	
				persistentDegreeCurricularPlan.write(degreeCurricularPlan);
				
		} catch(ExistingPersistentException existingException) {
			throw new ExistingServiceException("O plano curricular com nome " + infoDegreeCurricularPlan.getName(), existingException); 
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}