package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.EnumOperator;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchFunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;

public class ResearchUnitMembersGroup extends DomainBackedGroup<ResearchUnit>{

	private ResearchFunctionType functionType;
	
	private static final long serialVersionUID = 1L;

	public ResearchUnitMembersGroup(ResearchUnit unit, ResearchFunctionType type) {
		super(unit);
		this.functionType = type;
	}
	
	public String getName() {
		return this.functionType.getName();
	}
	
	public ResearchFunctionType getResearchFunctionType() {
		return this.functionType;
	}

	@Override
	public Set<Person> getElements() {
		Set<Person> people = new HashSet<Person>();
		for(Accountability accountability : getObject().getActiveResearchContracts(this.functionType)) {
			ResearchContract contract = (ResearchContract) accountability;
			people.add(contract.getPerson());
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

	@Override
	public boolean equals(Object other) {
		return super.equals(other) &&
			((ResearchUnitMembersGroup)other).functionType.equals(this.functionType);	
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.functionType.hashCode();
	}
	
	
	public static class Builder implements GroupBuilder {

		public Group build(Object[] arguments) {
			ResearchUnit unit = (ResearchUnit) arguments[0];
			ResearchFunctionType type = (ResearchFunctionType) arguments[1];
			if( unit == null || type == null) {
				throw new VariableNotDefinedException("unit or type"); 
			}
			return new ResearchUnitMembersGroup(unit,type);
			
		}

		public int getMaxArguments() {
			return 2;
		}

		public int getMinArguments() {
			return 2;
		}
		
	}

	
}
