package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.GradeScale;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class NullEctsComparabilityTable extends EctsComparabilityTable {
	private static final long serialVersionUID = 117805162304348863L;

	NullEctsComparabilityTable() {
		super(new char[0]);
	}

	@Override
	public String convert(int grade) {
		return GradeScale.NA;
	}

	@Override
	public String getPrintableFormat() {
		return ResourceBundle.getBundle("resources.GEPResources", Language.getLocale()).getString(
				"label.ects.table.nullPrintFormat");
	}

	@Override
	public String toString() {
		return "";
	}
}
