package net.sourceforge.fenixedu.domain.degreeStructure;

import net.sourceforge.fenixedu.domain.ICompetenceCourse;
import net.sourceforge.fenixedu.domain.IExecutionYear;

public class CompetenceCourseInformation extends CompetenceCourseInformation_Base {

    protected CompetenceCourseInformation() {
        super();
    }

    public CompetenceCourseInformation(ICompetenceCourse competenceCourse, IExecutionYear executionYear) {
        this();
        setCompetenceCourse(competenceCourse);
        setExecutionYear(executionYear);
    }
}
