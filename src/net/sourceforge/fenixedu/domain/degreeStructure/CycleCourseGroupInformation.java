package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.services.Service;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CycleCourseGroupInformation extends CycleCourseGroupInformation_Base {

	public static final Comparator<CycleCourseGroupInformation> COMPARATOR_BY_EXECUTION_YEAR =
			new Comparator<CycleCourseGroupInformation>() {

				@Override
				public int compare(CycleCourseGroupInformation arg0, CycleCourseGroupInformation arg1) {
					return arg0.getExecutionYear().isBefore(arg1.getExecutionYear()) ? 1 : -1;
				}

			};

	public CycleCourseGroupInformation() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public CycleCourseGroupInformation(final CycleCourseGroup cycleCourseGroup, final ExecutionYear executionYear,
			String graduatedTitle, String graduatedTitleEn) {
		this();

		setExecutionYear(executionYear);
		setCycleCourseGroup(cycleCourseGroup);
		MultiLanguageString graduatedTitleString = new MultiLanguageString();
		graduatedTitleString.setContent(Language.pt, graduatedTitle);
		graduatedTitleString.setContent(Language.en, graduatedTitleEn);

		setGraduatedTitle(graduatedTitleString);

		checkParameters();
	}

	private void checkParameters() {
		if (getExecutionYear() == null) {
			throw new DomainException("cycle.course.group.information.execution.year.cannot.be.empty");
		}

		if (getCycleCourseGroup() == null) {
			throw new DomainException("cycle.course.group.information.course.group.cannot.be.empty");
		}
	}

	public String getGraduatedTitlePt() {
		return getGraduatedTitle().getContent(Language.pt);
	}

	public String getGraduatedTitleEn() {
		return getGraduatedTitle().getContent(Language.en);
	}

	@Service
	public void edit(ExecutionYear editExecutionYear, String editGraduatedTitle, String editGraduatedTitleEn) {
		this.setExecutionYear(editExecutionYear);
		MultiLanguageString mls = this.getGraduatedTitle();
		mls.setContent(Language.pt, editGraduatedTitle);
		mls.setContent(Language.en, editGraduatedTitleEn);

		this.setGraduatedTitle(mls);

		checkParameters();
	}
}
