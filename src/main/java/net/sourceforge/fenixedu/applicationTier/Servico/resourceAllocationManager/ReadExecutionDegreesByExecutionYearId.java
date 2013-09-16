package net.sourceforge.fenixedu.applicationTier.Servico.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ReadExecutionDegreesByExecutionYearId {

    @Service
    public static List run(String executionYearId) {

        List<InfoExecutionDegree> infoExecutionDegreeList = null;

        ExecutionYear executionYear = null;
        if (StringUtils.isEmpty(executionYearId)) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        } else {
            executionYear = AbstractDomainObject.fromExternalId(executionYearId);
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