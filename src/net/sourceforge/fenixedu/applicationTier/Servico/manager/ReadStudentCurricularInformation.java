/*
 * Created on Feb 18, 2005
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class ReadStudentCurricularInformation extends Service {

    public List run(final Integer studentNumber, final DegreeType degreeType) throws ExcepcaoPersistencia {
        final List infoStudentCurricularPlans = new ArrayList();

        InfoStudent infoStudent = null;

        Registration student = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        if(student != null) {
        	final List<StudentCurricularPlan> studentCurricularPlans = student.getStudentCurricularPlans(); 
	        for (final Iterator iterator = studentCurricularPlans.iterator(); iterator.hasNext();) {
	            final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator
	                    .next();
	
	            if (infoStudent == null) {
	                infoStudent = constructInfoStudent(student);
	            }
	
	            final InfoStudentCurricularPlan infoStudentCurricularPlan = constructInfoStudentCurricularPlan(studentCurricularPlan);
	
	            infoStudentCurricularPlans.add(infoStudentCurricularPlan);
	        }
        }
        return infoStudentCurricularPlans;
    }

    protected InfoStudent constructInfoStudent(final Registration student) {
        return InfoStudent.newInfoFromDomain(student);
    }

    protected InfoStudentCurricularPlan constructInfoStudentCurricularPlan(
            final StudentCurricularPlan studentCurricularPlan) {
        return InfoStudentCurricularPlan.newInfoFromDomain(studentCurricularPlan);
    }

}
