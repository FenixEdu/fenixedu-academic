package ServidorAplicacao.Servico.enrollment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoBranch;
import DataBeans.enrollment.InfoAreas2Choose;
import Dominio.IBranch;
import Dominio.IStudent;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentStudent;
import ServidorPersistente.IStudentCurricularPlanPersistente;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author Fernanda Quitério 31/Jan/2004
 *  
 */
public class ReadSpecializationAndSecundaryAreasByStudent implements IService {
    public ReadSpecializationAndSecundaryAreasByStudent() {
    }

    // some of these arguments may be null. they are only needed for filter
    public InfoAreas2Choose run(Integer executionDegreeId, Integer studentCurricularPlanId, 
            Integer studentNumber) throws FenixServiceException {
        InfoAreas2Choose infoAreas2Choose = new InfoAreas2Choose();
       
        List finalSpecializationAreas = new ArrayList();
        List finalSecundaryAreas = new ArrayList();
        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();
            IPersistentStudent studentDAO = persistentSuport
                    .getIPersistentStudent();
            IStudentCurricularPlanPersistente studentCurricularPlanDAO = persistentSuport
                    .getIStudentCurricularPlanPersistente();

            IStudent student = studentDAO.readStudentByNumberAndDegreeType(
                    studentNumber, TipoCurso.LICENCIATURA_OBJ);

            if (student == null) {
                throw new ExistingServiceException("student");
            }
            IStudentCurricularPlan studentCurricularPlan = studentCurricularPlanDAO
                    .readActiveStudentCurricularPlan(student.getNumber(),
                            student.getDegreeType());

            if (studentCurricularPlan == null) {
                throw new ExistingServiceException("studentCurricularPlan");
            }

            List specializationAreas = studentCurricularPlan
                    .getDegreeCurricularPlan().getSpecializationAreas();
            List secundaryAreas = studentCurricularPlan
                    .getDegreeCurricularPlan().getSecundaryAreas();

            finalSecundaryAreas = (List) CollectionUtils.collect(
                    secundaryAreas, new Transformer() {

                        public Object transform(Object arg0) {

                            return InfoBranch.newInfoFromDomain((IBranch) arg0);
                        }
                    });

            finalSpecializationAreas = (List) CollectionUtils.collect(
                    specializationAreas, new Transformer() {

                        public Object transform(Object arg0) {

                            return InfoBranch.newInfoFromDomain((IBranch) arg0);
                        }
                    });
            infoAreas2Choose.setFinalSpecializationAreas(finalSpecializationAreas);
            infoAreas2Choose.setFinalSecundaryAreas(finalSecundaryAreas);
        } catch (ExcepcaoPersistencia e) {

            throw new FenixServiceException(e);
        }
        return infoAreas2Choose;
    }
}