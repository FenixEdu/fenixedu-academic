package net.sourceforge.fenixedu.applicationTier.Servico.teacher;


import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseResponsibleForTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Fernanda Quitério
 * 
 *         Modified by Tânia Pousão at 2/Dez/2003
 */
public class EditProgram {

    protected Boolean run(Integer executionCourseOID, Integer curricularCourseOID, InfoCurriculum infoCurriculumNew,
            String username) throws FenixServiceException {

        final Person person = Person.readPersonByUsername(username);
        final ExecutionCourse executionCourse = AbstractDomainObject.fromExternalId(executionCourseOID);
        final CurricularCourse curricularCourse = (CurricularCourse) AbstractDomainObject.fromExternalId(curricularCourseOID);
        final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        Curriculum curriculum =
                curricularCourse.findLatestCurriculumModifiedBefore(executionCourse.getExecutionYear().getEndDate());
        if (curriculum == null) {
            // information doesn't exists, so it's necessary create it
            curriculum =
                    curricularCourse.insertCurriculum("", "", "", "", "", "", executionCourse.getExecutionPeriod()
                            .getBeginDateYearMonthDay().toDateTimeAtMidnight());
        }

        if (!curriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !curriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {

            curriculum.edit(curriculum.getGeneralObjectives(), curriculum.getOperacionalObjectives(),
                    infoCurriculumNew.getProgram(), curriculum.getGeneralObjectivesEn(), curriculum.getOperacionalObjectivesEn(),
                    infoCurriculumNew.getProgramEn());

            curriculum.edit(curriculum.getGeneralObjectives(), curriculum.getOperacionalObjectives(),
                    infoCurriculumNew.getProgram(), curriculum.getGeneralObjectivesEn(), curriculum.getOperacionalObjectivesEn(),
                    infoCurriculumNew.getProgramEn());

        } else {
            final Curriculum newCurriculum =
                    curricularCourse.insertCurriculum(infoCurriculumNew.getProgram(), infoCurriculumNew.getProgramEn(),
                            curriculum.getOperacionalObjectives(), curriculum.getOperacionalObjectivesEn(),
                            curriculum.getGeneralObjectivesEn(), curriculum.getGeneralObjectivesEn(), executionCourse
                                    .getExecutionPeriod().getEndDateYearMonthDay().toDateTimeAtMidnight());

            newCurriculum.setPersonWhoAltered(person);
        }

        return true;
    }

    // Service Invokers migrated from Berserk

    private static final EditProgram serviceInstance = new EditProgram();

    @Service
    public static Boolean runEditProgram(Integer executionCourseOID, Integer curricularCourseOID,
            InfoCurriculum infoCurriculumNew, String username) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseResponsibleForTeacherAuthorizationFilter.instance.execute(executionCourseOID, curricularCourseOID,
                infoCurriculumNew, username);
        return serviceInstance.run(executionCourseOID, curricularCourseOID, infoCurriculumNew, username);
    }

}