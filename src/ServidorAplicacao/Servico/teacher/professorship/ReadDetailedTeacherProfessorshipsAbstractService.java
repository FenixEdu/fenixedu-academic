/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package ServidorAplicacao.Servico.teacher.professorship;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseWithInfoDegree;
import DataBeans.InfoProfessorship;
import DataBeans.InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod;
import DataBeans.teacher.professorship.DetailedProfessorship;
import Dominio.ICurricularCourse;
import Dominio.IExecutionCourse;
import Dominio.IProfessorship;
import Dominio.IResponsibleFor;
import Dominio.ITeacher;
import Dominio.ResponsibleFor;
import Dominio.Teacher;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTeacher;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import Util.TipoCurso;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsAbstractService implements IService {
    private final class Professorships2DetailProfessorship implements Transformer {
        private final List responsibleFors;

        private Professorships2DetailProfessorship(List responsibleFors) {
            super();
            this.responsibleFors = responsibleFors;
        }

        public Object transform(Object input) {
            IProfessorship professorship = (IProfessorship) input;
            InfoProfessorship infoProfessorShip = InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod
                    .newInfoFromDomain(professorship);

            final DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

            IExecutionCourse executionCourse = professorship.getExecutionCourse();
            List executionCourseCurricularCoursesList = getInfoCurricularCourses(detailedProfessorship,
                    executionCourse);

            IResponsibleFor responsibleFor = new ResponsibleFor();
            responsibleFor.setExecutionCourse(professorship.getExecutionCourse());
            responsibleFor.setTeacher(professorship.getTeacher());

            Boolean responsible = Boolean.valueOf(responsibleFors.contains(responsibleFor));
            detailedProfessorship.setResponsibleFor(responsible);

            detailedProfessorship.setInfoProfessorship(infoProfessorShip);
            detailedProfessorship
                    .setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

            return detailedProfessorship;
        }

        private List getInfoCurricularCourses(final DetailedProfessorship detailedProfessorship,
                IExecutionCourse executionCourse) {

            List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse
                    .getAssociatedCurricularCourses(), new Transformer() {

                public Object transform(Object input) {
                    ICurricularCourse curricularCourse = (ICurricularCourse) input;
                    InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                            .newInfoFromDomain(curricularCourse);
                    TipoCurso degreeType = curricularCourse.getDegreeCurricularPlan().getDegree()
                            .getTipoCurso();
                    if (degreeType.equals(TipoCurso.LICENCIATURA_OBJ)) {
                        detailedProfessorship.setMasterDegreeOnly(Boolean.FALSE);
                    }
                    return infoCurricularCourse;
                }
            });
            return infoCurricularCourses;
        }
    }

    /**
     * @author jpvl
     */
    public class NotFoundTeacher extends FenixServiceException {

    }

    protected List getDetailedProfessorships(List professorships, final List responsibleFors,
            ISuportePersistente sp) {

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships,
                new Professorships2DetailProfessorship(responsibleFors));

        return detailedProfessorshipList;
    }

    protected ISuportePersistente getDAOFactory() throws ExcepcaoPersistencia {
        return SuportePersistenteOJB.getInstance();
    }

    /**
     * @param teacherId
     * @return @throws
     *         ExcepcaoPersistencia
     */
    protected ITeacher readTeacher(Integer teacherId, IPersistentTeacher teacherDAO)
            throws NotFoundTeacher, ExcepcaoPersistencia {
        ITeacher teacher = (ITeacher) teacherDAO.readByOID(Teacher.class, teacherId);
        if (teacher == null) {
            throw new NotFoundTeacher();
        }
        return teacher;
    }
}