/*
 * Created on Jul 27, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.curricularCourseGroupManagement;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroupWithCoursesToAdd;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author João Mota
 *  
 */
public class CurricularCoursesGroupManagementDispatchAction extends FenixDispatchAction {

    public ActionForward manageCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        //   Integer degreeId = new Integer(request.getParameter("degreeId"));
        //        Integer degreeCurricularPlanId = new
        // Integer(request.getParameter("degreeCurricularPlanId"));
        Integer groupId = new Integer(request.getParameter("groupId"));
        Object[] args1 = { groupId };
        InfoCurricularCourseGroupWithCoursesToAdd composite;
        try {
            composite = (InfoCurricularCourseGroupWithCoursesToAdd) ServiceUtils.executeService(
                    userView, "ReadCoursesByCurricularCourseGroup", args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("groupWithAll", composite);
        DynaActionForm actionForm = (DynaActionForm) form;

        actionForm.set("courseIds", getListOfIds(composite.getInfoCurricularCourses()).toArray(
                new Integer[0]));

        //  request.setAttribute("degreeId",degreeId);
        //        request.setAttribute("degreeCurricularPlanId",degreeCurricularPlanId);
        return mapping.findForward("viewCurricularCourses");
    }

    public ActionForward removeCourses(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        //             Integer degreeID = new Integer(request.getParameter("degreeId"));
        //        Integer degreeCurricularPlanID = new
        // Integer(request.getParameter("degreeCurricularPlanId"));

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        Integer[] coursesIdsToRemove = (Integer[]) actionForm.get("courseIds");
        Integer groupId = new Integer(request.getParameter("groupId"));
        Object[] args1 = { groupId, coursesIdsToRemove };

        try {
            ServiceUtils.executeService(userView, "RemoveCurricularCoursesFromGroup", args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        //	request.setAttribute("degreeId", degreeID);
        //        		request.setAttribute("degreeCurricularPlanId",degreeCurricularPlanID);

        request.setAttribute("groupId", groupId);

        return manageCourses(mapping, form, request, response);
    }

    public ActionForward addCourses(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {

        //  Integer degreeID = new Integer(request.getParameter("degreeId"));
        //                Integer degreeCurricularPlanID = new
        //         Integer(request.getParameter("degreeCurricularPlanId"));

        IUserView userView = SessionUtils.getUserView(request);
        DynaActionForm actionForm = (DynaActionForm) form;
        Integer[] coursesIdsToAdd = (Integer[]) actionForm.get("courseIdsToAdd");
        Integer groupId = new Integer(request.getParameter("groupId"));
        Object[] args1 = { groupId, coursesIdsToAdd };

        try {
            ServiceUtils.executeService(userView, "AddCurricularCoursesToGroup", args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        //	request.setAttribute("degreeId", degreeID);
        //        		request.setAttribute("degreeCurricularPlanId",degreeCurricularPlanID);

        request.setAttribute("groupId", groupId);
        actionForm.set("courseIds", new Integer[0]);
        return manageCourses(mapping, form, request, response);
    }

    /**
     * @param composite
     * @return
     */
    private Collection getListOfIds(List courses) {
        return CollectionUtils.collect(courses, new Transformer() {

            public Object transform(Object arg0) {

                return ((InfoCurricularCourse) arg0).getIdInternal();
            }
        });
    }
}