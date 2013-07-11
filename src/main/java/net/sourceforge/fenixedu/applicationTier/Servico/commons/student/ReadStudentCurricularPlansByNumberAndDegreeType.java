/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;

public class ReadStudentCurricularPlansByNumberAndDegreeType {

    @Atomic
    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    public static List<StudentCurricularPlan> run(Integer studentNumber, DegreeType degreeType)
            throws NonExistingServiceException {

        List<Registration> registrations = Registration.readByNumberAndDegreeType(studentNumber, degreeType);

        if (registrations.isEmpty()) {
            throw new NonExistingServiceException();
        }

        List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();

        for (final Registration registration : registrations) {
            studentCurricularPlans.addAll(registration.getStudentCurricularPlans());
        }

        if (studentCurricularPlans.isEmpty()) {
            throw new NonExistingServiceException();
        }

        return studentCurricularPlans;
    }
}