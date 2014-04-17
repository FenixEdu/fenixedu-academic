package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import java.util.Locale;

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
        return ResourceBundle.getBundle("resources.GEPResources", I18N.getLocale()).getString(
                "label.ects.table.nullPrintFormat");
    }

    @Override
    public String toString() {
        return "";
    }

}
