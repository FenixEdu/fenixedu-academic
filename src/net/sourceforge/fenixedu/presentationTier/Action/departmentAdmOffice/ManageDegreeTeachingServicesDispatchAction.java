/**
 * Nov 14, 2005
 */
package net.sourceforge.fenixedu.presentationTier.Action.departmentAdmOffice;

import java.util.ArrayList;
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
import net.sourceforge.fenixedu.dataTransferObject.teacher.professorship.ProfessorshipDTO;
import net.sourceforge.fenixedu.domain.IDepartment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IProfessorship;
import net.sourceforge.fenixedu.domain.IShift;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.teacher.IDegreeTeachingService;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class ManageDegreeTeachingServicesDispatchAction extends FenixDispatchAction {

    public ActionForward prepareTeacherSearch(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;
        dynaForm.set("method", "showProfessorships");
        return mapping.findForward("search-teacher-form");
    }

    public ActionForward showProfessorships(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;

        IUserView userView = SessionUtils.getUserView(request);

        final Integer executionPeriodID = (Integer) dynaForm.get("executionPeriodId");
        final IExecutionPeriod executionPeriod = (IExecutionPeriod) ServiceUtils.executeService(
                userView, "ReadDomainExecutionPeriodByOID", new Object[] { executionPeriodID });

        Integer teacherNumber = Integer.valueOf(dynaForm.getString("teacherNumber"));
        List<IDepartment> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
        ITeacher teacher = null;
        for (IDepartment department : manageableDepartments) {
            teacher = department.getTeacherByPeriod(teacherNumber, executionPeriod.getBeginDate(),
                    executionPeriod.getEndDate());
            if(teacher != null){
                break;
            }
        }
        if (teacher == null) {
            request.setAttribute("teacherNotFound", "teacherNotFound");
            return mapping.findForward("teacher-not-found");
        }

        request.setAttribute("teacher", teacher);
        List<IProfessorship> professorships = teacher
                .getDegreeProfessorshipsByExecutionPeriod(executionPeriod);

        List<ProfessorshipDTO> professorshipDTOs = (List<ProfessorshipDTO>) CollectionUtils.collect(
                professorships, new Transformer() {

                    public Object transform(Object arg0) {
                        IProfessorship professorship = (IProfessorship) arg0;
                        return new ProfessorshipDTO(professorship);
                    }
                });
        if (!professorshipDTOs.isEmpty()) {
            Iterator professorshipDTOsIterator = new OrderedIterator(professorshipDTOs.iterator(),
                    new BeanComparator("professorship.executionCourse.nome"));
            request.setAttribute("professorshipDTOs", professorshipDTOsIterator);
        }
        return mapping.findForward("list-professorships");
    }

    public ActionForward showTeachingServiceDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
            FenixFilterException, FenixServiceException {

        DynaActionForm dynaForm = (DynaActionForm) form;

        Integer professorshipID = (Integer) dynaForm.get("professorshipID");

        IProfessorship professorship = (IProfessorship) ServiceUtils.executeService(SessionUtils
                .getUserView(request), "ReadDomainProfessorshipByOID", new Object[] { professorshipID });

        List<TeachingServicePercentage> teachingServicePercentages = new ArrayList();
        HashMap teacherPercentageMap = new HashMap();
        for (Iterator iter = professorship.getExecutionCourse().getAssociatedShiftsIterator(); iter
                .hasNext();) {
            IShift shift = (IShift) iter.next();
            Double availablePercentage = shift.getAvailableShiftPercentageForTeacher(professorship
                    .getTeacher());
            teachingServicePercentages.add(new TeachingServicePercentage(shift, availablePercentage));
            for (Iterator iterator = shift.getDegreeTeachingServices().iterator(); iterator.hasNext();) {
                IDegreeTeachingService degreeTeachingService = (IDegreeTeachingService) iterator.next();
                if (professorship == degreeTeachingService.getProfessorship()) {
                    teacherPercentageMap.put(shift.getIdInternal().toString(), degreeTeachingService
                            .getPercentage());
                    break;
                }
            }
        }

        ComparatorChain comparatorChain = new ComparatorChain();
        comparatorChain.addComparator(new BeanComparator("shift.tipo"));
        comparatorChain.addComparator(new BeanComparator("shift.nome"));
        Iterator teachingServicePercentagesIterator = new OrderedIterator(teachingServicePercentages
                .iterator(), comparatorChain);

        request.setAttribute("professorship", professorship);
        request.setAttribute("teachingServicePercentages", teachingServicePercentagesIterator);
        dynaForm.set("teacherPercentageMap", teacherPercentageMap);
        return mapping.findForward("show-teaching-service-percentages");
    }

    public ActionForward updateTeachingServices(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws NumberFormatException,
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
            if ((percentage != null) && (!(percentage.length() == 0))) {
                percentage = percentage.replace(',', '.');
                Integer shiftID = Integer.valueOf((String) entry.getKey());
                ShiftIDTeachingPercentage shiftIDPercentage = new ShiftIDTeachingPercentage(shiftID,
                        Double.valueOf(percentage));
                shiftIDPercentages.add(shiftIDPercentage);
            }
        }

        Object[] args = { professorshipID, shiftIDPercentages };
        ServiceUtils.executeService(userView, "UpdateDegreeTeachingServices", args);

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

        IShift shift;

        Double availablePercentage;

        public TeachingServicePercentage(IShift shift, Double availablePercentage) {
            setShift(shift);
            setAvailablePercentage(availablePercentage);
        }

        public Double getAvailablePercentage() {
            return availablePercentage;
        }

        public void setAvailablePercentage(Double availablePercentage) {
            this.availablePercentage = availablePercentage;
        }

        public IShift getShift() {
            return shift;
        }

        public void setShift(IShift shift) {
            this.shift = shift;
        }
    }
}
