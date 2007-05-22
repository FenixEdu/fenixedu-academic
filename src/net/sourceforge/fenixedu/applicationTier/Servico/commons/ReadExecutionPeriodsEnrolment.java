package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionPeriodsEnrolment extends Service {

    public List run(DegreeType degreeType) throws ExcepcaoPersistencia {
        final List<InfoExecutionPeriod> result = new ArrayList<InfoExecutionPeriod>();
        for (final ExecutionPeriod executionPeriod : rootDomainObject.getExecutionPeriods()) {
            result.add(InfoExecutionPeriod.newInfoFromDomain(executionPeriod));
        }
        return result;
    }

}