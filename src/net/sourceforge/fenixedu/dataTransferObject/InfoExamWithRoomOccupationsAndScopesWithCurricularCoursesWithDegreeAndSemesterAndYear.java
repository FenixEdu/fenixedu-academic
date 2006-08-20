/*
 * Created on 14/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.Exam;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear
        extends InfoExamWithRoomOccupations {

    public void copyFromDomain(Exam exam) {
        super.copyFromDomain(exam);
        if (exam != null) {
            setAssociatedCurricularCourseScope(copyICurricularCourseScope2InfoCurricularCourseScope(exam
                    .getAssociatedCurricularCourseScope()));
        }
    }

    private List copyICurricularCourseScope2InfoCurricularCourseScope(
            List associatedCurricularCourseScopes) {
        List associatedInfoCCScopes = (List) CollectionUtils.collect(associatedCurricularCourseScopes,
                new Transformer() {

                    public Object transform(Object arg0) {
                        return InfoCurricularCourseScope
                                .newInfoFromDomain((CurricularCourseScope) arg0);
                    }
                });

        return associatedInfoCCScopes;
    }

    public static InfoExam newInfoFromDomain(Exam exam) {
        InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear infoExam = null;
        if (exam != null) {
            infoExam = new InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear();
            infoExam.copyFromDomain(exam);
        }

        return infoExam;
    }
}