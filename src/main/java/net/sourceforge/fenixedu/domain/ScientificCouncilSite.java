package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ScientificCouncilSite extends ScientificCouncilSite_Base {

    public ScientificCouncilSite(ScientificCouncilUnit scientificCouncil) {
        super();

        setUnit(scientificCouncil);
    }

    @Override
    public Group getOwner() {
        return RoleGroup.get(RoleType.SCIENTIFIC_COUNCIL).or(UserGroup.of(Person.convertToUsers(getManagers())));
    }

    /**
     * This method searchs for the first instance of a ScientificCouncilSite.
     * 
     * @return the site associated with the Scientific Council or <code>null</code> if there is no such site
     */
    public static ScientificCouncilSite getSite() {
        final ScientificCouncilUnit scientificCouncilUnit = ScientificCouncilUnit.getScientificCouncilUnit();
        return scientificCouncilUnit == null ? null : (ScientificCouncilSite) scientificCouncilUnit.getSite();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString("");
    }
}
