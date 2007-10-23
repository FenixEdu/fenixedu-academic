package net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers;

import net.sourceforge.fenixedu.domain.assiduousness.Assiduousness;
import net.sourceforge.fenixedu.domain.assiduousness.util.AssiduousnessState;

public class SearchActiveEmployeeAssiduousness extends SearchEmployeeAssiduousness {

    @Override
    public boolean isValidCondition(Assiduousness assiduousness) {
	return assiduousness.getCurrentStatus() != null
		&& assiduousness.getCurrentStatus().getState() == AssiduousnessState.ACTIVE;
    }
}
