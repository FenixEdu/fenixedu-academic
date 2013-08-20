package net.sourceforge.fenixedu.dataTransferObject.department;

import net.sourceforge.fenixedu.domain.curriculum.IGrade;

public class CompetenceCourseStatisticsDTO extends CourseStatisticsDTO {
    public CompetenceCourseStatisticsDTO() {
        super();
    }

    public CompetenceCourseStatisticsDTO(String externalId, String name, int firstEnrolledCount, int firstApprovedCount,
            IGrade firstApprovedAverage, int restEnrolledCount, int restApprovedCount, IGrade restApprovedAverage,
            int totalEnrolledCount, int totalApprovedCount, IGrade totalApprovedAverage) {
        super(externalId, name, firstEnrolledCount, firstApprovedCount, firstApprovedAverage, restEnrolledCount,
                restApprovedCount, restApprovedAverage, totalEnrolledCount, totalApprovedCount, totalApprovedAverage);
    }
}
