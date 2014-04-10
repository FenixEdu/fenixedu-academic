package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry;
import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("cerimonyInquiry")
public class CerimonyInquiryGroup extends FenixGroup {
    private static final long serialVersionUID = 228948654158148141L;

    @GroupArgument
    private CerimonyInquiry cerimonyInquiry;

    private CerimonyInquiryGroup() {
        super();
    }

    private CerimonyInquiryGroup(CerimonyInquiry cerimonyInquiry) {
        this();
        this.cerimonyInquiry = cerimonyInquiry;
    }

    public static CerimonyInquiryGroup get(CerimonyInquiry cerimonyInquiry) {
        return new CerimonyInquiryGroup(cerimonyInquiry);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { cerimonyInquiry.getDescription() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<User>();
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : cerimonyInquiry.getCerimonyInquiryPersonSet()) {
            User user = cerimonyInquiryPerson.getPerson().getUser();
            if (user != null) {
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        return getMembers();
    }

    @Override
    public boolean isMember(User user) {
        if (user == null) {
            return false;
        }
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : user.getPerson().getCerimonyInquiryPersonSet()) {
            if (cerimonyInquiryPerson.getCerimonyInquiry().equals(cerimonyInquiry)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentCerimonyInquiryGroup.getInstance(cerimonyInquiry);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CerimonyInquiryGroup) {
            return Objects.equal(cerimonyInquiry, ((CerimonyInquiryGroup) object).cerimonyInquiry);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(cerimonyInquiry);
    }
}
