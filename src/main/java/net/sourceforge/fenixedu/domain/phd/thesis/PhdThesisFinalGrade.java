package net.sourceforge.fenixedu.domain.phd.thesis;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import pt.ist.fenixWebFramework.rendererExtensions.util.IPresentableEnum;
import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PhdThesisFinalGrade implements IPresentableEnum {

    NOT_APPROVED(true, false),

    APPROVED(true, true),

    APPROVED_WITH_PLUS(true, true),

    APPROVED_WITH_PLUS_PLUS(true, true),

    PRE_BOLONHA_NOT_APPROVED(true, false),

    PRE_BOLONHA_APPROVED(false, true),

    PRE_BOLONHA_APPROVED_WITH_PLUS(false, true),

    PRE_BOLONHA_APPROVED_WITH_PLUS_PLUS(false, true);

    private boolean forBolonha;

    private boolean approved;

    private PhdThesisFinalGrade(boolean forBolonha, boolean approved) {
        this.forBolonha = forBolonha;
        this.approved = approved;
    }

    public String getName() {
        return name();
    }

    @Override
    public String getLocalizedName() {
        return getLocalizedName(Language.getLocale());
    }

    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public boolean isForBolonha() {
        return forBolonha;
    }

    public boolean isApproved() {
        return approved;
    }

    static public List<PhdThesisFinalGrade> getGradesForBolonha() {
        ArrayList<PhdThesisFinalGrade> values = new ArrayList<PhdThesisFinalGrade>();
        for (PhdThesisFinalGrade grade : PhdThesisFinalGrade.values()) {
            if (grade.isForBolonha()) {
                values.add(grade);
            }
        }

        return values;
    }

    static public List<PhdThesisFinalGrade> getGradesForPreBolonha() {
        ArrayList<PhdThesisFinalGrade> values = new ArrayList<PhdThesisFinalGrade>();
        for (PhdThesisFinalGrade grade : PhdThesisFinalGrade.values()) {
            if (!grade.isForBolonha()) {
                values.add(grade);
            }
        }

        return values;
    }

}
