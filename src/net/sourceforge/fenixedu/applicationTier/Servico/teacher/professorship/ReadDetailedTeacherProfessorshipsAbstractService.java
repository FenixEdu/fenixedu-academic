/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorshipWithInfoExecutionCourseAndInfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IResponsibleFor;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ResponsibleFor;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTeacher;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import pt.utl.ist.berserk.logic.serviceManager.IService;

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
        return PersistenceSupportFactory.getDefaultPersistenceSupport();
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