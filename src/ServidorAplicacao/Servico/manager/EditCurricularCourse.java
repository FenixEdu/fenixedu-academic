/*
 * Created on 18/Ago/2003
 */
package ServidorAplicacao.Servico.manager;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import Dominio.CurricularCourse;
import Dominio.ICurricularCourse;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentCurricularCourse;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistente.exceptions.ExistingPersistentException;

/**
 * @author lmac1
 */
public class EditCurricularCourse implements IService {

    public EditCurricularCourse() {
    }

    public void run(InfoCurricularCourse newInfoCurricularCourse) throws FenixServiceException {

        IPersistentCurricularCourse persistentCurricularCourse = null;
        ICurricularCourse oldCurricularCourse = null;
        String newName = null;
        String newCode = null;
      
        try {

            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
            persistentCurricularCourse = persistentSuport.getIPersistentCurricularCourse();
            oldCurricularCourse = (ICurricularCourse) persistentCurricularCourse.readByOID(
                    CurricularCourse.class, newInfoCurricularCourse.getIdInternal(), true);

            newName = newInfoCurricularCourse.getName();
            newCode = newInfoCurricularCourse.getCode();
            final String newAcronym = newInfoCurricularCourse.getAcronym();

            if (oldCurricularCourse == null) {
                throw new NonExistingServiceException();
            }
            
            List curricularCourses = null;
            curricularCourses = oldCurricularCourse.getDegreeCurricularPlan().getCurricularCourses();
            
            ICurricularCourse cCourse = (ICurricularCourse) CollectionUtils.find(curricularCourses,new Predicate(){

				public boolean evaluate(Object arg0) {
					ICurricularCourse curricularCourse = (ICurricularCourse) arg0;
					if(newAcronym.equalsIgnoreCase(curricularCourse.getAcronym()))
						return true;
					return false;
			}});
            
            if (cCourse == null || newAcronym.equalsIgnoreCase(oldCurricularCourse.getAcronym())){
            	
            	oldCurricularCourse.setName(newName);
                oldCurricularCourse.setCode(newCode);
                oldCurricularCourse.setAcronym(newInfoCurricularCourse.getAcronym());
                oldCurricularCourse.setType(newInfoCurricularCourse.getType());
                oldCurricularCourse.setMandatory(newInfoCurricularCourse.getMandatory());
                oldCurricularCourse.setBasic(newInfoCurricularCourse.getBasic());
                oldCurricularCourse.setTheoreticalHours(newInfoCurricularCourse.getTheoreticalHours());
                oldCurricularCourse.setTheoPratHours(newInfoCurricularCourse.getTheoPratHours());
                oldCurricularCourse.setPraticalHours(newInfoCurricularCourse.getPraticalHours());
                oldCurricularCourse.setLabHours(newInfoCurricularCourse.getLabHours());
                oldCurricularCourse.setMaximumValueForAcumulatedEnrollments(newInfoCurricularCourse
                        .getMaximumValueForAcumulatedEnrollments());
                oldCurricularCourse.setMinimumValueForAcumulatedEnrollments(newInfoCurricularCourse
                        .getMinimumValueForAcumulatedEnrollments());
                oldCurricularCourse.setCredits(newInfoCurricularCourse.getCredits());
                oldCurricularCourse.setEctsCredits(newInfoCurricularCourse.getEctsCredits());
                oldCurricularCourse.setWeigth(newInfoCurricularCourse.getWeigth());
                oldCurricularCourse.setEnrollmentWeigth(newInfoCurricularCourse.getEnrollmentWeigth());
                oldCurricularCourse.setMandatoryEnrollment(newInfoCurricularCourse.getMandatoryEnrollment());
                oldCurricularCourse.setEnrollmentAllowed(newInfoCurricularCourse.getEnrollmentAllowed());
            } else {
            	throw new ExistingAcronymException();
            }
            
        } catch (ExistingPersistentException ex) {
            throw new ExistingServiceException(ex);
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