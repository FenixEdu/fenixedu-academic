package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheet;

import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import com.google.common.base.Supplier;

@CustomGroupOperator("teachersWithMarkSheetsToConfirm")
public class PersistentTeachersWithMarkSheetsToConfirmGroup extends PersistentTeachersWithMarkSheetsToConfirmGroup_Base {

    public PersistentTeachersWithMarkSheetsToConfirmGroup(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        super();
        init(period, degreeCurricularPlan);
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (MarkSheet markSheet : getPeriod().getMarkSheetsToConfirm(getDegreeCurricularPlan())) {
            if (markSheet.getResponsibleTeacher().getPerson() != null) {
                User user = markSheet.getResponsibleTeacher().getPerson().getUser();
                if (user != null) {
                    users.add(user);
                }
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
        return getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentTeachersWithMarkSheetsToConfirmGroup getInstance(final ExecutionSemester period,
            final DegreeCurricularPlan degreeCurricularPlan) {
        return getInstance(PersistentTeachersWithMarkSheetsToConfirmGroup.class, period, degreeCurricularPlan,
                new Supplier<PersistentTeachersWithMarkSheetsToConfirmGroup>() {
                    @Override
                    public PersistentTeachersWithMarkSheetsToConfirmGroup get() {
                        return new PersistentTeachersWithMarkSheetsToConfirmGroup(period, degreeCurricularPlan);
                    }
                });
    }
}
