/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.candidacy.util;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.period.GenericApplicationPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.security.Authenticate;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class GenericApplicationPeriodBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private MultiLanguageString title = new MultiLanguageString(MultiLanguageString.en, "");
    private MultiLanguageString description = new MultiLanguageString(MultiLanguageString.en, "");
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
