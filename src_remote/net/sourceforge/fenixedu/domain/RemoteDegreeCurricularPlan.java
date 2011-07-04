package net.sourceforge.fenixedu.domain;

public class RemoteDegreeCurricularPlan extends RemoteDegreeCurricularPlan_Base {

    public RemoteDegreeCurricularPlan() {
	super();
    }

    public Boolean isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree() {
	return toBoolean(readRemoteMethod("isDegreeOrBolonhaDegreeOrBolonhaIntegratedMasterDegree", null));

    }

}
