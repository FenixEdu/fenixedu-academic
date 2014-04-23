package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

public class AllEmployeesGroup extends Group {

    private static final long serialVersionUID = 1L;

    public AllEmployeesGroup() {
    }

    @Override
    public Set<Person> getElements() {
        final Set<Person> people = new HashSet<Person>();
        for (final Person person : Role.getRoleByRoleType(RoleType.EMPLOYEE).getAssociatedPersons()) {
            if (isMember(person)) {
                people.add(person);
            }
        }
        return people;
    }

    @Override
    public boolean isMember(Person person) {
        if (person != null) {
            PersonProfessionalData personProfessionalData = person.getPersonProfessionalData();
            if (personProfessionalData != null) {
                GiafProfessionalData giafProfessionalDataByCategoryType =
                        personProfessionalData.getGiafProfessionalDataByCategoryType(CategoryType.EMPLOYEE);
                if (giafProfessionalDataByCategoryType != null
                        && !giafProfessionalDataByCategoryType.getContractSituation().getEndSituation()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return null;
    }

    @Override
    public String getPresentationNameKey() {
        return super.getPresentationNameKey() + "." + RoleType.EMPLOYEE;
    }

    @Override
    public RoleCustomGroup convert() {
        return RoleCustomGroup.getInstance(RoleType.EMPLOYEE);
    }
}
