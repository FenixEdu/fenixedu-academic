/*
 * Created on 14/Mar/2003
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoStudent;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoDocumentoIdentificacao;
import framework.factory.ServiceManagerServiceFactory;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to Choose choose, visualize and edit a Guide.
 *  
 */
public class GuideListingDispatchAction extends DispatchAction {

    public ActionForward prepareChooseYear(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            DynaActionForm chooseYearForm = (DynaActionForm) form;

            chooseYearForm.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

            return mapping.findForward("PrepareReady");
        }
        throw new Exception();

    }

    public ActionForward chooseYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            DynaActionForm chooseGuideForm = (DynaActionForm) form;

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            // Get the Information
            Integer guideYear = new Integer((String) chooseGuideForm.get("year"));

            Object args[] = { guideYear };

            List result = null;
            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide",
                        args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Guia", e);
            }

            request.setAttribute(SessionConstants.GUIDE_LIST, result);

            return mapping.findForward("ShowGuideList");
        }
        throw new Exception();
    }

    public ActionForward chooseGuide(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            Integer guideYear = new Integer(request.getParameter("year"));
            Integer guideNumber = new Integer(request.getParameter("number"));

            Object args[] = { guideNumber, guideYear };

            List result = null;
            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide",
                        args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Guia", e);
            }
            
            request.setAttribute(SessionConstants.GUIDE_LIST, result);
            if (result.size() == 1) {
                request.setAttribute(SessionConstants.GUIDE, result.get(0));
                return mapping.findForward("ActionReady");
            }

            request.setAttribute(SessionConstants.GUIDE_YEAR, guideYear);
            request.setAttribute(SessionConstants.GUIDE_NUMBER, guideNumber);

            return mapping.findForward("ShowVersionList");
        }
        throw new Exception();
    }

    public ActionForward prepareChoosePerson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {
            // Create the type of Identification Document
            request.setAttribute(SessionConstants.IDENTIFICATION_DOCUMENT_TYPE_LIST,
                    TipoDocumentoIdentificacao.toArrayList());

            return mapping.findForward("PrepareSuccess");
        }
        throw new Exception();

    }

    public ActionForward getPersonGuideList(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
            DynaActionForm choosePersonForm = (DynaActionForm) form;

            String identificationDocumentNumber = (String) choosePersonForm
                    .get("identificationDocumentNumber");
            TipoDocumentoIdentificacao identificationDocumentType = new TipoDocumentoIdentificacao(
                    (String) choosePersonForm.get("identificationDocumentType"));
            String studentNumber = (String) choosePersonForm.get("studentNumber");

            if (identificationDocumentNumber == null || identificationDocumentNumber.length() == 0) {
                InfoStudent infoStudent = null;
                if (studentNumber != null && studentNumber.length() > 0) {
                    Object args[] = { Integer.valueOf(studentNumber) };

                    try {
                        infoStudent = (InfoStudent) ServiceManagerServiceFactory.executeService(
                                userView, "ReadStudentByNumberAndAllDegreeTypes", args);
                    } catch (FenixServiceException e) {
                        throw new FenixActionException(e);
                    }
                }
                if (infoStudent == null) {
                    throw new NonExistingActionException("A Pessoa");
                }
                identificationDocumentNumber = infoStudent.getInfoPerson()
                        .getNumeroDocumentoIdentificacao();
                identificationDocumentType = infoStudent.getInfoPerson().getTipoDocumentoIdentificacao();
            }

            Object args[] = { identificationDocumentNumber, identificationDocumentType };

            List result = null;
            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView, "ChooseGuide",
                        args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Pessoa", e);
            }

            if (result == null) {
                throw new NonExistingActionException("error.exception.noGuidesForPerson",
                        "Guias para esta pessoa");
            }

            request.setAttribute(SessionConstants.GUIDE_LIST, result);

            return mapping.findForward("ShowGuideList");
        }
        throw new Exception();

    }

    public ActionForward chooseGuideByPerson(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

            Integer personID = Integer.valueOf(request.getParameter("personID"));

            Object args[] = { personID };

            List result = null;
            try {
                result = (List) ServiceManagerServiceFactory.executeService(userView,
                        "ChooseGuideByPersonID", args);
            } catch (NonExistingServiceException e) {
                throw new NonExistingActionException("A Pessoa", e);
            }

            if (result == null) {
                throw new NonExistingActionException("error.exception.noGuidesForPerson",
                        "Guias para esta pessoa");
            }

            request.setAttribute(SessionConstants.GUIDE_LIST, result);

            return mapping.findForward("ShowGuideList");
        }
        throw new Exception();

    }

}