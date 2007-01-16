package net.sourceforge.fenixedu.presentationTier.renderers;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.SelectedDismissal;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.DismissalType;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.InputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlBlockContainer;
import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleHiddenField;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleValueComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButton;
import net.sourceforge.fenixedu.renderers.components.HtmlRadioButtonGroup;
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

import org.apache.commons.beanutils.BeanComparator;

public class StudentDismissalRenderer extends InputRenderer {
    
    private static final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources.EnumerationResources");
    
    private final ResourceBundle academicAdminOfficeResources = ResourceBundle.getBundle("resources.AcademicAdminOffice");

    
    private Integer initialWidth = 50;

    private Integer widthDecreasePerLevel = 3;

    private String tablesClasses = "showinfo3 mvert0";

    private String groupRowClasses = "bgcolor2";
    
    private String curricularCourseRowClasses = "";
    
    private String groupCellClasses = "smalltxt, aright";
    
    private String curricularCourseCellClasses = "smalltxt, aright";
    
    
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



    public StudentDismissalRenderer() {
	super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentDismissalLayout();
    }

    private class StudentDismissalLayout extends Layout {

	private final CopyCheckBoxValuesController curricularCoursesController = new CopyCheckBoxValuesController();
	
	private HtmlRadioButtonGroup radioButtonGroup = null;
	
	private DismissalBean dismissalBean = null;

	
	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    dismissalBean = (DismissalBean) object;

	    HtmlBlockContainer container = new HtmlBlockContainer();
	    if (dismissalBean == null) {
		return new HtmlText();
	    }
	    
	    if(dismissalBean.getDismissalType() == DismissalType.CURRICULUM_GROUP_CREDITS) {
		radioButtonGroup = new HtmlRadioButtonGroup();
		radioButtonGroup.bind(getInputContext().getMetaObject(), "curriculumGroup"); // slot refered by name
		radioButtonGroup.setConverter(new DomainObjectKeyConverter());
		container.addChild(radioButtonGroup);
		generateCurriculumGroup(container, dismissalBean.getStudentCurricularPlan(), dismissalBean.getStudentCurricularPlan().getRoot(), 0);
		
	    } else {
		HtmlMultipleHiddenField hiddenCurricularCourses = new HtmlMultipleHiddenField();
		hiddenCurricularCourses.bind(getInputContext().getMetaObject(), "dismissals"); // slot refered by name
		hiddenCurricularCourses.setConverter(new SelectedDismissalsKeyConverter());
		hiddenCurricularCourses.setController(curricularCoursesController);
		container.addChild(hiddenCurricularCourses);
		generateCurricularCourses(container, dismissalBean.getStudentCurricularPlan(), dismissalBean.getStudentCurricularPlan().getRoot(), 0);
	    }

	    return container;
	}

	
	private void generateCurricularCourses(HtmlBlockContainer blockContainer, StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup, int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    blockContainer.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");
	    
	    HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    
	    HtmlTableCell nameCell = htmlTableRow.createCell();
	    nameCell.setBody(new HtmlText(curriculumGroup.getName().getContent()));
	    nameCell.setClasses(getGroupNameClasses());
	    
	    generateGroupCurricularCourses(blockContainer, curriculumGroup, curriculumGroup.getCurricularCoursesToDismissal(), depth + getWidthDecreasePerLevel());
	    
	    List<CurriculumGroup> ordered = new ArrayList<CurriculumGroup>(curriculumGroup.getCurriculumGroups());
	    Collections.sort(ordered, new BeanComparator("childOrder"));
	    
	    for (CurriculumGroup group : ordered) {
		generateCurricularCourses(blockContainer, studentCurricularPlan, group, depth + getWidthDecreasePerLevel());
	    }
	}


	private void generateGroupCurricularCourses(HtmlBlockContainer blockContainer, CurriculumGroup curriculumGroup, Collection<CurricularCourse> curricularCoursesToDismissal, int depth) {
	    if(!curricularCoursesToDismissal.isEmpty()) {
		final HtmlTable groupTable = new HtmlTable();
		blockContainer.addChild(groupTable);
		groupTable.setClasses(getTablesClasses());
		groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");

		List<CurricularCourse> ordered = new ArrayList<CurricularCourse>(curricularCoursesToDismissal);
		Collections.sort(ordered, new BeanComparator("name", Collator.getInstance()));
		
		for (CurricularCourse curricularCourse : ordered) {
		    HtmlTableRow htmlTableRow = groupTable.createRow();
		    htmlTableRow.setClasses(getCurricularCourseRowClasses());
		    
		    HtmlTableCell nameCell = htmlTableRow.createCell(); 
		    nameCell.setBody(new HtmlText(curricularCourse.getName()));
		    nameCell.setClasses(getCurricularCourseNameClasses());
		    
		    HtmlTableCell checkBoxCell = htmlTableRow.createCell();
		    checkBoxCell.setClasses(getCurricularCourseCheckBoxClasses());
		    
		    HtmlCheckBox checkBox = new HtmlCheckBox(dismissalBean.containsDismissal(curriculumGroup, curricularCourse));
		    checkBox.setName("curricularCourseCheckBox" + curriculumGroup.getIdInternal() + "|" + curricularCourse.getIdInternal());
		    checkBox.setUserValue(SelectedDismissal.getKey(curriculumGroup, curricularCourse));
		    checkBoxCell.setBody(checkBox);
		    curricularCoursesController.addCheckBox(checkBox);
		}
	    }
	}
	
	private void generateCurriculumGroup(final HtmlBlockContainer blockContainer, final StudentCurricularPlan studentCurricularPlan, final CurriculumGroup curriculumGroup, int depth) {
	    final HtmlTable groupTable = new HtmlTable();
	    blockContainer.addChild(groupTable);
	    groupTable.setClasses(getTablesClasses());
	    groupTable.setStyle("width: " + (getInitialWidth() - depth) + "em; margin-left: " + depth + "em;");
	    
	    HtmlTableRow htmlTableRow = groupTable.createRow();
	    htmlTableRow.setClasses(getGroupRowClasses());
	    
	    HtmlTableCell nameCell = htmlTableRow.createCell();
	    nameCell.setBody(new HtmlText(curriculumGroup.getName().getContent()));
	    nameCell.setClasses(getGroupNameClasses());
	    
	    HtmlTableCell radioButtonCell = htmlTableRow.createCell();
	    HtmlRadioButton radioButton = radioButtonGroup.createRadioButton();
	    MetaObject curriculumGroupMetaObject = MetaObjectFactory.createObject(curriculumGroup, new Schema(CurriculumGroup.class));
	    radioButton.setUserValue(curriculumGroupMetaObject.getKey().toString());
	    radioButton.setChecked(curriculumGroup == dismissalBean.getCurriculumGroup());
	    radioButtonCell.setBody(radioButton);
	    radioButtonCell.setClasses(getGroupRadioClasses());
	    
	    List<CurriculumGroup> ordered = new ArrayList<CurriculumGroup>(curriculumGroup.getCurriculumGroups());
	    Collections.sort(ordered, new BeanComparator("childOrder"));
	    
	    for (CurriculumGroup group : ordered) {
		generateCurriculumGroup(blockContainer, studentCurricularPlan, group, depth + getWidthDecreasePerLevel());
	    }
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

    
    public static class SelectedDismissalsKeyConverter extends Converter {

	@Override
	public Object convert(Class type, Object value) {
	    DomainObjectKeyConverter converter = new DomainObjectKeyConverter();
	    List<SelectedDismissal> result = new ArrayList<SelectedDismissal>();

	    if (value == null) {
		return null;
	    }

	    String[] values = (String[]) value;
	    for (int i = 0; i < values.length; i++) {
		String key = values[i];

		String[] parts = key.split(",");
		if (parts.length != 2) {
		    throw new ConversionException("invalid key format: " + key);
		}

		CurriculumGroup curriculumGroup = (CurriculumGroup) converter.convert(type, parts[0]);
		CurricularCourse curricularCourse = (CurricularCourse) converter.convert(type, parts[1]);
		
		SelectedDismissal selectedCurricularCourse = new SelectedDismissal(curriculumGroup, curricularCourse);
		
		result.add(selectedCurricularCourse);
	    }
	    
	    return result;
	}
    }    
}
