package net.sourceforge.fenixedu.dataTransferObject.department;

import net.sourceforge.fenixedu.domain.curriculum.IGrade;

public class DegreeCourseStatisticsDTO extends CourseStatisticsDTO {
    public DegreeCourseStatisticsDTO() {
        super();
    }

    public DegreeCourseStatisticsDTO(int externalId, String name, int firstEnrolledCount, int firstApprovedCount,
            IGrade firstApprovedAverage, int restEnrolledCount, int restApprovedCount, IGrade restApprovedAverage,
            int totalEnrolledCount, int totalApprovedCount, IGrade totalApprovedAverage) {
        super(externalId, name, firstEnrolledCount, firstApprovedCount, firstApprovedAverage, restEnrolledCount,
                restApprovedCount, restApprovedAverage, totalEnrolledCount, totalApprovedCount, totalApprovedAverage);
    }
}
