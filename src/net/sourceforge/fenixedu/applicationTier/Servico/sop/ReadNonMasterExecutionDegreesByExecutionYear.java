package net.sourceforge.fenixedu.applicationTier.Servico.sop;

/**
 * @author tfc130
 * @version
 */

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ReadNonMasterExecutionDegreesByExecutionYear extends Service {

    public List run(InfoExecutionYear infoExecutionYear) throws FenixServiceException,
            ExcepcaoPersistencia {

        final String yearToSearch;
        if (infoExecutionYear.getYear() == null || infoExecutionYear.getYear().length() == 0) {
            yearToSearch = ExecutionYear.readCurrentExecutionYear().getYear();
        } else {
            yearToSearch = infoExecutionYear.getYear();
        }

        // remove ExecutionDegree DAO
        List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByExecutionYearAndDegreeType(yearToSearch,
                DegreeType.DEGREE);
        
        final List<InfoExecutionDegree> infoExecutionDegreeList = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            infoExecutionDegreeList.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
        }

        return infoExecutionDegreeList;
    }

}
