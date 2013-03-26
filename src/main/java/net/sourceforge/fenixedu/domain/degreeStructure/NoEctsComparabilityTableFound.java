package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;

public class NoEctsComparabilityTableFound extends DomainException {
    public NoEctsComparabilityTableFound(CurriculumLine curriculumLine) {
        super("error.no.ects.course.comparability.found", curriculumLine.getName().getContent());
    }

    public NoEctsComparabilityTableFound(AcademicInterval year, CycleType cycle) {
        super("error.no.ects.graduation.comparability.found", year.getPathName(), cycle.getDescription());
    }

    public NoEctsComparabilityTableFound(AcademicInterval year) {
        super("error.no.ects.any.comparability.found", year.getPathName());
    }
}
