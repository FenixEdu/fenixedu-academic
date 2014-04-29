package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiry;
import net.sourceforge.fenixedu.domain.alumni.CerimonyInquiryPerson;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("cerimonyInquiry")
public class PersistentCerimonyInquiryGroup extends PersistentCerimonyInquiryGroup_Base {
    protected PersistentCerimonyInquiryGroup(CerimonyInquiry cerimonyInquiry) {
        super();
        setCerimonyInquiry(cerimonyInquiry);
    }

    @CustomGroupArgument
    public static Argument<CerimonyInquiry> executionCourseArgument() {
        return new SimpleArgument<CerimonyInquiry, PersistentCerimonyInquiryGroup>() {
            @Override
            public CerimonyInquiry parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<CerimonyInquiry> getDomainObject(argument);
            }

            @Override
            public Class<? extends CerimonyInquiry> getType() {
                return CerimonyInquiry.class;
            }

            @Override
            public String extract(PersistentCerimonyInquiryGroup group) {
                return group.getCerimonyInquiry() != null ? group.getCerimonyInquiry().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getCerimonyInquiry().getDescription() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<User>();
        for (final CerimonyInquiryPerson cerimonyInquiryPerson : getCerimonyInquiry().getCerimonyInquiryPersonSet()) {
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
            if (cerimonyInquiryPerson.getCerimonyInquiry().equals(getCerimonyInquiry())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    @Override
    protected void gc() {
        setCerimonyInquiry(null);
        super.gc();
    }

    public static PersistentCerimonyInquiryGroup getInstance(CerimonyInquiry cerimonyInquiry) {
        PersistentCerimonyInquiryGroup instance = cerimonyInquiry.getGroup();
        return instance != null ? instance : create(cerimonyInquiry);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentCerimonyInquiryGroup create(CerimonyInquiry cerimonyInquiry) {
        PersistentCerimonyInquiryGroup instance = cerimonyInquiry.getGroup();
        return instance != null ? instance : new PersistentCerimonyInquiryGroup(cerimonyInquiry);
    }

}
