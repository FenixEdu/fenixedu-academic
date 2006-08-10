/**
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.applicationTier.Servico.commons.student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.ExcepcaoInexistente;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadStudentCurricularPlans extends Service {

    public List run(Integer studentNumber, DegreeType degreeType) throws ExcepcaoInexistente,
            FenixServiceException, ExcepcaoPersistencia {

    	Registration student = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
    	if(student == null) {
    		throw new NonExistingServiceException("student does not exist");
    	}
    	List<StudentCurricularPlan> studentCurricularPlans = student.getStudentCurricularPlans(); 

        if ((studentCurricularPlans == null) || (studentCurricularPlans.size() == 0)) {
            throw new NonExistingServiceException();
        }

        Iterator iterator = studentCurricularPlans.iterator();
        List result = new ArrayList();

        // FIXME: There's a problem with data of the Graduation Students
        // For now only Master Degree Students can view their Curriculum

        while (iterator.hasNext()) {
            StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();

            result.add(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                    .newInfoFromDomain(studentCurricularPlan));
        }

        if (result.size() == 0) {
            throw new NonExistingServiceException();
        }

        return result;
    }
}