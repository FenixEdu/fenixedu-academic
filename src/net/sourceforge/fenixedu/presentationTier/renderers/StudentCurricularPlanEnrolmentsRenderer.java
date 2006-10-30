package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanComparator;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.util.LanguageUtils;

public class StudentCurricularPlanEnrolmentsRenderer extends InputRenderer {
    
    private static final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources");
    
    private final ResourceBundle academicAdminOfficeResources = ResourceBundle.getBundle("resources.AcademicAdminOffice");

    
    private Integer initialWidth = 70;

    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";
    
    private String enrolmentClasses = "smalltxt, smalltxt aright, smalltxt aright, smalltxt aright, aright";
    
    private String curricularCoursesToEnrol = "smalltxt, smalltxt aright, smalltxt aright, aright";
    
    public Integer getInitialWidth() {
	return initialWidth;
    }

    public void setInitialWidth(Integer initialWidth) {
	this.initialWidth = initialWidth;
    }

    public Integer getWidthDecreasePerLevel() {
	return widthDecreasePerLevel;
    }

    public void setWidthDecreasePerLevel(Integer widthDecreasePerLevel) {
	this.widthDecreasePerLevel = widthDecreasePerLevel;
    }

    public String getTablesClasses() {
	return tablesClasses;
    }

    public void setTablesClasses(String tablesClasses) {
	this.tablesClasses = tablesClasses;
    }

    public String getGroupRowClasses() {
	return groupRowClasses;
    }

    public void setGroupRowClasses(String groupRowClasses) {
	this.groupRowClasses = groupRowClasses;
    }

    private String[] getEnrolmentClasses() {
	return enrolmentClasses.split(",");
    }

    public void setEnrolmentClasses(String enrolmentClasses) {
	this.enrolmentClasses = enrolmentClasses;
    }

    private String getEnrolmentNameClasses() {
        return getEnrolmentClasses()[0];
    }

    private String getEnrolmentYearClasses() {
        return getEnrolmentClasses()[1];
    }

    private String getEnrolmentSemesterClasses() {
        return getEnrolmentClasses()[2];
    }
    
    private String getEnrolmentEctsClasses() {
        return getEnrolmentClasses()[3];
    }

    private String getEnrolmentCheckBoxClasses() {
        return getEnrolmentClasses()[4];
    }
    
    private String[] getCurricularCourseClasses() {
	return curricularCoursesToEnrol.split(",");
    }

    public void setCurricularCourseClasses(String curricularCoursesToEnrol) {
	this.curricularCoursesToEnrol = curricularCoursesToEnrol;
    }

    
    private String getCurricularCourseNameClasses() {
        return getCurricularCourseClasses()[0];
    }

    private String getCurricularCourseYearClasses() {
        return getCurricularCourseClasses()[1];
    }
    
    private String getCurricularCourseEctsClasses() {
        return getCurricularCourseClasses()[2];
    }

    private String getCurricularCourseCheckBoxClasses() {
        return getCurricularCourseClasses()[3];
    }




    public StudentCurricularPlanEnrolmentsRenderer() {
	super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentCurricularPlanEnrolmentLayout();
    }

    private class StudentCurricularPlanEnrolmentLayout extends Layout {

	private final CopyCheckBoxValuesController enrollmentsController = new CopyCheckBoxValuesController();
	
	private final CopyCheckBoxValuesController degreeModulesToEnrolController = new CopyCheckBoxValuesController(); 
	
	private final List<CurriculumModule> initialCurriculumModules = new ArrayList<CurriculumModule>();

	
	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    StudentEnrolmentBean studentEnrolmentBean = (StudentEnrolmentBean) object;

	    HtmlBlockContainer container = new HtmlBlockContainer();
	    
	    if (studentEnrolmentBean == null) {
		return new HtmlText();
	    } 

	    HtmlMultipleHiddenField hiddenEnrollments = new HtmlMultipleHiddenField();
	    hiddenEnrollments.bind(getInputContext().getMetaObject(), "curriculumModules"); // slot refered by name
	    hiddenEnrollments.setConverter(new DomainObjectKeyArrayConverter());
	    hiddenEnrollments.setController(enrollmentsController);

	    HtmlMultipleHiddenField hiddenDegreeModulesToEnrol = new HtmlMultipleHiddenField();
	    hiddenDegreeModulesToEnrol.bind(getInputContext().getMetaObject(), "degreeModulesToEnrol"); // slot refered by name
	    hiddenDegreeModulesToEnrol.setConverter(new DegreeModuleToEnrolKeyConverter());
	    hiddenDegreeModulesToEnrol.setController(degreeModulesToEnrolController);

	    container.addChild(hiddenEnrollments);
	    container.addChild(hiddenDegreeModulesToEnrol);

	    generateModules(container, studentEnrolmentBean.getStudentCurricularPlan(), studentEnrolmentBean.getStudentCurricularPlan().getRoot(), studentEnrolmentBean.getExecutionPeriod(), 0);
	    studentEnrolmentBean.setInitialCurriculumModules(initialCurriculumModules);
	    return container;
	}

	private void generateModules(HtmlBlockContainer blockContainer, StudentCurricularPlan studentCurricularPlan, CurriculumGroup group, ExecutionPeriod executionPeriod, int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    blockContainer.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");
	    
	    HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    htmlTableRow.createCell().setBody(new HtmlText(group.getDegreeModule().getName()));
	    
	    HtmlTableCell checkBoxCell = htmlTableRow.createCell();
	    checkBoxCell.setClasses("aright");

	    HtmlCheckBox checkBox = new HtmlCheckBox(true);
	    MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(group, new Schema(CurriculumGroup.class));
	    checkBox.setName("enrolmentCheckBox" + group.getIdInternal());
	    checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
	    checkBoxCell.setBody(checkBox);
	    
	    if(group.hasAnyCurriculumModules()) {
		checkBox.setDisabled(true);
	    } else {
		enrollmentsController.addCheckBox(checkBox);
		initialCurriculumModules.add(group);		
	    }
	    
	    final HtmlTable coursesTable = new HtmlTable();
	    blockContainer.addChild(coursesTable);
	    coursesTable.setClasses(getTablesClasses());
	    coursesTable.setStyle("width: " + (getInitialWidth() - depth - getWidthDecreasePerLevel()) + "em; margin-left: " + (depth + getWidthDecreasePerLevel()) + "em;");
	    
	    generateEnrolments(group, executionPeriod, coursesTable);
	    generateCurricularCoursesToEnrol(coursesTable, group, executionPeriod);
	    generateGroups(blockContainer, group, studentCurricularPlan, executionPeriod, depth);
	}

	private void generateGroups(HtmlBlockContainer blockContainer, CurriculumGroup group, StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod, int depth) {
	    List<Context> courseGroupsToEnrol = group.getCourseGroupContextsToEnrol(executionPeriod);
	    Collections.sort(courseGroupsToEnrol);
	    
	    List<CurriculumGroup> curriculumGroups = new ArrayList<CurriculumGroup>(group.getCurriculumGroups());
	    Collections.sort(curriculumGroups, new CurriculumModuleComparator(executionPeriod));
	    
	    while(!courseGroupsToEnrol.isEmpty() || !curriculumGroups.isEmpty()) {
		if(!curriculumGroups.isEmpty() && courseGroupsToEnrol.isEmpty()) {
		    generateModules(blockContainer, studentCurricularPlan, curriculumGroups.get(0), executionPeriod, depth + getWidthDecreasePerLevel());
		    curriculumGroups.remove(0);
		} else if(curriculumGroups.isEmpty() && !courseGroupsToEnrol.isEmpty()) {
		    generateCourseGroupToEnroll(blockContainer, group, courseGroupsToEnrol.get(0), depth + getWidthDecreasePerLevel());
		    courseGroupsToEnrol.remove(0);
		} else if(curriculumGroups.get(0).getChildOrder(executionPeriod) <= courseGroupsToEnrol.get(0).getChildOrder()) {
		    generateModules(blockContainer, studentCurricularPlan, curriculumGroups.get(0), executionPeriod, depth + getWidthDecreasePerLevel());
		    curriculumGroups.remove(0);
		} else {
		    generateCourseGroupToEnroll(blockContainer, group, courseGroupsToEnrol.get(0), depth + getWidthDecreasePerLevel());
		    courseGroupsToEnrol.remove(0);
		}
	    }
	}

	private void generateCourseGroupToEnroll(HtmlBlockContainer blockContainer, CurriculumGroup group, Context context, int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    blockContainer.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");	    
	    HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    htmlTableRow.createCell().setBody(new HtmlText(context.getChildDegreeModule().getName()));
	    HtmlTableCell checkBoxCell = htmlTableRow.createCell();
	    checkBoxCell.setClasses("aright");

	    DegreeModuleToEnrol degreeModuleToEnrol = new DegreeModuleToEnrol(group, context);
	    HtmlCheckBox checkBox = new HtmlCheckBox(false);
	    checkBox.setName("degreeModuleToEnrolCheckBox" + degreeModuleToEnrol.getContext().getIdInternal() + ":" + degreeModuleToEnrol.getCurriculumGroup().getIdInternal());
	    checkBox.setUserValue(degreeModuleToEnrol.getKey());
	    degreeModulesToEnrolController.addCheckBox(checkBox);
	    checkBoxCell.setBody(checkBox);	    
	}
	



	private void generateCurricularCoursesToEnrol(HtmlTable groupTable, CurriculumGroup group, ExecutionPeriod executionPeriod) {
	    List<Context> curricularCoursesToEnrol = group.getCurricularCourseContextsToEnrol(executionPeriod);
	    Collections.sort(curricularCoursesToEnrol);
	    
	    for (Context context : curricularCoursesToEnrol) {
		CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();

		HtmlTableRow htmlTableRow = groupTable.createRow();
		HtmlTableCell cellName = htmlTableRow.createCell();
		cellName.setClasses(getCurricularCourseNameClasses());
		cellName.setBody(new HtmlText(curricularCourse.getName()));

		//Year
		final HtmlTableCell yearCell = htmlTableRow.createCell();
		yearCell.setClasses(getCurricularCourseYearClasses());
		yearCell.setColspan(2);

		final StringBuilder year = new StringBuilder();
		year.append(context.getCurricularPeriod().getFullLabel());
		yearCell.setBody(new HtmlText(year.toString()));

		//Ects
		final HtmlTableCell ectsCell = htmlTableRow.createCell();
		ectsCell.setClasses(getCurricularCourseEctsClasses());

		final StringBuilder ects = new StringBuilder();
		ects.append(curricularCourse.getEctsCredits()).append(" ").append(academicAdminOfficeResources.getString("credits.abbreviation"));
		ectsCell.setBody(new HtmlText(ects.toString()));


		HtmlTableCell checkBoxCell = htmlTableRow.createCell();
		checkBoxCell.setClasses(getCurricularCourseCheckBoxClasses());

		DegreeModuleToEnrol degreeModuleToEnrol = new DegreeModuleToEnrol(group, context);
		HtmlCheckBox checkBox = new HtmlCheckBox(false);
		checkBox.setName("degreeModuleToEnrolCheckBox" + degreeModuleToEnrol.getContext().getIdInternal() + ":" + degreeModuleToEnrol.getCurriculumGroup().getIdInternal());
		checkBox.setUserValue(degreeModuleToEnrol.getKey());
		degreeModulesToEnrolController.addCheckBox(checkBox);
		checkBoxCell.setBody(checkBox);
	    }
	}


	private void generateEnrolments(CurriculumGroup group, ExecutionPeriod executionPeriod, final HtmlTable groupTable) {
	    for (CurriculumLine curriculumLine : group.getCurriculumLines()) {
		if(((CurriculumLine) curriculumLine).isEnrolment()) {
		    Enrolment enrolment = (Enrolment) curriculumLine;
		    if(enrolment.getExecutionPeriod().equals(executionPeriod) && enrolment.isEnroled()) {
			generateEnrolment(groupTable, enrolment);
		    }
		}
	    }
	}

	private void generateEnrolment(final HtmlTable groupTable, Enrolment enrolment) {
	    HtmlTableRow htmlTableRow = groupTable.createRow();
	    HtmlTableCell cellName = htmlTableRow.createCell();
	    cellName.setClasses(getEnrolmentNameClasses());
	    cellName.setBody(new HtmlText(enrolment.getCurricularCourse().getName()));
	    
	    // Year
	    final HtmlTableCell yearCell = htmlTableRow.createCell();
	    yearCell.setClasses(getEnrolmentYearClasses());

	    final StringBuilder year = new StringBuilder();
	    year.append(enrolment.getExecutionPeriod().getExecutionYear().getYear());
	    yearCell.setBody(new HtmlText(year.toString()));

	    // Semester
	    final HtmlTableCell semesterCell = htmlTableRow.createCell();
	    semesterCell.setClasses(getEnrolmentSemesterClasses());

	    final StringBuilder semester = new StringBuilder();
	    semester.append(enrolment.getExecutionPeriod().getSemester().toString());
	    semester.append(" ");
	    semester.append(enumerationResources.getString("SEMESTER.ABBREVIATION"));
	    semesterCell.setBody(new HtmlText(semester.toString()));

	    //Ects
	    final HtmlTableCell ectsCell = htmlTableRow.createCell();
	    ectsCell.setClasses(getEnrolmentEctsClasses());

	    final StringBuilder ects = new StringBuilder();
	    ects.append(enrolment.getCurricularCourse().getEctsCredits()).append(" ").append(academicAdminOfficeResources.getString("credits.abbreviation"));
	    ectsCell.setBody(new HtmlText(ects.toString()));
	    

	    MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(enrolment, new Schema(Enrolment.class));

	    HtmlCheckBox checkBox = new HtmlCheckBox(true);
	    checkBox.setName("enrolmentCheckBox" + enrolment.getIdInternal());
	    checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
	    enrollmentsController.addCheckBox(checkBox);

	    HtmlTableCell cellCheckBox = htmlTableRow.createCell();
	    cellCheckBox.setClasses(getEnrolmentCheckBoxClasses());
	    cellCheckBox.setBody(checkBox);

	    initialCurriculumModules.add(enrolment);
	}
    }
    
    private static class CopyCheckBoxValuesController extends HtmlController {

	private List<HtmlCheckBox> checkboxes;
	
	public CopyCheckBoxValuesController() {
	    super();

	    this.checkboxes = new ArrayList<HtmlCheckBox>();
	}

	public void addCheckBox(HtmlCheckBox checkBox) {
	    this.checkboxes.add(checkBox);
	}

	@Override
	public void execute(IViewState viewState) {
	    HtmlMultipleValueComponent component = (HtmlMultipleValueComponent) getControlledComponent();
	    
	    List<String> values = new ArrayList<String>();

	    for (HtmlCheckBox checkBox : this.checkboxes) {
		if (checkBox.isChecked()) {
		    values.add(checkBox.getValue());
		}
	    }
	    
	    component.setValues(values.toArray(new String[0]));
	}
    }
    
    public static class DegreeModuleToEnrolKeyConverter extends Converter {

	@Override
	public Object convert(Class type, Object value) {
	    DomainObjectKeyConverter converter = new DomainObjectKeyConverter();
	    List<DegreeModuleToEnrol> result = new ArrayList<DegreeModuleToEnrol>();

	    if (value == null) {
		return null;
	    }

	    String[] values = (String[]) value;
	    for (int i = 0; i < values.length; i++) {
		String key = values[i];

		String[] parts = key.split(",");
		if (parts.length < 2) {
		    throw new ConversionException("invalid key format: " + key);
		}

		Context context = (Context) converter.convert(type, parts[0]);
		CurriculumGroup curriculumGroup = (CurriculumGroup) converter.convert(type, parts[1]);
		DegreeModuleToEnrol degreeModuleToEnrol = new DegreeModuleToEnrol(curriculumGroup, context);
		result.add(degreeModuleToEnrol);
	    }
	    
	    return result;
	}
    }
    
    public static class CurriculumModuleComparator implements Comparator<CurriculumGroup> {

	private ExecutionPeriod executionPeriod; 
	
	public CurriculumModuleComparator(ExecutionPeriod executionPeriod) {
	    this.executionPeriod = executionPeriod;
	}
	
	public int compare(CurriculumGroup o1, CurriculumGroup o2) {
	    return o1.getChildOrder(executionPeriod).compareTo(o2.getChildOrder(executionPeriod));
	}
	
    }
}
