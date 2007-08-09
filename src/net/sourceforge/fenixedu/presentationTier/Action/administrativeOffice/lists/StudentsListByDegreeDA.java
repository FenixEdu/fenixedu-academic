/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.lists;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.lists.SearchStudentsByDegreeParametersBean;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationWithStateForExecutionYearBean;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * @author - Ângela Almeida (argelina@ist.utl.pt)
 * 
 */
public class StudentsListByDegreeDA extends FenixDispatchAction {

    public ActionForward prepareByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("searchParametersBean", new SearchStudentsByDegreeParametersBean());
	return mapping.findForward("searchRegistrations");
    }

    public ActionForward postBack(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final Object renderedObject = getRenderedObject();
	RenderUtils.invalidateViewState();
	request.setAttribute("searchParametersBean", renderedObject);

	return mapping.findForward("searchRegistrations");
    }

    public ActionForward searchByDegree(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	final SearchStudentsByDegreeParametersBean searchBean = (SearchStudentsByDegreeParametersBean) getRenderedObject();

	final List<RegistrationWithStateForExecutionYearBean> registrations = (List<RegistrationWithStateForExecutionYearBean>) executeService(
		"SearchStudents", searchBean);

	request.setAttribute("searchParametersBean", searchBean);
	request.setAttribute("studentCurricularPlanList", registrations);

	return mapping.findForward("searchRegistrations");
    }

}
