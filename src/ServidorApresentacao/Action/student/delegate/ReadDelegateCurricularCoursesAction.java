/*
 * Created on Feb 19, 2004
 *  
 */
package ServidorApresentacao.Action.student.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;

import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoObject;
import DataBeans.student.InfoDelegate;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.DelegateYearType;

/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 *  
 */
public class ReadDelegateCurricularCoursesAction extends FenixAction {
    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        IUserView userView = SessionUtils.getUserView(request);

        HashMap parameters = new HashMap();
        parameters.put("user", userView.getUtilizador());
        Object args[] = { parameters };
        List infoCurricularCourses = (List) ServiceUtils.executeService(userView,
                "ReadDelegateCurricularCourses", args);

        Collections.sort(infoCurricularCourses, new Comparator() {
            public int compare(Object o1, Object o2) {
                InfoCurricularCourse infoCurricularCourse1 = (InfoCurricularCourse) o1;
                InfoCurricularCourse infoCurricularCourse2 = (InfoCurricularCourse) o2;
                return infoCurricularCourse1.getName().compareTo(infoCurricularCourse2.getName());
            }
        });

        InfoDelegate infoDelegate = (InfoDelegate) ServiceUtils.executeService(userView, "ReadDelegate",
                args);

        // must now determine an authorization to edit each of the curricular
        // course's student report
        // for this specific delegate
        List infoCurricularCoursesAuthorizationToEdit = new ArrayList();

        Iterator infoCurricularCoursesIterator = infoCurricularCourses.iterator();
        while (infoCurricularCoursesIterator.hasNext()) {
            InfoCurricularCourse infoCurricularCourse = (InfoCurricularCourse) infoCurricularCoursesIterator
                    .next();
            boolean canEdit = false;

            // note on types of delegate:
            // - degree delegate alone    	---> can only read all the student reports 
            // - degree and year delegate 	---> can read all student reports and edit his (her) year reports
            // - year delegate 				---> can only edit his (her) year reports
            // Furthermore, one can only edit the report if allowed by the remaining conditions
            
            if (!infoDelegate.getYearType().equals(DelegateYearType.DEGREE)) {
                // a delegate can edit the report if at least one curricular 
                // course scope corresponds to the delegate's year
                Iterator infoCurricularCourseScopeIterator = infoCurricularCourse.getInfoScopes()
                        .iterator();
                while (infoCurricularCourseScopeIterator.hasNext() && canEdit == false) {
                    InfoCurricularCourseScope infoCurricularCourseScope = (InfoCurricularCourseScope) infoCurricularCourseScopeIterator
                            .next();
                    if (infoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear()
                            .getYear().intValue() == infoDelegate.getYearType().getValue())
                        canEdit = true;
                }
            }
            
            if(canEdit)
            {
                // TODO there are more conditions to observe in order to grant
                // authorization!                
            }

            InfoCurricularCourseAuthorizationToEdit infoCurricularCourseAuthorizationToEdit = new InfoCurricularCourseAuthorizationToEdit();
            infoCurricularCourseAuthorizationToEdit.setInfoCurricularCourse(infoCurricularCourse);
            infoCurricularCourseAuthorizationToEdit.setCanEdit(canEdit);
            infoCurricularCoursesAuthorizationToEdit.add(infoCurricularCourseAuthorizationToEdit);
        }

        request.setAttribute("infoDelegate", infoDelegate);
        request.setAttribute("infoCurricularCoursesAuthorizationToEdit",
                infoCurricularCoursesAuthorizationToEdit);

        return mapping.findForward("show-curricular-courses");
    }

    public class InfoCurricularCourseAuthorizationToEdit extends InfoObject {

        private InfoCurricularCourse infoCurricularCourse;

        private boolean canEdit;

        public InfoCurricularCourseAuthorizationToEdit() {
        }

        /**
         * @return Returns the infoCurricularCourse.
         */
        public InfoCurricularCourse getInfoCurricularCourse() {
            return infoCurricularCourse;
        }

        /**
         * @param infoCurricularCourse
         *            The infoCurricularCourse to set.
         */
        public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
            this.infoCurricularCourse = infoCurricularCourse;
        }

        /**
         * @return Returns the canEdit.
         */
        public boolean isCanEdit() {
            return canEdit;
        }

        /**
         * @param canEdit
         *            The canEdit to set.
         */
        public void setCanEdit(boolean canEdit) {
            this.canEdit = canEdit;
        }
    }

}