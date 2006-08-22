/*
 * Created on Dec 17, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoProfessorship;
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.DetailedProfessorship;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ReadDetailedTeacherProfessorshipsAbstractService extends Service {
    
    private final class Professorships2DetailProfessorship implements Transformer {
        private Professorships2DetailProfessorship() {
            super();
        }

        public Object transform(Object input) {
            Professorship professorship = (Professorship) input;
            InfoProfessorship infoProfessorShip = InfoProfessorship.newInfoFromDomain(professorship);

            final DetailedProfessorship detailedProfessorship = new DetailedProfessorship();

            ExecutionCourse executionCourse = professorship.getExecutionCourse();
            List executionCourseCurricularCoursesList = getInfoCurricularCourses(detailedProfessorship,
                    executionCourse);
           
            detailedProfessorship.setResponsibleFor(professorship.getResponsibleFor());

            detailedProfessorship.setInfoProfessorship(infoProfessorShip);
            detailedProfessorship
                    .setExecutionCourseCurricularCoursesList(executionCourseCurricularCoursesList);

            return detailedProfessorship;
        }

        private List getInfoCurricularCourses(final DetailedProfessorship detailedProfessorship,
                ExecutionCourse executionCourse) {

            List infoCurricularCourses = (List) CollectionUtils.collect(executionCourse
                    .getAssociatedCurricularCourses(), new Transformer() {

                public Object transform(Object input) {
                    CurricularCourse curricularCourse = (CurricularCourse) input;
                    InfoCurricularCourse infoCurricularCourse = InfoCurricularCourse
                            .newInfoFromDomain(curricularCourse);
                    DegreeType degreeType = curricularCourse.getDegreeCurricularPlan().getDegree()
                            .getTipoCurso();
                    if (degreeType.equals(DegreeType.DEGREE)) {
                        detailedProfessorship.setMasterDegreeOnly(Boolean.FALSE);
                    }
                    return infoCurricularCourse;
                }
            });
            return infoCurricularCourses;
        }
    }

    public class NotFoundTeacher extends FenixServiceException {

    }

    protected List getDetailedProfessorships(List professorships, final List responsibleFors) {

        List detailedProfessorshipList = (List) CollectionUtils.collect(professorships,
                new Professorships2DetailProfessorship());

        return detailedProfessorshipList;
    }

    protected Teacher readTeacher(Integer teacherId) throws NotFoundTeacher, ExcepcaoPersistencia {
        final Teacher teacher = rootDomainObject.readTeacherByOID(teacherId);
        if (teacher == null) {
            throw new NotFoundTeacher();
        }
        return teacher;
    }
}