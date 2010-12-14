package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class PhdConfigurationIndividualProgramProcess extends PhdConfigurationIndividualProgramProcess_Base {

    private PhdConfigurationIndividualProgramProcess() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setGenerateAlert(true);
	setMigratedProcess(false);
    }

    public static PhdConfigurationIndividualProgramProcess createDefault() {
	return new PhdConfigurationIndividualProgramProcess();
    }

    public void configure(PhdConfigurationIndividualProgramProcessBean bean) {
	setGenerateAlert(bean.getGenerateAlerts());
	setMigratedProcess(bean.getMigratedProcess());
    }

    public Boolean isMigratedProcess() {
	return getMigratedProcess();
    }
}
