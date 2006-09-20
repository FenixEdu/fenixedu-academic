/*
 * Created on Jul 27, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.curricularCourseGroupManagement;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoAreaCurricularCourseGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoOptionalCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.AreaCurricularCourseGroup;
import net.sourceforge.fenixedu.domain.OptionalCurricularCourseGroup;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author João Mota
 *  
 */
public class CurricularCourseGroupManagementDispatchAction extends FenixDispatchAction {

    public ActionForward viewCurricularCourseGroups(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        //        Integer degreeID = new Integer(request.getParameter("degreeId"));
        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

        Object args[] = { degreeCurricularPlanId };

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlan", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        // in case the degreeCurricularPlan really exists
        List curricularCourseGroups = null;
        try {
            curricularCourseGroups = (List) ServiceUtils.executeService(userView,
                    "ReadCurricularCourseGroupsByDegreeCurricularPlan", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(curricularCourseGroups, new BeanComparator("name"));

        List executionDegrees = null;
        try {
            executionDegrees = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionDegreesByDegreeCurricularPlan", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(executionDegrees, new BeanComparator("infoExecutionYear.year"));

        request.setAttribute("curricularCourseGroups", curricularCourseGroups);
        request.setAttribute("executionDegreesList", executionDegrees);
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        request.setAttribute("degreeId", infoDegreeCurricularPlan.getInfoDegree().getIdInternal());
        request.setAttribute("degreeCurricularPlanId", infoDegreeCurricularPlan.getIdInternal());
        return mapping.findForward("viewGroups");
    }

    public ActionForward prepareInsertAreaCurricularCourseGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

        Object args[] = { degreeCurricularPlanId };

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlan", args);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        List branches;
        try {
            branches = (List) ServiceUtils.executeService(userView,
                    "ReadBranchesByDegreeCurricularPlan", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("areas", AreaType.toLabelValueBeanList());
        request.setAttribute("branches", branches);
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        request.setAttribute("degreeId", infoDegreeCurricularPlan.getInfoDegree().getIdInternal());
        request.setAttribute("degreeCurricularPlanId", infoDegreeCurricularPlan.getIdInternal());
        return mapping.findForward("viewInsertAreaCurricularCourseGroup");
    }

    public ActionForward prepareInsertOptionalCurricularCourseGroup(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

        Object args[] = { degreeCurricularPlanId };

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlan", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        List branches;
        try {
            branches = (List) ServiceUtils.executeService(userView,
                    "ReadBranchesByDegreeCurricularPlan", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("areas", AreaType.toLabelValueBeanList());
        request.setAttribute("branches", branches);
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        request.setAttribute("degreeId", infoDegreeCurricularPlan.getInfoDegree().getIdInternal());
        request.setAttribute("degreeCurricularPlanId", infoDegreeCurricularPlan.getIdInternal());
        return mapping.findForward("viewInsertOptionalCurricularCourseGroup");
    }

    public ActionForward insertCurricularCourseGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        DynaActionForm groupForm = (DynaActionForm) form;
        String name = (String) groupForm.get("name");
        Integer groupId = (Integer) groupForm.get("groupId");
        Integer branchId = (Integer) groupForm.get("branchId");
        Integer minimumValue = (Integer) groupForm.get("minimumValue");
        Integer maximumValue = (Integer) groupForm.get("maximumValue");
        String areaType = (String) groupForm.get("areaType");
        String type = (String) groupForm.get("type");
		
		AreaType realAreaType = ((areaType == null || areaType.equals(""))?null:AreaType.valueOf(areaType));
		
        Object args[] = { groupId, name, branchId, minimumValue, maximumValue, realAreaType,
                getClassName(type) };

        try {
            ServiceUtils.executeService(userView, "InsertCurricularCourseGroup", args);

        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

        Object args1[] = { degreeCurricularPlanId };

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlan", args1);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        request.setAttribute("degreeId", infoDegreeCurricularPlan.getInfoDegree().getIdInternal());
        request.setAttribute("degreeCurricularPlanId", infoDegreeCurricularPlan.getIdInternal());

        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        return viewCurricularCourseGroups(mapping, form, request, response);
    }

    public ActionForward deleteCurricularCourseGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

        Object args[] = { degreeCurricularPlanId };

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlan", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        Integer groupId = new Integer(request.getParameter("groupId"));
        Object[] args1 = { groupId };
        try {
            ServiceUtils.executeService(userView, "DeleteCurricularCourseGroup", args1);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("degreeId", infoDegreeCurricularPlan.getInfoDegree().getIdInternal());
        request.setAttribute("degreeCurricularPlanId", infoDegreeCurricularPlan.getIdInternal());

        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        return viewCurricularCourseGroups(mapping, form, request, response);
    }

    public ActionForward prepareEditCurricularCourseGroup(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException, FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);
        Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));

        Object args[] = { degreeCurricularPlanId };
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        try {
            infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) ServiceUtils.executeService(userView,
                    "ReadDegreeCurricularPlan", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        Integer groupId = new Integer(request.getParameter("groupId"));
        Object[] args1 = { groupId };
        InfoCurricularCourseGroup infoCurricularCourseGroup = null;
        try {
            infoCurricularCourseGroup = (InfoCurricularCourseGroup) ServiceUtils.executeService(
                    userView, "ReadCurricularCourseGroup", args1);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        List branches;
        try {
            branches = (List) ServiceUtils.executeService(userView,
                    "ReadBranchesByDegreeCurricularPlan", args);

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", "", e);
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        //  branches.remove(infoCurricularCourseGroup.getInfoBranch());
        DynaActionForm actionForm = (DynaActionForm) form;
        actionForm.set("name", infoCurricularCourseGroup.getName());
        actionForm.set("groupId", infoCurricularCourseGroup.getIdInternal());
        actionForm.set("branchId", infoCurricularCourseGroup.getInfoBranch().getIdInternal());
        if (infoCurricularCourseGroup instanceof InfoAreaCurricularCourseGroup) {
            actionForm.set("areaType", ((InfoAreaCurricularCourseGroup) infoCurricularCourseGroup)
                    .getAreaType().name());
            actionForm.set("minimumValue", ((InfoAreaCurricularCourseGroup) infoCurricularCourseGroup)
                    .getMinimumCredits());
            actionForm.set("maximumValue", ((InfoAreaCurricularCourseGroup) infoCurricularCourseGroup)
                    .getMaximumCredits());
        } else {
            actionForm.set("minimumValue",
                    ((InfoOptionalCurricularCourseGroup) infoCurricularCourseGroup)
                            .getMinimumNumberOfOptionalCourses());
            actionForm.set("maximumValue",
                    ((InfoOptionalCurricularCourseGroup) infoCurricularCourseGroup)
                            .getMaximumNumberOfOptionalCourses());
        }

        request.setAttribute("degreeId", infoDegreeCurricularPlan.getInfoDegree().getIdInternal());
        request.setAttribute("degreeCurricularPlanId", infoDegreeCurricularPlan.getIdInternal());

        request.setAttribute("branches", branches);
        request.setAttribute("areas", AreaType.toLabelValueBeanList());
        request.setAttribute("infoCurricularCourseGroup", infoCurricularCourseGroup);
        request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);
        return mapping.findForward("edit");
    }

    /**
     * @param type
     * @return
     */
    private Object getClassName(String type) {
        if (type.equals("area")) {
            return AreaCurricularCourseGroup.class.getName();
        }
        return OptionalCurricularCourseGroup.class.getName();
    }
}