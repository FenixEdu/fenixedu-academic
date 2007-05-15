package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ResearchFunction extends ResearchFunction_Base {

	public ResearchFunction(ResearchFunctionType researchFunctionType) {
		super();
		super.setType(AccountabilityTypeEnum.RESEARCH_CONTRACT);
		super.setResearchFunctionType(researchFunctionType);
	}

	public static AccountabilityType readAccountabilityTypeByResearchFunctionType(
			ResearchFunctionType researchFunctionType) {
		List<AccountabilityType> allAccountabilityTypes = RootDomainObject.getInstance()
				.getAccountabilityTypes();
		for (AccountabilityType accountabilityType : allAccountabilityTypes) {
			if (accountabilityType.getType().equals(AccountabilityTypeEnum.RESEARCH_CONTRACT)) {
				ResearchFunction function = (ResearchFunction) accountabilityType;
				if (function.getResearchFunctionType().equals(researchFunctionType)) {
					return accountabilityType;
				}
			}
		}
		return null;
	}
}
