package net.sourceforge.fenixedu.domain.accessControl.groups.language;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.DomainBackedGroup;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.EnumOperator;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class ResearchMembersGroup extends DomainBackedGroup<ResearchUnit> {

	private ResearchFunctionType functionType;
	
	public ResearchMembersGroup(ResearchUnit object, ResearchFunctionType type) {
		super(object);
		this.functionType = type;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public Set<Person> getElements() {
		ResearchUnit unit = getObject();
		Set<Person> people = new HashSet<Person>();
		for(Accountability accountability : unit.getActiveResearchContracts(this.functionType)) {
			people.add(((ResearchContract) accountability).getPerson());
		}
		return people;
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return new Argument[] {
				new IdOperator(getObject()),
				new EnumOperator(this.functionType)
		};
	}

}
