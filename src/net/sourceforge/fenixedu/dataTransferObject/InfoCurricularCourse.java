/*
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.util.CurricularCourseExecutionScope;

/**
 * @author tfc130
 */
public class InfoCurricularCourse extends InfoObject implements Comparable, ISiteComponent {

	private final CurricularCourse curricularCourse;

	private boolean showEnVersion = false;

    private List<InfoCurricularCourseScope> infoScopes;

    private List infoAssociatedExecutionCourses;

    private CurricularCourseType type;

    private CurricularCourseExecutionScope curricularCourseExecutionScope;

    private InfoUniversity infoUniversity;

    private String chosen;

    public InfoCurricularCourse(final CurricularCourse curricularCourse) {
    	this.curricularCourse = curricularCourse;
    }

    public Boolean getBasic() {
        return curricularCourse.getBasic();
    }

    public String getOwnershipType() {
    	return getBasic() == null ? "" : getBasic().booleanValue() ? "Básica" : "Não Básica";
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoCurricularCourse && curricularCourse == ((InfoCurricularCourse) obj).curricularCourse;
    }

    public String toString() {
    	return curricularCourse.toString();
    }

    public String getCode() {
        return curricularCourse.getCode();
    }

    public Double getCredits() {
        return curricularCourse.getCredits();
    }

    public Double getLabHours() {
        return curricularCourse.getLabHours();
    }

    public Double getPraticalHours() {
        return curricularCourse.getPraticalHours();
    }

    public Double getTheoPratHours() {
        return curricularCourse.getTheoPratHours();
    }

    public Double getTheoreticalHours() {
        return curricularCourse.getTheoreticalHours();
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
    	final InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan.newInfoFromDomain(curricularCourse.getDegreeCurricularPlan());
    	if (showEnVersion) {
    		infoDegreeCurricularPlan.prepareEnglishPresentation(Locale.ENGLISH);
    	}
    	return infoDegreeCurricularPlan;
    }

    public List getInfoScopes() {
        return infoScopes;
    }

    public void setInfoScopes(List<InfoCurricularCourseScope> infoScopes) {
        this.infoScopes = infoScopes;
    }

    public CurricularCourseType getType() {
        return curricularCourse.getType();
    }

    public CurricularCourseExecutionScope getCurricularCourseExecutionScope() {
        return curricularCourse.getCurricularCourseExecutionScope();
    }

    public Boolean getMandatory() {
        return curricularCourse.getMandatory();
    }

    public boolean infoCurricularCourseIsMandatory() {
        return getMandatory().booleanValue();
    }

    public InfoCurricularCourseScope getInfoCurricularCourseScope(InfoBranch infoBranch, Integer semester) {
        InfoCurricularCourseScope infoCurricularCourseScope = null;
        Iterator iterator = this.getInfoScopes().iterator();
        while (iterator.hasNext()) {
            InfoCurricularCourseScope infoCurricularCourseScope2 = (InfoCurricularCourseScope) iterator
                    .next();
            if (infoCurricularCourseScope2.getInfoBranch().equals(infoBranch)
                    && infoCurricularCourseScope2.getInfoCurricularSemester().getSemester().equals(
                            semester)) {
                infoCurricularCourseScope = infoCurricularCourseScope2;
                break;
            }
        }
        return infoCurricularCourseScope;
    }

    public InfoUniversity getInfoUniversity() {
        return infoUniversity;
    }

    public void setInfoUniversity(InfoUniversity university) {
        this.infoUniversity = university;
    }

    public int compareTo(Object arg0) {
        int result = 0;
        if (getMinScope() < ((InfoCurricularCourse) arg0).getMinScope()) {
            result = -1;
        } else if (getMinScope() > ((InfoCurricularCourse) arg0).getMinScope()) {
            return 1;
        }
        return result;
    }

    private int getMinScope() {
        int minScope = 0;
        List scopes = getInfoScopes();
        Iterator iter = scopes.iterator();
        while (iter.hasNext()) {
            InfoCurricularCourseScope infoScope = (InfoCurricularCourseScope) iter.next();
            if (minScope == 0
                    || minScope > infoScope.getInfoCurricularSemester().getInfoCurricularYear()
                            .getYear().intValue()) {
                minScope = infoScope.getInfoCurricularSemester().getInfoCurricularYear().getYear()
                        .intValue();
            }
        }

        return minScope;
    }

    public List getInfoAssociatedExecutionCourses() {
        return infoAssociatedExecutionCourses;
    }

    public void setInfoAssociatedExecutionCourses(List infoAssociatedExecutionCourses) {
        this.infoAssociatedExecutionCourses = infoAssociatedExecutionCourses;
    }

    public String getChosen() {
        return chosen;
    }

    public void setChosen(String chosen) {
        this.chosen = chosen;
    }

    public Double getEctsCredits() {
        return curricularCourse.getEctsCredits();
    }

    public Integer getEnrollmentWeigth() {
        return curricularCourse.getEnrollmentWeigth();
    }

    public Integer getMaximumValueForAcumulatedEnrollments() {
        return curricularCourse.getMaximumValueForAcumulatedEnrollments();
    }

    public Integer getMinimumValueForAcumulatedEnrollments() {
        return curricularCourse.getMinimumValueForAcumulatedEnrollments();
    }

    public Double getWeigth() {
        return curricularCourse.getWeigth();
    }

    public Boolean getMandatoryEnrollment() {
        return curricularCourse.getMandatoryEnrollment();
    }

    public Boolean getEnrollmentAllowed() {
        return curricularCourse.getEnrollmentAllowed();
    }

    public String getAcronym(){
    	return curricularCourse.getAcronym();
    }
    
    public static InfoCurricularCourse newInfoFromDomain(CurricularCourse curricularCourse) {
        InfoCurricularCourse infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourse(curricularCourse);
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }
        return infoCurricularCourse;
    }

    public String getName() {
    	return showEnVersion && curricularCourse.getNameEn() != null && curricularCourse.getNameEn().length() > 0 
    			? curricularCourse.getNameEn() : curricularCourse.getName();
    }

    public String getNameEn() {
        return curricularCourse.getNameEn();
    }
    
    public void prepareEnglishPresentation(Locale locale) {
        if (locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
        	showEnVersion = true;
            
            for (InfoCurricularCourseScope infoCurricularCourseScope : this.infoScopes) {
                infoCurricularCourseScope.prepareEnglishPresentation(locale);
            }
        }
    }
    
    public String getNameAndCode() {
        return getCode() + " - " + getName();
    }

	public GradeScale getGradeScale() {
		return curricularCourse.getGradeScale();
	}

    @Override
    public Integer getIdInternal() {
        return curricularCourse.getIdInternal();
    }

}