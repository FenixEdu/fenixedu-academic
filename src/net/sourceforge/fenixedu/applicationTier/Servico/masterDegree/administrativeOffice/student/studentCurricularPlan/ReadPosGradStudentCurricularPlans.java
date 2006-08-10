package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadPosGradStudentCurricularPlans extends Service {

	public List<InfoStudentCurricularPlan> run(Integer studentId) throws FenixServiceException, ExcepcaoPersistencia {
		List<InfoStudentCurricularPlan> result = new ArrayList<InfoStudentCurricularPlan>();

		Registration student = rootDomainObject.readRegistrationByOID(studentId);
		if (student == null) {
			throw new InvalidArgumentsServiceException("invalidStudentId");
		}
        
		if (student.getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
			List<StudentCurricularPlan> resultTemp = new ArrayList<StudentCurricularPlan>();
			resultTemp.addAll(student.getStudentCurricularPlans());

			Iterator iterator = resultTemp.iterator();
			while (iterator.hasNext()) {
				StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();
				result
						.add(InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan
								.newInfoFromDomain(studentCurricularPlan));
			}
		} else {
			throw new NotAuthorizedException();
		}

		return result;
	}
    
}
