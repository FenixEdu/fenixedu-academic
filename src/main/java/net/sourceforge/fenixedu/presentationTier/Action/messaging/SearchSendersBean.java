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
