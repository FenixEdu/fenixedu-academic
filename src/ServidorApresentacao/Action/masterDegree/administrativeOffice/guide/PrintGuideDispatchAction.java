package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.text.DateFormat;
import java.util.Iterator;
import java.util.List;
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
import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.GuideRequester;
import Util.TipoCurso;
import framework.factory.ServiceManagerServiceFactory;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class PrintGuideDispatchAction extends DispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = (IUserView) session
                    .getAttribute(SessionConstants.U_VIEW);
            Boolean passwordPrint = (Boolean) session
                    .getAttribute(SessionConstants.PRINT_PASSWORD);
            InfoGuide infoGuide = (InfoGuide) session
                    .getAttribute(SessionConstants.GUIDE);
            String graduationType = (String) request
                    .getAttribute("graduationType");
            if (graduationType == null)
                    graduationType = request.getParameter("graduationType");
            request.setAttribute("graduationType", graduationType);

            if (request.getParameter(SessionConstants.REQUESTER_NUMBER) != null) {
                Integer numberRequester = new Integer(request
                        .getParameter(SessionConstants.REQUESTER_NUMBER));
                request.setAttribute(SessionConstants.REQUESTER_NUMBER,
                        numberRequester);
            }

            if (infoGuide.getGuideRequester().equals(
                    GuideRequester.CANDIDATE_TYPE)) {
                // Read The Candidate
                InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
                try {
                    if (session.getAttribute(SessionConstants.REQUESTER_NUMBER) == null) {
                        Object args[] = { infoGuide.getInfoExecutionDegree(),
                                infoGuide.getInfoPerson()};
                        infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory
                                .executeService(userView,
                                        "ReadMasterDegreeCandidate", args);
                    } else {
                        Integer number = (Integer) session
                                .getAttribute(SessionConstants.REQUESTER_NUMBER);
                        Object args[] = { infoGuide.getInfoExecutionDegree(),
                                infoGuide.getInfoPerson(), number};
                        infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory
                                .executeService(
                                        userView,
                                        "ReadCandidateListByPersonAndExecutionDegree",
                                        args);
                    }
                } catch (FenixServiceException e) {
                    throw new FenixActionException();
                }
                session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE,
                        infoMasterDegreeCandidate);
                if ((passwordPrint != null) && passwordPrint.booleanValue()) {
                    infoMasterDegreeCandidate.getInfoPerson().setPassword(
                            infoGuide.getInfoPerson().getPassword());
                }
            }

            //			try {
            //				Object args[] = {infoGuide.getInfoPerson().getUsername()};
            //				InfoStudent infoStudent = (InfoStudent)
            // ServiceManagerServiceFactory
            //						.executeService(userView, "ReadStudentByUsername", args);
            //				session.setAttribute(SessionConstants.REQUESTER_NUMBER,
            //						infoStudent.getNumber());
            //			} catch (FenixServiceException e) {
            //				throw new FenixActionException();
            //			}
            Locale locale = new Locale("pt", "PT");
            String formatedDate = "Lisboa, "
                    + DateFormat.getDateInstance(DateFormat.LONG, locale)
                            .format(infoGuide.getCreationDate());
            session.setAttribute(SessionConstants.DATE, formatedDate);
            return mapping.findForward("PrintReady");
        } else
            throw new Exception();
    }

    public ActionForward print(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        HttpSession session = request.getSession(false);
        if (session != null) {
            IUserView userView = (IUserView) session
                    .getAttribute(SessionConstants.U_VIEW);
            Integer number = new Integer(request.getParameter("number"));
            Integer year = new Integer(request.getParameter("year"));
            Integer version = new Integer(request.getParameter("version"));
            session.removeAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE);

            if (request.getParameter(SessionConstants.REQUESTER_NUMBER) != null) {
                Integer numberRequester = new Integer(request
                        .getParameter(SessionConstants.REQUESTER_NUMBER));
                request.setAttribute(SessionConstants.REQUESTER_NUMBER,
                        numberRequester);
            }

            InfoGuide infoGuide = null;
            try {
                Object args[] = { number, year, version};
                infoGuide = (InfoGuide) ServiceManagerServiceFactory
                        .executeService(userView, "ChooseGuide", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }

            InfoStudent infoStudent = null;
            List infoStudents = null;

            Object args2[] = { infoGuide.getInfoPerson()};

            try {
                infoStudents = (List) ServiceUtils.executeService(userView,
                        "ReadStudentsByPerson", args2);

                Iterator it = infoStudents.iterator();
                while (it.hasNext()) {
                    infoStudent = (InfoStudent) it.next();
                    if (infoStudent.getDegreeType().equals(
                            TipoCurso.MESTRADO_OBJ)) break;
                }
                request.setAttribute(SessionConstants.STUDENT, infoStudent
                        .getNumber());
            } catch (FenixServiceException e) {
                throw new FenixActionException();
            }

            Locale locale = new Locale("pt", "PT");
            String formatedDate = "Lisboa, "
                    + DateFormat.getDateInstance(DateFormat.LONG, locale)
                            .format(infoGuide.getCreationDate());
            session.setAttribute(SessionConstants.DATE, formatedDate);
            session.setAttribute(SessionConstants.GUIDE, infoGuide);
            return mapping.findForward("PrintOneGuide");
        } else
            throw new Exception();
    }
}