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

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;

/**
 * @author Luis Cruz
 * 
 */
public class PublishedExamsMapAuthorizationFilter extends Filtro {

    @Override
    public void execute(ServiceRequest request) throws Exception {
        IUserView userView = AccessControl.getUserView();

        if (response.getReturnObject() instanceof ExecutionCourseSiteView) {

            ExecutionCourseSiteView executionCourseSiteView = (ExecutionCourseSiteView) response.getReturnObject();
            if (executionCourseSiteView.getComponent() instanceof InfoSiteEvaluation) {

                InfoSiteEvaluation infoSiteEvaluation = (InfoSiteEvaluation) executionCourseSiteView.getComponent();
                filterUnpublishedInformation(infoSiteEvaluation);

            }

        } else if (((userView != null && userView.getRoleTypes() != null && !userView.hasRoleType(getRoleType())))
                || (userView == null) || (userView.getRoleTypes() == null)) {

            if (response.getReturnObject() instanceof InfoExamsMap) {

                InfoExamsMap infoExamsMap = (InfoExamsMap) response.getReturnObject();
                filterUnpublishedInformation(infoExamsMap);

            }
        }
    }

    private void filterUnpublishedInformation(InfoSiteEvaluation infoSiteEvaluation) {
        // This code is usefull for when the exams are to be filtered from the
        // public execution course page.
        CollectionUtils.filter(infoSiteEvaluation.getInfoEvaluations(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                return !(arg0 instanceof InfoExam);
            }
        });
    }

    private void filterUnpublishedInformation(InfoExamsMap infoExamsMap) {
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

    protected RoleType getRoleType() {
        return RoleType.RESOURCE_ALLOCATION_MANAGER;
    }

}