package ServidorPersistente.middleware;

public class CourseCurricularYearPair {
    private String courseInitials;

    private Integer curricularYear;

    public CourseCurricularYearPair(String courseInitials, Integer curricularYear) {
        this.courseInitials = courseInitials;
        this.curricularYear = curricularYear;
    }

    public boolean equals(Object obj) {
        CourseCurricularYearPair courseCurricularYearPair = (CourseCurricularYearPair) obj;
        return courseInitials.equals(courseCurricularYearPair.getCourseInitials())
                && curricularYear.equals(courseCurricularYearPair.getCurricularYear());

    }

    /**
     * @return String
     */
    public String getCourseInitials() {
        return courseInitials;
    }

    /**
     * @return Integer
     */
    public Integer getCurricularYear() {
        return curricularYear;
    }

}