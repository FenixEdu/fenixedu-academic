/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionDegreesByExecutionYearAndType {

    @Atomic
    public static List run(String executionYearOID, Set<DegreeType> degreeTypes) {
        final ExecutionYear executionYear = FenixFramework.getDomainObject(executionYearOID);

        final List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
        for (final DegreeType degreeType : degreeTypes) {
            executionDegrees.addAll(ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear.getYear(), degreeType));
        }
        return getInfoExecutionDegrees(executionDegrees);
    }

    @Atomic
    public static List run(final DegreeType typeOfCourse) {

        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final List<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            final Degree degree = executionDegree.getDegreeCurricularPlan().getDegree();
            if (degree.getDegreeType().equals(typeOfCourse)) {
                infoExecutionDegrees.add(getInfoExecutionDegree(executionDegree));
            }
        }
        return infoExecutionDegrees;
    }

    @Atomic
    public static List run(Degree degree, ExecutionYear executionYear, String tmp) {
        final List<ExecutionDegree> executionDegrees =
                ExecutionDegree.getAllByDegreeAndExecutionYear(degree, executionYear.getYear());
        return getInfoExecutionDegrees(executionDegrees);
    }

    private static List getInfoExecutionDegrees(final List executionDegrees) {
        final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList<InfoExecutionDegree>(executionDegrees.size());
        for (int i = 0; i < executionDegrees.size(); i++) {
            final ExecutionDegree executionDegree = (ExecutionDegree) executionDegrees.get(i);
            final InfoExecutionDegree infoExecutionDegree = getInfoExecutionDegree(executionDegree);
            infoExecutionDegrees.add(infoExecutionDegree);
        }
        return infoExecutionDegrees;
    }

    private static InfoExecutionDegree getInfoExecutionDegree(final ExecutionDegree executionDegree) {
        return InfoExecutionDegree.newInfoFromDomain(executionDegree);
    }

}