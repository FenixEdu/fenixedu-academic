package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class UnitExtraWorkMovement extends UnitExtraWorkMovement_Base {

    public UnitExtraWorkMovement(UnitExtraWorkAmount unitExtraWorkAmount, Double amount) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setUnitExtraWorkAmount(unitExtraWorkAmount);
        setAmount(amount);
        setDate(new DateTime());
    }    
}
