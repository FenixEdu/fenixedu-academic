package net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager.AddShiftsToSchoolClass;
import net.sourceforge.fenixedu.dataTransferObject.InfoClass;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.base.FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

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
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(module = "resourceAllocationManager", path = "/addShifts", input = "/manageClass.do?method=prepare&page=0", attribute = "selectMultipleItemsForm", formBean = "selectMultipleItemsForm", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "EditClass", path = "/manageClass.do?method=prepare&page=0") })
public class AddShiftsDA extends FenixClassAndExecutionDegreeAndCurricularYearContextDispatchAction {

    public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	    throws Exception {

	final InfoClass infoClass = (InfoClass) request.getAttribute(PresentationConstants.CLASS_VIEW);

	final DynaActionForm addShiftsForm = (DynaActionForm) form;
	List<String> selectedShifts = Arrays.asList((String[]) addShiftsForm.get("selectedItems"));

	try {
	    AddShiftsToSchoolClass.run(infoClass, selectedShifts);
	} catch (ExistingServiceException ex) {
	    // No problem, the user refreshed the page after adding classes
	    request.setAttribute("selectMultipleItemsForm", null);
	    return mapping.getInputForward();
	}

	return mapping.findForward("EditClass");
    }

}