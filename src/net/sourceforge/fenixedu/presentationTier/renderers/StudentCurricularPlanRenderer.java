package net.sourceforge.fenixedu.presentationTier.renderers;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlDiv;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlTableRow;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell.CellType;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.MultiLanguageString;

public class StudentCurricularPlanRenderer extends OutputRenderer {

    private Boolean organizedByGroups;

    public StudentCurricularPlanRenderer() {
	super();
    }

    public Boolean isOrganizedByGroups() {
	return organizedByGroups;
    }

    /**
     * @property
     */
    public void setOrganizedByGroups(Boolean organizedByGroups) {
	this.organizedByGroups = organizedByGroups;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
	return new StudentCurricularPlanLayout();
    }

    private class StudentCurricularPlanLayout extends Layout {

	@Override
	public HtmlComponent createComponent(Object object, Class type) {
	    final StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) object;

	    final HtmlDiv scpDiv = new HtmlDiv();

	    if (studentCurricularPlan == null) {
		return new HtmlText();
	    } else if (isOrganizedByGroups() == null) {
		setOrganizedByGroups(studentCurricularPlan.isBolonha() ? Boolean.TRUE : Boolean.FALSE);
	    }

	    if (isOrganizedByGroups()) {
		final CurriculumGroup scpRoot = studentCurricularPlan.getRoot();
		final HtmlTable scpRootTable = scpDiv.createTable();
		scpRootTable.setClasses("showinfo3 mvert0");
		scpRootTable.setStyle("width: 70em;");

		final HtmlTableRow line = scpRootTable.createRow();
		line.setClasses("bgcolor2");

		final DegreeModule degreeRoot = scpRoot.getDegreeModule();
		final HtmlTableCell name = line.createCell();
		name.setType(CellType.HEADER);
		name.setClasses("aleft");
		name.setColspan(5);
		name.setBody(renderMultiLanguage(degreeRoot.getName(), degreeRoot.getNameEn()));

		generateModules(scpDiv, scpRoot, 0);
	    } else {
		return new HtmlText("Querias, mas ainda não há!");
	    }

	    return scpDiv;
	}

	private void generateModules(HtmlDiv scpDiv, final CurriculumGroup group, int depth) {
	    final HtmlDiv groupDiv = scpDiv.createDiv();
	    groupDiv.setStyle("padding-left: " + (depth + 3) + "em;");

	    final HtmlTable groupTable = groupDiv.createTable();
	    groupTable.setClasses("showinfo3 mvert0");
	    groupTable.setStyle("width: " + (70 - depth - 3) + "em;");

	    for (final CurriculumModule module : group.getCurriculumModules()) {
		final DegreeModule degreeModule = module.getDegreeModule();

		final HtmlTableRow line = groupTable.createRow();

		final HtmlTableCell name = line.createCell();
		name.setBody(renderMultiLanguage(degreeModule.getName(), degreeModule.getNameEn()));

		if (module.isLeaf()) {
		    final CurriculumLine curriculumLine = (CurriculumLine) module;

		    if (curriculumLine.isEnrolment()) {
			final ResourceBundle enumerationResources = ResourceBundle.getBundle(
				    "resources.EnumerationResources", LanguageUtils.getLocale());
			
			final Enrolment enrolment = (Enrolment) curriculumLine;

			// Year
			final HtmlTableCell yearCell = line.createCell();
			yearCell.setClasses("smalltxt");
			yearCell.setAlign("rigth");
			
			final StringBuilder year = new StringBuilder();
			year.append(enrolment.getExecutionPeriod().getExecutionYear().getYear());
			yearCell.setBody(new HtmlText(year.toString()));
			
			// Semester
			final HtmlTableCell semesterCell = line.createCell();
			semesterCell.setClasses("smalltxt");
			semesterCell.setAlign("rigth");

			final StringBuilder semester = new StringBuilder();
			semester.append(enrolment.getExecutionPeriod().getSemester().toString());
			semester.append(" ");
			semester.append(enumerationResources.getString("SEMESTER.ABBREVIATION"));
			semesterCell.setBody(new HtmlText(semester.toString()));

			// Enrolment
			final HtmlTableCell enrolmentCell = line.createCell();
			enrolmentCell.setAlign("rigth");
			
			if (enrolment.isApproved()) {
			    final String grade = enrolment.getLatestEnrolmentEvaluation().getGrade();
			    enrolmentCell.setBody(new HtmlText(grade));
			} else {
			    final String enrolmentState = enumerationResources.getString(enrolment.getEnrollmentState().toString()); 
			    enrolmentCell.setBody(new HtmlText(enrolmentState));
			}
		    }
		} else {
		    line.setClasses("bgcolor2");
		    name.setClasses("aleft");
		    name.setColspan(5);
		    name.setType(CellType.HEADER);

		    generateModules(scpDiv, (CurriculumGroup) module, depth + 3);
		}
	    }
	}

	private HtmlComponent renderMultiLanguage(String portuguese, String english) {
	    return renderValue(createSimpleMultiLanguageString(portuguese, english),
		    MultiLanguageString.class, null, null);
	}

	private MultiLanguageString createSimpleMultiLanguageString(String portuguese, String english) {
	    MultiLanguageString mls = new MultiLanguageString();

	    mls.setContent(Language.pt, portuguese);
	    mls.setContent(Language.en, english);

	    return mls;
	}
    }

}
