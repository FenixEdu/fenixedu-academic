/**
 * Aug 29, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCountry;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManagePreEnrollmentInquiriesDispatchAction extends FenixDispatchAction {

	public ActionForward prepareDislocatedStudentInquiry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		final Student student = getStudent(request);

		final DynaActionForm studentDataInquiryForm = (DynaActionForm) form;
		studentDataInquiryForm.set("studentID", student.getIdInternal());

		if (student.hasDislocatedStudent()) {
			return mapping.findForward("proceedToPersonalDataInquiry");
		}
		
		// TODO: later these questions will not be here
		final IUserView userView = getUserView(request);
		final List infoCountries = (List) ServiceManagerServiceFactory.executeService(userView,
				"ReadAllCountries", null);

		List uniqueInfoCountries = new ArrayList();
		Integer defaultCountryID = null;
		for (Iterator iter = infoCountries.iterator(); iter.hasNext();) {
			InfoCountry infoCountry = (InfoCountry) iter.next();
			if (!containsCountry(uniqueInfoCountries, infoCountry)) {
				uniqueInfoCountries.add(infoCountry);
				if (infoCountry.getName().equalsIgnoreCase("PORTUGAL")) {
					defaultCountryID = infoCountry.getIdInternal();
				}
			}
		}
		Collections.sort(uniqueInfoCountries, new BeanComparator("name"));
		
		studentDataInquiryForm.set("countryID", defaultCountryID);
		
		request.setAttribute("infoCountries", uniqueInfoCountries);
		request.setAttribute("infoDistricts", rootDomainObject.getDistricts());

		if (studentDataInquiryForm.get("dislocatedCountryID") != null) {
			Integer dislocatedCountryID = (Integer) studentDataInquiryForm.get("dislocatedCountryID");
			if (dislocatedCountryID.equals(defaultCountryID)) {
				request.setAttribute("portugal", "portugal");
			}
		}

		if (studentDataInquiryForm.get("dislocatedAnswer") != null) {
			String dislocatedAnswer = (String) studentDataInquiryForm.get("dislocatedAnswer");
			if (dislocatedAnswer.equalsIgnoreCase("true")) {
				request.setAttribute("dislocated", "dislocated");
			}
		}

		return mapping.findForward("showDislocatedStudentInquiry");
	}

	public ActionForward registerDislocatedStudentInquiryAnswers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		final Student student = getStudent(request);

		final DynaActionForm studentDataInquiryForm = (DynaActionForm) form;
		final Integer countryID = (Integer) studentDataInquiryForm.get("countryID");
		final String dislocatedStudent = studentDataInquiryForm.getString("dislocatedAnswer");
		Integer districtID = null;
		Integer dislocatedCountryID = null;
		if (!StringUtils.isEmpty(dislocatedStudent) && dislocatedStudent.equalsIgnoreCase("true")) {
			dislocatedCountryID = (Integer) studentDataInquiryForm.get("dislocatedCountryID");
			districtID = (Integer) studentDataInquiryForm.get("districtID");
		}

		final Object[] args = { student.getIdInternal(), countryID, dislocatedCountryID, districtID };
		ServiceManagerServiceFactory.executeService(getUserView(request), "WriteDislocatedStudentAnswer", args);

		return mapping.findForward("proceedToPersonalDataInquiry");
	}

	public ActionForward preparePersonalDataInquiry(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		final Student student = getStudent(request);

		if (student.getActualPersonalDataAuthorizationAnswer() != null) {
			return mapping.findForward("proceedToEnrollment");
		}

		return mapping.findForward("showAuthorizationInquiry");
	}

	public ActionForward registerPersonalDataInquiryAnswer(ActionMapping mapping, ActionForm actionForm,
			HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
			FenixServiceException {

		final Student student = getStudent(request);

		final DynaActionForm form = (DynaActionForm) actionForm;
		final String answer = form.getString("authorizationAnswer");

		if (StringUtils.isEmpty(answer)) {
			addActionMessage(request, "error.enrollment.personalInquiry.mandatory");
			return mapping.getInputForward();
		}
		ServiceManagerServiceFactory.executeService(getUserView(request),
				"WriteStudentPersonalDataAuthorizationAnswer", new Object[] { student.getIdInternal(),
						answer });

		return mapping.findForward("proceedToEnrollment");
	}

	private boolean containsCountry(List infoCountries, final InfoCountry country) {
		return CollectionUtils.exists(infoCountries, new Predicate() {

			public boolean evaluate(Object arg0) {
				InfoCountry infoCountry = (InfoCountry) arg0;
				return infoCountry.getName().equalsIgnoreCase(country.getName());
			}
		});
	}
	
	private Student getStudent(HttpServletRequest request) {
		return getUserView(request).getPerson().getStudentByUsername();
	}
}
