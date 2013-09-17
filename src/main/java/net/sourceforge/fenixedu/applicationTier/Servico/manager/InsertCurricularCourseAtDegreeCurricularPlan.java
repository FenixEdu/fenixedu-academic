package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collection;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseEditor;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class InsertCurricularCourseAtDegreeCurricularPlan {

    @Atomic
    public static void run(InfoCurricularCourseEditor infoCurricularCourse) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        String degreeCurricularPlanId = infoCurricularCourse.getInfoDegreeCurricularPlan().getExternalId();
        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);
        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        String name = infoCurricularCourse.getName();
        String nameEn = infoCurricularCourse.getNameEn();
        String code = infoCurricularCourse.getCode();
        final String acronym = infoCurricularCourse.getAcronym();

        Collection curricularCourses = null;
        curricularCourses = degreeCurricularPlan.getCurricularCourses();

        CurricularCourse cCourse = (CurricularCourse) CollectionUtils.find(curricularCourses, new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                CurricularCourse curricularCourse = (CurricularCourse) arg0;
                if (acronym.equalsIgnoreCase(curricularCourse.getAcronym())) {
                    return true;
                }
                return false;
            }
        });

        if (cCourse == null) {
            CurricularCourse curricularCourse =
                    degreeCurricularPlan.createCurricularCourse(name, code, infoCurricularCourse.getAcronym(),
                            infoCurricularCourse.getEnrollmentAllowed(), CurricularStage.OLD);
            curricularCourse.setBasic(infoCurricularCourse.getBasic());
            curricularCourse.setMandatory(infoCurricularCourse.getMandatory());
            curricularCourse.setNameEn(nameEn);
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
            curricularCourse.setGradeScale(infoCurricularCourse.getGradeScale());
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