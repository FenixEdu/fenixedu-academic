package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.studentEnrolment.StudentEnrolmentBean;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.RootDomainObject;
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

public class StudentCurricularPlanEnrolmentsRenderer extends InputRenderer {

    public StudentCurricularPlanEnrolmentsRenderer() {
	super();
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentCurricularPlanEnrolmentLayout();
    }

    private class StudentCurricularPlanEnrolmentLayout extends Layout {

	private CopyCheckBoxValuesController enrollmentsController = new CopyCheckBoxValuesController();
	
	private CopyCheckBoxValuesController degreeModulesToEnrolController = new CopyCheckBoxValuesController(); 
	
	private List<CurriculumModule> initialCurriculumModules = new ArrayList<CurriculumModule>();
	
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
	    HtmlBlockContainer container = new HtmlBlockContainer();
	    blockContainer.addChild(container);
	    container.setStyle("padding-left: " + depth + "em;");
	    final HtmlTable groupTable = new HtmlTable();
	    container.addChild(groupTable);
	    groupTable.setClasses("showinfo3 mvert0");
	    groupTable.setStyle("width: " + (70 - depth)  + "em;");
	    
	    HtmlTableRow htmlTableRow = groupTable.createRow();
	    if(group.hasAnyCurriculumModules()) {
		htmlTableRow.createCell().setBody(new HtmlText(group.getDegreeModule().getName()));
		generateDegreeModulesEnroled(blockContainer, group, studentCurricularPlan, executionPeriod, depth);
	    } else {
		htmlTableRow.createCell().setBody(new HtmlText(group.getDegreeModule().getName()));
		HtmlTableCell checkBoxCell = htmlTableRow.createCell();
		checkBoxCell.setClasses("aright");
		
		HtmlCheckBox checkBox = new HtmlCheckBox(true);
		MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(group, new Schema(CurriculumGroup.class));
		checkBox.setName("enrolmentCheckBox" + group.getIdInternal());
		checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
		enrollmentsController.addCheckBox(checkBox);
		checkBoxCell.setBody(checkBox);
		initialCurriculumModules.add(group);
	    }
		
	    generateDegreeModulesToEnrol(blockContainer, group, studentCurricularPlan, executionPeriod, depth);
	}

	private void generateDegreeModulesToEnrol(HtmlBlockContainer blockContainer, CurriculumGroup group, StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod, int depth) {
	    HtmlBlockContainer container = new HtmlBlockContainer();
	    blockContainer.addChild(container);
	    container.setStyle("padding-left: " + (depth + 3) + "em;");
	    final HtmlTable groupTable = new HtmlTable();
	    container.addChild(groupTable);
	    groupTable.setClasses("showinfo3 mvert0");
	    groupTable.setStyle("width: " + (70 - depth - 3)  + "em;");

	    Collection<Context> degreeModulesToEnrol = group.getDegreeModulesToEnrol(executionPeriod);
	    for (Context context : degreeModulesToEnrol) {
		if(context.getChildDegreeModule().isLeaf()) {
		    CurricularCourse curricularCourse = (CurricularCourse) context.getChildDegreeModule();
		    if(!studentCurricularPlan.getRoot().isAproved(curricularCourse, executionPeriod) &&
			    !studentCurricularPlan.getRoot().isEnroledInExecutionPeriod(curricularCourse, executionPeriod)) {
			HtmlTableRow htmlTableRow = groupTable.createRow();
			htmlTableRow.createCell().setBody(new HtmlText(curricularCourse.getName()));
			HtmlTableCell checkBoxCell = htmlTableRow.createCell();
			checkBoxCell.setClasses("aright");
						
			DegreeModuleToEnrol degreeModuleToEnrol = new DegreeModuleToEnrol(group, context);
			HtmlCheckBox checkBox = new HtmlCheckBox(false);
			checkBox.setName("degreeModuleToEnrolCheckBox" + degreeModuleToEnrol.getContext().getIdInternal() + ":" + degreeModuleToEnrol.getCurriculumGroup().getIdInternal());
			checkBox.setUserValue(degreeModuleToEnrol.getKey());
			degreeModulesToEnrolController.addCheckBox(checkBox);
			checkBoxCell.setBody(checkBox);
		    }
		} else {
		    if(!studentCurricularPlan.getRoot().hasDegreModule(context.getChildDegreeModule())) {
			HtmlTableRow htmlTableRow = groupTable.createRow();
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
		}
	    }    
	}

	private void generateDegreeModulesEnroled(HtmlBlockContainer blockContainer, CurriculumGroup group, StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod, int depth) {
	    HtmlBlockContainer container = new HtmlBlockContainer();
	    blockContainer.addChild(container);
	    container.setStyle("padding-left: " + (depth + 3) + "em;");
	    final HtmlTable groupTable = new HtmlTable();
	    container.addChild(groupTable);
	    groupTable.setClasses("showinfo3 mvert0");
	    groupTable.setStyle("width: " + (70 - depth - 3)  + "em;");

	    for (CurriculumModule curriculumModule : group.getCurriculumModules()) {
		if(curriculumModule.isLeaf()) {
		    if(((CurriculumLine) curriculumModule).isEnrolment()) {
			Enrolment enrolment = (Enrolment) curriculumModule;
			if(enrolment.getExecutionPeriod().equals(executionPeriod) && enrolment.isEnroled()) {
			    HtmlTableRow htmlTableRow = groupTable.createRow();
			    HtmlTableCell cellName = htmlTableRow.createCell();
			    cellName.setBody(new HtmlText(enrolment.getCurricularCourse().getName()));
			    
			    MetaObject enrolmentMetaObject = MetaObjectFactory.createObject(enrolment, new Schema(Enrolment.class));
			    
			    HtmlCheckBox checkBox = new HtmlCheckBox(true);
			    checkBox.setName("enrolmentCheckBox" + enrolment.getIdInternal());
			    checkBox.setUserValue(enrolmentMetaObject.getKey().toString());
			    enrollmentsController.addCheckBox(checkBox);
			    
			    HtmlTableCell cellCheckBox = htmlTableRow.createCell();
			    cellCheckBox.setClasses("aright");
			    cellCheckBox.setBody(checkBox);
			    
			    initialCurriculumModules.add(enrolment);
			}
		    }
		} else {
		    CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
		    generateModules(blockContainer, studentCurricularPlan, curriculumGroup, executionPeriod, depth + 3);
		}
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
}
