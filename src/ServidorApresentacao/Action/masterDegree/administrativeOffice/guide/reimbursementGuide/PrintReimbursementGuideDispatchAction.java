/*
 * Created on 25/Abr/2004
 *  
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide.reimbursementGuide;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.guide.reimbursementGuide.InfoReimbursementGuide;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali</a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed</a>
 *  
 */
public class PrintReimbursementGuideDispatchAction extends FenixDispatchAction
{

	public ActionForward print(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		IUserView userView = SessionUtils.getUserView(request);

		Integer reimbursementGuideId = new Integer(this.getFromRequest("id", request));

		InfoReimbursementGuide infoReimbursementGuide = null;

		Object args[] = { reimbursementGuideId };
		try
		{
			infoReimbursementGuide =
				(InfoReimbursementGuide) ServiceUtils.executeService(
					userView,
					"ViewReimbursementGuide",
					args);

		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e.getMessage(), mapping.findForward("error"));
		}

		Locale locale = this.getLocale(request);
		Date date =
			infoReimbursementGuide
				.getActiveInfoReimbursementGuideSituation()
				.getOfficialDate()
				.getTime();

		String formatedDate = DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);

		request.setAttribute(SessionConstants.DATE, formatedDate);
		request.setAttribute(SessionConstants.REIMBURSEMENT_GUIDE, infoReimbursementGuide);

		return mapping.findForward("start");

	}

	private String getFromRequest(String parameter, HttpServletRequest request)
	{
		String parameterString = request.getParameter(parameter);
		if (parameterString == null)
		{
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}

}
