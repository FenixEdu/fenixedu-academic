package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.MarkSheet;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("teachersWithMarkSheetsToConfirm")
public class TeachersWithMarkSheetsToConfirmGroup extends FenixGroup {
    private static final long serialVersionUID = -6490147275585620321L;

    @GroupArgument
    private ExecutionSemester period;

    @GroupArgument
    private DegreeCurricularPlan degreeCurricularPlan;

    private TeachersWithMarkSheetsToConfirmGroup() {
        super();
    }

    private TeachersWithMarkSheetsToConfirmGroup(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        this();
        this.period = period;
        this.degreeCurricularPlan = degreeCurricularPlan;
    }

    public static TeachersWithMarkSheetsToConfirmGroup get(ExecutionSemester period, DegreeCurricularPlan degreeCurricularPlan) {
        return new TeachersWithMarkSheetsToConfirmGroup(period, degreeCurricularPlan);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { degreeCurricularPlan.getPresentationName(), period.getName() };
    }

    @Override
    public Set<User> getMembers() {
        Set<User> users = new HashSet<>();
        for (MarkSheet markSheet : period.getMarkSheetsToConfirm(degreeCurricularPlan)) {
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
        return user != null && getMembers().contains(user);
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return isMember(user);
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentTeachersWithMarkSheetsToConfirmGroup.getInstance(period, degreeCurricularPlan);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof TeachersWithMarkSheetsToConfirmGroup) {
            TeachersWithMarkSheetsToConfirmGroup other = (TeachersWithMarkSheetsToConfirmGroup) object;
            return Objects.equal(period, other.period) && Objects.equal(degreeCurricularPlan, other.degreeCurricularPlan);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(period, degreeCurricularPlan);
    }
}
