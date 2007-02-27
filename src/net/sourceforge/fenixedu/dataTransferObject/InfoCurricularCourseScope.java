/*
 * InfoExecutionCourse.java
 * 
 * Created on 28 de Novembro de 2002, 3:41
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Calendar;
import java.util.Comparator;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;


/**
 * @author tfc130
 */
public class InfoCurricularCourseScope extends InfoObject {

    public static final ComparatorChain COMPARATOR_BY_YEAR_SEMESTER_AND_BRANCH;
    static {
	COMPARATOR_BY_YEAR_SEMESTER_AND_BRANCH = new ComparatorChain();
	COMPARATOR_BY_YEAR_SEMESTER_AND_BRANCH.addComparator(new Comparator() {

            public int compare(Object o1, Object o2) {
                final InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                final InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                
                return infoScope1.getInfoCurricularSemester().getInfoCurricularYear().getYear()
                        .compareTo(infoScope2.getInfoCurricularSemester().getInfoCurricularYear().getYear());
            }
        
        });
	COMPARATOR_BY_YEAR_SEMESTER_AND_BRANCH.addComparator(new Comparator() {

            public int compare(Object o1, Object o2) {
                final InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                final InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;

                return infoScope1.getInfoCurricularSemester().getSemester()
                	.compareTo(infoScope2.getInfoCurricularSemester().getSemester());
            }
        
        });
	COMPARATOR_BY_YEAR_SEMESTER_AND_BRANCH.addComparator(new Comparator() {

            public int compare(Object o1, Object o2) {
                final InfoCurricularCourseScope infoScope1 = (InfoCurricularCourseScope) o1;
                final InfoCurricularCourseScope infoScope2 = (InfoCurricularCourseScope) o2;
                
                final String acronym1 = infoScope1.getInfoBranch() == null ? null : infoScope1.getInfoBranch().getAcronym();
                final String acronym2 = infoScope2.getInfoBranch() == null ? null : infoScope2.getInfoBranch().getAcronym();
                if (StringUtils.isEmpty(acronym1) && StringUtils.isEmpty(acronym2)) {
                    return 0;
                } else if (!StringUtils.isEmpty(acronym1) && StringUtils.isEmpty(acronym2)) {
                    return 1;
                } else if (StringUtils.isEmpty(acronym1) && !StringUtils.isEmpty(acronym2)) {
                    return -1;
                }
                
                return acronym1.compareTo(acronym2);
            }
        });
        
    }
    
    private final DomainReference<CurricularCourseScope> curricularCourseScopeDomainReference;

    private boolean showEnVersion = (LanguageUtils.getUserLanguage() == Language.en);

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
    	return InfoBranch.newInfoFromDomain(getCurricularCourseScope().getBranch());
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

    @Override
    public Integer getIdInternal() {
    	return getCurricularCourseScope().getIdInternal();
    }
    
    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

}
