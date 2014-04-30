package net.sourceforge.fenixedu.domain.phd.seminar;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import java.util.Locale;

public enum PublicPresentationSeminarProcessStateType implements PhdProcessStateType {

    WAITING_FOR_COMMISSION_CONSTITUTION,

    COMMISSION_WAITING_FOR_VALIDATION,

    COMMISSION_VALIDATED,

    PUBLIC_PRESENTATION_DATE_SCHEDULED,

    REPORT_WAITING_FOR_VALIDATION,

    REPORT_VALIDATED,

    EXEMPTED;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getLocalizedName() {
        return getLocalizedName(I18N.getLocale());
    }

    @Override
    public String getLocalizedName(final Locale locale) {
        return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    private String getQualifiedName() {
        return getClass().getSimpleName() + "." + name();
    }

    public static List<PublicPresentationSeminarProcessStateType> getPossibleNextStates(PublicPresentationSeminarProcess process) {
        PublicPresentationSeminarProcessStateType activeState = process.getActiveState();

        return getPossibleNextStates(activeState);
    }

    public static List<PublicPresentationSeminarProcessStateType> getPossibleNextStates(
            final PublicPresentationSeminarProcessStateType type) {

        if (type == null) {
            return Collections.singletonList(WAITING_FOR_COMMISSION_CONSTITUTION);
        }

        switch (type) {
        case WAITING_FOR_COMMISSION_CONSTITUTION:
            return Arrays.asList(new PublicPresentationSeminarProcessStateType[] { COMMISSION_WAITING_FOR_VALIDATION, EXEMPTED });
        case COMMISSION_WAITING_FOR_VALIDATION:
            return Arrays.asList(new PublicPresentationSeminarProcessStateType[] { COMMISSION_VALIDATED,
                    WAITING_FOR_COMMISSION_CONSTITUTION });
        case COMMISSION_VALIDATED:
            return Collections.singletonList(PUBLIC_PRESENTATION_DATE_SCHEDULED);
        case PUBLIC_PRESENTATION_DATE_SCHEDULED:
            return Collections.singletonList(REPORT_WAITING_FOR_VALIDATION);
        case REPORT_WAITING_FOR_VALIDATION:
            return Collections.singletonList(REPORT_VALIDATED);
        case EXEMPTED:
            return Collections.emptyList();
        }

        return Collections.emptyList();
    }

}
