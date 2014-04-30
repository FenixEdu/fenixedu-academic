package net.sourceforge.fenixedu.domain.degreeStructure;

import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.GradeScale;
import java.util.Locale;

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
        return ResourceBundle.getBundle("resources.GEPResources", I18N.getLocale()).getString(
                "label.ects.table.nullPrintFormat");
    }

    @Override
    public String toString() {
        return "";
    }
}
