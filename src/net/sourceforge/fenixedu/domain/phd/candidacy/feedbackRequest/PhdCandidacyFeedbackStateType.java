package net.sourceforge.fenixedu.domain.phd.candidacy.feedbackRequest;

import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.phd.PhdProcessStateType;
import pt.utl.ist.fenix.tools.util.i18n.Language;

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
	return getLocalizedName(Language.getLocale());
    }

    @Override
    public String getLocalizedName(final Locale locale) {
	return ResourceBundle.getBundle("resources.PhdResources", locale).getString(getQualifiedName());
    }

    private String getQualifiedName() {
	return getClass().getSimpleName() + "." + name();
    }
}
