package net.sourceforge.fenixedu.domain;

import pt.ist.bennu.core.domain.Bennu;

/**
 * @author dcs-rjao
 * 
 *         19/Mar/2003
 */

public class CurricularSemester extends CurricularSemester_Base implements Comparable<CurricularSemester> {

    public CurricularSemester() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public CurricularSemester(final CurricularYear curricularYear, final Integer semester) {
        this();
        setCurricularYear(curricularYear);
        setSemester(semester);
    }

    @Override
    public int compareTo(final CurricularSemester curricularSemester) {
        return getCurricularYear() == curricularSemester.getCurricularYear() ? getSemester().compareTo(
                curricularSemester.getSemester()) : getCurricularYear().compareTo(curricularSemester.getCurricularYear());
    }

    public static CurricularSemester readBySemesterAndYear(final Integer semester, final Integer year) {
        for (CurricularSemester curricularSemester : Bennu.getInstance().getCurricularSemestersSet()) {
            if (curricularSemester.getSemester().equals(semester)
                    && curricularSemester.getCurricularYear().getYear().equals(year)) {
                return curricularSemester;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourseScope> getScopes() {
        return getScopesSet();
    }

    @Deprecated
    public boolean hasAnyScopes() {
        return !getScopesSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCurricularYear() {
        return getCurricularYear() != null;
    }

    @Deprecated
    public boolean hasSemester() {
        return getSemester() != null;
    }

}
