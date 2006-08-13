/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionDegreesByExecutionYearAndType extends Service {

    public List run(Integer executionYearOID, DegreeType typeOfCourse) throws ExcepcaoPersistencia {
        final ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearOID);

        final List executionDegrees = ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear
                .getYear(), typeOfCourse);
        return getInfoExecutionDegrees(executionDegrees);
    }

    public List run(final DegreeType typeOfCourse) throws ExcepcaoPersistencia {

        final ExecutionYear executionYear = ExecutionYear.readCurrentExecutionYear();
        final List<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList<InfoExecutionDegree>();
        for (final ExecutionDegree executionDegree : executionDegrees) {
            final Degree degree = executionDegree.getDegreeCurricularPlan().getDegree();
            if (degree.getTipoCurso().equals(typeOfCourse)) {
                infoExecutionDegrees.add(getInfoExecutionDegree(executionDegree));
            }
        }
        return infoExecutionDegrees;
    }

    public List run(Degree degree, ExecutionYear executionYear, String tmp)
            throws ExcepcaoPersistencia {
        final List<ExecutionDegree> executionDegrees = ExecutionDegree.getAllByDegreeAndExecutionYear(degree, executionYear.getYear());
        return getInfoExecutionDegrees(executionDegrees);
    }

    private List getInfoExecutionDegrees(final List executionDegrees) {
        final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList<InfoExecutionDegree>(executionDegrees.size());
        for (int i = 0; i < executionDegrees.size(); i++) {
            final ExecutionDegree executionDegree = (ExecutionDegree) executionDegrees.get(i);
            final InfoExecutionDegree infoExecutionDegree = getInfoExecutionDegree(executionDegree);

            infoExecutionDegree.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree.getDegreeCurricularPlan()));
            infoExecutionDegrees.add(infoExecutionDegree);
        }
        return infoExecutionDegrees;
    }

    private InfoExecutionDegree getInfoExecutionDegree(final ExecutionDegree executionDegree) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                    .newInfoFromDomain(executionDegree);
            infoExecutionDegree.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree.getDegreeCurricularPlan()));
            return infoExecutionDegree;
    }

}