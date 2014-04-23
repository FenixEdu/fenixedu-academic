package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.OidOperator;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.GiafProfessionalData;
import net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonProfessionalData;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.teacher.CategoryType;

public class AllEmployeesByCampus extends Group {

    private final Campus campus;

    public AllEmployeesByCampus(Campus campus) {
        super();
        this.campus = campus;
    }

    public Campus getCampus() {
        return campus;
    }

    @Override
    public boolean isMember(final Person person) {
        return isMember(person, getCampus());
    }

    public boolean isMember(final Person person, final Campus campus) {
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
    public Set<Person> getElements() {
        final Set<Person> people = new HashSet<Person>();
        final Campus campus = getCampus();
        for (final Person person : Role.getRoleByRoleType(RoleType.EMPLOYEE).getAssociatedPersons()) {
            if (isMember(person, campus)) {
                people.add(person);
            }
        }
        return people;
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[] { new OidOperator(getCampus()) };
    }

    public static class Builder implements GroupBuilder {

        @Override
        public Group build(Object[] arguments) {
            final Campus campus = (Campus) arguments[0];
            if (campus == null) {
                throw new VariableNotDefinedException("campus");
            }
            return new AllEmployeesByCampus(campus);

        }

        @Override
        public int getMaxArguments() {
            return 1;
        }

        @Override
        public int getMinArguments() {
            return 1;
        }

    }

    @Override
    public PersistentCampusEmployeeGroup convert() {
        return PersistentCampusEmployeeGroup.getInstance(getCampus());
    }
}
