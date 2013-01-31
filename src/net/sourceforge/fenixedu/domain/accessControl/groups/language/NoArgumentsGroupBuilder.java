package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * Utility group builder that can be used to create groups that have no
 * arguments.
 * 
 * @author cfgi
 */
public class NoArgumentsGroupBuilder implements GroupBuilder {

	private Class<? extends Group> groupType;

	public NoArgumentsGroupBuilder(Class<? extends Group> groupType) {
		super();

		this.groupType = groupType;
	}

	@Override
	public Group build(Object[] arguments) {
		try {
			return groupType.newInstance();
		} catch (Exception e) {
			throw new DomainException("accessControl.group.builder.noArguments.failed", e);
		}
	}

	@Override
	public int getMinArguments() {
		return 0;
	}

	@Override
	public int getMaxArguments() {
		return 0;
	}

}
