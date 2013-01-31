package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class NullEctsComparabilityPercentages extends EctsComparabilityPercentages {
	private static final long serialVersionUID = -929489243618382282L;

	public NullEctsComparabilityPercentages() {
		super(new double[0]);
	}

	@Override
	public double getPercentage(int grade) {
		return -1;
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
