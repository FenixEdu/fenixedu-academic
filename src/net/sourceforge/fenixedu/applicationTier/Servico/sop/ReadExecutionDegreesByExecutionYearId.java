package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * Servico LerSalas
 * 
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadExecutionDegreesByExecutionYearId extends Service {

    public List run(Integer executionYearId) throws ExcepcaoPersistencia {

        List<InfoExecutionDegree> infoExecutionDegreeList = null;

        ExecutionYear executionYear = null;
        if (executionYearId == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = rootDomainObject.readExecutionYearByOID(executionYearId);
        }

        List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYear(executionYear.getYear());

        if (executionDegrees != null && executionDegrees.size() > 0) {
            Iterator iterator = executionDegrees.iterator();
            infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();

            while (iterator.hasNext()) {
                ExecutionDegree executionDegree = (ExecutionDegree) iterator.next();
                InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree.newInfoFromDomain(executionDegree);
                infoExecutionDegreeList.add(infoExecutionDegree);
            }
        }

        return infoExecutionDegreeList;
    }

}