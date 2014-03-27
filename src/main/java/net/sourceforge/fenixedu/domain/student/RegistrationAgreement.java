package net.sourceforge.fenixedu.domain.student;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public enum RegistrationAgreement {

    NORMAL(true, true, true),

    AFA(false, false, false),

    MA(false, false, false),

    NC(false, false, false),

    ERASMUS(false, false, true),

    SOCRATES(false, false, true),

    SOCRATES_ERASMUS(false, false, true),

    TEMPUS(false, false, true),

    BILATERAL_AGREEMENT(false, false, true),

    ALFA2(false, false, true),

    UNIFOR(false, false, true),

    TIME(false, false, true),

    TOTAL(false, true, true),

    OTHER_EXTERNAL(false, false, true),

    MITP(false, true, true),

    SMILE(false, false, true),

    ANGOLA_TELECOM(false, true, true),

    ERASMUS_MUNDUS(false, false, true),

    ALMEIDA_GARRETT(false, false, true),

    INOV_IST(false, false, true),

    TECMIC(false, false, true),
    
    IST_UCP(false, false, true),
    
    IST_USP(false, false, true),
    
    CLUSTER(false, false, true),
    
    EUSYSBIO(false, false, true),

    IST_ISA(false, false, true),
    
    IST_PHARMACY_FACULTY(false, false, true),

    IBERO_SANTANDER(false, false, true),
    
    BRAZIL_SANTANDER(false, false, true),

    CHINA_AGREEMENTS(false, false, true),
    
    RUSSIA_AGREEMENTS(false, false, true),

    AFRICA_AGREEMENTS(false, false, true),
    
    BRAZIL_AGREEMENTS(false, false, true),

    SCIENCE_WITHOUT_BORDERS(false, false, true),
    
    USA_AGREEMENTS(false, true, true),
    
    KIC_INNOENERGY(false, false, true);

    private boolean enrolmentByStudentAllowed;

    private boolean payGratuity;

    private boolean allowsIDCard;

    private RegistrationAgreement(final boolean enrolmentByStudentAllowed, final boolean payGratuity, final boolean allowsIDCard) {
        this.enrolmentByStudentAllowed = enrolmentByStudentAllowed;
        this.payGratuity = payGratuity;
        this.allowsIDCard = allowsIDCard;
    }

    public boolean isNormal() {
        return this.equals(RegistrationAgreement.NORMAL);
    }

    public boolean isMilitaryAgreement() {
        return this.equals(RegistrationAgreement.AFA) || this.equals(RegistrationAgreement.MA);
    }

    public String getName() {
        return name();
    }

    public boolean isEnrolmentByStudentAllowed() {
        return this.enrolmentByStudentAllowed;
    }

    public boolean isToPayGratuity() {
        return payGratuity;
    }

    public boolean allowsIDCard() {
        return this.allowsIDCard;
    }

    public static RegistrationAgreement getByLegacyCode(int code) {

        switch (code) {
        case 1:
            return AFA;
        case 2:
            return MA;
        case 3:
            return NC;
        case 4:
            return ERASMUS;
        case 5:
            return SOCRATES;
        case 6:
            return SOCRATES_ERASMUS;
        case 7:
            return TEMPUS;
        case 8:
            return BILATERAL_AGREEMENT;
        case 9:
            return ALFA2;
        case 10:
            return UNIFOR;
        }

        return null;
    }

    public String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public String getDescription() {
        return getDescription(Language.getLocale());
    }

    public String getDescription(final Locale locale) {
        return ResourceBundle.getBundle("resources.EnumerationResources", locale).getString(getQualifiedName());
    }

    final public static List<RegistrationAgreement> EXEMPTED_AGREEMENTS = Arrays.asList(IST_UCP);

    final public static List<RegistrationAgreement> MOBILITY_AGREEMENTS = Arrays.asList(ERASMUS, SMILE, CLUSTER, TIME,
            BILATERAL_AGREEMENT, ERASMUS_MUNDUS, IBERO_SANTANDER, SCIENCE_WITHOUT_BORDERS, CHINA_AGREEMENTS, RUSSIA_AGREEMENTS,
            AFRICA_AGREEMENTS, BRAZIL_AGREEMENTS, ALMEIDA_GARRETT, ERASMUS_MUNDUS, USA_AGREEMENTS, KIC_INNOENERGY,
            BRAZIL_SANTANDER);

}
