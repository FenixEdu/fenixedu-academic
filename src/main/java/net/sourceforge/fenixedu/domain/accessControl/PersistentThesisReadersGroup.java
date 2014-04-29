package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.thesis.Thesis;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.AnyoneGroup;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("thesisReaders")
public class PersistentThesisReadersGroup extends PersistentThesisReadersGroup_Base {
    protected PersistentThesisReadersGroup(Thesis thesis) {
        super();
        setThesis(thesis);
    }

    @CustomGroupArgument
    public static Argument<Thesis> thesisArgument() {
        return new SimpleArgument<Thesis, PersistentThesisReadersGroup>() {
            @Override
            public Thesis parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Thesis> getDomainObject(argument);
            }

            @Override
            public Class<? extends Thesis> getType() {
                return Thesis.class;
            }

            @Override
            public String extract(PersistentThesisReadersGroup group) {
                return group.getThesis() != null ? group.getThesis().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getThesis().getTitle().getContent(), getThesis().getVisibility().getLocalizedName() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(new DateTime());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        if (getThesis().getDocumentsAvailableAfter() != null) {
            DateTime time = getThesis().getDocumentsAvailableAfter();

            if (time.isAfter(when)) {
                return Collections.emptySet();
            }
        }

        if (getThesis().getVisibility() == null) {
            return Collections.emptySet();
        }

        switch (getThesis().getVisibility()) {
        case INTRANET:
            return RoleCustomGroup.getInstance(RoleType.PERSON).getMembers(when);
        case PUBLIC:
            return AnyoneGroup.getInstance().getMembers(when);
        default:
            return Collections.emptySet();
        }
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, new DateTime());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        if (getThesis().getDocumentsAvailableAfter() != null) {
            DateTime time = getThesis().getDocumentsAvailableAfter();

            if (time.isAfter(when)) {
                return false;
            }
        }

        if (getThesis().getVisibility() == null) {
            return false;
        }

        switch (getThesis().getVisibility()) {
        case INTRANET:
            return RoleCustomGroup.getInstance(RoleType.PERSON).isMember(user, when);
        case PUBLIC:
            return AnyoneGroup.getInstance().isMember(user, when);
        default:
            return false;
        }
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    @Override
    protected void gc() {
        setThesis(null);
        super.gc();
    }

    public static PersistentThesisReadersGroup getInstance(Thesis thesis) {
        PersistentThesisReadersGroup instance = thesis.getReaders();
        return instance != null ? instance : create(thesis);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentThesisReadersGroup create(Thesis thesis) {
        PersistentThesisReadersGroup instance = thesis.getReaders();
        return instance != null ? instance : new PersistentThesisReadersGroup(thesis);
    }
}
