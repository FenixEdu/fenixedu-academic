/*
 * Created on 14/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.ChooseGuide;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide.ChooseGuideByPersonID;
import net.sourceforge.fenixedu.applicationTier.Servico.student.ReadStudentByNumberAndDegreeType;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NonExistingActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *         This is the Action to Choose choose, visualize and edit a Guide.
 * 
 */
public class GuideListingDispatchAction extends FenixDispatchAction {

    public ActionForward prepareChooseYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm chooseYearForm = (DynaActionForm) form;

        chooseYearForm.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

        return mapping.findForward("PrepareReady");

    }

    public ActionForward chooseYear(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm chooseGuideForm = (DynaActionForm) form;

        IUserView userView = getUserView(request);

        // Get the Information
        Integer guideYear = new Integer((String) chooseGuideForm.get("year"));

        List result = null;
        try {
            result = ChooseGuide.runChooseGuide(guideYear);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A Guia", e);
        }

        request.setAttribute(PresentationConstants.GUIDE_LIST, result);

        return mapping.findForward("ShowGuideList");

    }

    public ActionForward chooseGuide(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);
        Integer guideYear = new Integer(request.getParameter("year"));
        Integer guideNumber = new Integer(request.getParameter("number"));

        List result = null;
        try {
            result = ChooseGuide.runChooseGuide(guideNumber, guideYear);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A Guia", e);
        }

        request.setAttribute(PresentationConstants.GUIDE_LIST, result);
        if (result.size() == 1) {
            request.setAttribute(PresentationConstants.GUIDE, result.iterator().next());
            return mapping.findForward("ActionReady");
        }

        request.setAttribute(PresentationConstants.GUIDE_YEAR, guideYear);
        request.setAttribute(PresentationConstants.GUIDE_NUMBER, guideNumber);

        return mapping.findForward("ShowVersionList");

    }

    public ActionForward prepareChoosePerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        return mapping.findForward("PrepareSuccess");
    }

    public ActionForward getPersonGuideList(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);
        DynaActionForm choosePersonForm = (DynaActionForm) form;

        String identificationDocumentNumber = (String) choosePersonForm.get("identificationDocumentNumber");

        String identificationDocumentString = (String) choosePersonForm.get("identificationDocumentType");
        IDDocumentType identificationDocumentType = null;
        if (identificationDocumentString != null && !identificationDocumentString.equals("")) {
            identificationDocumentType = IDDocumentType.valueOf(identificationDocumentString);
        }
        String studentNumber = (String) choosePersonForm.get("studentNumber");

        if (identificationDocumentNumber == null || identificationDocumentNumber.length() == 0) {
            InfoStudent infoStudent = null;
            if (studentNumber != null && studentNumber.length() > 0) {

                infoStudent =
                        (InfoStudent) ReadStudentByNumberAndDegreeType.run(Integer.valueOf(studentNumber),
                                DegreeType.MASTER_DEGREE);
            }
            if (infoStudent == null) {
                throw new NonExistingActionException("A Person");
            }
            identificationDocumentNumber = infoStudent.getInfoPerson().getNumeroDocumentoIdentificacao();
            identificationDocumentType = infoStudent.getInfoPerson().getTipoDocumentoIdentificacao();
        }

        List result = null;
        try {
            result = ChooseGuide.runChooseGuide(identificationDocumentNumber, identificationDocumentType);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A Person", e);
        }

        if (result == null) {
            throw new NonExistingActionException("error.exception.noGuidesForPerson", "Guias para esta pessoa");
        }

        ComparatorChain comparator = new ComparatorChain();
        comparator.addComparator(new BeanComparator("year"), false);
        Collections.sort(result, comparator);

        request.setAttribute(PresentationConstants.GUIDE_LIST, result);

        return mapping.findForward("ShowGuideList");
    }

    public ActionForward chooseGuideByPerson(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        String personID = request.getParameter("personID");

        List result = null;
        try {
            result = ChooseGuideByPersonID.run(personID);
        } catch (NonExistingServiceException e) {
            throw new NonExistingActionException("A Person", e);
        }

        if (result == null) {
            throw new NonExistingActionException("error.exception.noGuidesForPerson", "Guias para esta pessoa");
        }

        request.setAttribute(PresentationConstants.GUIDE_LIST, result);

        return mapping.findForward("ShowGuideList");

    }

}