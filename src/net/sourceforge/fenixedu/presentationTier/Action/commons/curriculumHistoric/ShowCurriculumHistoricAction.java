/*
 * Created on Oct 11, 2004
 */
package net.sourceforge.fenixedu.presentationTier.Action.commons.curriculumHistoric;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.commons.curriculumHistoric.InfoCurriculumHistoricReport;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author nmgo
 * @author lmre
 */
public class ShowCurriculumHistoricAction extends FenixDispatchAction {

    public ActionForward showCurriculumHistoric(ActionMapping mapping, ActionForm form,
	    HttpServletRequest request, HttpServletResponse response) throws FenixServiceException,
	    FenixFilterException {

	final Integer curricularCourseOID = getIntegerFromRequest(request, "curricularCourseCode");
	final Integer semester = getIntegerFromRequest(request, "semester");
	final Integer executionYearOID = getIntegerFromRequest(request, "executionYearID");
	final Object[] args = new Object[] { curricularCourseOID, semester, executionYearOID };

	final InfoCurriculumHistoricReport result = (InfoCurriculumHistoricReport) ServiceUtils
		.executeService(getUserView(request), "ReadCurriculumHistoricReport", args);

	request.setAttribute("infoCurriculumHistoricReport", result);

	return mapping.findForward("show-report");
    }

}
