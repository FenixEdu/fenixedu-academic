package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuide;
import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoPrice;
import net.sourceforge.fenixedu.domain.DocumentType;
import net.sourceforge.fenixedu.domain.GraduationType;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.InvalidInformationInFormActionException;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.NoChangeMadeActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 *  
 */
public class StudentGuideDispatchAction extends FenixDispatchAction {

    public ActionForward createReady(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession(false);

        if (session != null) {

            DynaActionForm studentGuideForm = (DynaActionForm) form;
            InfoGuide infoGuide = (InfoGuide) session.getAttribute(SessionConstants.GUIDE);

            List certificateList = (List) session.getAttribute(SessionConstants.CERTIFICATE_LIST);

            String[] quantityList = request.getParameterValues("quantityList");

            String specializationGratuityQuantityString = (String) studentGuideForm
                    .get("specializationGratuityQuantity");
            String specializationGratuityAmountString = (String) studentGuideForm
                    .get("specializationGratuityAmount");
            if (specializationGratuityAmountString.equals("0.0"))
                specializationGratuityAmountString = "0";

            String graduationType = (String) request.getAttribute("graduationType");
            if (graduationType == null)
                graduationType = request.getParameter("graduationType");
            request.setAttribute("graduationType", graduationType);

            String requester = (String) studentGuideForm.get("requester");
            if (requester == null)
                requester = (String) request.getAttribute(SessionConstants.REQUESTER_TYPE);
            if (requester == null)
                requester = request.getParameter(SessionConstants.REQUESTER_TYPE);
            request.setAttribute(SessionConstants.REQUESTER_TYPE, requester);

            String othersGratuityAmountString = (String) studentGuideForm.get("othersGratuityAmount");
            Integer othersGratuityAmount = null;

            if (othersGratuityAmountString.equals("0.0"))
                othersGratuityAmountString = "0";

            if ((othersGratuityAmountString != null) && (othersGratuityAmountString.length() > 0)) {
                try {
                    othersGratuityAmount = new Integer(othersGratuityAmountString);
                    if (othersGratuityAmount.intValue() < 0)
                        throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    throw new InvalidInformationInFormActionException(new Throwable());
                }
            }

            String othersGratuityDescription = (String) studentGuideForm
                    .get("othersGratuityDescription");

            Iterator iterator = certificateList.iterator();

            int position = 0;
            infoGuide.setInfoGuideEntries(new ArrayList());

            while (iterator.hasNext()) {
                iterator.next();
                Integer quantity = null;

                try {
                    quantity = new Integer(quantityList[position]);
                    if (quantity.intValue() < 0)
                        throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    throw new InvalidInformationInFormActionException(new Throwable());
                }

                if (quantity.intValue() > 0) {
                    InfoPrice infoPrice = (InfoPrice) certificateList.get(position);
                    InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
                    infoGuideEntry.setDescription(infoPrice.getDescription());
                    infoGuideEntry.setDocumentType(infoPrice.getDocumentType());
                    infoGuideEntry.setGraduationType(infoPrice.getGraduationType());
                    infoGuideEntry.setPrice(infoPrice.getPrice());
                    infoGuideEntry.setQuantity(quantity);
                    infoGuide.getInfoGuideEntries().add(infoGuideEntry);
                }

                position++;

            }

            if ((specializationGratuityAmountString != null)
                    && (specializationGratuityAmountString.length() != 0)
                    && (specializationGratuityQuantityString != null)
                    && (specializationGratuityQuantityString.length() != 0)) {
                InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
                infoGuideEntry.setDescription("Pagamento para Especialização");
                infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
                infoGuideEntry.setDocumentType(DocumentType.GRATUITY);
                infoGuideEntry.setPrice(new Double(specializationGratuityAmountString));

                infoGuideEntry.setQuantity(new Integer(specializationGratuityQuantityString));
                infoGuide.getInfoGuideEntries().add(infoGuideEntry);
            }

            if (othersGratuityAmount != null) {
                InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
                infoGuideEntry.setDescription(othersGratuityDescription);
                infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE);
                infoGuideEntry.setDocumentType(DocumentType.GRATUITY);
                infoGuideEntry.setPrice(new Double(othersGratuityAmountString));
                infoGuideEntry.setQuantity(new Integer(1));
                infoGuide.getInfoGuideEntries().add(infoGuideEntry);
            }

            if (infoGuide.getInfoGuideEntries().size() == 0)
                throw new NoChangeMadeActionException("error.exception.noCertificateChosen");

            generateToken(request);
            saveToken(request);

            request.setAttribute(SessionConstants.GUIDE, infoGuide);

            Integer number = new Integer(request.getParameter("number"));
            request.setAttribute(SessionConstants.REQUESTER_NUMBER, number);

            return mapping.findForward("CreateStudentGuideReady");

        }
        throw new Exception();
    }

}