/*
 * Created on 17/Set/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.DeleteBranches;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditBranch;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.InsertBranch;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadBranch;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadBranchesByDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranchEditor;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.bennu.core.security.Authenticate;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/manageBranches", input = "/manageBranches.do?method=showBranches&page=0",
        attribute = "branchForm", formBean = "branchForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "manageBranches", path = "/manager/manageBranches_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/degreeCurricularPlanNavLocalManager.jsp")),
        @Forward(name = "deleteBranchConfirmation", path = "/manager/deleteBranchesConfirmation_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/manageBranchesNavLocalManager.jsp")),
        @Forward(name = "readDegree", path = "/readDegree.do"),
        @Forward(name = "insertBranch", path = "/manager/insertBranch_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/manageBranchesNavLocalManager.jsp")),
        @Forward(name = "editBranch", path = "/manager/editBranch_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/manageBranchesNavLocalManager.jsp")) })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
                key = "resources.Action.exceptions.NonExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
                key = "resources.Action.exceptions.ExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class ManageBranchesDA extends FenixDispatchAction {

    /**
     * Prepare information to show related branches
     */
    public ActionForward showBranches(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();
        String degreeCurricularPlanIdString = request.getParameter("degreeCurricularPlanId");
        String degreeIdString = request.getParameter("degreeId");

        String degreeCurricularPlanId = null;
        if (degreeCurricularPlanIdString != null) {
            degreeCurricularPlanId = degreeCurricularPlanIdString;
        }

        DynaActionForm branchesForm = (DynaActionForm) form;
        branchesForm.set("degreeCurricularPlanId", degreeCurricularPlanId);
        branchesForm.set("degreeId", new Integer(degreeIdString));

        // request.setAttribute("degreeCurricularPlanId",
        // degreeCurricularPlanIdString);
        // request.setAttribute("degreeId", degreeIdString);

        List infoBranches;
        try {
            infoBranches = ReadBranchesByDegreeCurricularPlan.run(degreeCurricularPlanId);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping.findForward("readDegree"));
        } catch (FenixServiceException ex) {
            throw new FenixActionException(ex.getMessage());
        }

        if (infoBranches != null) {
            Collections.sort(infoBranches, new BeanComparator("code"));
        }
        request.setAttribute("infoBranchesList", infoBranches);

        return mapping.findForward("manageBranches");
    }

    /**
     * Delete selected branches
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();
        DynaActionForm deleteForm = (DynaActionForm) form;

        List branchesIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));

        List errorCodes = new ArrayList();

        try {
            errorCodes = DeleteBranches.run(branchesIds, new Boolean(false));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage(), fenixServiceException);
        }

        if (!errorCodes.isEmpty()) {
            ActionErrors actionErrors = new ActionErrors();
            Iterator iter = errorCodes.iterator();
            ActionError error = null;
            while (iter.hasNext()) {
                error = new ActionError("errors.invalid.delete.not.empty.branch", iter.next());
                actionErrors.add("errors.invalid.delete.not.empty.branch", error);
            }
            saveErrors(request, actionErrors);
            request.setAttribute("branchesIds", branchesIds);
            return mapping.findForward("deleteBranchConfirmation");
        }
        return showBranches(mapping, form, request, response);
    }

    /**
     * Delete selected branches even if not empty
     */
    public ActionForward forceDelete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        User userView = Authenticate.getUser();
        DynaActionForm deleteForm = (DynaActionForm) form;

        List branchesIds = Arrays.asList((Integer[]) deleteForm.get("internalIds"));

        try {
            DeleteBranches.run(branchesIds, new Boolean(true));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        return showBranches(mapping, form, request, response);
    }

    /**
     * Prepare to insert a branch
     */
    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        return mapping.findForward("insertBranch");
    }

    /**
     * Insert a branch
     */
    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();
        String degreeCurricularPlanIdString = request.getParameter("degreeCurricularPlanId");
        String degreeIdString = request.getParameter("degreeId");

        request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanIdString);
        request.setAttribute("degreeId", degreeIdString);

        String degreeCurricularPlanId = null;
        if (degreeCurricularPlanIdString != null) {
            degreeCurricularPlanId = request.getParameter("degreeCurricularPlanId");
        }
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);

        DynaActionForm insertForm = (DynaActionForm) form;
        String name = (String) insertForm.get("name");
        String nameEn = (String) insertForm.get("nameEn");
        String code = (String) insertForm.get("code");

        // Constructing errors in case the user doesn´t submit the name or the
        // code
        ActionErrors errors = buildErrors(code, name);
        if (errors != null) {
            saveErrors(request, errors);
            return mapping.findForward("insertBranch");
        }

        // in case there are no errors
        InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan(degreeCurricularPlan);

        InfoBranchEditor infoBranch = new InfoBranchEditor();
        infoBranch.setCode(code);
        infoBranch.setName(name);
        infoBranch.setNameEn(nameEn);
        infoBranch.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

        try {
            InsertBranch.run(infoBranch);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping.findForward("readDegree"));
            // } catch (ExistingServiceException exception) {
            // throw new
            // ExistingActionException("message.already.existing.branch",
            // mapping.findForward("insertBranch"));
            // } catch (FenixServiceException ex) {
            // throw new FenixActionException(ex.getMessage());
        }

        return showBranches(mapping, form, request, response);
    }

    /**
     * Prepare to edit a branch
     */
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm editForm = (DynaActionForm) form;
        User userView = Authenticate.getUser();
        String branchId = request.getParameter("branchId");

        InfoBranch infoBranch;

        try {
            infoBranch = ReadBranch.run(branchId);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.non.existing.branch", showBranches(mapping, form, request, response));
        } catch (FenixServiceException ex) {
            throw new FenixActionException(ex.getMessage());
        }

        editForm.set("name", infoBranch.getName());
        editForm.set("nameEn", infoBranch.getNameEn());
        editForm.set("code", infoBranch.getCode());
        return mapping.findForward("editBranch");
    }

    /**
     * Edit a branch
     */
    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();
        String branchId = request.getParameter("branchId");

        DynaActionForm editForm = (DynaActionForm) form;
        String name = (String) editForm.get("name");
        String nameEn = (String) editForm.get("nameEn");
        String code = (String) editForm.get("code");

        // Constructing errors in case the user doesn´t submit the name or the
        // code
        ActionErrors errors = buildErrors(code, name);
        if (errors != null) {
            saveErrors(request, errors);
            return mapping.findForward("editBranch");
        }

        // in case there are no errors
        InfoBranchEditor infoBranch = new InfoBranchEditor();
        infoBranch.setCode(code);
        infoBranch.setName(name);
        infoBranch.setNameEn(nameEn);
        infoBranch.setExternalId(branchId);

        try {
            EditBranch.run(infoBranch);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.non.existing.branch", showBranches(mapping, form, request, response));
        } catch (ExistingServiceException exception) {
            throw new ExistingActionException("message.already.existing.branch", mapping.findForward("editBranch"));
        } catch (FenixServiceException ex) {
            throw new FenixActionException(ex.getMessage());
        }

        return showBranches(mapping, form, request, response);
    }

    /**
     * Auxiliar function to construct errors
     */
    private ActionErrors buildErrors(String code, String name) {
        ActionErrors errors = new ActionErrors();
        ActionError error;
        boolean existingError = false;
        if (code.compareTo("") == 0) {
            error = new ActionError("message.must.define.code");
            errors.add("message.must.define.code", error);
            existingError = true;
        }
        if (name.compareTo("") == 0) {
            error = new ActionError("message.must.define.name");
            errors.add("message.must.define.name", error);
            existingError = true;
        }
        if (existingError) {
            return errors;
        }

        return null;
    }
}