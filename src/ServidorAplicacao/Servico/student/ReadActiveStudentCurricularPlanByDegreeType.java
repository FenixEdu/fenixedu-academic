/*
 * Created on 2004/04/14
 *
 */
package ServidorAplicacao.Servico.student;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoStudentCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Luis Cruz
 *  
 */
public class ReadActiveStudentCurricularPlanByDegreeType implements IService {

    public ReadActiveStudentCurricularPlanByDegreeType() {
        super();
    }

    public InfoStudentCurricularPlan run(IUserView userView, TipoCurso degreeType)
            throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = SuportePersistenteOJB.getInstance();
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