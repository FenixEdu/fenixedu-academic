package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.coordinator;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileAlreadyExistsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FileNameTooLongServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.domain.ApplicationDocumentType;
import net.sourceforge.fenixedu.fileSuport.FileSuportObject;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class CandidateOperationDispatchAction extends FenixDispatchAction {

    /** request params * */
    public static final String REQUEST_DOCUMENT_TYPE = "documentType";

    public ActionForward getCandidates(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanID"));

            List candidates = null;
            Object args[] = { degreeCurricularPlanId };

            try {
                candidates = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ReadDegreeCandidates", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }

            if (candidates.size() == 0)
                throw new NonExistingActionException("error.exception.nonExistingCandidates", "", null);

            request.setAttribute("masterDegreeCandidateList", candidates);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanId);

            return mapping.findForward("ViewList");
        }
        throw new Exception();
    }

    public ActionForward chooseCandidate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {

        IUserView userView = SessionUtils.getUserView(request);

        Integer degreeCurricularPlanID = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        Integer candidateID = Integer.valueOf(request.getParameter("candidateID"));

        Object[] args = { candidateID };

        InfoMasterDegreeCandidate infoMasterDegreeCandidate;
        try {
            infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory
                    .executeService(userView, "ReadMasterDegreeCandidateByID", args);
        } catch (FenixServiceException e) {
            e.printStackTrace();
            throw new FenixActionException();
        }

        List candidateStudyPlan = getCandidateStudyPlanByCandidateID(candidateID, userView);

        request.setAttribute("masterDegreeCandidate", infoMasterDegreeCandidate);
        request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
        request.setAttribute("candidateStudyPlan", candidateStudyPlan);

        return mapping.findForward("ActionReady");
    }

    public ActionForward showApplicationDocuments(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String documentTypeStr = request.getParameter(REQUEST_DOCUMENT_TYPE);
        Integer degreeCurricularPlanID = Integer.valueOf(request.getParameter("degreeCurricularPlanID"));
        Integer candidateID = new Integer(request.getParameter("candidateID"));

        InfoMasterDegreeCandidate masterDegreeCandidate = null;
        try {
            Object args[] = { candidateID };
            masterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceManagerServiceFactory
                    .executeService(userView, "GetCandidatesByID", args);
        } catch (Exception e) {
            throw new Exception(e);
        }

        FileSuportObject file = null;
        try {
            Object args[] = {
                    masterDegreeCandidate.getInfoPerson().getIdInternal(),
                    ((documentTypeStr != null) ? ApplicationDocumentType.valueOf(documentTypeStr)
                            : ApplicationDocumentType.CURRICULUM_VITAE) };
            file = (FileSuportObject) ServiceUtils.executeService(userView,
                    "RetrieveApplicationDocument", args);

        } catch (FileAlreadyExistsServiceException e1) {
        } catch (FileNameTooLongServiceException e1) {
        } catch (FenixServiceException e1) {
        }

        if (file != null) {
            response.setHeader("Content-disposition", "attachment;filename=" + file.getFileName());
            response.setContentType(file.getContentType());
            DataOutputStream dos = new DataOutputStream(response.getOutputStream());
            dos.write(file.getContent());
            dos.close();
        } else {
            List candidateStudyPlan = getCandidateStudyPlanByCandidateID(candidateID, userView);
            request.setAttribute("masterDegreeCandidate", masterDegreeCandidate);
            request.setAttribute("degreeCurricularPlanID", degreeCurricularPlanID);
            request.setAttribute("candidateStudyPlan", candidateStudyPlan);
            return mapping.findForward("ActionReady");
        }

        return null;

    }

    /**
     * 
     * @author Ricardo Clerigo & Telmo Nabais
     * @param candidateID
     * @param userView
     * @return
     */
    private ArrayList getCandidateStudyPlanByCandidateID(Integer candidateID, IUserView userView) {
        Object[] args = { candidateID };

        try {
            return (ArrayList) ServiceManagerServiceFactory.executeService(userView,
                    "ReadCandidateEnrolmentsByCandidateID", args);
        } catch (Exception e) {
            return null;
        }
    }

}