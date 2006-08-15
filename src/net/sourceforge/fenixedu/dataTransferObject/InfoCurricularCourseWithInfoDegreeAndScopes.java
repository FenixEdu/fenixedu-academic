/*
 * Created on Nov 3, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;

/**
 * @author farsola
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InfoCurricularCourseWithInfoDegreeAndScopes extends
        InfoCurricularCourse {

    public InfoCurricularCourseWithInfoDegreeAndScopes(CurricularCourse curricularCourse) {
		super(curricularCourse);
	}

	public void copyFromDomain(CurricularCourse curricularCourse) {
        super.copyFromDomain(curricularCourse);
        if (curricularCourse != null) {
            List infoScopes = new ArrayList();
            List scopes = curricularCourse.getScopes();
            Iterator scopesIterator = scopes.iterator();
            
            while (scopesIterator.hasNext()) {
                CurricularCourseScope scope = (CurricularCourseScope)scopesIterator.next();
                infoScopes.add(InfoCurricularCourseScopeWithSemesterAndYear.newInfoFromDomain(scope));
            }
            setInfoScopes(infoScopes);           
        }
    }

    public static InfoCurricularCourse newInfoFromDomain(CurricularCourse curricularCourse) {
        InfoCurricularCourseWithInfoDegreeAndScopes infoCurricularCourse = null;
        if (curricularCourse != null) {
            infoCurricularCourse = new InfoCurricularCourseWithInfoDegreeAndScopes(curricularCourse);
            infoCurricularCourse.copyFromDomain(curricularCourse);
        }
        return infoCurricularCourse;
    }

}
