package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.YearMonthDay;

public class ResearchContract extends ResearchContract_Base {

	public ResearchContract(Person person, YearMonthDay beginDate, YearMonthDay endDate, Unit unit,
			ResearchFunctionType functionType) {

		super();
		for (Accountability accountability : person.getParentAccountabilities(
				AccountabilityTypeEnum.RESEARCH_CONTRACT, ResearchContract.class)) {
			ResearchContract contract = (ResearchContract) accountability;
			if (contract.getUnit().equals(unit)
					&& ((ResearchFunction) contract.getAccountabilityType()).getResearchFunctionType()
							.equals(functionType) && beginDate.equals(contract.getBeginDate())) {
				throw new DomainException("error.contract.already.exists");
			}
		}

		super.init(person, beginDate, endDate, unit);
		setAccountabilityType(ResearchFunction
				.readAccountabilityTypeByResearchFunctionType(functionType));
	}

}
