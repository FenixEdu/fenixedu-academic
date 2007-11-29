package net.sourceforge.fenixedu.presentationTier.renderers;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.DismissalType;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.controllers.CopyCheckBoxValuesController;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButton;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButtonGroup;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.converters.ConversionException;
import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.lang.StringUtils;

public class StudentDismissalRenderer extends InputRenderer {
    
    private Integer initialWidth = 60;

    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";
    
    private String curricularCourseRowClasses = "";
    
    private String groupCellClasses = "smalltxt, aright";
    
    private String curricularCourseCellClasses = ", aright";
    
    private String dismissalType;
    
    
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

    public String getCurricularCourseRowClasses() {
        return curricularCourseRowClasses;
    }

    public void setCurricularCourseRowClasses(String curricularCourseRowClasses) {
        this.curricularCourseRowClasses = curricularCourseRowClasses;
    }

    private String[] getGroupCellClasses() {
	return groupCellClasses.split(",");
    }

    public void setGroupCellClasses(String groupCellClasses) {
	this.groupCellClasses = groupCellClasses;
    }
    
    public String getGroupNameClasses() {
	return getGroupCellClasses()[0];
    }
    
    public String getGroupRadioClasses() {
	return getGroupCellClasses()[1];
    }
    
    private String[] getCurricularCourseCellClasses() {
	return curricularCourseCellClasses.split(",");
    }

    public void setCurricularCourseCellClasses(String curricularCourseCellClasses) {
	this.curricularCourseCellClasses = curricularCourseCellClasses;
    }
    
    public String getCurricularCourseNameClasses() {
	return getCurricularCourseCellClasses()[0];
    }
    
    public String getCurricularCourseCheckBoxClasses() {
	return getCurricularCourseCellClasses()[1];
    }
    
    public String getDismissalType() {
        return dismissalType;
    }

    public void setDismissalType(String dismissalType) {
        this.dismissalType = dismissalType;
    }



    public StudentDismissalRenderer() {
	super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentDismissalLayout();
    }

    private class StudentDismissalLayout extends Layout {

	private final CopyCheckBoxValuesController curricularCoursesController = new CopyCheckBoxValuesController();
	private final CopyCheckBoxValuesController optionalCurricularCoursesController = new CopyCheckBoxValuesController();
	
	private HtmlRadioButtonGroup radioButtonGroup = null;
	private DismissalBean dismissalBean = null;
	
	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    dismissalBean = (DismissalBean) object;

	    HtmlBlockContainer container = new HtmlBlockContainer();
	    if (dismissalBean == null
		    || dismissalBean.getExecutionPeriod() == null
		    || dismissalBean.getStudentCurricularPlan().getStartExecutionPeriod().isAfter(
			    dismissalBean.getExecutionPeriod())) {
		return new HtmlText();
	    }
	    
	    DismissalType dismissalTypeValue = getDismissalType() == null ? dismissalBean.getDismissalType() : DismissalType.valueOf(getDismissalType());  
	    
	    if(dismissalTypeValue == DismissalType.CURRICULUM_GROUP_CREDITS) {
		radioButtonGroup = new HtmlRadioButtonGroup();
		radioButtonGroup.bind(getInputContext().getMetaObject(), "courseGroup"); // slot refered by name
		radioButtonGroup.setConverter(new DomainObjectKeyConverter());
		container.addChild(radioButtonGroup);
		generateCourseGroupCycles(container, dismissalBean.getStudentCurricularPlan(), dismissalBean.getExecutionPeriod());
		
	    } else if(dismissalTypeValue == DismissalType.CURRICULAR_COURSE_CREDITS){
		final HtmlMultipleHiddenField hiddenCurricularCourses = new HtmlMultipleHiddenField();
		hiddenCurricularCourses.bind(getInputContext().getMetaObject(), "dismissals"); // slot refered by name
		hiddenCurricularCourses.setConverter(new SelectedCurricularCoursesKeyConverter());
		hiddenCurricularCourses.setController(curricularCoursesController);
		container.addChild(hiddenCurricularCourses);
		
		final HtmlMultipleHiddenField hiddenOptionalCurricularCourses = new HtmlMultipleHiddenField();
		hiddenOptionalCurricularCourses.bind(getInputContext().getMetaObject(), "optionalDismissals"); // slot refered by name
		hiddenOptionalCurricularCourses.setConverter(new SelectedOptionalCurricularCoursesKeyConverter());
		hiddenOptionalCurricularCourses.setController(optionalCurricularCoursesController);
		container.addChild(hiddenOptionalCurricularCourses);
		
		generateCurricularCourses(container, dismissalBean.getStudentCurricularPlan(), dismissalBean.getExecutionPeriod());
	    } else {
		radioButtonGroup = new HtmlRadioButtonGroup();
		radioButtonGroup.bind(getInputContext().getMetaObject(), "curriculumGroup"); // slot refered by name
		radioButtonGroup.setConverter(new DomainObjectKeyConverter());
		container.addChild(radioButtonGroup);
		generateNoCourseGroupCurriculumGroups(container, dismissalBean.getStudentCurricularPlan());
	    }

	    return container;
	}
	
	private void generateNoCourseGroupCurriculumGroups(HtmlBlockContainer container,
		StudentCurricularPlan studentCurricularPlan) {
	    for (NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup : studentCurricularPlan.getNoCourseGroupCurriculumGroups()) {
		generateNoCourseGroupCurriculumGroup(container, studentCurricularPlan, noCourseGroupCurriculumGroup, 0);
	    }
	    
	}

	private void generateNoCourseGroupCurriculumGroup(HtmlBlockContainer container,
		StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    container.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");
	    
	    final HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    
	    final HtmlTableCell nameCell = htmlTableRow.createCell();
	    nameCell.setBody(new HtmlText(curriculumGroup.getFullPath()));
	    nameCell.setClasses(getGroupNameClasses());
	    
	    
	    final HtmlTableCell radioButtonCell = htmlTableRow.createCell();
	    final HtmlRadioButton radioButton = radioButtonGroup.createRadioButton();
	    radioButton.setUserValue(MetaObjectFactory.createObject(curriculumGroup, new Schema(CurriculumGroup.class)).getKey().toString());
	    radioButton.setChecked(curriculumGroup == dismissalBean.getCurriculumGroup());
	    radioButtonCell.setBody(radioButton);
	    radioButtonCell.setClasses(getGroupRadioClasses());
	    radioButtonCell.setStyle("width: 2em;");
	    
	}

	private void generateCourseGroupCycles(final HtmlBlockContainer blockContainer,
		final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod) {
	    if (studentCurricularPlan.isBolonhaDegree()) {
		for (final CycleType cycleType : studentCurricularPlan.getDegreeType().getSupportedCyclesToEnrol()) {
		    final CourseGroup courseGroup = getCourseGroupWithCycleType(studentCurricularPlan, cycleType);
		    if (courseGroup != null) {
			generateCourseGroups(blockContainer, studentCurricularPlan, courseGroup, executionPeriod, 0);
		    }
		}
	    } else {
		generateCourseGroups(blockContainer, studentCurricularPlan, studentCurricularPlan.getRoot().getDegreeModule(), executionPeriod, 0);
	    }
	}
	
	private CourseGroup getCourseGroupWithCycleType(final StudentCurricularPlan studentCurricularPlan, final CycleType cycleType) {
	    final CycleCurriculumGroup curriculumGroup = studentCurricularPlan.getCycle(cycleType);
	    return curriculumGroup != null ? curriculumGroup.getDegreeModule() : studentCurricularPlan.getDegreeCurricularPlan().getCycleCourseGroup(cycleType);
	}

	private void generateCurricularCourses(final HtmlBlockContainer blockContainer, final StudentCurricularPlan studentCurricularPlan, final ExecutionPeriod executionPeriod) {
	    final HtmlTable groupTable = new HtmlTable();
	    blockContainer.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + getInitialWidth() + "em; margin-left: 0em;");
	    
	    final List<CurricularCourse> orderedCurricularCourses = new ArrayList<CurricularCourse>(studentCurricularPlan.getAllCurricularCoursesToDismissal(executionPeriod));
	    Collections.sort(orderedCurricularCourses, new BeanComparator("name", Collator.getInstance()));
	    
	    for (final CurricularCourse curricularCourse : orderedCurricularCourses) {
		final HtmlTableRow htmlTableRow = groupTable.createRow();
		htmlTableRow.setClasses(getCurricularCourseRowClasses());
		
		final HtmlTableCell nameCell = htmlTableRow.createCell(); 
    	    	
		final String code = curricularCourse.getCode();
		final String oneFullName = curricularCourse.getOneFullName();
		final String name = " <span class='bold'>" + curricularCourse.getName()  + "</span> (" + oneFullName.substring(0, oneFullName.lastIndexOf(">")) + ")"; 
		final String codeAndname = StringUtils.isEmpty(code) ? name : code + " - " + name;
		nameCell.setBody(new HtmlText(codeAndname, false));
    	    	
		nameCell.setClasses(getCurricularCourseNameClasses());
    	    
    	    	final HtmlTableCell checkBoxCell = htmlTableRow.createCell();
    	    	checkBoxCell.setClasses(getCurricularCourseCheckBoxClasses());
    	    	
    	    	final HtmlCheckBox checkBox = new HtmlCheckBox(dismissalBean.containsDismissalOrOptionalDismissal(curricularCourse));
    	    	checkBox.setName("curricularCourseCheckBox" + curricularCourse.getIdInternal());
    	    	if (curricularCourse.isOptionalCurricularCourse()) {
    	    	    final OptionalCurricularCourse optionalCurricularCourse = (OptionalCurricularCourse) curricularCourse;
    	    	    checkBox.setUserValue(new DismissalBean.SelectedOptionalCurricularCourse(optionalCurricularCourse, studentCurricularPlan).getKey());
    	    	    checkBoxCell.setBody(checkBox);
    	    	    optionalCurricularCoursesController.addCheckBox(checkBox);
    	    	} else {
    	    	    checkBox.setUserValue(new DismissalBean.SelectedCurricularCourse(curricularCourse, studentCurricularPlan).getKey());
    	    	    checkBoxCell.setBody(checkBox);
    	    	    curricularCoursesController.addCheckBox(checkBox);
    	    	}
	    }
	}

	private void generateCourseGroups(final HtmlBlockContainer blockContainer,
		final StudentCurricularPlan studentCurricularPlan, final CourseGroup courseGroup,
		final ExecutionPeriod executionPeriod, int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    blockContainer.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

	    final HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());

	    final HtmlTableCell nameCell = htmlTableRow.createCell();
	    nameCell.setBody(new HtmlText(courseGroup.getName()));
	    nameCell.setClasses(getGroupNameClasses());

	    final HtmlTableCell currentCreditsCell = htmlTableRow.createCell();
	    final double ectsCreditsForCourseGroup = studentCurricularPlan.getEctsCreditsForCourseGroup(courseGroup)
		    .doubleValue();
	    if (ectsCreditsForCourseGroup == 0d) {
		currentCreditsCell.setBody(new HtmlText("ECTS:  -"));
	    } else {
		currentCreditsCell.setBody(new HtmlText("ECTS: " + ectsCreditsForCourseGroup));
	    }
	    currentCreditsCell.setClasses("smalltxt");
	    currentCreditsCell.setStyle("width: 6em;");

	    final HtmlTableCell creditsMinCell = htmlTableRow.createCell();
	    creditsMinCell.setBody(new HtmlText("Min: "
		    + courseGroup.getMinEctsCredits(ExecutionPeriod.readActualExecutionPeriod())));
	    creditsMinCell.setClasses("smalltxt");
	    creditsMinCell.setStyle("width: 6em;");

	    final HtmlTableCell creditsMaxCell = htmlTableRow.createCell();
	    creditsMaxCell.setBody(new HtmlText("Max: "
		    + courseGroup.getMaxEctsCredits(ExecutionPeriod.readActualExecutionPeriod())));
	    creditsMaxCell.setClasses("smalltxt");
	    creditsMaxCell.setStyle("width: 6em;");

	    final HtmlTableCell radioButtonCell = htmlTableRow.createCell();
	    final HtmlRadioButton radioButton = radioButtonGroup.createRadioButton();
	    radioButton.setUserValue(MetaObjectFactory.createObject(courseGroup, new Schema(CourseGroup.class)).getKey()
		    .toString());
	    radioButton.setChecked(courseGroup == dismissalBean.getCourseGroup());
	    radioButtonCell.setBody(radioButton);
	    radioButtonCell.setClasses(getGroupRadioClasses());
	    radioButtonCell.setStyle("width: 2em;");

	    for (final Context context : courseGroup.getSortedOpenChildContextsWithCourseGroups(executionPeriod)) {
		generateCourseGroups(blockContainer, studentCurricularPlan, (CourseGroup) context.getChildDegreeModule(), executionPeriod, depth
			+ getWidthDecreasePerLevel());
	    }
	}
    }
    
    private static class SelectedCurricularCoursesKeyConverter extends Converter {

	@Override
	public Object convert(Class type, Object value) {
	    
	    if (value == null) {
		return null;
	    }
	    
	    final DomainObjectKeyConverter converter = new DomainObjectKeyConverter();
	    final List<SelectedCurricularCourse> result = new ArrayList<SelectedCurricularCourse>();

	    final String[] values = (String[]) value;
	    for (int i = 0; i < values.length; i++) {
		String key = values[i];

		String[] parts = key.split(",");
		if (parts.length < 3) {
		    throw new ConversionException("invalid key format: " + key);
		}

		final CurricularCourse curricularCourse = (CurricularCourse) converter.convert(type, parts[0]);
		final CurriculumGroup curriculumGroup = (CurriculumGroup) converter.convert(type, parts[1]);
		final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) converter.convert(type, parts[2]);
		
		final SelectedCurricularCourse selectedCurricularCourse = new SelectedCurricularCourse(curricularCourse, studentCurricularPlan);
		selectedCurricularCourse.setCurriculumGroup(curriculumGroup);
		result.add(selectedCurricularCourse);
	    }
	    return result;
	}
    }
    
    private static class SelectedOptionalCurricularCoursesKeyConverter extends Converter {

	@Override
	public Object convert(Class type, Object value) {
	    
	    if (value == null) {
		return null;
	    }
	    
	    final DomainObjectKeyConverter converter = new DomainObjectKeyConverter();
	    final List<SelectedOptionalCurricularCourse> result = new ArrayList<SelectedOptionalCurricularCourse>();

	    final String[] values = (String[]) value;
	    for (int i = 0; i < values.length; i++) {
		String key = values[i];

		String[] parts = key.split(",");
		if (parts.length < 3) {
		    throw new ConversionException("invalid key format: " + key);
		}
		
		final OptionalCurricularCourse curricularCourse = (OptionalCurricularCourse) converter.convert(type, parts[0]);
		final CurriculumGroup curriculumGroup = (CurriculumGroup) converter.convert(type, parts[1]);
		final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) converter.convert(type, parts[2]);
		
		final SelectedOptionalCurricularCourse selectedCurricularCourse = new SelectedOptionalCurricularCourse(curricularCourse, studentCurricularPlan);
		selectedCurricularCourse.setCurriculumGroup(curriculumGroup);
		result.add(selectedCurricularCourse);
	    }
	    return result;
	}
    }
}
