package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.StaticArgument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongNumberOfArgumentsException;

/**
 * Predicate group that accepts or rejects every person based on the boolean
 * flag given in it's construction.
 * 
 * @author pcma
 */
public class IfTrueGroup extends LeafGroup {

	/**
	 * Default serialization id. 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean flag;
	
	public IfTrueGroup(boolean flag) {
		super();
		
		this.flag = flag;
	}

	protected boolean isFlag() {
		return flag;
	}

	@Override
	public boolean isMember(Person person) {
		return this.flag;
	}

	@Override
	public Set<Person> getElements() {
		if (this.flag) {
			return new HashSet<Person>(Person.readAllPersons());
		}
		else {
			return Collections.emptySet();
		}
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return new Argument[] {
				new StaticArgument(flag)
		};
	}

	public static class Builder implements GroupBuilder {

		public Group build(Object[] arguments) {
			if (arguments.length != 1) {
                throw new WrongNumberOfArgumentsException(arguments.length, getMinArguments(),
                        getMaxArguments());
            }
			
			return new IfTrueGroup(new Boolean(String.valueOf(arguments[0])));
		}

		public int getMaxArguments() {
			return 1;
		}

		public int getMinArguments() {
			return 1;
		}
		
	}
}
