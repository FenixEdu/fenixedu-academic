package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.fenixedu.commons.i18n.I18N;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import java.util.Locale;

public enum PhdCandidacyFeedbackStateType implements PhdProcessStateType {

    NEW,

    WAITING_FOR_FEEDBACK,

    CONCLUDED;

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

    public static List<PhdCandidacyFeedbackStateType> getPossibleNextStates(PhdCandidacyFeedbackStateType type) {

        if (type == null) {
            return Collections.singletonList(NEW);
        }

        switch (type) {
        case NEW:
            return Collections.singletonList(WAITING_FOR_FEEDBACK);
        case WAITING_FOR_FEEDBACK:
            return Collections.singletonList(CONCLUDED);
        }

        return Collections.EMPTY_LIST;
    }
}
