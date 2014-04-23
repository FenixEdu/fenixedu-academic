package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

import org.fenixedu.bennu.core.annotation.CustomGroupArgument;
import org.fenixedu.bennu.core.annotation.CustomGroupOperator;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.domain.groups.Group;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;

@CustomGroupOperator("campusEmployee")
public class PersistentCampusEmployeeGroup extends PersistentCampusEmployeeGroup_Base {
    public PersistentCampusEmployeeGroup(Campus campus) {
        super();
        setCampus(campus);
    }

    @CustomGroupArgument
    public static Argument<Campus> thesisArgument() {
        return new SimpleArgument<Campus, PersistentCampusEmployeeGroup>() {
            @Override
            public Campus parse(String argument) {
                return Strings.isNullOrEmpty(argument) ? null : FenixFramework.<Campus> getDomainObject(argument);
            }

            @Override
            public Class<? extends Campus> getType() {
                return Campus.class;
            }

            @Override
            public String extract(PersistentCampusEmployeeGroup group) {
                return group.getCampus() != null ? group.getCampus().getExternalId() : "";
            }
        };
    }

    @Override
    public String[] getPresentationNameKeyArgs() {
        return new String[] { getCampus().getName() };
    }

    @Override
    public Set<User> getMembers() {
        return getMembers(new DateTime());
    }

    @Override
    public Set<User> getMembers(DateTime when) {
        Set<User> users = new HashSet<>();
        for (final Person person : Role.getRoleByRoleType(RoleType.EMPLOYEE).getAssociatedPersonsSet()) {
            if (isMember(person, getCampus(), when)) {
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
        return isMember(user.getPerson(), getCampus(), when);
    }

    public boolean isMember(final Person person, final Campus campus, DateTime when) {
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

    public static Set<Group> groupsForUser(User user) {
        return Collections.emptySet();
    }

    public static PersistentCampusEmployeeGroup getInstance(Campus campus) {
        PersistentCampusEmployeeGroup instance = campus.getCampusEmployeeGroup();
        return instance != null ? instance : create(campus);
    }

    @Atomic(mode = TxMode.WRITE)
    private static PersistentCampusEmployeeGroup create(Campus campus) {
        PersistentCampusEmployeeGroup instance = campus.getCampusEmployeeGroup();
        return instance != null ? instance : new PersistentCampusEmployeeGroup(campus);
    }
}
