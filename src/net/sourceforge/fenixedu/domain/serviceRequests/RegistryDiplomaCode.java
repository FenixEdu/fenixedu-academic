package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.student.curriculum.CycleConclusionProcess;

public class RegistryDiplomaCode extends RegistryDiplomaCode_Base {
    public RegistryDiplomaCode(InstitutionRegistryCodeGenerator generator, RegistryDiplomaRequest request) {
	super();
	init(generator, request);
	String type = null;
	CycleType cycle = request.getRequestedCycle();
	switch (cycle) {
	case FIRST_CYCLE:
	    type = "L";
	    break;
	case SECOND_CYCLE:
	    type = "M";
	    break;
	case THIRD_CYCLE:
	    type = "D";
	    break;
	}
	CycleConclusionProcess conclusion = request.getRegistration().getLastStudentCurricularPlan().getCycle(cycle)
		.getConclusionProcess();
	setCode(getCode() + "/ISTc" + type + "/" + conclusion.getConclusionDate().toString("yy"));
    }
}
