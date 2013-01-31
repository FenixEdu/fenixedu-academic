package net.sourceforge.fenixedu.domain.accessControl;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.WrongTypeOfArgumentException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;

public abstract class DegreeGroup extends DomainBackedGroup<Degree> {

	public DegreeGroup(Degree degree) {
		super(degree);
	}

	public Degree getDegree() {
		return getObject();
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return new Argument[] { new IdOperator(getObject()) };
	}

	public static class DegreeGroupBuilder implements GroupBuilder {

		public Degree getDegree(Object[] arguments) {

			try {
				return (Degree) arguments[0];
			} catch (ClassCastException e) {
				throw new WrongTypeOfArgumentException(0, Degree.class, arguments[0].getClass());
			}
		}

		@Override
		public Group build(Object[] arguments) {
			return new CurrentDegreeCoordinatorsGroup(getDegree(arguments));
		}

		@Override
		public int getMinArguments() {
			return 1;
		}

		@Override
		public int getMaxArguments() {
			return 1;
		}
	}
}
