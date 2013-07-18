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
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.services.UpdateDegreeTeachingServices;
import net.sourceforge.fenixedu.commons.OrderedIterator;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.teacher.DegreeTeachingService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageDegreeTeachingServicesDispatchAction extends FenixDispatchAction {

    public static final Comparator<TeachingServicePercentage> TEACHING_SERVICE_PERCENTAGE_COMPARATOR_BY_SHIFT =
            new Comparator<TeachingServicePercentage>() {
                @Override
                public int compare(TeachingServicePercentage teachingServicePercentage1,
                        TeachingServicePercentage teachingServicePercentage2) {
                    return Shift.SHIFT_COMPARATOR_BY_TYPE_AND_ORDERED_LESSONS.compare(teachingServicePercentage1.getShift(),
                            teachingServicePercentage2.getShift());
                }
            };

    protected void teachingServiceDetailsProcess(Professorship professorship, HttpServletRequest request, DynaActionForm dynaForm)
            throws NumberFormatException,  FenixServiceException {

        List<TeachingServicePercentage> teachingServicePercentages = new ArrayList<TeachingServicePercentage>();
        HashMap<String, Double> teacherPercentageMap = new HashMap<String, Double>();

        for (Shift shift : professorship.getExecutionCourse().getAssociatedShifts()) {
            Double availablePercentage = shift.getAvailableShiftPercentage(professorship);
            teachingServicePercentages.add(new TeachingServicePercentage(shift, availablePercentage));
            for (DegreeTeachingService degreeTeachingService : shift.getDegreeTeachingServices()) {
                if (professorship == degreeTeachingService.getProfessorship()) {
                    teacherPercentageMap.put(shift.getIdInternal().toString(), round(degreeTeachingService.getPercentage()));
                    break;
                }
            }
        }

        Iterator<TeachingServicePercentage> teachingServicePercentagesIterator =
                new OrderedIterator<TeachingServicePercentage>(teachingServicePercentages.iterator(),
                        TEACHING_SERVICE_PERCENTAGE_COMPARATOR_BY_SHIFT);

        request.setAttribute("professorship", professorship);
        request.setAttribute("teachingServicePercentages", teachingServicePercentagesIterator);
        dynaForm.set("teacherPercentageMap", teacherPercentageMap);
    }

    protected ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            RoleType roleType) throws NumberFormatException,  FenixServiceException {

        DynaActionForm teachingServiceForm = (DynaActionForm) form;
        IUserView userView = UserView.getUser();
        HashMap<String, String> teacherPercentageMap = (HashMap<String, String>) teachingServiceForm.get("teacherPercentageMap");

        Integer professorshipID = (Integer) teachingServiceForm.get("professorshipID");

        List<ShiftIDTeachingPercentage> shiftIDPercentages = new ArrayList<ShiftIDTeachingPercentage>();
        Iterator<Map.Entry<String, String>> entryInterator = teacherPercentageMap.entrySet().iterator();
        while (entryInterator.hasNext()) {
            Map.Entry<String, String> entry = entryInterator.next();
            String percentage = entry.getValue();
            if ((percentage != null) && (percentage.length() != 0)) {
                percentage = percentage.replace(',', '.');
                Integer shiftID = Integer.valueOf(entry.getKey());
                ShiftIDTeachingPercentage shiftIDPercentage = new ShiftIDTeachingPercentage(shiftID, Double.valueOf(percentage));
                shiftIDPercentages.add(shiftIDPercentage);
            }
        }

        try {
            UpdateDegreeTeachingServices.runUpdateDegreeTeachingServices(professorshipID, shiftIDPercentages, roleType);
        } catch (DomainException domainException) {
            ActionMessages actionMessages = new ActionMessages();
            actionMessages.add("error", new ActionMessage(domainException.getMessage(), domainException.getArgs()));
            saveMessages(request, actionMessages);
            Professorship professorship = rootDomainObject.readProfessorshipByOID(professorshipID);
            teachingServiceDetailsProcess(professorship, request, teachingServiceForm);
            return mapping.findForward("show-teaching-service-percentages");
        }

        return mapping.findForward("sucessfull-edit");
    }

    public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws NumberFormatException,  FenixServiceException {

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
