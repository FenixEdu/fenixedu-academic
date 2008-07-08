package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean.StudentAttendsStateType;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Lesson;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.util.WorkingStudentSelectionType;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBoxList;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlEMailLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlImage;
import pt.ist.fenixWebFramework.renderers.components.HtmlLabel;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlSubmitButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableHeader;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.converters.EnumArrayConverter;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class SearchExecutionCourseAttendsRenderer extends InputRenderer {

    private final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", Language
	    .getLocale());
    private final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", Language
	    .getLocale());

    private String mailURL;
    private String spreadSheetURL;

    private String searchTableClasses;
    private String attendsListTableClasses;
    private String summaryClasses;

    public String getMailURL() {
	return mailURL;
    }

    public void setMailURL(String mailURL) {
	this.mailURL = mailURL;
    }

    public String getSpreadSheetURL() {
	return spreadSheetURL;
    }

    public void setSpreadSheetURL(String spreadSheetURL) {
	this.spreadSheetURL = spreadSheetURL;
    }

    public String getSearchTableClasses() {
	return searchTableClasses;
    }

    public void setSearchTableClasses(String searchTableClasses) {
	this.searchTableClasses = searchTableClasses;
    }

    public String getAttendsListTableClasses() {
	return attendsListTableClasses;
    }

    public void setAttendsListTableClasses(String attendsListTableClasses) {
	this.attendsListTableClasses = attendsListTableClasses;
    }

    public String getSummaryClasses() {
	return summaryClasses;
    }

    public void setSummaryClasses(String summaryClasses) {
	this.summaryClasses = summaryClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new SearchExecutionCourseAttendsLayout();
    }

    private SearchExecutionCourseAttendsBean bean;

    private class SearchExecutionCourseAttendsLayout extends Layout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    bean = (SearchExecutionCourseAttendsBean) object;
	    HtmlContainer blockContainer = new HtmlBlockContainer();
	    blockContainer.addChild(renderSearcher());

	    bean.getExecutionCourse().searchAttends(bean);

	    blockContainer.addChild(renderAttendsListSize());

	    blockContainer.addChild(renderLinks());

	    blockContainer.addChild(renderAttendsList());

	    blockContainer.addChild(renderSummaryList());
	    return blockContainer;
	}

	private HtmlComponent renderLinks() {
	    HtmlBlockContainer blockContainer = new HtmlBlockContainer();

	    final HtmlLink mailLink = new HtmlLink();
	    mailLink.setText(applicationResources.getString("link.sendEmailToAllStudents"));
	    mailLink.setUrl(getMailURL() + getQueryURL("start"));
	    HtmlBlockContainer htmlInlineContainer = new HtmlBlockContainer();
	    htmlInlineContainer.addChild(mailLink);
	    blockContainer.addChild(htmlInlineContainer);

	    final HtmlLink spreadSheetLink = new HtmlLink();
	    spreadSheetLink.setText(applicationResources.getString("link.getExcelSpreadSheet"));
	    spreadSheetLink.setUrl(getSpreadSheetURL() + getQueryURL("prepare"));
	    HtmlBlockContainer htmlInlineContainer2 = new HtmlBlockContainer();
	    htmlInlineContainer2.addChild(spreadSheetLink);
	    blockContainer.addChild(htmlInlineContainer2);

	    return blockContainer;
	}

	private HtmlComponent renderAttendsListSize() {
	    HtmlBlockContainer htmlBlockContainer = new HtmlBlockContainer();
	    HtmlText htmlText = new HtmlText("<h3>" + Integer.valueOf(bean.getAttendsResult().size()).toString() + " "
		    + applicationResources.getString("message.attendingStudents") + "</h3>", false);
	    htmlBlockContainer.addChild(htmlText);
	    return htmlBlockContainer;
	}

	private HtmlComponent renderSummaryList() {
	    HtmlBlockContainer blockContainer = new HtmlBlockContainer();

	    HtmlBlockContainer containerSummary = new HtmlBlockContainer();
	    containerSummary.setClasses("mtop2 mbottom05");
	    containerSummary.addChild(new HtmlText(applicationResources.getString("label.attends.summary")));
	    blockContainer.addChild(containerSummary);

	    HtmlTable htmlTable = new HtmlTable();
	    htmlTable.setClasses(getSummaryClasses());
	    HtmlTableHeader tableHeader = htmlTable.createHeader();
	    HtmlTableRow headerRow = tableHeader.createRow();
	    headerRow.createCell(applicationResources.getString("label.attends.summary.enrollmentsNumber"));
	    headerRow.createCell(applicationResources.getString("label.attends.summary.studentsNumber"));

	    SortedSet<Integer> keys = new TreeSet<Integer>(bean.getEnrolmentsNumberMap().keySet());
	    for (Integer key : keys) {
		HtmlTableRow row = htmlTable.createRow();
		row.createCell(key.toString());
		row.createCell(bean.getEnrolmentsNumberMap().get(key).toString());
	    }
	    blockContainer.addChild(htmlTable);

	    return blockContainer;
	}

	private HtmlTable renderAttendsList() {
	    HtmlTable htmlTable = new HtmlTable();
	    htmlTable.setClasses(getAttendsListTableClasses());
	    HtmlTableHeader tableHeader = htmlTable.createHeader();

	    List<Grouping> groupings = new ArrayList<Grouping>(bean.getExecutionCourse().getGroupings());
	    Collections.sort(groupings, Grouping.COMPARATOR_BY_ENROLMENT_BEGIN_DATE);

	    List<ShiftType> shiftTypes = new ArrayList<ShiftType>(bean.getExecutionCourse().getShiftTypes());
	    Collections.sort(shiftTypes);

	    Integer rowSpan;
	    if (groupings.isEmpty() && shiftTypes.isEmpty()) {
		rowSpan = 1;
	    } else {
		rowSpan = 2;
	    }

	    HtmlTableRow row1 = tableHeader.createRow();
	    if (bean.getViewPhoto()) {
		HtmlTableCell photoCell = row1.createCell(applicationResources.getString("label.photo"));
		photoCell.setRowspan(rowSpan);
	    }
	    HtmlTableCell numberCell = row1.createCell(applicationResources.getString("label.number"));
	    numberCell.setRowspan(rowSpan);

	    HtmlTableCell numberOfEnrolmentsCell = row1.createCell(applicationResources.getString("label.numberOfEnrollments"));
	    numberOfEnrolmentsCell.setRowspan(rowSpan);

	    HtmlTableCell enrolmentStateCell1 = row1.createCell(applicationResources.getString("label.attends.enrollmentState"));
	    enrolmentStateCell1.setRowspan(rowSpan);

	    HtmlTableCell degreeCell = row1.createCell(applicationResources.getString("label.Degree"));
	    degreeCell.setRowspan(rowSpan);

	    HtmlTableCell nameCell = row1.createCell(applicationResources.getString("label.name"));
	    nameCell.setRowspan(rowSpan);

	    if (!groupings.isEmpty()) {
		HtmlTableCell groupingCell = row1.createCell(applicationResources.getString("label.projectGroup"));
		groupingCell.setColspan(groupings.size());
	    }

	    HtmlTableCell emailCell = row1.createCell(applicationResources.getString("label.mail"));
	    emailCell.setRowspan(rowSpan);

	    if (!shiftTypes.isEmpty()) {
		HtmlTableCell shiftCell = row1.createCell(applicationResources.getString("label.attends.shifts"));
		shiftCell.setColspan(shiftTypes.size());
	    }

	    HtmlTableRow row2 = tableHeader.createRow();
	    for (Grouping grouping : groupings) {
		row2.createCell(grouping.getName());
	    }
	    for (ShiftType shiftType : shiftTypes) {
		row2.createCell(enumerationResources.getString(shiftType.getName()));
	    }

	    List<Attends> attendsResult = new ArrayList<Attends>(bean.getAttendsResult());
	    Collections.sort(attendsResult, Attends.COMPARATOR_BY_STUDENT_NUMBER);
	    for (Attends attends : attendsResult) {
		HtmlTableRow row = htmlTable.createRow();
		if (bean.getViewPhoto()) {
		    HtmlImage htmlImage = new HtmlImage();
		    htmlImage.setSource(RenderUtils.getContextRelativePath("")
			    + "/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="
			    + attends.getRegistration().getStudent().getPerson().getIdInternal());
		    row.createCell().setBody(htmlImage);
		}
		row.createCell(attends.getRegistration().getStudent().getNumber().toString());
		if (attends.getEnrolment() == null) {
		    row.createCell("--");
		} else {
		    row.createCell(String.valueOf(attends.getEnrolment().getNumberOfTotalEnrolmentsInThisCourse(
			    attends.getEnrolment().getExecutionPeriod())));
		}
		row.createCell(enumerationResources.getString(attends.getAttendsStateType().getQualifiedName()));
		row.createCell(attends.getStudentCurricularPlanFromAttends().getDegreeCurricularPlan().getName());
		row.createCell(attends.getRegistration().getStudent().getPerson().getFirstAndLastName());
		for (Grouping grouping : groupings) {
		    StudentGroup studentGroup = attends.getStudentGroupByGrouping(grouping);
		    if (studentGroup == null) {
			row.createCell("N/A");
		    } else {
			HtmlLink groupLink = new HtmlLink();
			groupLink.setText(studentGroup.getGroupNumber().toString());
			groupLink.setUrl(getGroupURL(studentGroup));
			row.createCell().setBody(groupLink);
		    }
		}

		String email = attends.getRegistration().getStudent().getPerson().getEmail();
		row.createCell().setBody(email != null ? new HtmlEMailLink(email) : new HtmlLabel(""));

		for (ShiftType shiftType : shiftTypes) {
		    Shift shift = attends.getRegistration().getShiftFor(bean.getExecutionCourse(), shiftType);
		    if (shift == null) {
			row.createCell("N/A");
		    } else {
			row.createCell(shift.getNome());
		    }
		}
	    }

	    return htmlTable;
	}

	private String getGroupURL(StudentGroup studentGroup) {
	    StringBuilder stringBuilder = new StringBuilder("/viewStudentGroupInformation.do?");
	    stringBuilder.append("studentGroupCode=" + studentGroup.getIdInternal());
	    stringBuilder.append("&method=viewStudentGroupInformation");
	    stringBuilder.append("&objectCode=" + bean.getExecutionCourse().getIdInternal());
	    stringBuilder.append("&groupPropertiesCode=" + studentGroup.getGrouping().getIdInternal());
	    return stringBuilder.toString();
	}

	private String getQueryURL(String method) {
	    StringBuilder stringBuilder = new StringBuilder("?");
	    stringBuilder.append("objectCode=" + bean.getExecutionCourse().getIdInternal());
	    stringBuilder.append("&method=" + method);
	    for (DegreeCurricularPlan degreeCurricularPlan : bean.getDegreeCurricularPlans()) {
		stringBuilder.append("&coursesIDs=" + degreeCurricularPlan.getIdInternal());
	    }
	    for (Shift shift : bean.getShifts()) {
		stringBuilder.append("&shiftIDs=" + shift.getIdInternal());
	    }
	    for (WorkingStudentSelectionType workingStudentSelectionType : bean.getWorkingStudentTypes()) {
		stringBuilder.append("&workingStudentType=" + workingStudentSelectionType.name());
	    }
	    for (StudentAttendsStateType studentAttendsStateType : bean.getAttendsStates()) {
		stringBuilder.append("&enrollmentType=" + studentAttendsStateType.name());
	    }

	    return stringBuilder.toString();
	}

	private HtmlTable renderSearcher() {
	    HtmlTable htmlTable = new HtmlTable();
	    htmlTable.setClasses(getSearchTableClasses());
	    HtmlTableHeader header = htmlTable.createHeader();
	    HtmlTableRow headerRow = header.createRow();

	    HtmlTableRow row = htmlTable.createRow();
	    createAttendsStateTypeSearch(row, headerRow);
	    createDCPSearch(row, headerRow);
	    createShiftSearch(row, headerRow);
	    createWorkingStudentSearch(row, headerRow);

	    HtmlTableRow row2 = htmlTable.createRow();
	    HtmlTableCell photoCell = row2.createCell();
	    photoCell.setColspan(headerRow.getCells().size());
	    HtmlCheckBox checkBox = new HtmlCheckBox(applicationResources.getString("label.viewPhoto"), bean.getViewPhoto());
	    checkBox.bind(getInputContext().getMetaObject(), "viewPhoto");
	    photoCell.setBody(checkBox);

	    HtmlTableRow row3 = htmlTable.createRow();
	    HtmlTableCell submitCell = row3.createCell();
	    submitCell.setColspan(headerRow.getCells().size());
	    submitCell.setBody(new HtmlSubmitButton(applicationResources.getString("button.selectShift")));

	    return htmlTable;
	}

	private void createWorkingStudentSearch(HtmlTableRow row, HtmlTableRow headerRow) {
	    headerRow.createCell(applicationResources.getString("label.workingStudents"));

	    HtmlCheckBoxList workingStudentCheckBoxList = new HtmlCheckBoxList();
	    for (WorkingStudentSelectionType workingStudentSelectionType : WorkingStudentSelectionType.values()) {
		HtmlCheckBox option = workingStudentCheckBoxList.addOption(new HtmlLabel(enumerationResources
			.getString(workingStudentSelectionType.getQualifiedName())), workingStudentSelectionType.name());
		option.setChecked(bean.getWorkingStudentTypes().contains(workingStudentSelectionType));
	    }
	    row.createCell().setBody(workingStudentCheckBoxList);

	    workingStudentCheckBoxList.bind(getInputContext().getMetaObject(), "workingStudentTypes");
	    workingStudentCheckBoxList.setConverter(new EnumArrayConverter(WorkingStudentSelectionType.class));
	    workingStudentCheckBoxList.setSelectAllShown(true);

	}

	private void createShiftSearch(HtmlTableRow row, HtmlTableRow headerRow) {
	    headerRow.createCell(applicationResources.getString("label.selectShift"));

	    HtmlCheckBoxList shiftCheckBoxList = new HtmlCheckBoxList();
	    for (Shift shift : bean.getExecutionCourse().getAssociatedShifts()) {
		MetaObject shiftMetaObject = MetaObjectFactory.createObject(shift, new Schema(Shift.class));
		HtmlCheckBox option = shiftCheckBoxList.addOption(new HtmlLabel(getShiftLabel(shift)), shiftMetaObject.getKey()
			.toString());
		option.setChecked(bean.getShifts().contains(shift));
	    }
	    row.createCell().setBody(shiftCheckBoxList);

	    shiftCheckBoxList.bind(getInputContext().getMetaObject(), "shifts");
	    shiftCheckBoxList.setConverter(new DomainObjectKeyArrayConverter());
	    shiftCheckBoxList.setSelectAllShown(true);

	}

	private String getShiftLabel(Shift shift) {
	    StringBuilder stringBuilder = new StringBuilder(shift.getNome());
	    if (shift.hasAnyAssociatedLessons()) {
		stringBuilder.append(" ( ");

		for (Iterator<Lesson> iterator = shift.getAssociatedLessonsIterator(); iterator.hasNext();) {
		    Lesson lesson = iterator.next();
		    stringBuilder.append(lesson.getDiaSemana().toString());
		    stringBuilder.append(" ");
		    stringBuilder.append(lesson.getBeginHourMinuteSecond().toString("HH:mm"));
		    stringBuilder.append(" - ");
		    stringBuilder.append(lesson.getEndHourMinuteSecond().toString("HH:mm"));
		    if (lesson.hasSala()) {
			stringBuilder.append(" - ");
			stringBuilder.append(lesson.getSala().getIdentification());
		    }
		    if (iterator.hasNext()) {
			stringBuilder.append(" ; ");
		    }
		}
		stringBuilder.append(" ) ");
	    }
	    return stringBuilder.toString();
	}

	private void createDCPSearch(HtmlTableRow row, HtmlTableRow headerRow) {
	    headerRow.createCell(applicationResources.getString("label.attends.courses"));

	    HtmlCheckBoxList dcpCheckBoxList = new HtmlCheckBoxList();
	    for (DegreeCurricularPlan degreeCurricularPlan : bean.getExecutionCourse().getAttendsDegreeCurricularPlans()) {
		MetaObject dcpMetaObject = MetaObjectFactory.createObject(degreeCurricularPlan, new Schema(
			DegreeCurricularPlan.class));
		HtmlCheckBox option = dcpCheckBoxList.addOption(new HtmlLabel(degreeCurricularPlan.getName()), dcpMetaObject
			.getKey().toString());
		option.setChecked(bean.getDegreeCurricularPlans().contains(degreeCurricularPlan));
	    }
	    row.createCell().setBody(dcpCheckBoxList);

	    dcpCheckBoxList.bind(getInputContext().getMetaObject(), "degreeCurricularPlans");
	    dcpCheckBoxList.setConverter(new DomainObjectKeyArrayConverter());
	    dcpCheckBoxList.setSelectAllShown(true);
	}

	private void createAttendsStateTypeSearch(HtmlTableRow row, HtmlTableRow headerRow) {
	    headerRow.createCell(applicationResources.getString("label.selectStudents"));

	    HtmlCheckBoxList attendsStateCheckBoxList = new HtmlCheckBoxList();
	    for (StudentAttendsStateType attendsStateType : StudentAttendsStateType.values()) {
		HtmlCheckBox option = attendsStateCheckBoxList.addOption(new HtmlLabel(enumerationResources
			.getString(attendsStateType.getQualifiedName())), attendsStateType.name());
		option.setChecked(bean.getAttendsStates().contains(attendsStateType));
	    }
	    row.createCell().setBody(attendsStateCheckBoxList);

	    attendsStateCheckBoxList.bind(getInputContext().getMetaObject(), "attendsStates");
	    attendsStateCheckBoxList.setConverter(new EnumArrayConverter(StudentAttendsStateType.class));
	    attendsStateCheckBoxList.setSelectAllShown(true);
	}

    }

}
