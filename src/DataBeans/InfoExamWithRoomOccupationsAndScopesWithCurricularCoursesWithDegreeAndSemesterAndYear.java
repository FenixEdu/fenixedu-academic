/*
 * Created on 14/Jul/2004
 *
 */
package DataBeans;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

import Dominio.ICurricularCourseScope;
import Dominio.IExam;

/**
 * @author Tânia Pousão
 *
 */
public class InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear extends InfoExamWithRoomOccupations {

    public void copyFromDomain(IExam exam) {
        super.copyFromDomain(exam);
        if (exam != null) {
            setAssociatedCurricularCourseScope(copyICurricularCourseScope2InfoCurricularCourseScope(exam
                    .getAssociatedCurricularCourseScope()));
        }
    }

    /**
     * @param associatedCurricularCourseScope
     * @return
     */
    private List copyICurricularCourseScope2InfoCurricularCourseScope(
            List associatedCurricularCourseScopes) {
        List associatedInfoCCScopes = (List) CollectionUtils.collect(associatedCurricularCourseScopes,
                new Transformer() {

                    public Object transform(Object arg0) {
                        return InfoCurricularCourseScopeWithCurricularCourseAndDegreeAndSemesterAndYear.newInfoFromDomain((ICurricularCourseScope) arg0);
                    }            
                });

        return associatedInfoCCScopes;
    }
    
    public static InfoExam newInfoFromDomain(IExam exam) {
        InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear infoExam = null;
        if(exam != null) {
            infoExam = new InfoExamWithRoomOccupationsAndScopesWithCurricularCoursesWithDegreeAndSemesterAndYear();
            infoExam.copyFromDomain(exam);
        }
        
        return infoExam;
    }
}
