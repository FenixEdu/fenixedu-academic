package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;

/**
 * Specialization of the WebSiteManagersGroup that represents the
 * managers of a particular site: the institution site.
 * 
 * @author cfgi
 */
public class InstitutionSiteManagers extends LeafGroup {

	/**
	 * Serial version id.
	 */
	private static final long serialVersionUID = 1L;
	private transient UnitSite institutionSite;

	public InstitutionSiteManagers() {
		super();
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return new Argument[0];
	}

	@Override
	public Set<Person> getElements() {
		return initialize().getManagersSet();
	}

	/**
	 * Lazy initialization to avoid the necessity of loading many objects when the group is being created.
	 */
	private UnitSite initialize() {
		if (this.institutionSite == null) {
			this.institutionSite = RootDomainObject.getInstance().getInstitutionUnit().getSite();
		}
		
		return this.institutionSite;
	}
	
}
