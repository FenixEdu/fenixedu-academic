package net.sourceforge.fenixedu.domain.phd.debts;

import net.sourceforge.fenixedu._development.PropertiesManager;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;

public class FctScholarshipPhdGratuityContribuitionEvent extends FctScholarshipPhdGratuityContribuitionEvent_Base {
    public  FctScholarshipPhdGratuityContribuitionEvent() {
        super();
        init(EventType.FCT_SCOLARSHIP, Party.readByContributorNumber(PropertiesManager.getProperty("fct.contributor.number")));
        setRootDomainObject(RootDomainObject.getInstance());
    }

    @Override
    protected PhdProgram getPhdProgram() {
	return ((PhdGratuityEvent)getPhdGratuityFctScholarshipExemption().getEvent()).getPhdProgram();
    }

    @Override
    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
	return ((PhdGratuityEvent)getPhdGratuityFctScholarshipExemption().getEvent()).getPhdIndividualProgramProcess();
    }
    
}