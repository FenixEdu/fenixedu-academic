/**
 * Nov 14, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.credits;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageDegreeTeachingServicesDispatchAction extends FenixDispatchAction {

    public static final Comparator<TeachingServicePercentage> TEACHING_SERVICE_PERCENTAGE_COMPARATOR_BY_SHIFT = new Comparator<TeachingServicePercentage>() {
        public int compare(TeachingServicePercentage teachingServicePercentage1, TeachingServicePercentage teachingServicePercentage2) {
            return Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS.compare(teachingServicePercentage1.getShift(), teachingServicePercentage2.getShift());
        }        
    };

    protected void teachingServiceDetailsProcess(Professorship professorship,
            HttpServletRequest request, DynaActionForm dynaForm) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        List<TeachingServicePercentage> teachingServicePercentages = new ArrayList();
        HashMap teacherPercentageMap = new HashMap();
        for (Iterator iter = professorship.getExecutionCourse().getAssociatedShiftsIterator(); iter
                .hasNext();) {
            Shift shift = (Shift) iter.next();
            Double availablePercentage = shift.getAvailableShiftPercentageForTeacher(professorship
                    .getTeacher());
            teachingServicePercentages.add(new TeachingServicePercentage(shift, availablePercentage));
            for (Iterator iterator = shift.getDegreeTeachingServices().iterator(); iterator.hasNext();) {
                DegreeTeachingService degreeTeachingService = (DegreeTeachingService) iterator.next();
                if (professorship == degreeTeachingService.getProfessorship()) {
                    teacherPercentageMap.put(shift.getIdInternal().toString(), round(degreeTeachingService
                            .getPercentage()));
                    break;
                }
            }
        }

        Iterator teachingServicePercentagesIterator = new OrderedIterator(teachingServicePercentages
                .iterator(), TEACHING_SERVICE_PERCENTAGE_COMPARATOR_BY_SHIFT);

        request.setAttribute("professorship", professorship);
        request.setAttribute("teachingServicePercentages", teachingServicePercentagesIterator);
        dynaForm.set("teacherPercentageMap", teacherPercentageMap);
    }
    
    protected ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, RoleType roleType) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm teachingServiceForm = (DynaActionForm) form;
        IUserView userView = SessionUtils.getUserView(request);
        HashMap teacherPercentageMap = (HashMap) teachingServiceForm.get("teacherPercentageMap");

        Integer professorshipID = (Integer) teachingServiceForm.get("professorshipID");

        List<ShiftIDTeachingPercentage> shiftIDPercentages = new ArrayList<ShiftIDTeachingPercentage>();
        Iterator entryInterator = teacherPercentageMap.entrySet().iterator();
        while (entryInterator.hasNext()) {
            Map.Entry entry = (Map.Entry) entryInterator.next();
            String percentage = (String) entry.getValue();
            if ((percentage != null) && (percentage.length() != 0)) {
                percentage = percentage.replace(',', '.');
                Integer shiftID = Integer.valueOf((String) entry.getKey());
                ShiftIDTeachingPercentage shiftIDPercentage = new ShiftIDTeachingPercentage(shiftID,
                        Double.valueOf(percentage));
                shiftIDPercentages.add(shiftIDPercentage);
            }
        }

        try {
            Object[] args = { professorshipID, shiftIDPercentages, roleType };
            ServiceUtils.executeService(userView, "UpdateDegreeTeachingServices", args);
        } catch (DomainException domainException) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error", new ActionMessage(domainException.getMessage(), domainException
                    .getArgs()));
            saveMessages(request, actionMessages);
            Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);
            teachingServiceDetailsProcess(professorship, request, teachingServiceForm);
            return mapping.findForward("show-teaching-service-percentages");
        }

        return mapping.findForward("sucessfull-edit");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws NumberFormatException, FenixFilterException,
            FenixServiceException {

        return mapping.findForward("sucessfull-edit");
    }

    public class ShiftIDTeachingPercentage {

        Integer shiftID;

        Double percentage;

        public ShiftIDTeachingPercentage(Integer shiftID, Double percentage) {
            setShiftID(shiftID);
            setPercentage(percentage);
        }

        public Double getPercentage() {
            return percentage;
        }

        public void setPercentage(Double percentage) {
            this.percentage = percentage;
        }

        public Integer getShiftID() {
            return shiftID;
        }

        public void setShiftID(Integer shiftID) {
            this.shiftID = shiftID;
        }

    }

    public class TeachingServicePercentage {

        Shift shift;

        Double availablePercentage;

        public TeachingServicePercentage(Shift shift, Double availablePercentage) {
            setShift(shift);
            setAvailablePercentage(availablePercentage);
        }

        public Double getAvailablePercentage() {
            return availablePercentage;
        }

        public void setAvailablePercentage(Double availablePercentage) {
            this.availablePercentage = availablePercentage;
        }

        public Shift getShift() {
            return shift;
        }

        public void setShift(Shift shift) {
            this.shift = shift;
        }
    }
    
    private Double round(double n) {
	return Math.round((n * 100.0)) / 100.0;
    }
}
