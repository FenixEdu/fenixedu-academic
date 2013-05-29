package net.sourceforge.fenixedu.dataTransferObject.department;

import java.text.Collator;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public abstract class CourseStatisticsDTO {
    private String externalId;

    private String name;

    private int firstEnrolledCount;

    private int firstApprovedCount;

    private IGrade firstApprovedAverage;

    private int restEnrolledCount;

    private int restApprovedCount;

    private IGrade restApprovedAverage;

    private int totalEnrolledCount;

    private int totalApprovedCount;

    private IGrade totalApprovedAverage;

    public static final Comparator<CourseStatisticsDTO> COURSE_STATISTICS_COMPARATOR_BY_NAME =
            new Comparator<CourseStatisticsDTO>() {

                @Override
                public int compare(CourseStatisticsDTO o1, CourseStatisticsDTO o2) {
                    return Collator.getInstance(Language.getLocale()).compare(o1.getName(), o2.getName());
                }

            };

    public CourseStatisticsDTO() {
    }

    public CourseStatisticsDTO(String externalId, String name, int firstEnrolledCount, int firstApprovedCount,
            IGrade firstApprovedAverage, int restEnrolledCount, int restApprovedCount, IGrade restApprovedAverage,
            int totalEnrolledCount, int totalApprovedCount, IGrade totalApprovedAverage) {
        super();

        this.externalId = externalId;
        this.name = name;

        this.firstEnrolledCount = firstEnrolledCount;
        this.firstApprovedCount = firstApprovedCount;
        this.firstApprovedAverage = firstApprovedAverage;

        this.restEnrolledCount = restEnrolledCount;
        this.restApprovedCount = restApprovedCount;
        this.restApprovedAverage = restApprovedAverage;

        this.totalEnrolledCount = totalEnrolledCount;
        this.totalApprovedCount = totalApprovedCount;
        this.totalApprovedAverage = totalApprovedAverage;
    }

    public IGrade getFirstApprovedAverage() {
        return firstApprovedAverage;
    }

    public void setFirstApprovedAverage(IGrade firstApprovedAverage) {
        this.firstApprovedAverage = firstApprovedAverage;
    }

    public int getFirstApprovedCount() {
        return firstApprovedCount;
    }

    public void setFirstApprovedCount(int firstApprovedCount) {
        this.firstApprovedCount = firstApprovedCount;
    }

    public int getFirstEnrolledCount() {
        return firstEnrolledCount;
    }

    public void setFirstEnrolledCount(int firstEnrolledCount) {
        this.firstEnrolledCount = firstEnrolledCount;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public IGrade getRestApprovedAverage() {
        return restApprovedAverage;
    }

    public void setRestApprovedAverage(IGrade restApprovedAverage) {
        this.restApprovedAverage = restApprovedAverage;
    }

    public int getRestApprovedCount() {
        return restApprovedCount;
    }

    public void setRestApprovedCount(int restApprovedCount) {
        this.restApprovedCount = restApprovedCount;
    }

    public int getRestEnrolledCount() {
        return restEnrolledCount;
    }

    public void setRestEnrolledCount(int restEnrolledCount) {
        this.restEnrolledCount = restEnrolledCount;
    }

    public IGrade getTotalApprovedAverage() {
        return totalApprovedAverage;
    }

    public void setTotalApprovedAverage(IGrade totalApprovedAverage) {
        this.totalApprovedAverage = totalApprovedAverage;
    }

    public int getTotalApprovedCount() {
        return totalApprovedCount;
    }

    public void setTotalApprovedCount(int totalApprovedCount) {
        this.totalApprovedCount = totalApprovedCount;
    }

    public int getTotalEnrolledCount() {
        return totalEnrolledCount;
    }

    public void setTotalEnrolledCount(int totalEnrolledCount) {
        this.totalEnrolledCount = totalEnrolledCount;
    }

}
