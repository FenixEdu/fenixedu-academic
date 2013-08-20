/*
 * Created on 28/Jul/2003
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.EditDegree;
import net.sourceforge.fenixedu.applicationTier.Servico.manager.ReadDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

/**
 * @author lmac1
 */

@Mapping(module = "manager", path = "/editDegree", input = "/editDegree.do?method=prepareEdit&page=0", attribute = "degreeForm",
        formBean = "degreeForm", scope = "request", parameter = "method")
@Forwards(value = {
        @Forward(name = "editDegree", path = "/manager/editDegree_bd.jsp", tileProperties = @Tile(
                navLocal = "/manager/degreeNavLocalManager.jsp")), @Forward(name = "readDegrees", path = "/readDegrees.do"),
        @Forward(name = "readDegree", path = "/readDegree.do") })
@Exceptions(value = {
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException.class,
                key = "resources.Action.exceptions.NonExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request"),
        @ExceptionHandling(type = net.sourceforge.fenixedu.presentationTier.Action.exceptions.ExistingActionException.class,
                key = "resources.Action.exceptions.ExistingActionException",
                handler = net.sourceforge.fenixedu.presentationTier.config.FenixErrorExceptionHandler.class, scope = "request") })
public class EditDegreeDispatchAction extends FenixDispatchAction {

    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        DynaActionForm readDegreeForm = (DynaActionForm) form;

        InfoDegree oldInfoDegree = null;

        try {
            oldInfoDegree = ReadDegree.run(request.getParameter("degreeId"));

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegree", mapping.findForward("readDegrees"));
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }

        DegreeType degreeType = (DegreeType) oldInfoDegree.getDegreeType();

        readDegreeForm.set("name", oldInfoDegree.getNome());
        readDegreeForm.set("code", oldInfoDegree.getSigla());
        readDegreeForm.set("nameEn", oldInfoDegree.getNameEn());
        readDegreeForm.set("degreeType", degreeType.toString());
        if (oldInfoDegree.getGradeScale() != null) {
            readDegreeForm.set("gradeType", oldInfoDegree.getGradeScale().toString());
        }
        return mapping.findForward("editDegree");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {

        DynaActionForm editDegreeForm = (DynaActionForm) form;
        String oldDegreeId = request.getParameter("degreeId");
        String code = (String) editDegreeForm.get("code");
        String name = (String) editDegreeForm.get("name");
        String nameEn = (String) editDegreeForm.get("nameEn");
        String degreeTypeInt = (String) editDegreeForm.get("degreeType");
        String gradeTypeString = (String) editDegreeForm.get("gradeType");

        DegreeType degreeType = DegreeType.valueOf(degreeTypeInt);
        GradeScale gradeScale = null;
        if (gradeTypeString != null && gradeTypeString.length() > 0) {
            gradeScale = GradeScale.valueOf(gradeTypeString);
        }

        try {
            EditDegree.run(oldDegreeId, code, name, nameEn, degreeType, gradeScale, ExecutionYear.readCurrentExecutionYear());

        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("message.nonExistingDegree", mapping.findForward("readDegrees"));
        } catch (ExistingServiceException e) {
            throw new ExistingActionException("message.manager.existing.degree");
        } catch (FenixServiceException fenixServiceException) {
            throw new FenixActionException(fenixServiceException.getMessage());
        }
        return mapping.findForward("readDegree");
    }
}