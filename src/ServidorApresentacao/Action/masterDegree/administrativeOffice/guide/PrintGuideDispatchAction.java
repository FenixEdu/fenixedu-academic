package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoGuide;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.GuideRequester;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class PrintGuideDispatchAction extends DispatchAction
{

    public ActionForward prepare(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {
            GestorServicos serviceManager = GestorServicos.manager();

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            Boolean passwordPrint = (Boolean) session.getAttribute(SessionConstants.PRINT_PASSWORD);
            InfoGuide infoGuide = (InfoGuide) session.getAttribute(SessionConstants.GUIDE);

            String graduationType = (String) request.getAttribute("graduationType");
            if (graduationType == null)
                graduationType = request.getParameter("graduationType");
            request.setAttribute("graduationType", graduationType);

            if (infoGuide.getGuideRequester().equals(GuideRequester.CANDIDATE_TYPE))
            {
                // Read The Candidate
                InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
                try
                {

                    if (session.getAttribute(SessionConstants.REQUESTER_NUMBER) == null)
                    {
                        Object args[] = { infoGuide.getInfoExecutionDegree(), infoGuide.getInfoPerson()};
                        infoMasterDegreeCandidate =
                            (InfoMasterDegreeCandidate) serviceManager.executar(
                                userView,
                                "ReadMasterDegreeCandidate",
                                args);
                    } else
                    {
                        Integer number =
                            (Integer) session.getAttribute(SessionConstants.REQUESTER_NUMBER);
                        Object args[] =
                            { infoGuide.getInfoExecutionDegree(), infoGuide.getInfoPerson(), number };
                        infoMasterDegreeCandidate =
                            (InfoMasterDegreeCandidate) serviceManager.executar(
                                userView,
                                "ReadCandidateListByPersonAndExecutionDegree",
                                args);
                    }
                } catch (FenixServiceException e)
                {
                    throw new FenixActionException();
                }
                session.setAttribute(
                    SessionConstants.MASTER_DEGREE_CANDIDATE,
                    infoMasterDegreeCandidate);
                if ((passwordPrint != null) && passwordPrint.booleanValue())
                {
                    infoMasterDegreeCandidate.getInfoPerson().setPassword(
                        infoGuide.getInfoPerson().getPassword());
                }
            }

            Locale locale = new Locale("pt", "PT");
            Date date = new Date();

            String formatedDate =
                "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
            session.setAttribute(SessionConstants.DATE, formatedDate);

            return mapping.findForward("PrintReady");
        } else
            throw new Exception();

    }

    public ActionForward print(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {
            GestorServicos serviceManager = GestorServicos.manager();

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            Integer number = new Integer(request.getParameter("number"));
            Integer year = new Integer(request.getParameter("year"));
            Integer version = new Integer(request.getParameter("version"));

            session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE);

            InfoGuide infoGuide = null;
            try
            {
                Object args[] = { number, year, version };
                infoGuide = (InfoGuide) serviceManager.executar(userView, "ChooseGuide", args);
            } catch (FenixServiceException e)
            {
                throw new FenixActionException();
            }

            Locale locale = new Locale("pt", "PT");
            Date date = new Date();

            String formatedDate =
                "Lisboa, " + DateFormat.getDateInstance(DateFormat.LONG, locale).format(date);
            session.setAttribute(SessionConstants.DATE, formatedDate);

            session.setAttribute(SessionConstants.GUIDE, infoGuide);

            return mapping.findForward("PrintOneGuide");
        } else
            throw new Exception();

    }

}
