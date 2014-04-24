package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

@CustomGroupOperator("alumni")
public class PersistentAlumniGroup extends PersistentAlumniGroup_Base {
    protected PersistentAlumniGroup(Degree degree) {
        super();
        setDegree(degree);
        if (degree != null) {
            setRootForFenixPredicate(null);
        }
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getDegree().getNameI18N().getContent() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(DateTime.now());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        //TODO: time specific is just using Alumni.getRegisteredWhen() date?
        Set<User> users = new HashSet<>();
        for (final Alumni alumni : Bennu.getInstance().getAlumnisSet()) {
            User user = alumni.getStudent().getPerson().getUser();
            if (user != null) {
                if (getDegree() != null && !isMember(user, when)) {
                    continue;
                }
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, DateTime.now());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        if (user.getPerson().getStudent() == null || user.getPerson().getStudent().getAlumni() == null) {
            return false;
        }
        if (getDegree() != null) {
            for (Registration registration : user.getPerson().getStudent().getRegistrationsFor(getDegree())) {
                if (new RegistrationConclusionBean(registration).isConcluded()) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentAlumniGroup getInstance() {
        return getInstance(null);
    }

    public static PersistentAlumniGroup getInstance(Degree degree) {
        PersistentAlumniGroup instance = degree == null ? find(PersistentAlumniGroup.class).orNull() : degree.getAlumniGroup();
        return instance != null ? instance : create(degree);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentAlumniGroup create(Degree degree) {
        PersistentAlumniGroup instance = degree == null ? find(PersistentAlumniGroup.class).orNull() : degree.getAlumniGroup();
        return instance != null ? instance : new PersistentAlumniGroup(degree);
    }
}
