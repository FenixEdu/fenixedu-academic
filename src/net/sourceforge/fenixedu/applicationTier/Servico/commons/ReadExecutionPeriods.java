package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionPeriodWithInfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionPeriod;

public class ReadExecutionPeriods extends Service {

    public List run() throws ExcepcaoPersistencia {
        final IPersistentExecutionPeriod executionPeriodDAO = persistentSupport.getIPersistentExecutionPeriod();

        final Collection<ExecutionPeriod> executionPeriods = executionPeriodDAO.readAll(ExecutionPeriod.class);
        final List result = new ArrayList(executionPeriods.size());
        for (final ExecutionPeriod executionPeriod : executionPeriods) {
            result.add(InfoExecutionPeriodWithInfoExecutionYear.newInfoFromDomain(executionPeriod));
        }
        return result;
    }

    public List run(DegreeType degreeType) throws ExcepcaoPersistencia {
        // the degree type is used in the pos-filtration
        return this.run();
    }

}