/*
 * Created on 31/Jul/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.IServico;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ICursoPersistente;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author lmac1
 */
public class InsertDegreeCurricularPlanService implements IServico {

	private static InsertDegreeCurricularPlanService service = new InsertDegreeCurricularPlanService();

	public static InsertDegreeCurricularPlanService getService() {
		return service;
	}

	private InsertDegreeCurricularPlanService() {
	}

	public final String getNome() {
		return "InsertDegreeCurricularPlanService";
	}
	

	public List run(InfoDegreeCurricularPlan infoDegreeCurricularPlan,Integer degreeId) throws FenixServiceException {

		IDegreeCurricularPlan degreeCurricularPlan = null;
		ICurso degree = null;
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
		ICursoPersistente persistentDegree = null;

		
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
				List degreeCurricularPlans = persistentDegreeCurricularPlan.readAll();
				persistentDegree = persistentSuport.getICursoPersistente();
				degree = persistentDegree.readByIdInternal(degreeId);	

				String name = infoDegreeCurricularPlan.getName();
				InfoDegree infoDegree = (InfoDegree) Cloner.copyIDegree2InfoDegree(degree);
			
			
				List errors = new ArrayList(2);
				errors.add(null);
				errors.add(null);
				int modified = 0; 
				
				Iterator iter = degreeCurricularPlans.iterator();
				while(iter.hasNext()) {
					IDegreeCurricularPlan degreeCurricularPlanIter = (IDegreeCurricularPlan) iter.next();
					System.out.println("infoDegree QUE QUERO INSERIR"+infoDegree);
					System.out.println("infoDegree QUE a comparar INSERIR"+Cloner.copyIDegree2InfoDegree(degreeCurricularPlanIter.getDegree()));
					if(name.compareToIgnoreCase(degreeCurricularPlanIter.getName())==0 && 
					   infoDegree.equals( (InfoDegree) Cloner.copyIDegree2InfoDegree(degreeCurricularPlanIter.getDegree()) ) ){
						modified++;
						errors.set(2, name);
						errors.set(1, infoDegree);
					}
				}

				if(modified == 0) {
					errors = null; 
					
					degreeCurricularPlan = new DegreeCurricularPlan();
//					ICurso degree = (ICurso) Cloner.copyInfoDegree2IDegree(infoDegree);
//					degree = (ICurso) persistentSuport.getICursoPersistente().readByOId(degree,false);
					System.out.println("TA NO SERVICO INSERT DEGREE CURRICULAR PLAN!!!");
					degreeCurricularPlan.setName(name);
					degreeCurricularPlan.setDegree(degree);
					degreeCurricularPlan.setState(infoDegreeCurricularPlan.getState());
					degreeCurricularPlan.setInitialDate(infoDegreeCurricularPlan.getInitialDate());
					degreeCurricularPlan.setEndDate(infoDegreeCurricularPlan.getEndDate());
					degreeCurricularPlan.setDegreeDuration(infoDegreeCurricularPlan.getDegreeDuration());
					degreeCurricularPlan.setMinimalYearForOptionalCourses(infoDegreeCurricularPlan.getMinimalYearForOptionalCourses());
					degreeCurricularPlan.setNeededCredits(infoDegreeCurricularPlan.getNeededCredits());
					degreeCurricularPlan.setMarkType(infoDegreeCurricularPlan.getMarkType());
					degreeCurricularPlan.setNumerusClausus(infoDegreeCurricularPlan.getNumerusClausus());
					System.out.println("TA NO SERVICO INSERT DEGREE CURRICULAR PLAN!!!DEPOIS DOS SETS");
	
					persistentDegreeCurricularPlan.simpleLockWrite(degreeCurricularPlan);
				}
				return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}