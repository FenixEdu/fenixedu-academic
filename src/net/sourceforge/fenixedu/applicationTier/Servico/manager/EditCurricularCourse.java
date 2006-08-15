/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseEditor;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author lmac1
 */
public class EditCurricularCourse extends Service {

    public EditCurricularCourse() {
    }

    public void run(InfoCurricularCourseEditor newInfoCurricularCourse) throws FenixServiceException, ExcepcaoPersistencia {
        CurricularCourse oldCurricularCourse = null;
        String newName = null;
        String newNameEn = null;
        String newCode = null;
      
        oldCurricularCourse = (CurricularCourse) rootDomainObject.readDegreeModuleByOID(newInfoCurricularCourse.getIdInternal());

        newName = newInfoCurricularCourse.getName();
        newNameEn = newInfoCurricularCourse.getNameEn();
        newCode = newInfoCurricularCourse.getCode();
        final String newAcronym = newInfoCurricularCourse.getAcronym();

        if (oldCurricularCourse == null) {
            throw new NonExistingServiceException();
        }
        
        List curricularCourses = null;
        curricularCourses = oldCurricularCourse.getDegreeCurricularPlan().getCurricularCourses();
        
        CurricularCourse cCourse = (CurricularCourse) CollectionUtils.find(curricularCourses,new Predicate(){

			public boolean evaluate(Object arg0) {
				CurricularCourse curricularCourse = (CurricularCourse) arg0;
				if(newAcronym.equalsIgnoreCase(curricularCourse.getAcronym()))
					return true;
				return false;
		}});
        
        if (cCourse == null || newAcronym.equalsIgnoreCase(oldCurricularCourse.getAcronym())){
        	
        	oldCurricularCourse.setName(newName);
            oldCurricularCourse.setNameEn(newNameEn);
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
            oldCurricularCourse.setGradeScale(newInfoCurricularCourse.getGradeScale());
        } else {
        	throw new ExistingAcronymException();
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