/*
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.RegimeType;
import net.sourceforge.fenixedu.util.CurricularCourseExecutionScope;
import net.sourceforge.fenixedu.util.LanguageUtils;

/**
 * @author tfc130
 */
public class InfoCurricularCourse extends InfoObject implements Comparable, ISiteComponent {

    private final DomainReference<CurricularCourse> curricularCourseDomainReference;

    private final boolean showEnVersion = (LanguageUtils.getUserLanguage() == Language.en); 
    
    private List<InfoCurricularCourseScope> infoScopes;

    private List infoAssociatedExecutionCourses;

    private InfoUniversity infoUniversity;

    private String chosen;

    public InfoCurricularCourse(final CurricularCourse curricularCourse) {
	curricularCourseDomainReference = new DomainReference<CurricularCourse>(curricularCourse);
    }

    public CurricularCourse getCurricularCourse() {
	return curricularCourseDomainReference == null ? null : curricularCourseDomainReference
		.getObject();
    }

    public Boolean getBasic() {
	return getCurricularCourse().getBasic();
    }

    public String getOwnershipType() {
	return getBasic() == null ? "" : getBasic().booleanValue() ? "Básica" : "Não Básica";
    }

    public boolean equals(Object obj) {
	return obj instanceof InfoCurricularCourse
		&& getCurricularCourse() == ((InfoCurricularCourse) obj).getCurricularCourse();
    }

    public String toString() {
	return getCurricularCourse().toString();
    }

    public String getCode() {
	return getCurricularCourse().getCode();
    }

    public Double getCredits() {
	return getCurricularCourse().getCredits();
    }

    public Double getLabHours() {
	return getCurricularCourse().getLabHours();
    }

    public Double getPraticalHours() {
	return getCurricularCourse().getPraticalHours();
    }

    public Double getTheoPratHours() {
	return getCurricularCourse().getTheoPratHours();
    }

    public Double getTheoreticalHours() {
	return getCurricularCourse().getTheoreticalHours();
    }

    public InfoDegreeCurricularPlan getInfoDegreeCurricularPlan() {
	return InfoDegreeCurricularPlan.newInfoFromDomain(getCurricularCourse().getDegreeCurricularPlan());
    }

    public List getInfoScopes() {
	return infoScopes;
    }

    public void setInfoScopes(List<InfoCurricularCourseScope> infoScopes) {
	this.infoScopes = infoScopes;
    }

    public CurricularCourseType getType() {
	return getCurricularCourse().getType();
    }

    public CurricularCourseExecutionScope getCurricularCourseExecutionScope() {
	return getCurricularCourse().getCurricularCourseExecutionScope();
    }

    public Boolean getMandatory() {
	return getCurricularCourse().getMandatory();
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
	return getCurricularCourse().getEctsCredits();
    }

    public Integer getEnrollmentWeigth() {
	return getCurricularCourse().getEnrollmentWeigth();
    }

    public Integer getMaximumValueForAcumulatedEnrollments() {
	return getCurricularCourse().getMaximumValueForAcumulatedEnrollments();
    }

    public Integer getMinimumValueForAcumulatedEnrollments() {
	return getCurricularCourse().getMinimumValueForAcumulatedEnrollments();
    }

    public Double getWeigth() {
	return getCurricularCourse().getWeigth();
    }

    public Boolean getMandatoryEnrollment() {
	return getCurricularCourse().getMandatoryEnrollment();
    }

    public Boolean getEnrollmentAllowed() {
	return getCurricularCourse().getEnrollmentAllowed();
    }

    public String getAcronym() {
	return getCurricularCourse().getAcronym();
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
	return showEnVersion && getCurricularCourse().getNameEn() != null
		&& getCurricularCourse().getNameEn().length() > 0 ? getCurricularCourse().getNameEn()
		: getCurricularCourse().getName();
    }

    public String getNameEn() {
	return getCurricularCourse().getNameEn();
    }

    public String getNameAndCode() {
	return getCode() + " - " + getName();
    }

    public GradeScale getGradeScale() {
	return getCurricularCourse().getGradeScale();
    }

    @Override
    public Integer getIdInternal() {
	return getCurricularCourse().getIdInternal();
    }

    public boolean getIsBolonha() {
	return getCurricularCourse().isBolonha();
    }
    
    public RegimeType getRegimeType() {
	return getCurricularCourse().getRegimeType();
    }

}