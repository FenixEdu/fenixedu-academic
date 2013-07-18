/*
 * Created on 30/Mai/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.InsertDegree;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

/**
 * @author lmac1
 */
@Mapping(module = "manager", path = "/insertDegree", input = "/insertDegree.do?method=prepareInsert&page=0",
        attribute = "degreeForm", formBean = "degreeForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "insertDegree", path = "/manager/insertDegree_bd.jsp"),
        @Forward(name = "readDegrees", path = "/readDegrees.do") })
@Exceptions(value = { @ExceptionHandling(
        type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
        key = "resources.Action.exceptions.ExistingActionException",
        handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class InsertDegreeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareInsert(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {

        return mapping.findForward("insertDegree");
    }

    public ActionForward insert(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        IUserView userView = UserView.getUser();

        DynaActionForm dynaForm = (DynaValidatorForm) form;
        String code = (String) dynaForm.get("code");
        String name = (String) dynaForm.get("name");
        String nameEn = (String) dynaForm.get("nameEn");
        String degreeTypeInt = (String) dynaForm.get("degreeType");
        String gradeTypeString = (String) dynaForm.get("gradeType");

        DegreeType degreeType = DegreeType.valueOf(degreeTypeInt);
        GradeScale gradeScale = null;
        if (gradeTypeString != null && gradeTypeString.length() > 0) {
            gradeScale = GradeScale.valueOf(gradeTypeString);
        }

        try {
            InsertDegree.run(code, name, nameEn, degreeType, gradeScale);

        } catch (ExistingServiceException ex) {
            throw new ExistingActionException("message.already.existing.degree", mapping.findForward("readDegrees"));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e.getMessage());
        }

        return mapping.findForward("readDegrees");
    }
}