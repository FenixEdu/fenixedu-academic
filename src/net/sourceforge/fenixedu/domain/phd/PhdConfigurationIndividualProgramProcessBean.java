package net.sourceforge.fenixedu.domain.phd;

import java.io.Serializable;

public class PhdConfigurationIndividualProgramProcessBean implements Serializable {
	private Boolean generateAlerts;
	private Boolean migratedProcess;
	private Boolean isBolonha;

	public PhdConfigurationIndividualProgramProcessBean() {
	}

	public PhdConfigurationIndividualProgramProcessBean(final PhdIndividualProgramProcess process) {
		setGenerateAlerts(process.getPhdConfigurationIndividualProgramProcess().getGenerateAlert());
		setMigratedProcess(process.getPhdConfigurationIndividualProgramProcess().getMigratedProcess());
		setIsBolonha(process.getPhdConfigurationIndividualProgramProcess().getIsBolonha());
	}

	public Boolean getGenerateAlerts() {
		return generateAlerts;
	}

	public void setGenerateAlerts(Boolean generateAlerts) {
		this.generateAlerts = generateAlerts;
	}

	public Boolean getMigratedProcess() {
		return migratedProcess;
	}

	public void setMigratedProcess(Boolean migratedProcess) {
		this.migratedProcess = migratedProcess;
	}

	public Boolean getIsBolonha() {
		return isBolonha;
	}

	public void setIsBolonha(Boolean isBolonha) {
		this.isBolonha = isBolonha;
	}
}
