/*
 * Created on 2004/04/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;

/**
 * @author Luis Cruz
 *  
 */
public class ReadActiveStudentCurricularPlanByDegreeType extends Service {

    public ReadActiveStudentCurricularPlanByDegreeType() {
        super();
    }

    public InfoStudentCurricularPlan run(IUserView userView, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();

        Student student = persistentStudent.readByUsername(userView.getUtilizador());
        StudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(student.getNumber(), degreeType);

        InfoStudentCurricularPlan infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlan();
            if (studentCurricularPlan.getDegreeCurricularPlan() != null) {
                InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
                infoDegreeCurricularPlan.setName(studentCurricularPlan.getDegreeCurricularPlan()
                        .getName());
                infoStudentCurricularPlan.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
                infoStudentCurricularPlan.getInfoDegreeCurricularPlan().setIdInternal(
                        studentCurricularPlan.getDegreeCurricularPlan().getIdInternal());
            }
        }

        return infoStudentCurricularPlan;
    }

}