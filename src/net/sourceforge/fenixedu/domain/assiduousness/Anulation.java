package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.assiduousness.util.AnulationState;

import org.joda.time.DateTime;

public class Anulation extends Anulation_Base {

    public Anulation(AnulationState anulationState, JustificationMotive justificationMotive,
            AssiduousnessRecord assiduousnessRecord, DateTime lastModifiedDate, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAnulatedAssiduousnessRecord(assiduousnessRecord);
        setAssiduousness(assiduousnessRecord.getAssiduousness());
        setOracleSequence(assiduousnessRecord.getOracleSequence());
        setState(anulationState);
        setJustificationMotive(justificationMotive);
        setDate(lastModifiedDate);
        setLastModifiedDate(lastModifiedDate);
        setModifiedBy(modifiedBy);
        setOjbConcreteClass(Anulation.class.getName());
    }

    public Anulation(AssiduousnessRecord assiduousnessRecord, Employee modifiedBy) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setAnulatedAssiduousnessRecord(assiduousnessRecord);
        setAssiduousness(assiduousnessRecord.getAssiduousness());
        setOracleSequence(assiduousnessRecord.getOracleSequence());
        setState(AnulationState.VALID);
        setJustificationMotive(getJustificationMotiveForAnulation());
        DateTime now = new DateTime();
        setDate(now);
        setLastModifiedDate(now);
        setModifiedBy(modifiedBy);
        setOjbConcreteClass(Anulation.class.getName());
    }

    protected JustificationMotive getJustificationMotiveForAnulation() {
        for (JustificationMotive justificationMotive : RootDomainObject.getInstance()
                .getJustificationMotives()) {
            if (justificationMotive.getAcronym().equalsIgnoreCase("REG_MA")) {
                return justificationMotive;
            }
        }
        return null;
    }

}
