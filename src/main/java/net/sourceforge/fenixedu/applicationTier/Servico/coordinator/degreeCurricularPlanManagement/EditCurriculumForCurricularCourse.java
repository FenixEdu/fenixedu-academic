package net.sourceforge.fenixedu.applicationTier.Servico.coordinator.degreeCurricularPlanManagement;

import java.util.Calendar;

import net.sourceforge.fenixedu.applicationTier.Filtro.CurrentDegreeCoordinatorAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurriculum;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Curriculum;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério 21/Nov/2003
 * 
 */
public class EditCurriculumForCurricularCourse {

    protected Boolean run(String infoExecutionDegreeId, String oldCurriculumId, String curricularCourseCode,
            InfoCurriculum newInfoCurriculum, String username, String language) throws FenixServiceException {
        Boolean result = new Boolean(false);

        if (oldCurriculumId == null) {
            throw new FenixServiceException("nullCurriculumCode");
        }
        if (curricularCourseCode == null) {
            throw new FenixServiceException("nullCurricularCourseCode");
        }
        if (newInfoCurriculum == null) {
            throw new FenixServiceException("nullCurriculum");
        }
        if (username == null) {
            throw new FenixServiceException("nullUsername");
        }

        CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseCode);
        if (curricularCourse == null) {
            throw new NonExistingServiceException("noCurricularCourse");
        }

        Person person = Person.readPersonByUsername(username);
        if (person == null) {
            throw new NonExistingServiceException("noPerson");
        }

        Curriculum oldCurriculum = FenixFramework.getDomainObject(oldCurriculumId);
        if (oldCurriculum == null) {
            oldCurriculum = new Curriculum();

            oldCurriculum.setCurricularCourse(curricularCourse);
            Calendar today = Calendar.getInstance();
            oldCurriculum.setLastModificationDate(today.getTime());
        }

        ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

        if (!oldCurriculum.getLastModificationDate().before(currentExecutionYear.getBeginDate())
                && !oldCurriculum.getLastModificationDate().after(currentExecutionYear.getEndDate())) {

            oldCurriculum.edit(newInfoCurriculum.getGeneralObjectives(), newInfoCurriculum.getOperacionalObjectives(),
                    newInfoCurriculum.getProgram(), newInfoCurriculum.getGeneralObjectivesEn(),
                    newInfoCurriculum.getOperacionalObjectivesEn(), newInfoCurriculum.getProgramEn());

        } else {
            Curriculum newCurriculum = new Curriculum();
            newCurriculum.setCurricularCourse(curricularCourse);

            newCurriculum.edit(newInfoCurriculum.getGeneralObjectives(), newInfoCurriculum.getOperacionalObjectives(),
                    newInfoCurriculum.getProgram(), newInfoCurriculum.getGeneralObjectivesEn(),
                    newInfoCurriculum.getOperacionalObjectivesEn(), newInfoCurriculum.getProgramEn());

        }
        result = Boolean.TRUE;

        return result;
    }

    // Service Invokers migrated from Berserk

    private static final EditCurriculumForCurricularCourse serviceInstance = new EditCurriculumForCurricularCourse();

    @Atomic
    public static Boolean runEditCurriculumForCurricularCourse(String infoExecutionDegreeId, String oldCurriculumId,
            String curricularCourseCode, InfoCurriculum newInfoCurriculum, String username, String language)
            throws FenixServiceException, NotAuthorizedException {
        CurrentDegreeCoordinatorAuthorizationFilter.instance.execute(infoExecutionDegreeId);
        return serviceInstance.run(infoExecutionDegreeId, oldCurriculumId, curricularCourseCode, newInfoCurriculum, username,
                language);
    }

}