package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.Language;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.renderers.OutputRenderer;
import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlTable;
import net.sourceforge.fenixedu.renderers.components.HtmlTableCell;
import net.sourceforge.fenixedu.renderers.components.HtmlText;
import net.sourceforge.fenixedu.renderers.layouts.Layout;
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
	    StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) object;

	    if (studentCurricularPlan == null) {
		return new HtmlText();
	    } else if (isOrganizedByGroups() == null) {
		setOrganizedByGroups(studentCurricularPlan.isBolonha() ? Boolean.TRUE : Boolean.FALSE);
	    }

	    HtmlTable table = new HtmlTable();

	    if (isOrganizedByGroups()) {
		generateModules(table, studentCurricularPlan.getRoot());
	    } else {
		return new HtmlText("Querias, mas ainda não há!");		
	    }
	    
	    return table;
	}

	private void generateModules(HtmlTable table, CurriculumGroup group) {
	    for (final CurriculumModule module : group.getCurriculumModules()) {
		final DegreeModule degreeModule = module.getDegreeModule();

		HtmlTableCell cell = table.createRow().createCell();
		cell.setBody(renderMultiLanguage(degreeModule.getName(), degreeModule.getNameEn()));

		if (module.isLeaf()) {
		    
		} else {
		    cell.setType(HtmlTableCell.CellType.HEADER);
		    generateModules(table, (CurriculumGroup) module);
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
