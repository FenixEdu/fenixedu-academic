/*
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainReference;

/**
 * @author tfc130
 */
public class InfoCurricularCourseScope extends InfoObject {

    private final DomainReference<CurricularCourseScope> curricularCourseScopeDomainReference;

	private boolean showEnVersion = false;

    public InfoCurricularCourseScope(final CurricularCourseScope curricularCourseScope) {
    	curricularCourseScopeDomainReference = new DomainReference<CurricularCourseScope>(curricularCourseScope);
    }

    public CurricularCourseScope getCurricularCourseScope() {
        return curricularCourseScopeDomainReference == null ? null : curricularCourseScopeDomainReference.getObject();
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoCurricularCourseScope && getCurricularCourseScope() == ((InfoCurricularCourseScope) obj).getCurricularCourseScope();
    }

    public String toString() {
    	return getCurricularCourseScope().toString();
    }

    public Boolean isActive() {
    	return getCurricularCourseScope().isActive();
    }

    public Calendar getBeginDate() {
        return getCurricularCourseScope().getBeginDate();
    }

    public Calendar getEndDate() {
        return getCurricularCourseScope().getEndDate();
    }

    public InfoBranch getInfoBranch() {
    	final InfoBranch infoBranch = InfoBranch.newInfoFromDomain(getCurricularCourseScope().getBranch());
    	if (showEnVersion) {
    		infoBranch.prepareEnglishPresentation(Locale.ENGLISH);
    	}
    	return infoBranch;
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
        return InfoCurricularCourse.newInfoFromDomain(getCurricularCourseScope().getCurricularCourse());
    }

    public InfoCurricularSemester getInfoCurricularSemester() {
        return InfoCurricularSemester.newInfoFromDomain(getCurricularCourseScope().getCurricularSemester());
    }

    public static InfoCurricularCourseScope newInfoFromDomain(final CurricularCourseScope curricularCourseScope) {
    	return curricularCourseScope == null ? null : new InfoCurricularCourseScope(curricularCourseScope);
    }

    public String getAnotation() {
        return getCurricularCourseScope().getAnotation();
    }

    public void prepareEnglishPresentation(Locale locale) {
    	showEnVersion = locale.getLanguage().equals(Locale.ENGLISH.getLanguage());        
    }

	@Override
	public Integer getIdInternal() {
		return getCurricularCourseScope().getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
