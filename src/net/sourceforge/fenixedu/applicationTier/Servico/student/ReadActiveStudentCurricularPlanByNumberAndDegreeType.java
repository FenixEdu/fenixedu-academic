package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/*
 * Created on 24/Set/2003, 11:26:29
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 24/Set/2003, 11:26:29
 * 
 */
public class ReadActiveStudentCurricularPlanByNumberAndDegreeType extends Service {

    public InfoStudentCurricularPlan run(Integer studentNumber, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        Registration registration = Registration.readStudentByNumberAndDegreeType(studentNumber, degreeType);
        if(registration == null) {
        	return null;
        }
        StudentCurricularPlan studentCurricularPlan = registration.getActiveOrConcludedStudentCurricularPlan();
        if (studentCurricularPlan != null) {
            return InfoStudentCurricularPlan
                    .newInfoFromDomain(studentCurricularPlan);
        }
        return null;
    }

}