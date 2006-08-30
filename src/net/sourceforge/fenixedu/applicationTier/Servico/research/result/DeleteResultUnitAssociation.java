package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;

public class DeleteResultUnitAssociation extends Service {

    public void run(Integer oid) {
	final ResultUnitAssociation association = ResultUnitAssociation.readByOid(oid);
	association.delete();
    }
}
