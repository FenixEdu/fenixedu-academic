/*
 * Created on 24/Nov/2003
 *  
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide.reimbursementGuide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoGuide;
import DataBeans.guide.reimbursementGuide.InfoReimbursementGuide;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidGuideSituationServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidReimbursementValueServiceException;
import ServidorAplicacao.Servico.exceptions.guide.InvalidReimbursementValueSumServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota</a> 24/Nov/2003
 *  
 */
public class ReimbursementGuideDispatchAction extends FenixDispatchAction
{

    public ActionForward prepareCreate(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        String guideIdString = request.getParameter("guideId");
        Integer guideId = null;
        if (guideIdString != null)
        {
            guideId = new Integer(guideIdString);
        }
        else
        {
            guideId = (Integer) request.getAttribute("guideId");
        }
        Object[] args = { guideId };
        try
        {
            InfoGuide infoGuide = (InfoGuide) ServiceUtils.executeService(userView, "ReadGuide", args);
            request.setAttribute("infoGuide", infoGuide);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("createReimbursementGuide");
    }

    public ActionForward create(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        DynaActionForm reimbursementGuideForm = (DynaActionForm) form;
        Integer guideId = (Integer) reimbursementGuideForm.get("guideId");
        String justification = (String) reimbursementGuideForm.get("justification");
        Double value = (Double) reimbursementGuideForm.get("value");
        Object[] args = { guideId, justification, value, userView };
        Object[] args1 = { guideId };
        InfoGuide infoGuide;
        ActionForward viewGuideForward = null;
        try
        {
            infoGuide = (InfoGuide) ServiceUtils.executeService(userView, "ReadGuide", args1);
            request.setAttribute(SessionConstants.GUIDE, infoGuide);
            request.setAttribute("guideNumber", infoGuide.getNumber());
            request.setAttribute("guideYear", infoGuide.getYear());
            request.setAttribute("guideId", infoGuide.getIdInternal());
            viewGuideForward = buildViewGuideForward(infoGuide, mapping.findForward("viewGuide"));
        }
        catch (FenixServiceException e1)
        {
            throw new FenixActionException(e1);
        }

        try
        {
            ServiceUtils.executeService(userView, "CreateReimbursementGuide", args);

            return viewGuideForward;
        }
        catch (InvalidReimbursementValueServiceException e)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(
                "InvalidReimbursementValueServiceException",
                new ActionError("errors.InvalidReimbursementValueServiceException"));
            saveErrors(request, actionErrors);

            return prepareCreate(mapping, form, request, response);
        }
        catch (InvalidReimbursementValueSumServiceException e)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(
                "InvalidReimbursementValueSumServiceException",
                new ActionError("errors.InvalidReimbursementValueSumServiceException"));
            saveErrors(request, actionErrors);
            return prepareCreate(mapping, form, request, response);
        }
        catch (InvalidGuideSituationServiceException e)
        {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add(
                "InvalidGuideSituationServiceException",
                new ActionError("errors.InvalidGuideSituationServiceException"));
            saveErrors(request, actionErrors);
            return prepareCreate(mapping, form, request, response);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }
    }

    /**
	 * @param infoGuide
	 * @param forward
	 * @return
	 */
    private ActionForward buildViewGuideForward(InfoGuide infoGuide, ActionForward forward)
    {
        ActionForward viewGuideForward = new ActionForward();
        try
        {
            PropertyUtils.copyProperties(viewGuideForward, forward);
            StringBuffer path = new StringBuffer(viewGuideForward.getPath());
            path.append("&guideNumber=").append(infoGuide.getNumber()).append("&guideYear=").append(
                infoGuide.getYear());
            viewGuideForward.setPath(path.toString());

        }
        catch (Exception e)
        {
            e.printStackTrace(System.out);
            //ignored
        }
        return viewGuideForward;
    }

    public ActionForward view(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws FenixActionException
    {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String reimbursementGuideIdString = request.getParameter("reimbursementGuideId");
        Integer reimbursementGuideId = new Integer(reimbursementGuideIdString);

        Object[] args = { reimbursementGuideId };
        try
        {
            InfoReimbursementGuide infoReimbursementGuide =
                (InfoReimbursementGuide) ServiceUtils.executeService(
                    userView,
                    "ViewReimbursementGuide",
                    args);
            request.setAttribute("infoGuide", infoReimbursementGuide.getInfoGuide());
            request.setAttribute("infoReimbursementGuide", infoReimbursementGuide);
        }
        catch (FenixServiceException e)
        {
            throw new FenixActionException(e);
        }

        return mapping.findForward("viewReimbursementGuide");
    }
}
