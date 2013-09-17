package net.sourceforge.fenixedu.domain.phd;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class PhdConfigurationIndividualProgramProcess extends PhdConfigurationIndividualProgramProcess_Base {

    private PhdConfigurationIndividualProgramProcess() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setGenerateAlert(true);
        setMigratedProcess(false);
    }

    private PhdConfigurationIndividualProgramProcess(boolean generateAlerts, boolean migratedProcess) {
        this();
        setGenerateAlert(generateAlerts);
        setMigratedProcess(migratedProcess);
    }

    public static PhdConfigurationIndividualProgramProcess createDefault() {
        return new PhdConfigurationIndividualProgramProcess();
    }

    public static PhdConfigurationIndividualProgramProcess createMigratedProcessConfiguration() {
        return new PhdConfigurationIndividualProgramProcess(false, true);
    }

    public void configure(PhdConfigurationIndividualProgramProcessBean bean) {
        setGenerateAlert(bean.getGenerateAlerts());
        setMigratedProcess(bean.getMigratedProcess());
        setIsBolonha(bean.getIsBolonha());
    }

    public Boolean isMigratedProcess() {
        return getMigratedProcess();
    }
    @Deprecated
    public boolean hasIsBolonha() {
        return getIsBolonha() != null;
    }

    @Deprecated
    public boolean hasGenerateAlert() {
        return getGenerateAlert() != null;
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasPhdIndividualProgramProcess() {
        return getPhdIndividualProgramProcess() != null;
    }

    @Deprecated
    public boolean hasMigratedProcess() {
        return getMigratedProcess() != null;
    }

}
