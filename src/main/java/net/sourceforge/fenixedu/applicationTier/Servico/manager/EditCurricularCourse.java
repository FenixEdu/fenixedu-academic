/*
 * Created on 18/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseEditor;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.predicates.RolePredicates;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */
public class EditCurricularCourse {

    public EditCurricularCourse() {
    }

    @Atomic
    public static void run(InfoCurricularCourseEditor newInfoCurricularCourse) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        CurricularCourse oldCurricularCourse = null;
        String newName = null;
        String newNameEn = null;
        String newCode = null;

        oldCurricularCourse = (CurricularCourse) FenixFramework.getDomainObject(newInfoCurricularCourse.getExternalId());

        newName = newInfoCurricularCourse.getName();
        newNameEn = newInfoCurricularCourse.getNameEn();
        newCode = newInfoCurricularCourse.getCode();
        final String newAcronym = newInfoCurricularCourse.getAcronym();

        if (oldCurricularCourse == null) {
            throw new NonExistingServiceException();
        }

        Collection<CurricularCourse> curricularCourses = null;
        curricularCourses = oldCurricularCourse.getDegreeCurricularPlan().getCurricularCourses();

        CurricularCourse cCourse = (CurricularCourse) CollectionUtils.find(curricularCourses, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                CurricularCourse curricularCourse = (CurricularCourse) arg0;
                if (newAcronym.equalsIgnoreCase(curricularCourse.getAcronym())) {
                    return true;
                }
                return false;
            }
        });

        if (cCourse == null || newAcronym.equalsIgnoreCase(oldCurricularCourse.getAcronym())) {

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
            oldCurricularCourse.setRegimeType(newInfoCurricularCourse.getRegimeType());
        } else {
            throw new ExistingAcronymException();
        }
    }

    public static class ExistingAcronymException extends FenixServiceException {

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

        @Override
        public String toString() {
            String result = "[ExistingAcronymException\n";
            result += "message" + this.getMessage() + "\n";
            result += "cause" + this.getCause() + "\n";
            result += "]";
            return result;
        }

    }
}