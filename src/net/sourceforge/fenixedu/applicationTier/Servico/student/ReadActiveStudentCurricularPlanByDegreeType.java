/*
 * Created on 2004/04/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class ReadActiveStudentCurricularPlanByDegreeType extends Service {

    public InfoStudentCurricularPlan run(IUserView userView, DegreeType degreeType)
            throws ExcepcaoPersistencia {

    	final Person person = userView.getPerson();
    	final Registration student = person.getStudentByType(degreeType);

        if(student != null) {
        	final StudentCurricularPlan studentCurricularPlan = student.getActiveOrConcludedStudentCurricularPlan();
            if (studentCurricularPlan != null) {
            	final InfoStudentCurricularPlan infoStudentCurricularPlan = new InfoStudentCurricularPlan();
                if (studentCurricularPlan.getDegreeCurricularPlan() != null) {
                    InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
                    infoDegreeCurricularPlan.setName(studentCurricularPlan.getDegreeCurricularPlan()
                            .getName());
                    infoStudentCurricularPlan.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
                    infoStudentCurricularPlan.getInfoDegreeCurricularPlan().setIdInternal(
                            studentCurricularPlan.getDegreeCurricularPlan().getIdInternal());
                }
                return infoStudentCurricularPlan;
            }
        }

        return null;
    }

}