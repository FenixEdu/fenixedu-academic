package net.sourceforge.fenixedu.applicationTier.Servico.enrollment;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.enrollment.InfoAreas2Choose;
import net.sourceforge.fenixedu.domain.IBranch;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudent;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IPersistentStudent studentDAO = persistentSuport.getIPersistentStudent();
            IPersistentStudentCurricularPlan studentCurricularPlanDAO = persistentSuport
                    .getIStudentCurricularPlanPersistente();

            IStudent student = studentDAO.readStudentByNumberAndDegreeType(studentNumber,
                    TipoCurso.LICENCIATURA_OBJ);

            if (student == null) {
                throw new ExistingServiceException("student");
            }
            IStudentCurricularPlan studentCurricularPlan = studentCurricularPlanDAO
                    .readActiveStudentCurricularPlan(student.getNumber(), student.getDegreeType());

            if (studentCurricularPlan == null) {
                throw new ExistingServiceException("studentCurricularPlan");
            }

            List specializationAreas = studentCurricularPlan.getDegreeCurricularPlan()
                    .getSpecializationAreas();
            List secundaryAreas = studentCurricularPlan.getDegreeCurricularPlan().getSecundaryAreas();

            finalSecundaryAreas = (List) CollectionUtils.collect(secundaryAreas, new Transformer() {

                public Object transform(Object arg0) {

                    return InfoBranch.newInfoFromDomain((IBranch) arg0);
                }
            });

            finalSpecializationAreas = (List) CollectionUtils.collect(specializationAreas,
                    new Transformer() {

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