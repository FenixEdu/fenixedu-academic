package ServidorApresentacao.Action.masterDegree.administrativeOffice.gratuity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.LookupDispatchAction;

import DataBeans.InfoGratuityValues;
import DataBeans.InfoPaymentPhase;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.Data;

/**
 * @author Fernanda Quitério 6/Jan/2003
 *  
 */
public class InsertGratuityDataLookupDispatchAction extends LookupDispatchAction
{
	public ActionForward addPhase(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);
		ActionErrors errors = new ActionErrors();

		DynaActionForm gratuityForm = (DynaActionForm) form;
		String[] paymentPhases = (String[]) gratuityForm.get("paymentPhases");
		String initialDate = (String) gratuityForm.get("initialDatePartialPayment");
		String finalDate = (String) gratuityForm.get("finalDatePartialPayment");
		String phaseValue = (String) gratuityForm.get("phaseValue");

		if (finalDate == null
			|| finalDate.length() == 0
			|| phaseValue == null
			|| phaseValue.length() == 0)
		{
			errors.add("wrongValues", new ActionError("error.masterDegree.gratuity.phaseValues"));
			saveErrors(request, errors);
		}

		List infoPaymentPhases = new ArrayList();
		for (int i = 0; i < paymentPhases.length; i = i + 3)
		{
			fillPaymentPhasesList(
				paymentPhases[i],
				paymentPhases[i + 1],
				paymentPhases[i + 2],
				infoPaymentPhases);
		}

		if (errors.isEmpty())
		{
			if (initialDate == null
				|| initialDate.length() == 0
				|| (initialDate.length() > 0
					&& !Data.convertStringDate(finalDate, "/").before(
						Data.convertStringDate(initialDate, "/"))))
			{
				fillPaymentPhasesList(
					initialDate,
					finalDate,
					phaseValue,
					infoPaymentPhases);
			}
			else
			{
				errors.add(
					"wrongDates",
					new ActionError("error.masterDegree.gratuity.phase.wrongDates"));
				saveErrors(request, errors);
			}
		}
		Collections.sort(infoPaymentPhases, new BeanComparator("endDate"));
		request.setAttribute("infoPaymentPhases", infoPaymentPhases);

		return mapping.findForward("insertGratuityData");
	}

	private void fillPaymentPhasesList(
		String initialDate,
		String finalDate,
		String phaseValue,
		List infoPaymentPhases)
	{
		InfoPaymentPhase newInfoPaymentPhase = new InfoPaymentPhase();
		if (initialDate != null)
		{
			newInfoPaymentPhase.setStartDate(Data.convertStringDate(initialDate, "/"));
		}
		newInfoPaymentPhase.setEndDate(Data.convertStringDate(finalDate, "/"));
		newInfoPaymentPhase.setValue(new Double(phaseValue));
		infoPaymentPhases.add(newInfoPaymentPhase);
	}

	public ActionForward removePhase(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		DynaActionForm gratuityForm = (DynaActionForm) form;
		String[] paymentPhases = (String[]) gratuityForm.get("paymentPhases");

		List infoPaymentPhases = new ArrayList();
		for (int i = 0; i < paymentPhases.length; i = i + 3)
		{
			fillPaymentPhasesList(
				paymentPhases[i],
				paymentPhases[i + 1],
				paymentPhases[i + 2],
				infoPaymentPhases);
		}
		Collections.sort(infoPaymentPhases, new BeanComparator("endDate"));

		String[] phasesToRemove = (String[]) gratuityForm.get("removedPhases");
//		for (int i = 0; i < phasesToRemove.length; i++)
//		{
//			System.out.println(phasesToRemove[i]);
//		}

		removePhasesFromList(phasesToRemove, infoPaymentPhases);
		gratuityForm.set("removedPhases", new String[] {});
		request.setAttribute("infoPaymentPhases", infoPaymentPhases);

		return mapping.findForward("insertGratuityData");
	}

	private void removePhasesFromList(String[] phasesToRemove, List infoPaymentPhases)
	{
		List objectsToRemove = new ArrayList();
		List toRemoveList = Arrays.asList(phasesToRemove);
		Iterator iterToRemove = toRemoveList.iterator();
		while (iterToRemove.hasNext())
		{
			String toRemove = (String) iterToRemove.next();
			objectsToRemove.add(infoPaymentPhases.get(Integer.valueOf(toRemove).intValue()));
		}		
		infoPaymentPhases.removeAll(objectsToRemove);
	}

	public ActionForward cancelInsertGratuityData(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);
		return mapping.findForward("cancel");
	}

	public ActionForward insertGratuityData(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = SessionUtils.getUserView(request);
		DynaActionForm actionForm = (DynaActionForm) form;
		
		InfoGratuityValues infoGratuityValues = fillGratuity(actionForm);
		
		return mapping.findForward("insertGratuityData");
	} /*
	   * (non-Javadoc)
	   * 
	   * @see org.apache.struts.actions.LookupDispatchAction#getKeyMethodMap()
	   */
	/**
	 * @param actionForm
	 * @return
	 */
	private InfoGratuityValues fillGratuity(DynaActionForm actionForm)
	{
		InfoGratuityValues infoGratuityValues = new InfoGratuityValues();
		
		if(actionForm.get("annualValue") != null) {
		infoGratuityValues.setAnualValue(Double.valueOf((String) actionForm.get("annualValue")));
		}
		if(actionForm.get("scholarPart") != null) {
		infoGratuityValues.setScholarShipValue(Double.valueOf((String) actionForm.get("scholarPart")));
		infoGratuityValues.setFinalProofValue(Double.valueOf((String) actionForm.get("thesisPart")));
		}
		infoGratuityValues.setProofRequestPayment((Boolean) actionForm.get("paymentWhen"));
		if(actionForm.get("totalPayment") != null && ((Boolean)actionForm.get("totalPayment")).equals(Boolean.TRUE)) {
			infoGratuityValues.setEndPayment(Data.convertStringDate((String) actionForm.get("finalDateTotalPayment"),"/"));
			if(actionForm.get("initialDateTotalPayment") != null) {
				infoGratuityValues.setStartPayment(Data.convertStringDate((String) actionForm.get("initialDateTotalPayment"),"/"));
			}
		}
		if(actionForm.get("partialPayment") != null && ((Boolean)actionForm.get("partialPayment")).equals(Boolean.TRUE)) {
			if(actionForm.get("registrationPayment") != null && ((Boolean)actionForm.get("registrationPayment")).equals(Boolean.TRUE)) {
				
				infoGratuityValues.setRegistrationPayment((Boolean)actionForm.get("registrationPayment"));
				
				// treat registration payment as a phase payment
				InfoPaymentPhase infoPaymentPhase = new InfoPaymentPhase();
				infoPaymentPhase.setEndDate(Data.convertStringDate((String) actionForm.get("finalDateRegistrationPayment"),"/"));
				if(actionForm.get("initialDateRegistrationPayment") != null) {
					infoPaymentPhase.setStartDate(Data.convertStringDate((String) actionForm.get("initialDateRegistrationPayment"),"/"));
				}
				infoPaymentPhase.setValue(Double.valueOf((String) actionForm.get("registrationValue")));
				
				String[] paymentPhases = (String[]) actionForm.get("paymentPhases");

				List infoPaymentPhases = new ArrayList();
				for (int i = 0; i < paymentPhases.length; i = i + 3)
				{
					fillPaymentPhasesList(
						paymentPhases[i],
						paymentPhases[i + 1],
						paymentPhases[i + 2],
						infoPaymentPhases);
				}
				infoPaymentPhases.add(infoPaymentPhase);
				
			}
		}
		
		return null;
	}

	protected Map getKeyMethodMap()
	{

		Map map = new HashMap();
		map.put("button.masterDegree.gratuity.submit", "insertGratuityData");
		map.put("button.masterDegree.gratuity.addPhase", "addPhase");
		map.put("button.masterDegree.gratuity.remove", "removePhase");
		map.put("button.cancel", "cancelInsertGratuityData");
		return map;
	}
}