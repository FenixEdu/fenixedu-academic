package net.sourceforge.fenixedu.applicationTier.Filtro;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.dataTransferObject.ExecutionCourseSiteView;
import net.sourceforge.fenixedu.dataTransferObject.InfoExam;
import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteEvaluation;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.apache.commons.collections.Predicate;

/**
 * @author Luis Cruz
 * 
 */
public class PublishedExamsMapAuthorizationFilter {

    public static void execute(Object returnedObject) {
        IUserView userView = AccessControl.getUserView();

        if (returnedObject instanceof ExecutionCourseSiteView) {

            ExecutionCourseSiteView executionCourseSiteView = (ExecutionCourseSiteView) returnedObject;
            if (executionCourseSiteView.getComponent() instanceof InfoSiteEvaluation) {

                InfoSiteEvaluation infoSiteEvaluation = (InfoSiteEvaluation) executionCourseSiteView.getComponent();
                filterUnpublishedInformation(infoSiteEvaluation);

            }

        } else if (((userView != null && userView.getRoleTypes() != null && !userView.hasRoleType(getRoleType())))
                || (userView == null) || (userView.getRoleTypes() == null)) {

            if (returnedObject instanceof InfoExamsMap) {

                InfoExamsMap infoExamsMap = (InfoExamsMap) returnedObject;
                filterUnpublishedInformation(infoExamsMap);

            }
        }
    }

    private static void filterUnpublishedInformation(InfoSiteEvaluation infoSiteEvaluation) {
        // This code is usefull for when the exams are to be filtered from the
        // public execution course page.
        CollectionUtils.filter(infoSiteEvaluation.getInfoEvaluations(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return !(arg0 instanceof InfoExam);
            }
        });
    }

    private static void filterUnpublishedInformation(InfoExamsMap infoExamsMap) {
        if (infoExamsMap != null
                && infoExamsMap.getInfoExecutionDegree() != null
                && infoExamsMap.getInfoExecutionDegree().isPublishedExam(
                        infoExamsMap.getInfoExecutionPeriod().getExecutionPeriod())) {
            for (Object element : infoExamsMap.getExecutionCourses()) {
                InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) element;
                infoExecutionCourse.getAssociatedInfoExams().clear();
            }
        }
    }

    protected static RoleType getRoleType() {
        return RoleType.RESOURCE_ALLOCATION_MANAGER;
    }

}