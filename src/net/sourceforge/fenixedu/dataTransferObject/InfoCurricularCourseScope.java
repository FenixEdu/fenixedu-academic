/*
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.Locale;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;

/**
 * @author tfc130
 */
public class InfoCurricularCourseScope extends InfoObject {

	private final CurricularCourseScope curricularCourseScope;

	private boolean showEnVersion = false;

    public InfoCurricularCourseScope(final CurricularCourseScope curricularCourseScope) {
    	this.curricularCourseScope = curricularCourseScope;
    }

    public boolean equals(Object obj) {
    	return obj instanceof InfoCurricularCourseScope && curricularCourseScope == ((InfoCurricularCourseScope) obj).curricularCourseScope;
    }

    public String toString() {
    	return curricularCourseScope.toString();
    }

    public Boolean isActive() {
    	return curricularCourseScope.isActive();
    }

    public Calendar getBeginDate() {
        return curricularCourseScope.getBeginDate();
    }

    public Calendar getEndDate() {
        return curricularCourseScope.getEndDate();
    }

    public InfoBranch getInfoBranch() {
    	final InfoBranch infoBranch = InfoBranch.newInfoFromDomain(curricularCourseScope.getBranch());
    	if (showEnVersion) {
    		infoBranch.prepareEnglishPresentation(Locale.ENGLISH);
    	}
    	return infoBranch;
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
        return InfoCurricularCourse.newInfoFromDomain(curricularCourseScope.getCurricularCourse());
    }

    public InfoCurricularSemester getInfoCurricularSemester() {
        return InfoCurricularSemester.newInfoFromDomain(curricularCourseScope.getCurricularSemester());
    }

    public static InfoCurricularCourseScope newInfoFromDomain(final CurricularCourseScope curricularCourseScope) {
    	return curricularCourseScope == null ? null : new InfoCurricularCourseScope(curricularCourseScope);
    }

    public String getAnotation() {
        return curricularCourseScope.getAnotation();
    }

    public void prepareEnglishPresentation(Locale locale) {
    	showEnVersion = locale.getLanguage().equals(Locale.ENGLISH.getLanguage());        
    }

	@Override
	public Integer getIdInternal() {
		return curricularCourseScope.getIdInternal();
	}

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
