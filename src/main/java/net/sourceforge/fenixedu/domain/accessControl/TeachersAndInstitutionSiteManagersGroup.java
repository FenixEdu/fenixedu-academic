package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import org.fenixedu.bennu.core.domain.groups.Group;

/**
 * Specific group that represents the union of all Teachers and the Institution
 * site managers.
 * 
 * @author cfgi
 */
public class TeachersAndInstitutionSiteManagersGroup extends LeafGroup {

    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;
    private final IGroup group;

    public TeachersAndInstitutionSiteManagersGroup() {
        super();

        RoleTypeGroup teachers = new RoleTypeGroup(RoleType.TEACHER);
        InstitutionSiteManagers websiteManagers = new InstitutionSiteManagers();

        this.group = new GroupUnion(teachers, websiteManagers);
    }

    @Override
    public Set<Person> getElements() {
        return this.group.getElements();
    }

    @Override
    public boolean isMember(Person person) {
        return this.group.isMember(person);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return new Argument[0];
    }

    @Override
    public Group convert() {
        return group.convert();
    }
}
