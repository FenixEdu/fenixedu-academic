/*
 * Created on 8/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import Dominio.CurricularCourse;
import Dominio.DegreeCurricularPlan;
import Dominio.ICurricularCourse;
import Dominio.IDegreeCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.manager.EditCurricularCourse.ExistingAcronymException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.IPersistentDegreeCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */

public class InsertCurricularCourseAtDegreeCurricularPlan implements IService {

    public InsertCurricularCourseAtDegreeCurricularPlan() {
    }

    public void run(InfoCurricularCourse infoCurricularCourse) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            Integer degreeCurricularPlanId = infoCurricularCourse.getInfoDegreeCurricularPlan()
                    .getIdInternal();
            IPersistentDegreeCurricularPlan persistentDegreeCurricularPlan = persistentSuport
                    .getIPersistentDegreeCurricularPlan();

            IDegreeCurricularPlan degreeCurricularPlan = (IDegreeCurricularPlan) persistentDegreeCurricularPlan
                    .readByOID(DegreeCurricularPlan.class, degreeCurricularPlanId);

            if (degreeCurricularPlan == null)
                throw new NonExistingServiceException();

            String name = infoCurricularCourse.getName();
            String code = infoCurricularCourse.getCode();
            final String acronym = infoCurricularCourse.getAcronym();
            
            List curricularCourses = null;
            curricularCourses = degreeCurricularPlan.getCurricularCourses();
            
            ICurricularCourse cCourse = (ICurricularCourse) CollectionUtils.find(curricularCourses,new Predicate(){

				public boolean evaluate(Object arg0) {
					ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
					if(acronym.equalsIgnoreCase(curricularCourse.getAcronym()))
						return true;
					return false;
			}});
            
            if(cCourse == null) {

	            IPersistentCurricularCourse persistentCurricularCourse = persistentSuport
						.getIPersistentCurricularCourse();
	
	            ICurricularCourse curricularCourse = new CurricularCourse();
	            persistentCurricularCourse.simpleLockWrite(curricularCourse);
	            
	            
	
	            curricularCourse.setBasic(infoCurricularCourse.getBasic());
	            curricularCourse.setCode(code);
	            curricularCourse.setDegreeCurricularPlan(degreeCurricularPlan);
	            curricularCourse.setMandatory(infoCurricularCourse.getMandatory());
	            curricularCourse.setName(name);
	            curricularCourse.setType(infoCurricularCourse.getType());
	            curricularCourse.setTheoreticalHours(infoCurricularCourse.getTheoreticalHours());
	            curricularCourse.setTheoPratHours(infoCurricularCourse.getTheoPratHours());
	            curricularCourse.setPraticalHours(infoCurricularCourse.getPraticalHours());
	            curricularCourse.setLabHours(infoCurricularCourse.getLabHours());
	            curricularCourse.setMaximumValueForAcumulatedEnrollments(infoCurricularCourse
	                    .getMaximumValueForAcumulatedEnrollments());
	            curricularCourse.setMinimumValueForAcumulatedEnrollments(infoCurricularCourse
	                    .getMinimumValueForAcumulatedEnrollments());
	            curricularCourse.setCredits(infoCurricularCourse.getCredits());
	            curricularCourse.setEctsCredits(infoCurricularCourse.getEctsCredits());
	            curricularCourse.setEnrollmentWeigth(infoCurricularCourse.getEnrollmentWeigth());
	            curricularCourse.setWeigth(infoCurricularCourse.getWeigth());
	            curricularCourse.setMandatoryEnrollment(infoCurricularCourse.getMandatoryEnrollment());
	            curricularCourse.setEnrollmentAllowed(infoCurricularCourse.getEnrollmentAllowed());
	            curricularCourse.setAssociatedExecutionCourses(new ArrayList());
	            curricularCourse.setAcronym(infoCurricularCourse.getAcronym());
            } else {
            	throw new ExistingAcronymException();
            }

        } catch (ExistingPersistentException existingException) {
            throw new ExistingServiceException("A disciplina curricular "
                    + infoCurricularCourse.getName() + ", com código " + infoCurricularCourse.getCode(),
                    existingException);
        } catch (ExcepcaoPersistencia excepcaoPersistencia) {
            throw new FenixServiceException(excepcaoPersistencia);
        }
    }
    
    public class ExistingAcronymException extends FenixServiceException {

        public ExistingAcronymException() {
        }

        public ExistingAcronymException(String message) {
            super(message);
        }

        public ExistingAcronymException(Throwable cause) {
            super(cause);
        }

        public ExistingAcronymException(String message, Throwable cause) {
            super(message, cause);
        }

        public String toString() {
            String result = "[ExistingAcronymException\n";
            result += "message" + this.getMessage() + "\n";
            result += "cause" + this.getCause() + "\n";
            result += "]";
            return result;
        }

    }
}