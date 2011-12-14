package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.executionCourse.SearchExecutionCourseAttendsBean.StudentAttendsStateType;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.util.WorkingStudentSelectionType;
import pt.ist.fenixWebFramework.rendererExtensions.converters.DomainObjectKeyArrayConverter;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBox;
import pt.ist.fenixWebFramework.renderers.components.HtmlCheckBoxList;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlLabel;
import pt.ist.fenixWebFramework.renderers.components.HtmlSubmitButton;
import pt.ist.fenixWebFramework.renderers.components.HtmlTable;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableCell;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableHeader;
import pt.ist.fenixWebFramework.renderers.components.HtmlTableRow;
import pt.ist.fenixWebFramework.renderers.converters.EnumArrayConverter;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class SearchExecutionCourseAttendsRenderer extends InputRenderer {

    private final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources", Language
	    .getLocale());
    private final ResourceBundle applicationResources = ResourceBundle.getBundle("resources.ApplicationResources", Language
	    .getLocale());

    private String searchTableClasses;

    public String getSearchTableClasses() {
	return searchTableClasses;
    }

    public void setSearchTableClasses(String searchTableClasses) {
	this.searchTableClasses = searchTableClasses;
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

	    // bean.getExecutionCourse().searchAttends(bean);
	    //
	    // blockContainer.addChild(renderAttendsListSize());
	    //
	    // blockContainer.addChild(renderLinks());
	    //
	    // blockContainer.addChild(renderAttendsList());
	    //
	    // blockContainer.addChild(renderSummaryList());
	    return blockContainer;
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
		HtmlCheckBox option = shiftCheckBoxList.addOption(new HtmlLabel(shift.getPresentationName()), shiftMetaObject
			.getKey().toString());
		option.setChecked(bean.getShifts().contains(shift));
	    }
	    row.createCell().setBody(shiftCheckBoxList);

	    shiftCheckBoxList.bind(getInputContext().getMetaObject(), "shifts");
	    shiftCheckBoxList.setConverter(new DomainObjectKeyArrayConverter());
	    shiftCheckBoxList.setSelectAllShown(true);

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
