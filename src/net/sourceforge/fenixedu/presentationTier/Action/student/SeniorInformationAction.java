/*
 * Created on Dec 10, 2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.student;

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.student.InfoSenior;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *  
 */
public class SeniorInformationAction extends FenixDispatchAction {

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return forward to the action mapping configuration called show-form
     * @throws Exception
     */
    public ActionForward prepareEdit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = getUserView(request);

        Object[] args = { userView };
        InfoSenior infoSenior = (InfoSenior) ServiceUtils.executeService(userView,
                "ReadSeniorInfoByUsername", args);

        if (infoSenior == null) {
            return setError(request, mapping, "error.senior.studentNotASenior", "show-form", null);
        }

        DynaActionForm seniorInfoForm = (DynaActionForm) form;
        seniorInfoForm.set("seniorIDInternal", infoSenior.getIdInternal());
        seniorInfoForm.set("name", infoSenior.getName());
        seniorInfoForm.set("address", infoSenior.getAddress());
        seniorInfoForm.set("areaCode", infoSenior.getAreaCode());
        seniorInfoForm.set("areaCodeArea", infoSenior.getAreaCodeArea());
        seniorInfoForm.set("phone", infoSenior.getPhone());
        seniorInfoForm.set("mobilePhone", infoSenior.getMobilePhone());
        seniorInfoForm.set("email", infoSenior.getEmail());
        seniorInfoForm.set("availablePhoto", infoSenior.getAvailablePhoto());
        seniorInfoForm.set("personID", infoSenior.getPersonID());

        if (infoSenior.getExpectedDegreeTermination() != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(infoSenior.getExpectedDegreeTermination());
            seniorInfoForm.set("expectedDegreeTerminationDay", new Integer(calendar
                    .get(Calendar.DAY_OF_MONTH)));
            seniorInfoForm.set("expectedDegreeTerminationMonth", new Integer(
                    calendar.get(Calendar.MONTH) + 1));
            seniorInfoForm.set("expectedDegreeTerminationYear", new Integer(calendar.get(Calendar.YEAR)));
        }

        seniorInfoForm.set("expectedDegreeAverageGrade", infoSenior.getExpectedDegreeAverageGrade());
        seniorInfoForm.set("specialtyField", infoSenior.getSpecialtyField());
        seniorInfoForm.set("professionalInterests", infoSenior.getProfessionalInterests());
        seniorInfoForm.set("languageSkills", infoSenior.getLanguageSkills());
        seniorInfoForm.set("informaticsSkills", infoSenior.getInformaticsSkills());
        seniorInfoForm.set("extracurricularActivities", infoSenior.getExtracurricularActivities());
        seniorInfoForm.set("professionalExperience", infoSenior.getProfessionalExperience());
        seniorInfoForm.set("lastModificationDate", infoSenior.getLastModificationDate());

        return mapping.findForward("show-form");
    }

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        DynaActionForm changedSeniorInfoForm = (DynaActionForm) form;

        Integer expectedDegreeTerminationDay = (Integer) changedSeniorInfoForm
                .get("expectedDegreeTerminationDay");
        Integer expectedDegreeTerminationMonth = (Integer) changedSeniorInfoForm
                .get("expectedDegreeTerminationMonth");
        Integer expectedDegreeTerminationYear = (Integer) changedSeniorInfoForm
                .get("expectedDegreeTerminationYear");
        Calendar expectedDegreeTermination = Calendar.getInstance();
        expectedDegreeTermination.set(expectedDegreeTerminationYear.intValue(),
                expectedDegreeTerminationMonth.intValue() - 1, expectedDegreeTerminationDay.intValue());

        // display an error if date is set before today
/*        if (expectedDegreeTermination.before(Calendar.getInstance())) {
            return setError(request, mapping, "error.senior.terminationDateBeforeToday", "show-form",
                    null);
        }
*/
        InfoSenior changedInfoSenior = new InfoSenior();
        changedInfoSenior.setIdInternal((Integer) changedSeniorInfoForm.get("seniorIDInternal"));
        changedInfoSenior.setExpectedDegreeTermination(expectedDegreeTermination.getTime());
        changedInfoSenior.setExpectedDegreeAverageGrade((Integer) changedSeniorInfoForm
                .get("expectedDegreeAverageGrade"));
        changedInfoSenior.setSpecialtyField((String) changedSeniorInfoForm.get("specialtyField"));
        changedInfoSenior.setProfessionalInterests((String) changedSeniorInfoForm
                .get("professionalInterests"));
        changedInfoSenior.setLanguageSkills((String) changedSeniorInfoForm.get("languageSkills"));
        changedInfoSenior.setInformaticsSkills((String) changedSeniorInfoForm.get("informaticsSkills"));
        changedInfoSenior.setExtracurricularActivities((String) changedSeniorInfoForm
                .get("extracurricularActivities"));
        changedInfoSenior.setProfessionalExperience((String) changedSeniorInfoForm
                .get("professionalExperience"));

        IUserView userView = SessionUtils.getUserView(request);
        Object[] args = { changedInfoSenior };
        ServiceUtils.executeService(userView, "EditSeniorInfo", args);

        request.setAttribute("seniorInfoForm", changedSeniorInfoForm);
        
        return mapping.findForward("show-result");
    }

    /*
     * Sets an error to display later in the Browser and sets the mapping
     * forward.
     */
    protected ActionForward setError(HttpServletRequest request, ActionMapping mapping,
            String errorMessage, String forwardPage, Object actionArg) {
        ActionErrors errors = new ActionErrors();
        String notMessageKey = errorMessage;
        ActionError error = new ActionError(notMessageKey, actionArg);
        errors.add(notMessageKey, error);
        saveErrors(request, errors);

        if (forwardPage != null) {
            return mapping.findForward(forwardPage);
        }

        return mapping.getInputForward();

    }
}