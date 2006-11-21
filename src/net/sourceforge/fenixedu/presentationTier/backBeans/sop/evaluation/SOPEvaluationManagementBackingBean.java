package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.faces.component.html.HtmlInputHidden;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.dataTransferObject.comparators.ComparatorByNameForInfoExecutionDegree;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseScope;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeModuleScope;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Evaluation;
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.domain.CurricularCourseScope.DegreeModuleScopeCurricularCourseScope;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.Context.DegreeModuleScopeContext;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.space.OldRoom;
import net.sourceforge.fenixedu.domain.space.RoomOccupation;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation.EvaluationManagementBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.DateFormatUtil;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.Season;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.util.MessageResources;

public class SOPEvaluationManagementBackingBean extends EvaluationManagementBackingBean {

    private static final MessageResources messages = MessageResources
            .getMessageResources("resources/ApplicationResourcesSOP");
    private static final MessageResources enumerations = MessageResources
            .getMessageResources("resources/EnumerationResources");
    private static final DateFormat hourFormat = new SimpleDateFormat("HH:mm");

    protected Integer executionPeriodID;
    protected HtmlInputHidden executionPeriodIdHidden;
    protected boolean disableDropDown;
    protected Integer executionDegreeID;
    protected HtmlInputHidden executionDegreeIdHidden;
    protected Integer curricularYearID;
    protected HtmlInputHidden curricularYearIdHidden;
    protected String curricularYearIDsParameterString;
    protected Integer calendarPeriod;
    protected HtmlInputHidden calendarPeriodHidden;
    private Integer[] curricularYearIDs;
    private String chooseMessage = messages.getMessage("label.choose.message");
    private HtmlInputHidden dayHidden;
    private HtmlInputHidden monthHidden;
    private HtmlInputHidden yearHidden;
    private HtmlInputHidden beginHourHidden;
    private HtmlInputHidden beginMinuteHidden;
    private HtmlInputHidden endHourHidden;
    private HtmlInputHidden endMinuteHidden;
    private Integer orderCriteria;
    private String labelVacancies = messages.getMessage("label.vacancies");
    private List<Integer> associatedExecutionCourses;

    private Map<Integer, String> associatedExecutionCoursesNames = new HashMap<Integer, String>();
    private Map<Integer, List<SelectItem>> curricularCourseScopesSelectItems = new HashMap<Integer, List<SelectItem>>();
    private Map<Integer, List<SelectItem>> curricularCourseContextSelectItems = new HashMap<Integer, List<SelectItem>>();
    private Map<Integer, List<WrittenEvaluation>> writtenEvaluations = new HashMap<Integer, List<WrittenEvaluation>>();;
    private Map<Integer, Integer> writtenEvaluationsMissingPlaces = new HashMap<Integer, Integer>();
    private Map<Integer, String> writtenEvaluationsRooms = new HashMap<Integer, String>();
    private Map<Integer, Integer> executionCoursesEnroledStudents = new HashMap<Integer, Integer>();

    private String comment;
    private Integer executionPeriodOID;

    // BEGIN executionPeriod
    public Integer getExecutionPeriodID() {
        if (this.executionPeriodID == null && this.executionPeriodIdHidden.getValue() != null
                && !this.executionPeriodIdHidden.getValue().equals("")) {
            this.executionPeriodID = Integer.valueOf(this.executionPeriodIdHidden.getValue().toString());
        } else if (this.getRequestAttribute("executionPeriodID") != null
                && !this.getRequestAttribute("executionPeriodID").equals("")) {
            this.executionPeriodID = Integer.valueOf(this.getRequestAttribute("executionPeriodID")
                    .toString());
        } else if (this.getRequestParameter("executionPeriodID") != null
                && !this.getRequestParameter("executionPeriodID").equals("")) {
            this.executionPeriodID = Integer.valueOf(this.getRequestParameter("executionPeriodID"));
        } else if (this.executionPeriodID == null) {
            this.executionPeriodID = getExecutionPeriodOID();
        }
        return executionPeriodID;
    }

    public void setExecutionPeriodID(Integer executionPeriodID) {
        if (executionPeriodID != null) {
            this.executionPeriodIdHidden.setValue(executionPeriodID);
        }
        this.executionPeriodID = executionPeriodID;
    }

    public HtmlInputHidden getExecutionPeriodIdHidden() {
        if (this.executionPeriodIdHidden == null) {
            this.executionPeriodIdHidden = new HtmlInputHidden();
            this.executionPeriodIdHidden.setValue(this.getExecutionPeriodID());
        }
        return executionPeriodIdHidden;
    }

    public void setExecutionPeriodIdHidden(HtmlInputHidden executionPeriodIdHidden) {
        if (executionPeriodIdHidden.getValue() != null) {
            this.executionPeriodID = Integer.valueOf(executionPeriodIdHidden.getValue().toString());
        }
        this.executionPeriodIdHidden = executionPeriodIdHidden;
    }

    public ExecutionPeriod getExecutionPeriod() {
        return rootDomainObject.readExecutionPeriodByOID(this.getExecutionPeriodID());
    }

    public String getExecutionPeriodLabel() {
        ExecutionPeriod executionPeriodSelected = getExecutionPeriod();

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(executionPeriodSelected.getName());
        stringBuffer.append(" - ");
        stringBuffer.append(executionPeriodSelected.getExecutionYear().getYear());

        return stringBuffer.toString();
    }

    // END executionPeriod

    // BEGIN disableDropDown
    public boolean getDisableDropDown() {
        if (this.getExecutionPeriodID() == null || this.getExecutionPeriodID() == 0) {
            this.disableDropDown = Boolean.TRUE;
        } else {
            this.disableDropDown = Boolean.FALSE;
        }
        return disableDropDown;
    }

    public void setDisableDropDown(boolean disableDropDown) {
        this.disableDropDown = disableDropDown;
    }

    // END disableDropDown

    // BEGIN executionDegree
    public Integer getExecutionDegreeID() {
        if (this.executionDegreeID == null && this.executionDegreeIdHidden.getValue() != null
                && !this.executionDegreeIdHidden.getValue().equals("")) {
            this.executionDegreeID = Integer.valueOf(this.executionDegreeIdHidden.getValue().toString());
        } else if (this.getRequestAttribute("executionDegreeID") != null
                && !this.getRequestAttribute("executionDegreeID").equals("")) {
            this.executionDegreeID = Integer.valueOf(this.getRequestAttribute("executionDegreeID")
                    .toString());
        } else if (this.getRequestParameter("executionDegreeID") != null
                && !this.getRequestParameter("executionDegreeID").equals("")) {
            this.executionDegreeID = Integer.valueOf(this.getRequestParameter("executionDegreeID"));
        }
        return executionDegreeID;
    }

    public void setExecutionDegreeID(Integer executionDegreeID) {
        if (executionDegreeID != null) {
            this.executionDegreeIdHidden = new HtmlInputHidden();
            this.executionDegreeIdHidden.setValue(executionDegreeID);
        }
        this.executionDegreeID = executionDegreeID;
    }

    public HtmlInputHidden getExecutionDegreeIdHidden() {
        if (this.executionDegreeIdHidden == null) {
            this.executionDegreeIdHidden = new HtmlInputHidden();
            this.executionDegreeIdHidden.setValue(this.getExecutionDegreeID());
        }
        return executionDegreeIdHidden;
    }

    public void setExecutionDegreeIdHidden(HtmlInputHidden executionDegreeIdHidden) {
        if (executionDegreeIdHidden != null && executionDegreeIdHidden.getValue() != null
                && !executionDegreeIdHidden.getValue().equals("")) {
            this.executionDegreeID = Integer.valueOf(executionDegreeIdHidden.getValue().toString());
        }
        this.executionDegreeIdHidden = executionDegreeIdHidden;
    }

    public ExecutionDegree getExecutionDegree() {        
       return rootDomainObject.readExecutionDegreeByOID(this.getExecutionDegreeID());       
    }

    public String getExecutionDegreeLabel() {
        ExecutionDegree executionDegreeSelected = getExecutionDegree();

        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append(enumerations.getMessage(executionDegreeSelected.getDegreeCurricularPlan()
                .getDegree().getTipoCurso().toString()));
        stringBuffer.append(" em ");
        stringBuffer.append(executionDegreeSelected.getDegreeCurricularPlan().getDegree().getNome());

        return stringBuffer.toString();
    }

    public Integer getCurricularYearID() {
        if (this.curricularYearID == null && this.curricularYearIdHidden.getValue() != null
                && !this.curricularYearIdHidden.getValue().equals("")) {
            this.curricularYearID = Integer.valueOf(this.curricularYearIdHidden.getValue().toString());
        } else if (this.getRequestAttribute("curricularYearID") != null
                && !this.getRequestAttribute("curricularYearID").equals("")) {
            this.curricularYearID = Integer.valueOf(this.getRequestAttribute("curricularYearID")
                    .toString());
        } else if (this.getRequestParameter("curricularYearID") != null
                && !this.getRequestParameter("curricularYearID").equals("")) {
            this.curricularYearID = Integer.valueOf(this.getRequestParameter("curricularYearID"));
        }
        return curricularYearID;
    }

    public Integer getCalendarPeriod() {
        if (this.calendarPeriod == null && this.getCalendarPeriodHidden().getValue() != null
                && !this.calendarPeriodHidden.getValue().equals("")) {
            this.calendarPeriod = Integer.valueOf(this.getCalendarPeriodHidden().getValue().toString());
        } else if (this.getRequestAttribute("calendarPeriod") != null
                && !this.getRequestAttribute("calendarPeriod").equals("")) {
            this.calendarPeriod = Integer.valueOf(this.getRequestAttribute("calendarPeriod")
                    .toString());
        } else if (this.getRequestParameter("calendarPeriod") != null
                && !this.getRequestParameter("calendarPeriod").equals("")) {
            this.calendarPeriod = Integer.valueOf(this.getRequestParameter("calendarPeriod"));
        }
        return calendarPeriod;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        if (curricularYearID != null) {
            this.curricularYearIdHidden = new HtmlInputHidden();
            this.curricularYearIdHidden.setValue(curricularYearID);
        }
        this.curricularYearID = curricularYearID;
    }

    public void setCalendarPeriod(Integer calendarPeriod) {
        if (calendarPeriod != null) {
            this.calendarPeriodHidden = new HtmlInputHidden();
            this.calendarPeriodHidden.setValue(calendarPeriod);
        }
        this.calendarPeriod = calendarPeriod;
    }

    public HtmlInputHidden getCurricularYearIdHidden() {
        if (this.curricularYearIdHidden == null) {
            this.curricularYearIdHidden = new HtmlInputHidden();
            this.curricularYearIdHidden.setValue(this.getCurricularYearID());
        }
        return curricularYearIdHidden;
    }

    public HtmlInputHidden getCalendarPeriodHidden() {
        if (this.calendarPeriodHidden == null) {
            this.calendarPeriodHidden = new HtmlInputHidden();
            this.calendarPeriodHidden.setValue(this.getCalendarPeriod());
        }
        return calendarPeriodHidden;
    }

    public void setCurricularYearIdHidden(HtmlInputHidden curricularYearIdHidden) {
        if (curricularYearIdHidden != null && curricularYearIdHidden.getValue() != null
                && !curricularYearIdHidden.getValue().equals("")) {
            this.curricularYearID = Integer.valueOf(curricularYearIdHidden.getValue().toString());
        }
        this.curricularYearIdHidden = curricularYearIdHidden;
    }

    public void setCalendarPeriodHidden(HtmlInputHidden calendarPeriodHidden) {
        if (calendarPeriodHidden != null && calendarPeriodHidden.getValue() != null
                && !calendarPeriodHidden.getValue().equals("")) {
            this.calendarPeriod = Integer.valueOf(calendarPeriodHidden.getValue().toString());
        }
        this.calendarPeriodHidden = calendarPeriodHidden;
    }

    public String getCurricularYear() {
        return this.getCurricularYearItems().get(getCurricularYearID()).getLabel();
    }

    // END curricularYear

    // BEGIN day, month, year, hour, minute
    public HtmlInputHidden getDayHidden() throws FenixFilterException, FenixServiceException {
        if (this.dayHidden == null) {
            this.dayHidden = new HtmlInputHidden();
            this.dayHidden.setValue(this.getDay());
        }
        return dayHidden;
    }

    public void setDayHidden(HtmlInputHidden dayHidden) {
        if (dayHidden.getValue() != null) {
            final String dayValue = dayHidden.getValue().toString();
            if (dayValue.length() > 0) {
                this.day = Integer.valueOf(dayValue);
            }
        }
        this.dayHidden = dayHidden;
    }

    public HtmlInputHidden getMonthHidden() throws FenixFilterException, FenixServiceException {
        if (this.monthHidden == null) {
            this.monthHidden = new HtmlInputHidden();
            this.monthHidden.setValue(this.getMonth());
        }
        return monthHidden;
    }

    public void setMonthHidden(HtmlInputHidden monthHidden) {
        if (monthHidden.getValue() != null) {
            final String monthValue = monthHidden.getValue().toString();
            if (monthValue.length() > 0) {
                this.month = Integer.valueOf(monthValue);
            }
        }
        this.monthHidden = monthHidden;
    }

    public HtmlInputHidden getYearHidden() throws FenixFilterException, FenixServiceException {
        if (this.yearHidden == null) {
            this.yearHidden = new HtmlInputHidden();
            this.yearHidden.setValue(this.getYear());
        }
        return yearHidden;
    }

    public void setYearHidden(HtmlInputHidden yearHidden) {
        if (yearHidden.getValue() != null) {
            final String yearValue = yearHidden.getValue().toString();
            if (yearValue.length() > 0) {
                this.year = Integer.valueOf(yearValue);
            }
        }
        this.yearHidden = yearHidden;
    }

    public HtmlInputHidden getBeginHourHidden() throws FenixFilterException, FenixServiceException {
        if (this.beginHourHidden == null) {
            this.beginHourHidden = new HtmlInputHidden();
            this.beginHourHidden.setValue(this.getBeginHour());
        }
        return beginHourHidden;
    }

    public void setBeginHourHidden(HtmlInputHidden beginHourHidden) {
        if (beginHourHidden.getValue() != null) {
            this.beginHour = Integer.valueOf(beginHourHidden.getValue().toString());
        }
        this.beginHourHidden = beginHourHidden;
    }

    public HtmlInputHidden getBeginMinuteHidden() throws FenixFilterException, FenixServiceException {
        if (this.beginMinuteHidden == null) {
            this.beginMinuteHidden = new HtmlInputHidden();
            this.beginMinuteHidden.setValue(this.getBeginMinute());
        }
        return beginMinuteHidden;
    }

    public void setBeginMinuteHidden(HtmlInputHidden beginMinuteHidden) {
        if (beginMinuteHidden.getValue() != null) {
            this.beginMinute = Integer.valueOf(beginMinuteHidden.getValue().toString());
        }
        this.beginMinuteHidden = beginMinuteHidden;
    }

    public HtmlInputHidden getEndHourHidden() throws FenixFilterException, FenixServiceException {
        if (this.endHourHidden == null) {
            this.endHourHidden = new HtmlInputHidden();
            this.endHourHidden.setValue(this.getEndHour());
        }
        return endHourHidden;
    }

    public void setEndHourHidden(HtmlInputHidden endHourHidden) {
        if (endHourHidden.getValue() != null) {
            this.endHour = Integer.valueOf(endHourHidden.getValue().toString());
        }
        this.endHourHidden = endHourHidden;
    }

    public HtmlInputHidden getEndMinuteHidden() throws FenixFilterException, FenixServiceException {
        if (this.endMinuteHidden == null) {
            this.endMinuteHidden = new HtmlInputHidden();
            this.endMinuteHidden.setValue(this.getEndMinute());
        }
        return endMinuteHidden;
    }

    public void setEndMinuteHidden(HtmlInputHidden endMinuteHidden) {
        if (endMinuteHidden.getValue() != null) {
            this.endMinute = Integer.valueOf(endMinuteHidden.getValue().toString());
        }
        this.endMinuteHidden = endMinuteHidden;
    }

    // END day, month, year, hour, minute

    // BEGIN Drop down menu logic
    public List getExecutionPeriods() throws FenixFilterException, FenixServiceException {
        List<InfoExecutionPeriod> infoExecutionPeriods = (List<InfoExecutionPeriod>) ServiceUtils
                .executeService(getUserView(), "ReadNotClosedExecutionPeriods", null);

        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(new BeanComparator("infoExecutionYear.year"), true);
        chainComparator.addComparator(new BeanComparator("semester"), true);
        Collections.sort(infoExecutionPeriods, chainComparator);

        List<SelectItem> executionPeriodItems = new ArrayList<SelectItem>(infoExecutionPeriods.size());
        executionPeriodItems.add(new SelectItem(0, this.chooseMessage));
        for (InfoExecutionPeriod infoExecutionPeriod : infoExecutionPeriods) {
            String label = infoExecutionPeriod.getName() + " - "
                    + infoExecutionPeriod.getInfoExecutionYear().getYear();
            executionPeriodItems.add(new SelectItem(infoExecutionPeriod.getIdInternal(), label));
        }

        return executionPeriodItems;
    }

    public void enableDropDowns(ValueChangeEvent valueChangeEvent) {
        this.setExecutionPeriodID((Integer) valueChangeEvent.getNewValue());

        if ((Integer) valueChangeEvent.getNewValue() == 0) {
            this.setDisableDropDown(true);
        } else {
            this.setDisableDropDown(false);
        }

        this.setExecutionDegreeID(0);
        this.setCurricularYearID(0);

        return;
    }

    public List getExecutionDegrees() throws FenixFilterException, FenixServiceException {
        if (this.getDisableDropDown()) {
            return new ArrayList();
        }

        Object[] args = { this.getExecutionPeriod().getExecutionYear().getIdInternal() };
        List<InfoExecutionDegree> infoExecutionDegrees = (List<InfoExecutionDegree>) ServiceUtils
                .executeService(getUserView(), "ReadExecutionDegreesByExecutionYearId", args);
        Collections.sort(infoExecutionDegrees, new ComparatorByNameForInfoExecutionDegree());

        List<SelectItem> result = new ArrayList<SelectItem>(infoExecutionDegrees.size());
        result.add(new SelectItem(0, this.chooseMessage));
        for (InfoExecutionDegree infoExecutionDegree : (List<InfoExecutionDegree>) infoExecutionDegrees) {
            StringBuilder label = new StringBuilder();
            label.append(enumerations.getMessage(infoExecutionDegree.getInfoDegreeCurricularPlan()
                    .getInfoDegree().getTipoCurso().toString()));
            label.append(" em ");
            label.append(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree().getNome());
            label.append(addAnotherInfoDegreeToLabel(infoExecutionDegrees, infoExecutionDegree) ? "-"
                    + infoExecutionDegree.getInfoDegreeCurricularPlan().getName() : "");

            result.add(new SelectItem(infoExecutionDegree.getIdInternal(), label.toString()));
        }

        return result;
    }

    private boolean addAnotherInfoDegreeToLabel(List<InfoExecutionDegree> infoExecutionDegrees,
            InfoExecutionDegree infoExecutionDegree) {
        InfoDegree infoDegree = infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree();

        for (InfoExecutionDegree infoExecutionDegree2 : infoExecutionDegrees) {
            if (infoDegree.equals(infoExecutionDegree2.getInfoDegreeCurricularPlan().getInfoDegree())
                    && !(infoExecutionDegree.equals(infoExecutionDegree2)))
                return true;
        }

        return false;
    }

    public List<SelectItem> getCurricularYearItems() {
        if (this.getDisableDropDown()) {
            return new ArrayList<SelectItem>();
        }

        List<SelectItem> curricularYearItems = new ArrayList<SelectItem>(6);
        curricularYearItems.add(new SelectItem(1, "1º Ano"));
        curricularYearItems.add(new SelectItem(2, "2º Ano"));
        curricularYearItems.add(new SelectItem(3, "3º Ano"));
        curricularYearItems.add(new SelectItem(4, "4º Ano"));
        curricularYearItems.add(new SelectItem(5, "5º Ano"));

        return curricularYearItems;
    }

    public List<SelectItem> getCalendarPeriodItems() {
        if (this.getDisableDropDown()) {
            return new ArrayList<SelectItem>();
        }

        List<SelectItem> calendarPeriodItems = new ArrayList<SelectItem>(7);

        calendarPeriodItems.add(new SelectItem(0, messages.getMessage("label.calendarPeriodItem.all")));
        calendarPeriodItems.add(new SelectItem(1, messages.getMessage("label.calendarPeriodItem.lesson.period")));
        calendarPeriodItems.add(new SelectItem(2, messages.getMessage("label.calendarPeriodItem.exam.period")));

        return calendarPeriodItems;
    }

    public void setNewValueExecutionDegreeID(ValueChangeEvent valueChangeEvent) {
        this.setExecutionDegreeID((Integer) valueChangeEvent.getNewValue());
    }

    public void setNewValueCurricularYearID(ValueChangeEvent valueChangeEvent) {
        this.setCurricularYearID((Integer) valueChangeEvent.getNewValue());
    }

    public void setNewValueCurricularYearIDs(ValueChangeEvent valueChangeEvent) {
        this.setCurricularYearIDs((Integer[]) valueChangeEvent.getNewValue());
    }

    public Integer[] getCurricularYearIDParameters() {
    	final String curricularYearIDsParameterString = getRequestParameter("curricularYearIDsParameterString");
    	if (curricularYearIDsParameterString != null && curricularYearIDsParameterString.length() > 0 && !curricularYearIDsParameterString.equals("null")) {
    		final String[] curricularYearIDsParameterStringTokens = curricularYearIDsParameterString.split(",");
    		final Integer[] curricularYearIDParameters = new Integer[curricularYearIDsParameterStringTokens.length];
    		for (int i = 0; i < curricularYearIDParameters.length; i++) {
    			curricularYearIDParameters[i] = Integer.valueOf(curricularYearIDsParameterStringTokens[i]);
    		}
    		return curricularYearIDParameters;
    	}
    	return null;
    }

    public String getCurricularYearIDsParameterString() {
    	final Integer[] curricularYearIDs = getCurricularYearIDs();
    	if (curricularYearIDs != null && curricularYearIDs.length > 0) {
    		buildString(curricularYearIDs);
    	}
    	return curricularYearIDsParameterString;
    }

    private void buildString(final Integer[] curricularYearIDs) {
        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < curricularYearIDs.length; i++) {
        	if (i > 0) {
        		stringBuilder.append(',');
        	}    			
        	stringBuilder.append(curricularYearIDs[i].toString());
        }
        this.curricularYearIDsParameterString = stringBuilder.toString();
    }

    public void setCurricularYearIDsParameterString(String curricularYearIDsParameterString) {
    	this.curricularYearIDsParameterString = curricularYearIDsParameterString;
    }

    public Integer[] getCurricularYearIDs() {
        if (curricularYearIDs == null) {
            curricularYearIDs = (Integer[]) getViewState().getAttribute("curricularYearIDs");
        }
        if (curricularYearIDs == null) {
        	curricularYearIDs = getCurricularYearIDParameters();
        }
        return curricularYearIDs;
    }

    public void setCurricularYearIDs(Integer[] curricularYearIDs) {
        this.curricularYearIDs = curricularYearIDs;
        getViewState().setAttribute("curricularYearIDs", curricularYearIDs);
    }

    public void setNewValueCalendarPeriod(ValueChangeEvent valueChangeEvent) {
        this.setCalendarPeriod((Integer) valueChangeEvent.getNewValue());
    }

    public void setNewValueExecutionCourseID(ValueChangeEvent valueChangeEvent) {
        this.setExecutionCourseID((Integer) valueChangeEvent.getNewValue());
    }

    public boolean getRenderContextSelection() {
        if (this.getExecutionPeriodID() == null || this.getExecutionDegreeID() == null
                || this.getCurricularYearIDs() == null || this.getCurricularYearIDs().length <= 0
                || this.getExecutionPeriodID() == 0 || this.getExecutionDegreeID() == 0) {
            return false;
        }
        return true;
    }

    public Date getWrittenEvaluationsCalendarBegin() {
        Date beginDate = getExecutionPeriod().getBeginDate();
        final Integer calendarPeriod = getCalendarPeriod();
        if(calendarPeriod != null && calendarPeriod.intValue() != 0) {
            final ExecutionDegree executionDegree = getExecutionDegree();
            if (executionDegree != null) {            
                if (getExecutionPeriod().getSemester().intValue() == 1
                        && executionDegree.getPeriodLessonsFirstSemester().getStart() != null) {
                    if (calendarPeriod != null && calendarPeriod.intValue() == 2) {
                        beginDate = executionDegree.getPeriodExamsFirstSemester().getStart();
                    } else {
                        beginDate = executionDegree.getPeriodLessonsFirstSemester().getStart();
                    }
                } else if (getExecutionPeriod().getSemester().intValue() == 2
                        && executionDegree.getPeriodLessonsSecondSemester().getStart() != null) {
                    if (calendarPeriod != null && calendarPeriod.intValue() == 2) {
                        beginDate = executionDegree.getPeriodExamsSecondSemester().getStart();
                    } else {
                        beginDate = executionDegree.getPeriodLessonsSecondSemester().getStart();
                    }
                }
            }
        }
        return beginDate;
    }

    public Date getWrittenEvaluationsCalendarEnd() {
        Date endDate = getExecutionPeriod().getEndDate();
        final Integer calendarPeriod = getCalendarPeriod();
        if(calendarPeriod != null && calendarPeriod != 0) {
            final ExecutionDegree executionDegree = getExecutionDegree();
            if (executionDegree != null) {
                if (getExecutionPeriod().getSemester().intValue() == 1
                        && executionDegree.getPeriodExamsFirstSemester().getEnd() != null) {
                    if (calendarPeriod != null && calendarPeriod.intValue() == 1) {
                        endDate = executionDegree.getPeriodLessonsFirstSemester().getEnd();
                    } else {
                        endDate = executionDegree.getPeriodExamsFirstSemester().getEnd();
                    }
                } else if (getExecutionPeriod().getSemester().intValue() == 2
                        && executionDegree.getPeriodExamsSecondSemester().getEnd() != null) {
                    if (calendarPeriod != null && calendarPeriod.intValue() == 1) {
                        endDate = executionDegree.getPeriodLessonsSecondSemester().getEnd();
                    } else {
                        endDate = executionDegree.getPeriodExamsSecondSemester().getEnd();
                    }
                    
                }
            }
        }
        return endDate;
    }

    public List<CalendarLink> getWrittenTestsCalendarLink() throws FenixFilterException, FenixServiceException {
        List<CalendarLink> result = new ArrayList<CalendarLink>();

        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            for (final Evaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                if (evaluation instanceof WrittenEvaluation) {
                    final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;

                    final CalendarLink calendarLink = new CalendarLink();
                    calendarLink.setObjectOccurrence(writtenEvaluation.getDay());
                    calendarLink.setObjectLinkLabel(constructEvaluationCalendarPresentationString(
                            writtenEvaluation, executionCourse));
                    calendarLink.setLinkParameters(constructLinkParameters(executionCourse,
                            writtenEvaluation));

                    result.add(calendarLink);
                }
            }
        }

        return result;
    }

    private Map<String, String> constructLinkParameters(final ExecutionCourse executionCourse,
            final WrittenEvaluation writtenEvaluation) {
        final Map<String, String> linkParameters = new HashMap<String, String>();
        linkParameters.put("executionCourseID", executionCourse.getIdInternal().toString());
        linkParameters.put("evaluationID", writtenEvaluation.getIdInternal().toString());
        linkParameters.put("executionPeriodID", this.executionPeriodID.toString());
        linkParameters.put("executionDegreeID", this.executionDegreeID.toString());
        linkParameters.put("curricularYearIDsParameterString", getCurricularYearIDsParameterString());
        linkParameters.put("evaluationTypeClassname", writtenEvaluation.getClass().getName());
        if (this.getExecutionPeriodOID() != null) {
            linkParameters.put("executionPeriodOID", this.getExecutionPeriodOID().toString());
        } else {
            linkParameters.put("executionPeriodOID", executionCourse.getExecutionPeriod().getIdInternal().toString());
        }
        return linkParameters;
    }

    private String constructEvaluationCalendarPresentationString(
            final WrittenEvaluation writtenEvaluation, final ExecutionCourse executionCourse) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (writtenEvaluation instanceof WrittenTest) {
            stringBuilder.append(messages.getMessage("label.evaluation.shortname.test"));
        } else if (writtenEvaluation instanceof Exam) {
            stringBuilder.append(messages.getMessage("label.evaluation.shortname.exam"));
        }
        stringBuilder.append(" ");
        stringBuilder.append(executionCourse.getSigla());
        stringBuilder.append(" (");
        stringBuilder.append(hourFormat.format(writtenEvaluation.getBeginning().getTime()));
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    private List<ExecutionCourse> getExecutionCourses() throws FenixFilterException, FenixServiceException {
        
        final List<ExecutionCourse> executionCourses = new ArrayList<ExecutionCourse>();
        Integer[] curricularYears = getCurricularYearIDs();
        if(curricularYears != null) {
            for (final Integer curricularYearID : curricularYears) {
                final Object args[] = { this.getExecutionDegree().getDegreeCurricularPlan().getIdInternal(), 
                        this.getExecutionPeriodID(), curricularYearID };
                executionCourses.addAll((Collection<ExecutionCourse>) ServiceManagerServiceFactory
                        .executeService(getUserView(), 
                                "ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear", args));
            }
        }        
        Collections.sort(executionCourses, new BeanComparator("sigla"));
        return executionCourses;        
    }

    public List<ExecutionCourseWrittenEvaluationAgregationBean> getExecutionCourseWrittenEvaluationAgregationBeans()
    		throws FenixFilterException, FenixServiceException {
	final List<ExecutionCourseWrittenEvaluationAgregationBean> executionCourseWrittenEvaluationAgregationBean
		= new ArrayList<ExecutionCourseWrittenEvaluationAgregationBean>();
        final Integer[] curricularYears = getCurricularYearIDs();
        if(curricularYears != null) {
            final DegreeCurricularPlan degreeCurricularPlan = this.getExecutionDegree().getDegreeCurricularPlan();
            for (final Integer curricularYearID : curricularYears) {
                final Object args[] = { degreeCurricularPlan.getIdInternal(), 
                        this.getExecutionPeriodID(), curricularYearID };
                final Collection<ExecutionCourse> executionCourses = (Collection<ExecutionCourse>) ServiceManagerServiceFactory
                        .executeService(getUserView(), "ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear", args);
                for (final ExecutionCourse executionCourse : executionCourses) {
                    final Set<WrittenEvaluation> writtenEvaluations = new TreeSet<WrittenEvaluation>(WrittenEvaluation.COMPARATOR_BY_BEGIN_DATE);
                    for (final Evaluation evaluation : executionCourse.getAssociatedEvaluationsSet()) {
                	if (evaluation instanceof WrittenEvaluation) {
                	    final WrittenEvaluation writtenEvaluation = (WrittenEvaluation) evaluation;
                	    for (final CurricularCourseScope curricularCourseScope : writtenEvaluation.getAssociatedCurricularCourseScopeSet()) {
                		if (curricularCourseScope.getCurricularSemester().getCurricularYear().getIdInternal().equals(curricularYearID)
                			&& degreeCurricularPlan == curricularCourseScope.getCurricularCourse().getDegreeCurricularPlan()) {
                		    writtenEvaluations.add(writtenEvaluation);
                		}
                	    }
                	}
                    }
                    if (!writtenEvaluations.isEmpty()) {
                	executionCourseWrittenEvaluationAgregationBean.add(new ExecutionCourseWrittenEvaluationAgregationBean(
                		curricularYearID, executionCourse, writtenEvaluations));
                    }
                }
            }
        }
        Collections.sort(executionCourseWrittenEvaluationAgregationBean, ExecutionCourseWrittenEvaluationAgregationBean.COMPARATOR_BY_EXECUTION_COURSE_CODE_AND_CURRICULAR_YEAR);
	return executionCourseWrittenEvaluationAgregationBean;
    }

    public List<ExecutionCourse> getExecutionCoursesWithWrittenEvaluations() throws FenixFilterException, FenixServiceException {
        List<ExecutionCourse> executionCoursesWithWrittenEvaluations = new ArrayList<ExecutionCourse>();

        Collections.sort(getExecutionCourses(), new BeanComparator("sigla"));
        writtenEvaluations.clear();
        writtenEvaluationsMissingPlaces.clear();
        writtenEvaluationsRooms.clear();
        executionCoursesEnroledStudents.clear();
        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            final List associatedWrittenEvaluations = executionCourse.getAssociatedWrittenTests();
            associatedWrittenEvaluations.addAll(executionCourse.getAssociatedExams());
            if (!associatedWrittenEvaluations.isEmpty()) {
                Collections.sort(associatedWrittenEvaluations, new BeanComparator("dayDate"));
                processWrittenTestAdditionalValues(executionCourse, associatedWrittenEvaluations);
                writtenEvaluations.put(executionCourse.getIdInternal(), associatedWrittenEvaluations);
                executionCoursesWithWrittenEvaluations.add(executionCourse);
            }
        }
        return executionCoursesWithWrittenEvaluations;
    }

    private int calculateEnroledStudents(
            final List<WrittenEvaluation> associatedWrittenEvaluations,
            final ExecutionPeriod executionPeriod) {

        final Set<Integer> curricularCourseIDs = new HashSet<Integer>();
        int numberOfEnroledStudents = 0;
        for (final WrittenEvaluation evaluation : associatedWrittenEvaluations) {
            for (final CurricularCourseScope curricularCourseScope : evaluation.getAssociatedCurricularCourseScope()) {
                if (!curricularCourseIDs.contains(curricularCourseScope.getCurricularCourse().getIdInternal())) {
                    curricularCourseIDs.add(curricularCourseScope.getCurricularCourse().getIdInternal());
                    for (final CurriculumModule curriculumModule : curricularCourseScope.getCurricularCourse().getCurriculumModules()) {
                    	Enrolment enrolment = (Enrolment) curriculumModule;
                        if (enrolment.getExecutionPeriod() == executionPeriod) {
                            numberOfEnroledStudents++;
                        }
                    }
                }
            }
            for (final Context context : evaluation.getAssociatedContexts()) {
                if (!curricularCourseIDs.contains(context.getChildDegreeModule().getIdInternal())) {
                    curricularCourseIDs.add(context.getChildDegreeModule().getIdInternal());
                    for (final CurriculumModule curriculumModule : context.getChildDegreeModule().getCurriculumModules()) {
                        Enrolment enrolment = (Enrolment) curriculumModule;
                        if (enrolment.getExecutionPeriod() == executionPeriod) {
                            numberOfEnroledStudents++;
                        }
                    }
                }
            }
        }
        return numberOfEnroledStudents;
    }

    private void processWrittenTestAdditionalValues(final ExecutionCourse executionCourse,
            final List<WrittenEvaluation> associatedWrittenEvaluations) {
        
        for (final WrittenEvaluation writtenTest : associatedWrittenEvaluations) {
            int totalCapacity = 0;
            final StringBuilder buffer = new StringBuilder(20);
            for (final OldRoom room : writtenTest.getAssociatedRooms()) {
                buffer.append(room.getNome()).append("; ");
                totalCapacity += room.getCapacidadeExame();
            }
            if (buffer.length() > 0) {
                buffer.delete(buffer.length() - 2, buffer.length() - 1);
            }
            writtenEvaluationsRooms.put(writtenTest.getIdInternal(), buffer.toString());
            int numberOfEnroledStudents = writtenTest.getCountStudentsEnroledAttendingExecutionCourses();
//            int numberOfEnroledStudents = calculateEnroledStudents(associatedWrittenEvaluations, getExecutionPeriod());
            System.out.println("Execution course: " + executionCourse.getIdInternal() + " " + executionCourse.getNome() + " written evaluation: " + writtenTest.getIdInternal()  + " " + numberOfEnroledStudents);
//            executionCoursesEnroledStudents.put(executionCourse.getIdInternal(), numberOfEnroledStudents);
            executionCoursesEnroledStudents.put(writtenTest.getIdInternal(), numberOfEnroledStudents);
            writtenEvaluationsMissingPlaces.put(writtenTest.getIdInternal(), Integer.valueOf(numberOfEnroledStudents - totalCapacity));
        }
    }

    public List<ExecutionCourse> getExecutionCoursesWithoutWrittenEvaluations() throws FenixFilterException, FenixServiceException {
        List<ExecutionCourse> result = new ArrayList<ExecutionCourse>();
        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            if (executionCourse.getAssociatedWrittenTests().isEmpty()
                    && executionCourse.getAssociatedExams().isEmpty()) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public List<SelectItem> getExecutionCoursesLabels() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ExecutionCourse executionCourse : getExecutionCourses()) {
            result.add(new SelectItem(executionCourse.getIdInternal(), executionCourse.getNome()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(0, this.chooseMessage));
        return result;
    }

    // END Drop down menu logic

    // BEGIN Select Execution Course and Evaluation Type page logic
    public String continueToCreateWrittenEvaluation() throws FenixFilterException, FenixServiceException {
        if (this.getEvaluationTypeClassname() == null || this.getExecutionCourseID() == null
                || this.getEvaluationTypeClassname().equals("noSelection")
                || this.getExecutionCourseID() == 0) {
            this.setErrorMessage("label.choose.request");
            return "";
        } else {
            return "createWrittenEvaluation";
        }
    }

    public List<SelectItem> getEvaluationTypeClassnameLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("noSelection", this.chooseMessage));
        result.add(new SelectItem(WrittenTest.class.getName(), messages.getMessage("label.test")));
        result.add(new SelectItem(Exam.class.getName(), messages.getMessage("label.exam")));
        return result;
    }

    public List<SelectItem> getSeasonLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        result.add(new SelectItem("noSelection", this.chooseMessage));
        result.add(new SelectItem(Season.SEASON1_STRING, messages.getMessage("property.exam.1stExam")));
        result.add(new SelectItem(Season.SEASON2_STRING, messages.getMessage("property.exam.2stExam")));
        return result;
    }

    // END Select Execution Course and Evaluation Type page logic

    // BEGIN Associate OldRoom logic
    public Integer getOrderCriteria() {
        if (this.orderCriteria == null) {
            orderCriteria = new Integer(0);
        }
        return orderCriteria;
    }

    public void setOrderCriteria(Integer orderCriteria) {
        this.orderCriteria = orderCriteria;
    }

    public List<SelectItem> getOrderByCriteriaItems() {
        MessageResources messageResources = MessageResources
                .getMessageResources("resources/ApplicationResourcesSOP");

        List<SelectItem> orderByCriteriaItems = new ArrayList<SelectItem>(3);
        orderByCriteriaItems.add(new SelectItem(0, messageResources.getMessage("label.capacity")));
        orderByCriteriaItems
                .add(new SelectItem(1, messageResources.getMessage("property.room.building")));
        orderByCriteriaItems.add(new SelectItem(2, messageResources.getMessage("label.room.type")));

        return orderByCriteriaItems;
    }

    public Integer[] getChosenRoomsIDs() throws FenixFilterException, FenixServiceException {
        if (this.getViewState().getAttribute("chosenRoomsIDs") == null && this.getEvaluationID() != null) {
            List<Integer> associatedRooms = new ArrayList<Integer>();

            for (OldRoom room : ((WrittenEvaluation) this.getEvaluation()).getAssociatedRooms()) {
                associatedRooms.add(room.getIdInternal());
            }

            Integer[] selectedRooms = {};
            this.setChosenRoomsIDs(associatedRooms.toArray(selectedRooms));
        }

        return (Integer[]) this.getViewState().getAttribute("chosenRoomsIDs");
    }

    public void setChosenRoomsIDs(Integer[] chosenRoomsIDs) {
        this.getViewState().setAttribute("chosenRoomsIDs", chosenRoomsIDs);
    }

    public List<SelectItem> getRoomsSelectItems() throws FenixFilterException, FenixServiceException {
        Calendar examDate = Calendar.getInstance();
        examDate.set(Calendar.YEAR, getYear());
        examDate.set(Calendar.MONTH, getMonth() - 1);
        examDate.set(Calendar.DAY_OF_MONTH, getDay());
        examDate.set(Calendar.SECOND, 0);
        examDate.set(Calendar.MILLISECOND, 0);

        DiaSemana dayOfWeek = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));

        Calendar examStartTime = Calendar.getInstance();
        examStartTime.set(Calendar.HOUR_OF_DAY, getBeginHour());
        examStartTime.set(Calendar.MINUTE, getBeginMinute());
        examStartTime.set(Calendar.SECOND, 0);
        examStartTime.set(Calendar.MILLISECOND, 0);
        
        Calendar examEndTime = Calendar.getInstance();
        examEndTime.set(Calendar.HOUR_OF_DAY, getEndHour());
        examEndTime.set(Calendar.MINUTE, getEndMinute());
        examEndTime.set(Calendar.SECOND, 0);
        examEndTime.set(Calendar.MILLISECOND, 0);

        Object args[] = { examDate, examDate, examStartTime, examEndTime, dayOfWeek, null, null,
                Integer.valueOf(RoomOccupation.DIARIA), null, Boolean.FALSE };
        List<InfoRoom> availableInfoRoom = (List<InfoRoom>) ServiceUtils.executeService(this
                .getUserView(), "ReadAvailableRoomsForExam", args);

        if (this.getEvaluationID() != null) {
            for (OldRoom room : ((WrittenEvaluation) this.getEvaluation()).getAssociatedRooms()) {
                InfoRoom associatedRoom = InfoRoom.newInfoFromDomain(room);
                if (!availableInfoRoom.contains(associatedRoom)) {
                    availableInfoRoom.add(associatedRoom);
                }
            }
        }

        if (this.getOrderCriteria() == 0) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator(
                    "capacidadeExame")));
            comparatorChain.addComparator(new BeanComparator("nome"));
           
            Collections.sort(availableInfoRoom, comparatorChain);
        } else if (this.getOrderCriteria() == 1) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator(
                    "edificio")));
            comparatorChain.addComparator(new BeanComparator("nome"));
           
            Collections.sort(availableInfoRoom, comparatorChain);
        } else if (this.getOrderCriteria() == 2) {
            final ComparatorChain comparatorChain = new ComparatorChain();
            comparatorChain.addComparator(new ReverseComparator(new BeanComparator(
                    "tipo")));
            comparatorChain.addComparator(new BeanComparator("nome"));
           
            Collections.sort(availableInfoRoom, comparatorChain);
        }

        List<SelectItem> items = new ArrayList<SelectItem>(availableInfoRoom.size());
        for (InfoRoom infoRoom : (List<InfoRoom>) availableInfoRoom) {
            StringBuilder label = new StringBuilder();
            label.append(infoRoom.getNome());
            label.append("  ( ");
            label.append(infoRoom.getCapacidadeExame());
            label.append(" ");
            label.append(this.labelVacancies);
            label.append(", ");
            label.append(infoRoom.getEdificio());
            label.append(", ");
            label.append(getRoomType(infoRoom.getTipo()));
            label.append(" )");

            items.add(new SelectItem(infoRoom.getIdInternal(), label.toString()));
        }

        return items;
    }

    public String getAssociatedRooms() throws FenixFilterException, FenixServiceException {
        StringBuilder result = new StringBuilder();

        if (this.getChosenRoomsIDs() != null && this.getChosenRoomsIDs().length != 0) {
            for (Integer chosenRoomID : this.getChosenRoomsIDs()) {                
                OldRoom room = (OldRoom) rootDomainObject.readSpaceByOID(chosenRoomID);
                result.append(room.getNome());
                result.append("; ");
            }

            if (result.length() > 0) {
                result.delete(result.length() - 2, result.length() - 1);
            }

            return result.toString();
        } else {
            return messages.getMessage("label.no.associated.rooms");
        }
    }

    public String getRoomType(TipoSala roomType) {
        if (roomType.getTipo() == TipoSala.ANFITEATRO) {
            return enumerations.getMessage("ANFITEATRO");
        } else if (roomType.getTipo() == TipoSala.LABORATORIO) {
            return enumerations.getMessage("LABORATORIO");
        } else if (roomType.getTipo() == TipoSala.PLANA) {
            return enumerations.getMessage("PLANA");
        }
        return "";
    }

    public String associateRoomToWrittenEvaluation() {
        return returnToCreateOrEdit();
    }

    // END Associate OldRoom logic

    // BEGIN Create and Edit logic
    public String createWrittenEvaluation() throws FenixFilterException, FenixServiceException {
        
        if (this.getSeason() != null && this.getSeason().equals("noSelection")) {
            this.setErrorMessage("label.choose.request");
            return "";
        }

        List<String> executionCourseIDs = new ArrayList<String>(this.getAssociatedExecutionCourses().size());
        List<String> curricularCourseScopeIDs = new ArrayList<String>();
        List<String> curricularCourseContextIDs = new ArrayList<String>();
        
        List<String> roomsIDs = new ArrayList<String>();
        
        if (!prepareArguments(executionCourseIDs, curricularCourseScopeIDs, curricularCourseContextIDs, roomsIDs)) {
            return "";
        }

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        final Object[] args = { null, this.getBegin().getTime(), this.getBegin().getTime(),
                this.getEnd().getTime(), executionCourseIDs, curricularCourseScopeIDs, 
                curricularCourseContextIDs, roomsIDs, season, this.getDescription() };
        try {
            ServiceUtils.executeService(getUserView(), "CreateWrittenEvaluation", args);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        return WrittenTest.class.getSimpleName();
    }

    public boolean prepareArguments(List<String> executionCourseIDs, List<String> curricularCourseScopeIDs, 
            List<String> curricularCourseContextIDs, List<String> roomsIDs) throws FenixFilterException,
            FenixServiceException {
        
        for (Integer executionCourseID : this.associatedExecutionCourses) {

            executionCourseIDs.add(executionCourseID.toString());

            if (this.getCurricularCourseScopesToAssociate().get(executionCourseID).length == 0 && 
                    this.getCurricularCourseContextToAssociate().get(executionCourseID).length == 0) {
                this.setErrorMessage("error.invalidCurricularCourseScope");
                return false;
            }

            for (Integer curricularCourseScopesId : this.getCurricularCourseScopesToAssociate().get(executionCourseID)) {
                curricularCourseScopeIDs.add(curricularCourseScopesId.toString());
            }            
            for (Integer curricularCourseContextId : this.getCurricularCourseContextToAssociate().get(executionCourseID)) {
                curricularCourseContextIDs.add(curricularCourseContextId.toString());
            }
        }

        if (this.getChosenRoomsIDs() != null) {
            for (Integer roomID : this.getChosenRoomsIDs()) {
                roomsIDs.add(roomID.toString());
            }
        }
        return true;
    }

    public String editWrittenEvaluation() throws FenixFilterException, FenixServiceException, IOException {
        if (this.getSeason() != null && this.getSeason().equals("noSelection")) {
            this.setErrorMessage("label.choose.request");
            return "";
        }

        List<String> executionCourseIDs = new ArrayList<String>(this.getAssociatedExecutionCourses()
                .size());
        List<String> curricularCourseScopeIDs = new ArrayList<String>();
        List<String> curricularCourseContextIDs = new ArrayList<String>();        
        List<String> roomsIDs = new ArrayList<String>();

        if (!prepareArguments(executionCourseIDs, curricularCourseScopeIDs, curricularCourseContextIDs, roomsIDs)) {
            return "";
        }

        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        final Object[] args = { null, this.getBegin().getTime(), this.getBegin().getTime(),
                this.getEnd().getTime(), executionCourseIDs, curricularCourseScopeIDs, 
                curricularCourseContextIDs, roomsIDs, this.evaluationID, season, this.getDescription() };
        try {
            ServiceUtils.executeService(getUserView(), "EditWrittenEvaluation", args);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof NotAuthorizedFilterException) {
                errorMessage = "message.error.notAuthorized";
            }
            if (e instanceof DomainException) {
                final DomainException domainException = (DomainException) e;
                setErrorMessageArguments(domainException.getArgs());
            }
            this.setErrorMessage(errorMessage);
            return "";
        }

        final String originPage = getOriginPage();
        if (originPage != null && originPage.length() > 0) {
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getApplicationContext());
            stringBuilder.append("/sop/searchWrittenEvaluationsByDate.do?method=returnToSearchPage&amp;page=0&date=");
            stringBuilder.append(DateFormatUtil.format("yyyy/MM/dd", this.getBegin().getTime()));
            if (getSelectedBegin() != null && getSelectedBegin().length() > 0 && getSelectedBegin().equals("true")) {
                stringBuilder.append("&selectedBegin=");
                stringBuilder.append(DateFormatUtil.format("HH:mm", this.getBegin().getTime()));
            }
            if (getSelectedEnd() != null && getSelectedEnd().length() > 0 && getSelectedEnd().equals("true")) {
                stringBuilder.append("&selectedEnd=");
                stringBuilder.append(DateFormatUtil.format("HH:mm", this.getEnd().getTime()));
            }
            stringBuilder.append("&executionPeriodOID=").append(getExecutionPeriod().getIdInternal());
            FacesContext.getCurrentInstance().getExternalContext().redirect(stringBuilder.toString());
            return originPage;
        } else {
            return WrittenTest.class.getSimpleName();
            //return this.getEvaluation().getClass().getSimpleName();
        }
    }

    // END Create and Edit logic

    // BEGIN Associate Execution Course and Scopes logic
    public void setAssociatedExecutionCourses(List<Integer> associatedExecutionCourses) {
        this.associatedExecutionCourses = associatedExecutionCourses;
        this.getViewState().setAttribute("associatedExecutionCourses", associatedExecutionCourses);
    }

    public List<Integer> getAssociatedExecutionCourses() throws FenixFilterException,
            FenixServiceException {
        
        if (this.getViewState().getAttribute("associatedExecutionCourses") != null) {
            this.associatedExecutionCourses = (List<Integer>) this.getViewState().getAttribute("associatedExecutionCourses");
        } else if (this.getEvaluationID() != null) {
            List<Integer> result = new ArrayList<Integer>();
            for (ExecutionCourse executionCourse : this.getEvaluation().getAssociatedExecutionCourses()) {
                result.add(executionCourse.getIdInternal());
                List<Integer> selectedScopes = new ArrayList<Integer>();
                List<Integer> selectedContexts = new ArrayList<Integer>();
                for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                    for (DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {                   
                        if(degreeModuleScope.isActiveForExecutionPeriod(executionCourse.getExecutionPeriod())) {
                            if (((WrittenEvaluation) this.getEvaluation()).getDegreeModuleScopes().contains(degreeModuleScope)) {
                                if(degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {
                                    selectedScopes.add(degreeModuleScope.getIdInternal());
                                } else if(degreeModuleScope instanceof DegreeModuleScopeContext) {
                                    selectedContexts.add(degreeModuleScope.getIdInternal());
                                }
                            }                        
                        }
                    }
                }

                Integer[] selected = {};
                this.getCurricularCourseScopesToAssociate().put(executionCourse.getIdInternal(), selectedScopes.toArray(selected));
                this.getCurricularCourseContextToAssociate().put(executionCourse.getIdInternal(), selectedContexts.toArray(selected));
            }
            this.setAssociatedExecutionCourses(result);
        } else {
            List<Integer> result = new ArrayList<Integer>();
            result.add(this.getExecutionCourse().getIdInternal());
            this.setAssociatedExecutionCourses(result);
        }

        fillInAuxiliarMaps();
        return this.associatedExecutionCourses;
    }

    private void fillInAuxiliarMaps() throws FenixFilterException, FenixServiceException {
        
        for (Integer executionCourseID : this.associatedExecutionCourses) {            
        
            ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(executionCourseID);
            this.associatedExecutionCoursesNames.put(executionCourseID, executionCourse.getNome());

            List<SelectItem> items = new ArrayList<SelectItem>();
            List<SelectItem> items2 = new ArrayList<SelectItem>();
            
            Integer[] curricularCourseScopesToAssociate = this.getCurricularCourseScopesToAssociate().get(executionCourse.getIdInternal());
            Integer[] curricularCourseContextsToAssociate = this.getCurricularCourseContextToAssociate().get(executionCourse.getIdInternal());
            
            List<Integer> auxiliarArray = new ArrayList<Integer>();
            List<Integer> auxiliarArray2 = new ArrayList<Integer>();            
            if (curricularCourseScopesToAssociate != null) {
                for (Integer curricularCourseScopeToAssociate : curricularCourseScopesToAssociate) {
                    auxiliarArray.add(curricularCourseScopeToAssociate);
                }
            }
            if (curricularCourseContextsToAssociate != null) {
                for (Integer curricularCourseContextToAssociate : curricularCourseContextsToAssociate) {
                    auxiliarArray2.add(curricularCourseContextToAssociate);
                }
            }
            
            for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                for (DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {      
                    if(degreeModuleScope.isActiveForExecutionPeriod(executionCourse.getExecutionPeriod())) {
                        StringBuilder label = new StringBuilder();
                        label.append(curricularCourse.getDegreeCurricularPlan().getName());
                        label.append(" ");
                        label.append(degreeModuleScope.getCurricularYear());
                        label.append(" º Ano");
                        if (!degreeModuleScope.getBranch().equals("")) {
                            label.append(" ");
                            label.append(degreeModuleScope.getBranch());
                        } 
                        if(degreeModuleScope instanceof DegreeModuleScopeCurricularCourseScope) {                            
                            items.add(new SelectItem(degreeModuleScope.getIdInternal(), label.toString()));
                        } else if(degreeModuleScope instanceof DegreeModuleScopeContext) {                        
                            items2.add(new SelectItem(degreeModuleScope.getIdInternal(), label.toString()));
                        }                                      
                    }
                }
            }

            this.curricularCourseScopesSelectItems.put(executionCourse.getIdInternal(), items);
            this.curricularCourseContextSelectItems.put(executionCourse.getIdInternal(), items2);
            
            Integer[] selected = {};
            this.getCurricularCourseScopesToAssociate().put(executionCourse.getIdInternal(), auxiliarArray.toArray(selected));
            this.getCurricularCourseContextToAssociate().put(executionCourse.getIdInternal(), auxiliarArray2.toArray(selected));
        }
    }
    
    public Map<Integer, List<SelectItem>> getCurricularCourseContextSelectItems() {
        return curricularCourseContextSelectItems;
    }

    public void setCurricularCourseContextSelectItems(Map<Integer, List<SelectItem>> curricularCourseContextSelectItems) {
        this.curricularCourseContextSelectItems = curricularCourseContextSelectItems;
    }

    public Map<Integer, List<SelectItem>> getCurricularCourseScopesSelectItems() {
        return curricularCourseScopesSelectItems;
    }

    public void setCurricularCourseScopesSelectItems(Map<Integer, List<SelectItem>> curricularCourseScopesSelectItems) {
        this.curricularCourseScopesSelectItems = curricularCourseScopesSelectItems;
    }

    public Map<Integer, Integer[]> getCurricularCourseScopesToAssociate() {
        if (this.getViewState().getAttribute("curricularCourseScopesToAssociate") == null) {
            this.getViewState().setAttribute("curricularCourseScopesToAssociate", new HashMap<Integer, Integer[]>());
        }
        return (Map<Integer, Integer[]>) this.getViewState().getAttribute("curricularCourseScopesToAssociate");
    }

    public void setCurricularCourseScopesToAssociate(Map<Integer, Integer[]> curricularCourseScopesToAssociate) {
        this.getViewState().setAttribute("curricularCourseScopesToAssociate", curricularCourseScopesToAssociate);
    }
    
    public Map<Integer, Integer[]> getCurricularCourseContextToAssociate() {
        if (this.getViewState().getAttribute("curricularCourseContextToAssociate") == null) {
            this.getViewState().setAttribute("curricularCourseContextToAssociate", new HashMap<Integer, Integer[]>());
        }
        return (Map<Integer, Integer[]>) this.getViewState().getAttribute("curricularCourseContextToAssociate");
    }

    public void setCurricularCourseContextToAssociate(Map<Integer, Integer[]> curricularCourseContextToAssociate) {
        this.getViewState().setAttribute("curricularCourseContextToAssociate", curricularCourseContextToAssociate);
    }    

    public String associateExecutionCourse() throws FenixFilterException, FenixServiceException {
        if (this.getSelectedExecutionDegreeID() == null || this.getSelectedCurricularYearID() == null
                || this.getSelectedExecutionCourseID() == null
                || this.getSelectedExecutionDegreeID() == 0 || this.getSelectedCurricularYearID() == 0
                || this.getSelectedExecutionCourseID() == 0) {
            this.setErrorMessage("label.choose.request");
            return "";
        } else {
            List<Integer> list = this.getAssociatedExecutionCourses();
            Integer integer = this.getSelectedExecutionCourseID();

            if (!list.contains(integer)) {
                list.add(integer);
            }
            
            ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(integer);
            List<Integer> auxiliarArray = new ArrayList<Integer>();
            for (CurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                for (DegreeModuleScope degreeModuleScope : curricularCourse.getDegreeModuleScopes()) {
                    auxiliarArray.add(degreeModuleScope.getIdInternal());
                }
            }

            Integer[] selected = {};
            this.getCurricularCourseScopesToAssociate().put(executionCourse.getIdInternal(),
                    auxiliarArray.toArray(selected));

            return returnToCreateOrEdit();
        }
    }

    public void disassociateExecutionCourse() throws FenixFilterException, FenixServiceException {
        List<Integer> associatedExecutionCourses = this.getAssociatedExecutionCourses();
        Integer executionCourseToDisassociate = Integer.valueOf(this.getRequestParameter("executionCourseToDisassociate"));

        associatedExecutionCourses.remove(executionCourseToDisassociate);
        this.setAssociatedExecutionCourses(associatedExecutionCourses);

        this.getCurricularCourseScopesToAssociate().remove(executionCourseToDisassociate);
    }

    public String returnToCreateOrEdit() {
        if (this.getEvaluationID() == null) {
            return "createWrittenTest";
        } else {
            return "editWrittenTest";
        }
    }

    public Map<Integer, String> getAssociatedExecutionCoursesNames() {
        return associatedExecutionCoursesNames;
    }

    public void setAssociatedExecutionCoursesNames(Map<Integer, String> associatedExecutionCoursesNames) {
        this.associatedExecutionCoursesNames = associatedExecutionCoursesNames;
    }

    public Map<Integer, List<WrittenEvaluation>> getWrittenEvaluations() {
        return writtenEvaluations;
    }

    public Map<Integer, Integer> getWrittenEvaluationsFreeSpace() {
        return writtenEvaluationsMissingPlaces;
    }

    public Map<Integer, String> getWrittenEvaluationsRooms() {
        return writtenEvaluationsRooms;
    }

    // END Associate Execution Course and Scopes logic

    // BEGIN Execution Course comment logic
    public String getComment() throws FenixFilterException, FenixServiceException {
        if (this.comment == null && this.getExecutionCourse() != null) {
            this.comment = getExecutionCourse().getComment();
        }
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String commentExecutionCourse() throws FenixFilterException, FenixServiceException {
        try {
            Object argsDefineComment[] = { this.getExecutionCourse().getSigla(),
                    this.getExecutionCourse().getExecutionPeriod().getIdInternal(), this.getComment() };
            ServiceUtils.executeService(this.getUserView(), "DefineExamComment", argsDefineComment);
        } catch (FenixFilterException e) {
            this.setErrorMessage(e.getMessage());
            return "";
        } catch (FenixServiceException e) {
            this.setErrorMessage(e.getMessage());
            return "";
        }
        return "writtenEvaluationCalendar";
    }

    // END Execution Course comment logic

    // BEGIN Code to avoid bug in associating execution courses to written evaluation....
    public Integer getSelectedExecutionDegreeID() {
        Integer selectedExecutionDegreeID = (Integer) this.getViewState().getAttribute(
                "selectedExecutionDegreeID");

        if (selectedExecutionDegreeID == null) {
            selectedExecutionDegreeID = Integer.valueOf((String) this.getExecutionDegreeIdHidden()
                    .getValue());
            setSelectedExecutionDegreeID(selectedExecutionDegreeID);
        }

        return selectedExecutionDegreeID;
    }

    public void setSelectedExecutionDegreeID(Integer selectedExecutionDegreeID) {
        this.getViewState().setAttribute("selectedExecutionDegreeID", selectedExecutionDegreeID);
    }

    public Integer getSelectedCurricularYearID() {
        Integer selectedCurricularYearID = (Integer) this.getViewState().getAttribute(
                "selectedCurricularYearID");

        if (selectedCurricularYearID == null) {
            Integer[] curricularYears = getCurricularYearIDs();
            if(curricularYears != null || curricularYears.length != 0) {
                selectedCurricularYearID = curricularYears[0];
                setSelectedCurricularYearID(selectedCurricularYearID);
            }            
        }

        return selectedCurricularYearID;
    }

    public void setSelectedCurricularYearID(Integer selectedCurricularYearID) {
        this.getViewState().setAttribute("selectedCurricularYearID", selectedCurricularYearID);
    }

    public Integer getSelectedExecutionCourseID() {
        Integer selectedExecutionCourseID = (Integer) this.getViewState().getAttribute(
                "selectedExecutionCourseID");

        if (selectedExecutionCourseID == null) {
            selectedExecutionCourseID = Integer.valueOf((String) this.getExecutionCourseIdHidden()
                    .getValue());
            setSelectedExecutionCourseID(selectedExecutionCourseID);
        }

        return selectedExecutionCourseID;
    }

    public void setSelectedExecutionCourseID(Integer selectedExecutionCourseID) {
        this.getViewState().setAttribute("selectedExecutionCourseID", selectedExecutionCourseID);
    }

    public void onExecutionDegreeChanged(ValueChangeEvent valueChangeEvent) {
        setSelectedExecutionDegreeID((Integer) valueChangeEvent.getNewValue());
    }

    public void onCurricularYearChanged(ValueChangeEvent valueChangeEvent) {
        setSelectedCurricularYearID((Integer) valueChangeEvent.getNewValue());
    }

    public void onExecutionCourseChanged(ValueChangeEvent valueChangeEvent) {
        setSelectedExecutionCourseID((Integer) valueChangeEvent.getNewValue());
    }

    private List<ExecutionCourse> readExecutionCourses() throws FenixFilterException, FenixServiceException {
        ExecutionDegree executionDegree = rootDomainObject.readExecutionDegreeByOID(this.getSelectedExecutionDegreeID());            
        final Object args[] = { executionDegree.getDegreeCurricularPlan().getIdInternal(),
                this.getExecutionPeriodID(), this.getSelectedCurricularYearID() };
        List<ExecutionCourse> executionCourses = new ArrayList(
                (List) ServiceManagerServiceFactory
                .executeService(getUserView(), "ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear", args));
        Collections.sort(executionCourses, new BeanComparator("sigla"));
        return executionCourses;            
    }

    public List<SelectItem> getExecutionCoursesItems() throws FenixFilterException, FenixServiceException {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final ExecutionCourse executionCourse : readExecutionCourses()) {
            result.add(new SelectItem(executionCourse.getIdInternal(), executionCourse.getNome()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(0, this.chooseMessage));
        return result;
    }

    // END Code to avoid bug in associating execution courses to written evaluation....

    public Integer getExecutionPeriodOID() {
        return (executionPeriodOID == null) ? executionPeriodOID = getAndHoldIntegerParameter("executionPeriodOID")
                : executionPeriodOID;
    }

    public Map<Integer, Integer> getExecutionCoursesEnroledStudents() {
        return executionCoursesEnroledStudents;
    }
}
