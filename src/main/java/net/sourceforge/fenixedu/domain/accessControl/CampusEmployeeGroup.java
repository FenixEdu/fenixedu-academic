package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.fenixedu.bennu.core.annotation.GroupArgument;
import org.fenixedu.bennu.core.annotation.GroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.PersistentGroup;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

import com.google.common.base.Objects;

@GroupOperator("campusEmployee")
public class CampusEmployeeGroup extends FenixGroup {
    private static final long serialVersionUID = 4185082898828533195L;

    @GroupArgument
    private Space campus;

    private CampusEmployeeGroup() {
        super();
    }

    private CampusEmployeeGroup(Space campus) {
        this();
        this.campus = campus;
    }

    public static CampusEmployeeGroup get(Space campus) {
        return new CampusEmployeeGroup(campus);
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { campus.getName() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(new DateTime());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        Set<User> users = new HashSet<>();
        for (final Person person : Role.getRoleByRoleType(RoleType.EMPLOYEE).getAssociatedPersonsSet()) {
            if (isMember(person, campus, when)) {
                User user = person.getUser();
                if (user != null) {
                    users.add(user);
                }
            }
        }
        return users;
    }

    @Override
    public boolean isMember(User user) {
        return isMember(user, new DateTime());
    }

    @Override
    public boolean isMember(User user, DateTime when) {
        return user != null && isMember(user.getPerson(), campus, when);
    }

    public boolean isMember(final Person person, final Space campus, DateTime when) {
        //when is ignored, professional data doesn't seem to have proper historic
        if (person != null) {
            PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
            if (personProfessionalData != null) {
                GiafProfessionalData giafProfessionalDataByCategoryType =
                        personProfessionalData.getGiafProfessionalDataByCategoryType(CategoryType.EMPLOYEE);
                if (giafProfessionalDataByCategoryType != null && giafProfessionalDataByCategoryType.getCampus() != null
                        && giafProfessionalDataByCategoryType.getCampus().equals(campus)
                        && !giafProfessionalDataByCategoryType.getContractSituation().getEndSituation()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public PersistentGroup toPersistentGroup() {
        return PersistentCampusEmployeeGroup.getInstance(campus);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof CampusEmployeeGroup) {
            return Objects.equal(campus, ((CampusEmployeeGroup) object).campus);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(campus);
    }
}
