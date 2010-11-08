package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

public class PhdConfigurationIndividualProgramProcessBean implements Serializable {
    private Boolean generateAlerts;

    public PhdConfigurationIndividualProgramProcessBean() {
    }

    public PhdConfigurationIndividualProgramProcessBean(final PhdIndividualProgramProcess process) {
	setGenerateAlerts(process.getPhdConfigurationIndividualProgramProcess().getGenerateAlert());
    }

    public Boolean getGenerateAlerts() {
	return generateAlerts;
    }

    public void setGenerateAlerts(Boolean generateAlerts) {
	this.generateAlerts = generateAlerts;
    }
}
