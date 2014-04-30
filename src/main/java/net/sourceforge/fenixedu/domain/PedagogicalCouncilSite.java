package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.CoordinatorGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * Specific site instance that is associated with the unit that represents the
 * Pedagogical Council of the institution.
 * <p>
 * There should exist only one instance of this site (matching the single unit that represents the council). Nevertheless that is
 * not verified.
 * 
 * @author cfgi
 */
public class PedagogicalCouncilSite extends PedagogicalCouncilSite_Base {

    public PedagogicalCouncilSite(PedagogicalCouncilUnit pedagogicalCouncil) {
        super();

        setUnit(pedagogicalCouncil);
    }

    @Override
    public Group getOwner() {
        return RoleGroup.get(RoleType.PEDAGOGICAL_COUNCIL).or(RoleGroup.get(RoleType.TUTORSHIP))
                .or(UserGroup.of(Person.convertToUsers(getManagers())));
    }

    @Override
    public List<Group> getContextualPermissionGroups() {
        List<Group> list = super.getContextualPermissionGroups();

        list.add(CoordinatorGroup.get(DegreeType.DEGREE));

        return list;
    }

    /**
     * This method searchs for the first instance of a PedagogicalCouncilSite.
     * 
     * @return the site associated with the Pedagogical Council or <code>null</code> if there is no such site
     */
    public static PedagogicalCouncilSite getSite() {
        final PedagogicalCouncilUnit pedagogicalCouncilUnit = PedagogicalCouncilUnit.getPedagogicalCouncilUnit();
        return pedagogicalCouncilUnit == null ? null : (PedagogicalCouncilSite) pedagogicalCouncilUnit.getSite();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString("");
    }
}
