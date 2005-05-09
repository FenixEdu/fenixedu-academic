/*
 * Created on 2004/04/14
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 *  
 */
public class ReadActiveStudentCurricularPlanByDegreeType implements IService {

    public ReadActiveStudentCurricularPlanByDegreeType() {
        super();
    }

    public InfoStudentCurricularPlan run(IUserView userView, DegreeType degreeType)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudent persistentStudent = persistentSupport.getIPersistentStudent();
        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = persistentSupport
                .getIStudentCurricularPlanPersistente();

        IStudent student = persistentStudent.readByUsername(userView.getUtilizador());
        IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
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