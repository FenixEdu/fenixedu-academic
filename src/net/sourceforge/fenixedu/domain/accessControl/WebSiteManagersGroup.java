package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.UnitSite;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

public class WebSiteManagersGroup extends DomainBackedGroup<UnitSite> {

	/**
	 * Default serial id. 
	 */
	private static final long serialVersionUID = 1L;

	public WebSiteManagersGroup(UnitSite site) {
		super(site);
	}

	@Override
	public Set<Person> getElements() {
		return getObject().getManagersSet();
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return new Argument[] {
				new IdOperator(getObject())
		};
	}

	public static class Builder implements GroupBuilder {

		public Group build(Object[] arguments) {
			UnitSite site = (UnitSite) arguments[0];
			return new WebSiteManagersGroup(site);
		}

		public int getMaxArguments() {
			return 1;
		}

		public int getMinArguments() {
			return 1;
		}
	}
}
