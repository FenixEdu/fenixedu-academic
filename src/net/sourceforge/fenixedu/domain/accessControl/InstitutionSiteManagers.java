package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;

/**
 * Specialization of the WebSiteManagersGroup that represents the
 * managers of a particular site: the institution site.
 * 
 * @author cfgi
 */
public class InstitutionSiteManagers extends WebSiteManagersGroup {

	/**
	 * Serial version id.
	 */
	private static final long serialVersionUID = 1L;

	public InstitutionSiteManagers() {
		super(RootDomainObject.getInstance().getInstitutionUnit().getSite());
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return new Argument[0];
	}
	
}
