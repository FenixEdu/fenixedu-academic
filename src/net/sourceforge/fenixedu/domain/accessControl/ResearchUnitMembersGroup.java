package net.sourceforge.fenixedu.domain.accessControl;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.Argument;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.GroupBuilder;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.exceptions.VariableNotDefinedException;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.ClassOperator;
import net.sourceforge.fenixedu.domain.accessControl.groups.language.operators.IdOperator;
import net.sourceforge.fenixedu.domain.organizationalStructure.Accountability;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchContract;
import net.sourceforge.fenixedu.domain.organizationalStructure.ResearchUnit;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ResearchUnitMembersGroup extends DomainBackedGroup<ResearchUnit>{

	private Class classType;
	
	private static final long serialVersionUID = 1L;

	public ResearchUnitMembersGroup(ResearchUnit unit, Class type) {
		super(unit);
		this.classType = type;
	}
	
	public String getName() {
		return RenderUtils.getResourceString("MESSAGING_RESOURCES","label." + classType.getSimpleName());
	}

	@Override
	public Set<Person> getElements() {
		Set<Person> people = new HashSet<Person>();
		for(Accountability accountability : getObject().getActiveResearchContracts(classType)) {
			ResearchContract contract = (ResearchContract) accountability;
			people.add(contract.getPerson());
		}
		return people;
	}

	@Override
	protected Argument[] getExpressionArguments() {
		return new Argument[] {
				new IdOperator(getObject()),
				new ClassOperator(this.classType)
		};

	}

	@Override
	public boolean equals(Object other) {
		return super.equals(other) &&
			((ResearchUnitMembersGroup)other).classType.equals(this.classType);	
	}

	@Override
	public int hashCode() {
		return super.hashCode() + this.classType.hashCode();
	}
	
	
	public static class Builder implements GroupBuilder {

		public Group build(Object[] arguments) {
			ResearchUnit unit = (ResearchUnit) arguments[0];
			Class type = (Class) arguments[1];
			if( unit == null || type == null) {
				throw new VariableNotDefinedException("unit or contract class"); 
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
