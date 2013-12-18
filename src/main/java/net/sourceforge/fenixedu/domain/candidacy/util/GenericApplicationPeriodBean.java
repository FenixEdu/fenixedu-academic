package net.sourceforge.fenixedu.domain.candidacy.util;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class GenericApplicationPeriodBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultiLanguageString title = new MultiLanguageString(Language.en, "");
    private MultiLanguageString description = new MultiLanguageString(Language.en, "");
    private DateTime start;
    private DateTime end;

    public GenericApplicationPeriodBean() {
    }

    public MultiLanguageString getTitle() {
        return title;
    }

    public void setTitle(MultiLanguageString title) {
        this.title = title;
    }

    public MultiLanguageString getDescription() {
        return description;
    }

    public void setDescription(MultiLanguageString description) {
        this.description = description;
    }

    public DateTime getStart() {
        return start;
    }

    public void setStart(DateTime start) {
        this.start = start;
    }

    public DateTime getEnd() {
        return end;
    }

    public void setEnd(DateTime end) {
        this.end = end;
    }

    @Atomic
    public void createNewPeriod() {
        final User userView = Authenticate.getUser();
        if (userView != null && userView.getPerson().hasRole(RoleType.MANAGER)) {
            if (title != null && title.hasContent() && start != null && end != null) {
                new GenericApplicationPeriod(title, description, start, end);
            }
        }
    }

}
