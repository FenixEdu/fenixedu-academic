package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.domain.groups.Group;

/**
 * The group of very logged person. This group has as elements all the persons
 * that may login in the system but only allows a person or a UserView when the
 * person is logged in.
 * 
 * @author cfgi
 */
public class InternalPersonGroup extends RoleGroup {
    /**
     * Serial version id.
     */
    private static final long serialVersionUID = 1L;

    public InternalPersonGroup() {
        super(RoleType.PERSON);
    }

    @Override
    protected Argument[] getExpressionArguments() {
        return null;
    }

    @Override
    public String getPresentationNameBundle() {
        return "resources.SiteResources";
    }

    @Override
    public String getPresentationNameKey() {
        return "label.net.sourceforge.fenixedu.domain.accessControl.InternalPersonGroup";
    }

    @Override
    public Group convert() {
        return RoleCustomGroup.getInstance(RoleType.PERSON);
    }
}
