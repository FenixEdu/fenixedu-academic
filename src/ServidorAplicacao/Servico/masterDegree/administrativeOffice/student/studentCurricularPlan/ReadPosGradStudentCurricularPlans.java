package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.studentCurricularPlan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import Dominio.Student;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.InvalidArgumentsServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IPersistentStudentCurricularPlan;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author João Mota 2/Out/2003
 */

public class ReadPosGradStudentCurricularPlans implements IService {

    public List run(Integer studentId) throws FenixServiceException {
        List result = new ArrayList();
        try {
            ISuportePersistente sp = SuportePersistenteOJB.getInstance();
            IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                    .getIStudentCurricularPlanPersistente();
            IPersistentStudent persistentStudent = sp.getIPersistentStudent();
            IStudent student = (IStudent) persistentStudent.readByOID(Student.class, studentId);

            if (student == null) {
                throw new InvalidArgumentsServiceException("invalidStudentId");
            }
            if (student.getDegreeType().getTipoCurso().intValue() == TipoCurso.MESTRADO) {
                List resultTemp = new ArrayList();
                resultTemp.addAll(persistentStudentCurricularPlan.readByStudentNumberAndDegreeType(
                        student.getNumber(), new TipoCurso(TipoCurso.MESTRADO)));

                Iterator iterator = resultTemp.iterator();
                while (iterator.hasNext()) {
                    IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator
                            .next();
                    result
                            .add(InfoStudentCurricularPlanWithInfoStudentAndInfoBranchAndSecondaryBranchAndInfoDegreeCurricularPlan
                                    .newInfoFromDomain(studentCurricularPlan));
                }
            } else {
                throw new NotAuthorizedException();
            }

        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }

        return result;
    }
}