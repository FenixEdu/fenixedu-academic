package net.sourceforge.fenixedu.presentationTier.backBeans.sop.evaluation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlInputHidden;
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
import net.sourceforge.fenixedu.domain.Exam;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IEvaluation;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IRoom;
import net.sourceforge.fenixedu.domain.IWrittenEvaluation;
import net.sourceforge.fenixedu.domain.Room;
import net.sourceforge.fenixedu.domain.RoomOccupation;
import net.sourceforge.fenixedu.domain.WrittenTest;
import net.sourceforge.fenixedu.framework.factory.ServiceManagerServiceFactory;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.backBeans.teacher.evaluation.EvaluationManagementBackingBean;
import net.sourceforge.fenixedu.presentationTier.jsf.components.util.CalendarLink;
import net.sourceforge.fenixedu.util.DiaSemana;
import net.sourceforge.fenixedu.util.Season;
import net.sourceforge.fenixedu.util.TipoSala;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.struts.util.MessageResources;

public class SOPEvaluationManagementBackingBean extends EvaluationManagementBackingBean {

    private static final MessageResources messages = MessageResources.getMessageResources("ServidorApresentacao/ApplicationResourcesSOP");
    private static final MessageResources enumerations = MessageResources.getMessageResources("ServidorApresentacao/EnumerationResources");

    private static final DateFormat hourFormat = new SimpleDateFormat("HH:mm");

    protected Integer executionPeriodID;
    protected HtmlInputHidden executionPeriodIdHidden;

    protected boolean disableDropDown;
    
    protected Integer executionDegreeID;
    protected HtmlInputHidden executionDegreeIdHidden;

    protected Integer curricularYearID;
    protected HtmlInputHidden curricularYearIdHidden;

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
    private Map<Integer, String> associatedExecutionCoursesNames = new HashMap<Integer,String>();
    //private Map<Integer, Integer[]> curricularCourseScopesToAssociate = new HashMap<Integer,Integer[]>();
    private Map<Integer, List<SelectItem>> curricularCourseScopesSelectItems = new HashMap<Integer,List<SelectItem>>();

    private Map<Integer, List<IWrittenEvaluation>> writtenEvaluations = new HashMap<Integer,List<IWrittenEvaluation>>();;
    private Map<Integer, Integer> writtenEvaluationsFreeSpace = new HashMap<Integer, Integer>();
    private Map<Integer, String> writtenEvaluationsRooms = new HashMap<Integer, String>();
    
    private String comment;
    private Integer executionPeriodOID;
    
    // BEGIN executionPeriod
    public Integer getExecutionPeriodID() {
        if (this.executionPeriodID == null && this.executionPeriodIdHidden.getValue() != null && !this.executionPeriodIdHidden.getValue().equals("")) {
            this.executionPeriodID = Integer.valueOf(this.executionPeriodIdHidden.getValue().toString());
        } else if (this.getRequestAttribute("executionPeriodID") != null && !this.getRequestAttribute("executionPeriodID").equals("")) {
            this.executionPeriodID = Integer.valueOf(this.getRequestAttribute("executionPeriodID")
                    .toString());
        } else if (this.getRequestParameter("executionPeriodID") != null && !this.getRequestParameter("executionPeriodID").equals("")) {
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

    public IExecutionPeriod getExecutionPeriod() {
        final Object[] args = { ExecutionPeriod.class, this.getExecutionPeriodID() };
        try {
            return (IExecutionPeriod) ServiceUtils.executeService(null, "ReadDomainObject", args);
        } catch (Exception e) {
            return null;
        }
    }
    
    public String getExecutionPeriodLabel() {
        IExecutionPeriod executionPeriodSelected = getExecutionPeriod();
        
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(executionPeriodSelected.getName());
        stringBuffer.append(" - ");
        stringBuffer.append(executionPeriodSelected.getExecutionYear().getYear());
        
        return stringBuffer.toString();
    }
    // END executionPeriod
    
    
    // BEGIN disableDropDown
    public boolean getDisableDropDown() {
        if(this.getExecutionPeriodID() == null  || this.getExecutionPeriodID() == 0) {
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
        if (this.executionDegreeID == null && this.executionDegreeIdHidden.getValue() != null && !this.executionDegreeIdHidden.getValue().equals("")) {
            this.executionDegreeID = Integer.valueOf(this.executionDegreeIdHidden.getValue().toString());
        } else if (this.getRequestAttribute("executionDegreeID") != null && !this.getRequestAttribute("executionDegreeID").equals("")) {
            this.executionDegreeID = Integer.valueOf(this.getRequestAttribute("executionDegreeID")
                    .toString());
        } else if (this.getRequestParameter("executionDegreeID") != null && !this.getRequestParameter("executionDegreeID").equals("")) {
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
        if (executionDegreeIdHidden != null && executionDegreeIdHidden.getValue() != null && !executionDegreeIdHidden.getValue().equals("")) {
            this.executionDegreeID = Integer.valueOf(executionDegreeIdHidden.getValue().toString());
        }
        this.executionDegreeIdHidden = executionDegreeIdHidden;
    }
    
    public IExecutionDegree getExecutionDegree() {
        final Object[] args = { ExecutionDegree.class, this.getExecutionDegreeID() };
        try {
            return (IExecutionDegree) ServiceUtils.executeService(null, "ReadDomainObject", args);
        } catch (Exception e) {
            return null;
        } 
    }
    
    public String getExecutionDegreeLabel() {
        IExecutionDegree executionDegreeSelected = getExecutionDegree();
        
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(enumerations.getMessage(executionDegreeSelected.getDegreeCurricularPlan().getDegree().getTipoCurso().toString()));
        stringBuffer.append(" em ");
        stringBuffer.append(executionDegreeSelected.getDegreeCurricularPlan().getDegree().getNome());
        
        return stringBuffer.toString();
    }
    // END executionDegree

    
    // BEGIN curricularYear
    public Integer getCurricularYearID() {
        if (this.curricularYearID == null && this.curricularYearIdHidden.getValue() != null && !this.curricularYearIdHidden.getValue().equals("")) {
            this.curricularYearID = Integer.valueOf(this.curricularYearIdHidden.getValue().toString());
        } else if (this.getRequestAttribute("curricularYearID") != null && !this.getRequestAttribute("curricularYearID").equals("")) {
            this.curricularYearID = Integer.valueOf(this.getRequestAttribute("curricularYearID")
                    .toString());
        } else if (this.getRequestParameter("curricularYearID") != null && !this.getRequestParameter("curricularYearID").equals("")) {
            this.curricularYearID = Integer.valueOf(this.getRequestParameter("curricularYearID"));
        }
        return curricularYearID;
    }

    public void setCurricularYearID(Integer curricularYearID) {
        if (curricularYearID != null) {
            this.curricularYearIdHidden = new HtmlInputHidden();
            this.curricularYearIdHidden.setValue(curricularYearID);
        }
        this.curricularYearID = curricularYearID;
    }

    public HtmlInputHidden getCurricularYearIdHidden() {
        if (this.curricularYearIdHidden == null) {
            this.curricularYearIdHidden = new HtmlInputHidden();
            this.curricularYearIdHidden.setValue(this.getCurricularYearID());
        }
        return curricularYearIdHidden;
    }

    public void setCurricularYearIdHidden(HtmlInputHidden curricularYearIdHidden) {
        if (curricularYearIdHidden != null && curricularYearIdHidden.getValue() != null && !curricularYearIdHidden.getValue().equals("")) {
            this.curricularYearID = Integer.valueOf(curricularYearIdHidden.getValue().toString());
        }
        this.curricularYearIdHidden = curricularYearIdHidden;
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
            this.day = Integer.valueOf(dayHidden.getValue().toString());
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
            this.month = Integer.valueOf(monthHidden.getValue().toString());
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
            this.year = Integer.valueOf(yearHidden.getValue().toString());
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
            StringBuffer label = new StringBuffer();
            label.append(enumerations.getMessage(infoExecutionDegree.getInfoDegreeCurricularPlan().getInfoDegree()
                    .getTipoCurso().toString()));
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

        curricularYearItems.add(new SelectItem(0, this.chooseMessage));
        curricularYearItems.add(new SelectItem(1, "1º Ano"));
        curricularYearItems.add(new SelectItem(2, "2º Ano"));
        curricularYearItems.add(new SelectItem(3, "3º Ano"));
        curricularYearItems.add(new SelectItem(4, "4º Ano"));
        curricularYearItems.add(new SelectItem(5, "5º Ano"));

        return curricularYearItems;
    }

    public void setNewValueExecutionDegreeID(ValueChangeEvent valueChangeEvent) {
        this.setExecutionDegreeID((Integer) valueChangeEvent.getNewValue());
    }

    public void setNewValueCurricularYearID(ValueChangeEvent valueChangeEvent) {
        this.setCurricularYearID((Integer) valueChangeEvent.getNewValue());
    }

    public void setNewValueExecutionCourseID(ValueChangeEvent valueChangeEvent) {
        this.setExecutionCourseID((Integer) valueChangeEvent.getNewValue());
    }

    public boolean getRenderContextSelection() {
        if (this.getExecutionPeriodID() == null 
                || this.getExecutionDegreeID() == null
                || this.getCurricularYearID() == null || this.getExecutionPeriodID() == 0 
                || this.getExecutionDegreeID() == 0
                || this.getCurricularYearID() == 0) {
            return false;
        }
        return true;
    }
    // END Drop down menu logic

    
    // BEGIN Build of Calendar
    public Date getWrittenEvaluationsCalendarBegin() {
        Date beginDate = getExecutionPeriod().getBeginDate();
        final IExecutionDegree executionDegree = getExecutionDegree();
        if (executionDegree != null) {
            if (getExecutionPeriod().getSemester().intValue() == 1
                    && executionDegree.getPeriodLessonsFirstSemester().getStart() != null) {
                beginDate = executionDegree.getPeriodLessonsFirstSemester().getStart();
            } else if (getExecutionPeriod().getSemester().intValue() == 2
                    && executionDegree.getPeriodLessonsSecondSemester().getStart() != null) {
                beginDate = executionDegree.getPeriodLessonsSecondSemester().getStart();
            }
        }
        return beginDate;
    }

    public Date getWrittenEvaluationsCalendarEnd() {
        Date endDate = getExecutionPeriod().getEndDate();
        final IExecutionDegree executionDegree = getExecutionDegree();
        if (executionDegree != null) {
            if (getExecutionPeriod().getSemester().intValue() == 1
                    && executionDegree.getPeriodExamsFirstSemester().getEnd() != null) {
                endDate = executionDegree.getPeriodExamsFirstSemester().getEnd();
            } else if (getExecutionPeriod().getSemester().intValue() == 2
                    && executionDegree.getPeriodExamsSecondSemester().getEnd() != null) {
                endDate = executionDegree.getPeriodExamsSecondSemester().getEnd();
            }
        }
        return endDate;
    }
    
    public List<CalendarLink> getWrittenTestsCalendarLink() {
        List<CalendarLink> result = new ArrayList<CalendarLink>();

        for (final IExecutionCourse executionCourse : getExecutionCourses()) {
            for (final IEvaluation evaluation : executionCourse.getAssociatedEvaluations()) {
                if (evaluation instanceof IWrittenEvaluation) {
                    final IWrittenEvaluation writtenEvaluation = (IWrittenEvaluation) evaluation;

                    final CalendarLink calendarLink = new CalendarLink();
                    calendarLink.setObjectOccurrence(writtenEvaluation.getDay());
                    calendarLink.setObjectLinkLabel(constructEvaluationCalendarPresentationString(writtenEvaluation, executionCourse));
                    calendarLink.setLinkParameters(constructLinkParameters(executionCourse, writtenEvaluation));

                    result.add(calendarLink);
                }
            }
        }

        return result;
    }

    private Map<String, String> constructLinkParameters(final IExecutionCourse executionCourse, final IWrittenEvaluation writtenEvaluation) {
        final Map<String, String> linkParameters = new HashMap<String, String>();
        linkParameters.put("executionCourseID", executionCourse.getIdInternal().toString());
        linkParameters.put("evaluationID", writtenEvaluation.getIdInternal().toString());
        linkParameters.put("executionPeriodID", this.executionPeriodID.toString());
        linkParameters.put("executionDegreeID", this.executionDegreeID.toString());
        linkParameters.put("curricularYearID", this.getCurricularYearID().toString());
        linkParameters.put("evaluationTypeClassname", writtenEvaluation.getClass().getName());
        linkParameters.put("executionPeriodOID", this.getExecutionPeriodOID().toString());
        return linkParameters;
    }

    private String constructEvaluationCalendarPresentationString(final IWrittenEvaluation writtenEvaluation, final IExecutionCourse executionCourse) {
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
    // END Build of Calendar
    
    
    // BEGIN Build of Lists
    private List<IExecutionCourse> getExecutionCourses() {
        try {
            final Object args[] = {
                    this.getExecutionDegree().getDegreeCurricularPlan().getIdInternal(),
                    this.getExecutionPeriodID(), this.getCurricularYearID() };
            List<IExecutionCourse> executionCourses = new ArrayList((List) ServiceManagerServiceFactory.executeService(
                    getUserView(),
                    "ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear",
                    args));
            Collections.sort(executionCourses, new BeanComparator("sigla"));
            return executionCourses;
        } catch (Exception e) {
            
        }
        return new ArrayList<IExecutionCourse>();
    }

    public List<IExecutionCourse> getExecutionCoursesWithWrittenEvaluations() {
        List<IExecutionCourse> executionCoursesWithWrittenEvaluations = new ArrayList<IExecutionCourse>();

        Collections.sort(getExecutionCourses(), new BeanComparator("sigla"));
        writtenEvaluations.clear();
        writtenEvaluationsFreeSpace.clear();
        writtenEvaluationsRooms.clear();
        for (final IExecutionCourse executionCourse : getExecutionCourses()) {
            final List associatedWrittenEvaluations = executionCourse.getAssociatedWrittenTests();
            associatedWrittenEvaluations.addAll(executionCourse.getAssociatedExams());
            if (!associatedWrittenEvaluations.isEmpty()) {
                Collections.sort(associatedWrittenEvaluations, new BeanComparator("dayDate"));
                writtenEvaluations.put(executionCourse.getIdInternal(), associatedWrittenEvaluations);
                processWrittenTestAdditionalValues(associatedWrittenEvaluations);
                executionCoursesWithWrittenEvaluations.add(executionCourse);
            }
        }

        return executionCoursesWithWrittenEvaluations;
    }

    private void processWrittenTestAdditionalValues(final List<IWrittenEvaluation> associatedWrittenEvaluations) {
        for (final IWrittenEvaluation writtenTest : associatedWrittenEvaluations) {
            int totalCapacity = 0;
            final StringBuffer buffer = new StringBuffer(20);
            for (final IRoom room : writtenTest.getAssociatedRooms()) {
                buffer.append(room.getNome()).append("; ");
                totalCapacity += room.getCapacidadeExame();
            }
            if (buffer.length() > 0) {
                buffer.delete(buffer.length() - 2, buffer.length() - 1);
            }            
            writtenEvaluationsRooms.put(writtenTest.getIdInternal(), buffer.toString());
            writtenEvaluationsFreeSpace.put(writtenTest.getIdInternal(), Integer.valueOf(totalCapacity
                    - writtenTest.getWrittenEvaluationEnrolmentsCount()));
        }
    }
    
    public List<IExecutionCourse> getExecutionCoursesWithoutWrittenEvaluations() {
        List<IExecutionCourse> result = new ArrayList<IExecutionCourse>();
        for (final IExecutionCourse executionCourse : getExecutionCourses()) {
            if (executionCourse.getAssociatedWrittenTests().isEmpty() && executionCourse.getAssociatedExams().isEmpty()) {
                result.add(executionCourse);
            }
        }
        return result;
    }

    public List<SelectItem> getExecutionCoursesLabels() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final IExecutionCourse executionCourse : getExecutionCourses()) {
            result.add(new SelectItem(executionCourse.getIdInternal(), executionCourse.getNome()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(0, this.chooseMessage));
        return result;
    }
    // END Drop down menu logic
    
    
    // BEGIN Select Execution Course and Evaluation Type page logic
    public String continueToCreateWrittenEvaluation() throws FenixFilterException, FenixServiceException {
        if (this.getEvaluationTypeClassname() == null || 
                this.getExecutionCourseID() == null ||
                this.getEvaluationTypeClassname().equals("noSelection") || this.getExecutionCourseID() == 0) {
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
    
    
    // BEGIN Associate Room logic
    public Integer getOrderCriteria() {
        if(this.orderCriteria == null) {
            orderCriteria = new Integer(0);
        }
        return orderCriteria;
    }

    public void setOrderCriteria(Integer orderCriteria) {
        this.orderCriteria = orderCriteria;
    }

    public List<SelectItem> getOrderByCriteriaItems() {
        MessageResources messageResources = MessageResources
                .getMessageResources("ServidorApresentacao/ApplicationResourcesSOP");

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

            for (IRoom room : ((IWrittenEvaluation)this.getEvaluation()).getAssociatedRooms()) {
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

        DiaSemana dayOfWeek = new DiaSemana(examDate.get(Calendar.DAY_OF_WEEK));

        Calendar examStartTime = Calendar.getInstance();
        examStartTime.set(Calendar.HOUR_OF_DAY, getBeginHour());
        examStartTime.set(Calendar.MINUTE, getBeginMinute());

        Calendar examEndTime = Calendar.getInstance();
        examEndTime.set(Calendar.HOUR_OF_DAY, getEndHour());
        examEndTime.set(Calendar.MINUTE, getEndMinute());

        Object args[] = { examDate, examDate, examStartTime, examEndTime, dayOfWeek, null, null,
                Integer.valueOf(RoomOccupation.DIARIA), null, Boolean.FALSE };
        List<InfoRoom> availableInfoRoom = (List<InfoRoom>) ServiceUtils.executeService(this
                .getUserView(), "ReadAvailableRoomsForExam", args);

        if (this.getEvaluationID() != null) {
            for (IRoom room : ((IWrittenEvaluation)this.getEvaluation()).getAssociatedRooms()) {
                InfoRoom associatedRoom = InfoRoom.newInfoFromDomain(room);
                if (!availableInfoRoom.contains(associatedRoom)) {
                    availableInfoRoom.add(associatedRoom);
                }
            }
        }
        
        if (this.getOrderCriteria() == 0) {
            Collections.sort(availableInfoRoom, new BeanComparator("capacidadeExame"));
            Collections.reverse(availableInfoRoom);
        } else if (this.getOrderCriteria() == 1) {
            Collections.sort(availableInfoRoom, new BeanComparator("edificio"));
        } else if (this.getOrderCriteria() == 2) {
            Collections.sort(availableInfoRoom, new BeanComparator("tipo"));
        }
        
        List<SelectItem> items = new ArrayList<SelectItem>(availableInfoRoom.size());
        for (InfoRoom infoRoom : (List<InfoRoom>) availableInfoRoom) {
            StringBuffer label = new StringBuffer();
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
        StringBuffer result = new StringBuffer();
            
        if (this.getChosenRoomsIDs() != null && this.getChosenRoomsIDs().length != 0) {
            for (Integer chosenRoomID : this.getChosenRoomsIDs()) {
                final Object[] args = { Room.class, chosenRoomID };
                IRoom room = (IRoom) ServiceUtils.executeService(null, "ReadDomainObject", args);

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
    // END Associate Room logic
    
    
    // BEGIN Create and Edit logic
    public String createWrittenEvaluation() throws FenixFilterException, FenixServiceException {
        if (this.getSeason() != null && this.getSeason().equals("noSelection")) {
            this.setErrorMessage("label.choose.request");
            return "";
        }
            
        List<String> executionCourseIDs = new ArrayList<String>(this.getAssociatedExecutionCourses().size());
        List<String> curricularCourseScopeIDs = new ArrayList<String>();
        List<String> roomsIDs = null;

        if (!prepareArguments(executionCourseIDs, curricularCourseScopeIDs, roomsIDs)) {
            return "";
        }
        
        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        final Object[] args = { null, this.getBegin(), this.getBegin(), this.getEnd(), executionCourseIDs,
                curricularCourseScopeIDs, roomsIDs, season, this.getDescription() };
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

    public boolean prepareArguments(List<String> executionCourseIDs, List<String> curricularCourseScopeIDs, List<String> roomsIDs) throws FenixFilterException, FenixServiceException {
        for (Integer executionCourseID : this.associatedExecutionCourses) {
            executionCourseIDs.add(executionCourseID.toString());

            if (this.getCurricularCourseScopesToAssociate().get(executionCourseID).length == 0) {
                this.setErrorMessage("error.invalidCurricularCourseScope");
                return false;
            }

            for (Integer roomID : this.getCurricularCourseScopesToAssociate().get(executionCourseID)) {
                curricularCourseScopeIDs.add(roomID.toString());
            }
        }
        
        if (this.getChosenRoomsIDs() != null) {
            roomsIDs = new ArrayList<String>(this.getChosenRoomsIDs().length);
            for (Integer roomID : this.getChosenRoomsIDs()) {
                roomsIDs.add(roomID.toString());
            }
        }
        
        return true;
    }
    
    public String editWrittenEvaluation() throws FenixFilterException, FenixServiceException {
        if (this.getSeason() != null && this.getSeason().equals("noSelection")) {
            this.setErrorMessage("label.choose.request");
            return "";
        }

        List<String> executionCourseIDs = new ArrayList<String>(this.getAssociatedExecutionCourses().size());
        List<String> curricularCourseScopeIDs = new ArrayList<String>();
        List<String> roomsIDs = null;

        if (!prepareArguments(executionCourseIDs, curricularCourseScopeIDs, roomsIDs)) {
            return "";
        }
        
        final Season season = (getSeason() != null) ? new Season(getSeason()) : null;
        final Object[] args = { null, this.getBegin(), this.getBegin(), this.getEnd(), executionCourseIDs,
                curricularCourseScopeIDs, roomsIDs, this.evaluationID, season, this.getDescription() };
        try {
            ServiceUtils.executeService(getUserView(), "EditWrittenEvaluation", args);
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
    // END Create and Edit logic
    
    
    // BEGIN Associate Execution Course and Scopes logic
    public void setAssociatedExecutionCourses(List<Integer> associatedExecutionCourses) {
        this.associatedExecutionCourses = associatedExecutionCourses;
        this.getViewState().setAttribute("associatedExecutionCourses", associatedExecutionCourses);
    }
    
    public List<Integer> getAssociatedExecutionCourses() throws FenixFilterException, FenixServiceException {
        if (this.getViewState().getAttribute("associatedExecutionCourses") != null) {
            this.associatedExecutionCourses = (List<Integer>) this.getViewState().getAttribute("associatedExecutionCourses");            
        } else if (this.getEvaluationID() != null) {
            List<Integer> result = new ArrayList<Integer>();
            for (IExecutionCourse executionCourse : this.getEvaluation().getAssociatedExecutionCourses()) {
                result.add(executionCourse.getIdInternal());    
            
                List<Integer> selectedScopes = new ArrayList<Integer>();
                for (ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                    for (ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                        if (((IWrittenEvaluation) this.getEvaluation()).getAssociatedCurricularCourseScope().contains(curricularCourseScope)) {
                            selectedScopes.add(curricularCourseScope.getIdInternal());    
                        }
                    }
                }
                
                Integer[] selected = {};
                this.getCurricularCourseScopesToAssociate().put(executionCourse.getIdInternal(), selectedScopes.toArray(selected));
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
            final Object[] args = { ExecutionCourse.class, executionCourseID };
            IExecutionCourse executionCourse = (IExecutionCourse) ServiceUtils.executeService(null, "ReadDomainObject", args);
            this.associatedExecutionCoursesNames.put(executionCourseID, executionCourse.getNome());
            
            List<SelectItem> items = new ArrayList<SelectItem>();
            Integer[] curricularCourseScopesToAssociate = this.getCurricularCourseScopesToAssociate().get(executionCourse.getIdInternal());
            List<Integer> auxiliarArray = new ArrayList<Integer>();
            if (curricularCourseScopesToAssociate != null) {
                for (Integer curricularCourseScopeToAssociate : curricularCourseScopesToAssociate) {
                    auxiliarArray.add(curricularCourseScopeToAssociate);
                }
            }
            for (ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                for (ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                    StringBuffer label = new StringBuffer();
                    label.append(curricularCourse.getDegreeCurricularPlan().getName());
                    label.append(" ");
                    label.append(curricularCourseScope.getCurricularSemester().getCurricularYear().getYear());
                    if (!curricularCourseScope.getBranch().getName().equals("")) {
                        label.append(" º Ano, ");
                        label.append(curricularCourseScope.getBranch().getName());
                    } else {
                        label.append(" º Ano");
                    }
                    
                    items.add(new SelectItem(curricularCourseScope.getIdInternal(), label.toString()));
                }
            }

            this.curricularCourseScopesSelectItems.put(executionCourse.getIdInternal(), items);
            Integer[] selected = {};
            this.getCurricularCourseScopesToAssociate().put(executionCourse.getIdInternal(), auxiliarArray.toArray(selected));        
        }
    }

    public Map<Integer, List<SelectItem>> getCurricularCourseScopesSelectItems() {
        return curricularCourseScopesSelectItems;
    }

    public void setCurricularCourseScopesSelectItems(
            Map<Integer, List<SelectItem>> curricularCourseScopesSelectItems) {
        this.curricularCourseScopesSelectItems = curricularCourseScopesSelectItems;
    }

    public Map<Integer, Integer[]> getCurricularCourseScopesToAssociate() {
        if (this.getViewState().getAttribute("curricularCourseScopesToAssociate") == null) {
            this.getViewState().setAttribute("curricularCourseScopesToAssociate", new HashMap<Integer,Integer[]>());
        }
        return (Map<Integer, Integer[]>) this.getViewState().getAttribute("curricularCourseScopesToAssociate");
    }

    public void setCurricularCourseScopesToAssociate(Map<Integer, Integer[]> curricularCourseScopesToAssociate) {
        this.getViewState().setAttribute("curricularCourseScopesToAssociate", curricularCourseScopesToAssociate);
    }
    
    public String associateExecutionCourse() throws FenixFilterException, FenixServiceException {
        if (this.getSelectedExecutionDegreeID() == null
                || this.getSelectedCurricularYearID() == null 
                || this.getSelectedExecutionCourseID() == null
                || this.getSelectedExecutionDegreeID() == 0
                || this.getSelectedCurricularYearID() == 0
                || this.getSelectedExecutionCourseID() == 0) {
            this.setErrorMessage("label.choose.request");
            return "";
        } else {
            List<Integer> list = this.getAssociatedExecutionCourses(); 
            Integer integer = this.getSelectedExecutionCourseID();

            if (!list.contains(integer)) {
                list.add(integer);
            }
            
            final Object[] args = { ExecutionCourse.class, integer };
            IExecutionCourse executionCourse = (IExecutionCourse) ServiceUtils.executeService(null, "ReadDomainObject", args);
            
            List<Integer> auxiliarArray = new ArrayList<Integer>();
            for (ICurricularCourse curricularCourse : executionCourse.getAssociatedCurricularCourses()) {
                for (ICurricularCourseScope curricularCourseScope : curricularCourse.getScopes()) {
                    auxiliarArray.add(curricularCourseScope.getIdInternal());
                }
            }

            Integer[] selected = {};
            this.getCurricularCourseScopesToAssociate().put(executionCourse.getIdInternal(), auxiliarArray.toArray(selected));        
            
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

    public Map<Integer, List<IWrittenEvaluation>> getWrittenEvaluations() {
        return writtenEvaluations;
    }

    public Map<Integer, Integer> getWrittenEvaluationsFreeSpace() {
        return writtenEvaluationsFreeSpace;
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
            selectedCurricularYearID = Integer.valueOf((String) this.getCurricularYearIdHidden()
                    .getValue());
            setSelectedCurricularYearID(selectedCurricularYearID);
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
            setSelectedCurricularYearID(selectedExecutionCourseID);
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

    private List<IExecutionCourse> readExecutionCourses() {
        IExecutionDegree executionDegree;
        final Object[] argsRead = { ExecutionDegree.class, this.getSelectedExecutionDegreeID() };
        try {
            executionDegree = (IExecutionDegree) ServiceUtils.executeService(null, "ReadDomainObject", argsRead);
        } catch (Exception e) {
            return null;
        }

        try {
            final Object args[] = { executionDegree.getDegreeCurricularPlan().getIdInternal(),
                    this.getExecutionPeriodID(), this.getSelectedCurricularYearID() };
            List<IExecutionCourse> executionCourses = new ArrayList(
                    (List) ServiceManagerServiceFactory
                            .executeService(
                                    getUserView(),
                                    "ReadExecutionCoursesByDegreeCurricularPlanAndExecutionPeriodAndCurricularYear",
                                    args));
            Collections.sort(executionCourses, new BeanComparator("sigla"));
            return executionCourses;
        } catch (Exception e) {
        }
        return new ArrayList<IExecutionCourse>();
    }

    public List<SelectItem> getExecutionCoursesItems() {
        final List<SelectItem> result = new ArrayList<SelectItem>();
        for (final IExecutionCourse executionCourse : readExecutionCourses()) {
            result.add(new SelectItem(executionCourse.getIdInternal(), executionCourse.getNome()));
        }
        Collections.sort(result, new BeanComparator("label"));
        result.add(0, new SelectItem(0, this.chooseMessage));
        return result;
    }
    // END Code to avoid bug in associating execution courses to written evaluation....

    
    public Integer getExecutionPeriodOID() {
        return (executionPeriodOID == null) ? executionPeriodOID = getAndHoldParameter("executionPeriodOID")
                : executionPeriodOID;
    }

    private Integer getAndHoldParameter(final String parameterName) {
        final String parameterString = getRequestParameter(parameterName);
        final Integer parameterValue;
        if (parameterString != null && parameterString.length() > 0) {
            parameterValue = Integer.valueOf(parameterString);
            setRequestAttribute(parameterName, parameterValue);
        } else {
            parameterValue = null;
        }
        return parameterValue;
    }
    
}
