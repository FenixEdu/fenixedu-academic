/*
 * Created on 6/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

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
public class EditDegreeCurricularPlan implements IServico {

	private static EditDegreeCurricularPlan service = new EditDegreeCurricularPlan();

	public static EditDegreeCurricularPlan getService() {
		return service;
	}

	private EditDegreeCurricularPlan() {
	}

	public final String getNome() {
		return "EditDegreeCurricularPlan";
	}
	

	public void run(Integer oldDegreeCPId, InfoDegreeCurricularPlan newInfoDegreeCP) throws FenixServiceException {

		ICurso degree = null;
		IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = null;
		IDegreeCurricularPlan oldDegreeCP = null;
		ICursoPersistente persistentDegree = null;

		
		try {
				ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
				persistentDegreeCurricularPlan = persistentSuport.getIPersistentDegreeCurricularPlan();
				oldDegreeCP = (IDegreeCurricularPlan) persistentDegreeCurricularPlan.readByOId(new DegreeCurricularPlan(oldDegreeCPId), false);
				
				List degreeCurricularPlans = persistentDegreeCurricularPlan.readAll();
			 	degreeCurricularPlans.remove((IDegreeCurricularPlan)oldDegreeCP);
				
				persistentDegree = persistentSuport.getICursoPersistente();
				Integer degreeId =oldDegreeCP.getDegree().getIdInternal();
				degree = persistentDegree.readByIdInternal(degreeId);	
			
			
				String newName = newInfoDegreeCP.getName();
			
			
//				List errors = new ArrayList(2);
//				errors.add(null);
//				errors.add(null);
//				
//				if(newName.compareToIgnoreCase(oldDegreeCP.getName())==0)
//					errors = null;
//				else
//					int modified = 0; 
//				
//					Iterator iter = degreeCurricularPlans.iterator();
//					while(iter.hasNext()) {
//						IDegreeCurricularPlan degreeCurricularPlanIter = (IDegreeCurricularPlan) iter.next();
//						if(newName.compareToIgnoreCase(degreeCurricularPlanIter.getName())==0){
//							modified++;
//							errors.set(0, newName);
//						}
//					}
//
//					if(modified == 0) {
//						errors = null; 
					
					if(oldDegreeCP != null){
						try {

					oldDegreeCP.setName(newName);
					oldDegreeCP.setDegree(degree);
					oldDegreeCP.setState(newInfoDegreeCP.getState());
					oldDegreeCP.setInitialDate(newInfoDegreeCP.getInitialDate());
					oldDegreeCP.setEndDate(newInfoDegreeCP.getEndDate());
					oldDegreeCP.setDegreeDuration(newInfoDegreeCP.getDegreeDuration());
					oldDegreeCP.setMinimalYearForOptionalCourses(newInfoDegreeCP.getMinimalYearForOptionalCourses());
					oldDegreeCP.setNeededCredits(newInfoDegreeCP.getNeededCredits());
					oldDegreeCP.setMarkType(newInfoDegreeCP.getMarkType());
					oldDegreeCP.setNumerusClausus(newInfoDegreeCP.getNumerusClausus());
					
					persistentDegreeCurricularPlan.write(oldDegreeCP);
					} catch (ExistingPersistentException ex) {
						throw new ExistingServiceException("O Plano curricular de nome "+newName, ex);
					}
		
					
					}

//				}
							
//				return errors;
			
		} catch (ExcepcaoPersistencia excepcaoPersistencia) {
			throw new FenixServiceException(excepcaoPersistencia);
		}
	}
}