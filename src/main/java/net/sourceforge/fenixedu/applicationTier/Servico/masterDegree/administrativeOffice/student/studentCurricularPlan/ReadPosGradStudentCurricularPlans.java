package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class ReadPosGradStudentCurricularPlans {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static List<InfoStudentCurricularPlan> run(String studentId) throws FenixServiceException {
        List<InfoStudentCurricularPlan> result = new ArrayList<InfoStudentCurricularPlan>();

        Registration registration = FenixFramework.getDomainObject(studentId);
        if (registration == null) {
            throw new InvalidArgumentsServiceException("invalidStudentId");
        }

        if (registration.getDegreeType().equals(DegreeType.MASTER_DEGREE)
                || registration.getDegreeType().equals(DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA)) {
            List<StudentCurricularPlan> resultTemp = new ArrayList<StudentCurricularPlan>();
            resultTemp.addAll(registration.getStudentCurricularPlans());

            Iterator iterator = resultTemp.iterator();
            while (iterator.hasNext()) {
                StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();
                result.add(InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan));
            }
        } else {
            throw new NotAuthorizedException();
        }

        return result;
    }

}