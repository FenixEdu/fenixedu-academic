/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.publico;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionDegreesByExecutionYearAndDegree extends Service {

    public List<InfoExecutionDegree> run(Degree curso, ExecutionYear year) {
	List<InfoExecutionDegree> result = new ArrayList<InfoExecutionDegree>();

	List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByDegreeAndExecutionYear(curso, year.getYear());
	for (ExecutionDegree executionDegree : executionDegrees) {
	    result.add(InfoExecutionDegree.newInfoFromDomain(executionDegree));
	}

	return result;
    }

}
