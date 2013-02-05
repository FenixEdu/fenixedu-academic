package net.sourceforge.fenixedu.domain.accounting;

import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum PaymentCodeType {

    TOTAL_GRATUITY(0),

    GRATUITY_FIRST_INSTALLMENT(1),

    GRATUITY_SECOND_INSTALLMENT(2),

    ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE(3),

    INSURANCE(4),

    PRE_BOLONHA_MASTER_DEGREE_TOTAL_GRATUITY(5),

    PRE_BOLONHA_MASTER_DEGREE_INSURANCE(6),

    RESIDENCE_FEE(7),

    INSTITUTION_ACCOUNT_CREDIT(8, true),

    INTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS(15),

    EXTERNAL_DEGREE_CHANGE_INDIVIDUAL_CANDIDACY_PROCESS(16),

    INTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS(17),

    EXTERNAL_DEGREE_TRANSFER_INDIVIDUAL_CANDIDACY_PROCESS(18),

    SECOND_CYCLE_INDIVIDUAL_CANDIDACY_PROCESS(19),

    INTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS(20),

    EXTERNAL_DEGREE_CANDIDACY_FOR_GRADUATED_PERSON_INDIVIDUAL_PROCESS(21),

    OVER_23_INDIVIDUAL_CANDIDACY_PROCESS(22),

    PHD_PROGRAM_CANDIDACY_PROCESS(23),

    RECTORATE(99);

    private int typeDigit;

    private boolean reusable;

    private PaymentCodeType(int typeDigit) {
        this(typeDigit, false);
    }

    private PaymentCodeType(int typeDigit, boolean reusable) {
        this.typeDigit = typeDigit;
        this.reusable = reusable;
    }

    public String getName() {
        return name();
    }

    public String getQualifiedName() {
        return PaymentCodeType.class.getSimpleName() + "." + name();
    }

    public String getFullyQualifiedName() {
        return PaymentCodeType.class.getName() + "." + name();
    }

    public int getTypeDigit() {
        return typeDigit;
    }

    public boolean isReusable() {
        return reusable;
    }

    public String localizedName(Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    protected String localizedName() {
        return localizedName(Language.getLocale());
    }

    public String getLocalizedName() {
        return localizedName();
    }

}
