package ServidorAplicacao.Filtro;

import java.util.Iterator;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import DataBeans.ExecutionCourseSiteView;
import DataBeans.InfoExamsMap;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSiteEvaluation;
import ServidorAplicacao.IUserView;
import Util.RoleType;

/**
 * @author Luis Cruz
 * 
 */
public class PublishedExamsMapAuthorizationFilter extends Filtro {

	    public void execute(ServiceRequest request, ServiceResponse response) throws Exception {
	        IUserView userView = getRemoteUser(request);

	        if (response.getReturnObject() instanceof ExecutionCourseSiteView) {

        		ExecutionCourseSiteView executionCourseSiteView = (ExecutionCourseSiteView) response.getReturnObject();
        		if (executionCourseSiteView.getComponent() instanceof InfoSiteEvaluation) {

        			InfoSiteEvaluation infoSiteEvaluation  = (InfoSiteEvaluation) executionCourseSiteView.getComponent();
        			filterUnpublishedInformation(infoSiteEvaluation);

        		}

        	} else if (((userView != null && userView.getRoles() != null && !AuthorizationUtils.containsRole(
	                userView.getRoles(), getRoleType())))
	                || (userView == null) || (userView.getRoles() == null)) {

	        	if (response.getReturnObject() instanceof InfoExamsMap) {

	        		InfoExamsMap infoExamsMap = (InfoExamsMap) response.getReturnObject();
	        		filterUnpublishedInformation(infoExamsMap);

	        	}
	        }
	    }

		private void filterUnpublishedInformation(InfoSiteEvaluation infoSiteEvaluation) {
		    // This code is usefull for when the exams are to be filtered from the public execution course page.
//			CollectionUtils.filter(infoSiteEvaluation.getInfoEvaluations(), new Predicate() {
//				public boolean evaluate(Object arg0) {
//					return !(arg0 instanceof InfoExam);
//				}});
		}

		private void filterUnpublishedInformation(InfoExamsMap infoExamsMap) {
			if (infoExamsMap.getInfoExecutionDegree().getTemporaryExamMap().booleanValue()) {
				for (Iterator iterator = infoExamsMap.getExecutionCourses().iterator(); iterator.hasNext();) {
					InfoExecutionCourse infoExecutionCourse = (InfoExecutionCourse) iterator.next();
					infoExecutionCourse.getAssociatedInfoExams().clear();
				}
			}
		}

		protected RoleType getRoleType() {
	        return RoleType.TIME_TABLE_MANAGER;
	    }

}