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
package net.sourceforge.fenixedu.presentationTier.Action.messaging;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.util.email.Sender;

import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;

public class SearchSendersBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private String searchString;
    private String[] words;

    public SearchSendersBean() {
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
        if (searchString == null || searchString.trim().isEmpty()) {
            words = null;
        } else {
            words = StringNormalizer.normalize(searchString).trim().split(" ");
        }
    }

    public Set<Sender> getResult() {
        final Set<Sender> result = new TreeSet<Sender>(Sender.COMPARATOR_BY_FROM_NAME);
        if (searchString != null && !searchString.trim().isEmpty()) {
            for (final Sender sender : Bennu.getInstance().getUtilEmailSendersSet()) {
                if (match(sender.getFromName())) {
                    result.add(sender);
                }
            }
        }
        return result;
    }

    private boolean match(final String fromName) {
        final String n = StringNormalizer.normalize(fromName);
        for (final String word : words) {
            if (n.indexOf(word) >= 0) {
                return true;
            }
        }
        return false;
    }

}
