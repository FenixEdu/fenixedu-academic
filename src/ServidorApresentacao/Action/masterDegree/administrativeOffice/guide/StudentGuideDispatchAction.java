package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoGuide;
import DataBeans.InfoGuideEntry;
import DataBeans.InfoPrice;
import ServidorApresentacao.Action.exceptions.InvalidInformationInFormActionException;
import ServidorApresentacao.Action.exceptions.NoChangeMadeActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.DocumentType;
import Util.GraduationType;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class StudentGuideDispatchAction extends DispatchAction
{

    public ActionForward createReady(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
        throws Exception
    {

        HttpSession session = request.getSession(false);

        if (session != null)
        {
		
            DynaActionForm studentGuideForm = (DynaActionForm) form;
            InfoGuide infoGuide = (InfoGuide) session.getAttribute(SessionConstants.GUIDE);


            List certificateList = (List) session.getAttribute(SessionConstants.CERTIFICATE_LIST);
			
            String[] quantityList = request.getParameterValues("quantityList");

            String specializationGratuityQuantityString =
                (String) studentGuideForm.get("specializationGratuityQuantity");
            String specializationGratuityAmountString =
                (String) studentGuideForm.get("specializationGratuityAmount");
			if (specializationGratuityAmountString.equals("0.0"))
				specializationGratuityAmountString="0";
			
            String graduationType = (String) request.getAttribute("graduationType");
            if (graduationType == null)
                graduationType = request.getParameter("graduationType");

            request.setAttribute("graduationType", graduationType);

            String othersGratuityAmountString = (String) studentGuideForm.get("othersGratuityAmount");
            Integer othersGratuityAmount = null;
            
			if (othersGratuityAmountString.equals("0.0"))
				othersGratuityAmountString="0";
				
            if ((othersGratuityAmountString != null) && (othersGratuityAmountString.length() > 0))
            {
                try
                {
                    othersGratuityAmount = new Integer(othersGratuityAmountString);
                    if (othersGratuityAmount.intValue() < 0)
                        throw new NumberFormatException();
                } catch (NumberFormatException e)
                {	
                    throw new InvalidInformationInFormActionException(new Throwable());
                }
            }

            String othersGratuityDescription =
                (String) studentGuideForm.get("othersGratuityDescription");

            Iterator iterator = certificateList.iterator();

            int position = 0;
            infoGuide.setInfoGuideEntries(new ArrayList());

            while (iterator.hasNext())
            {
                iterator.next();
                Integer quantity = null;

                try
                {
                    quantity = new Integer(quantityList[position]);
                    if (quantity.intValue() < 0)
                        throw new NumberFormatException();
                } catch (NumberFormatException e)
                {
                    throw new InvalidInformationInFormActionException(new Throwable());
                }

                if (quantity.intValue() > 0)
                {
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
                && (specializationGratuityQuantityString.length() != 0))
            {
                InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
                infoGuideEntry.setDescription("Pagamento para Especialização");
                infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
                infoGuideEntry.setDocumentType(DocumentType.GRATUITY_TYPE);
                infoGuideEntry.setPrice(new Double(specializationGratuityAmountString));

                infoGuideEntry.setQuantity(new Integer(specializationGratuityQuantityString));
                infoGuide.getInfoGuideEntries().add(infoGuideEntry);
            }
			
            if (othersGratuityAmount != null)
            {
                InfoGuideEntry infoGuideEntry = new InfoGuideEntry();
                infoGuideEntry.setDescription(othersGratuityDescription);
                infoGuideEntry.setGraduationType(GraduationType.MASTER_DEGREE_TYPE);
                infoGuideEntry.setDocumentType(DocumentType.GRATUITY_TYPE);
                infoGuideEntry.setPrice(new Double(othersGratuityAmountString));
                infoGuideEntry.setQuantity(new Integer(1));
                infoGuide.getInfoGuideEntries().add(infoGuideEntry);
            }
			
            if (infoGuide.getInfoGuideEntries().size() == 0)
                throw new NoChangeMadeActionException("error.exception.noCertificateChosen");

            generateToken(request);
            saveToken(request);

            request.setAttribute(SessionConstants.GUIDE, infoGuide);


            return mapping.findForward("CreateStudentGuideReady");

        } else
            throw new Exception();
    }


}
