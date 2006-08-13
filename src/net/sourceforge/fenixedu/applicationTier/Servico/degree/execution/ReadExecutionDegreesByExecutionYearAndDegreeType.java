/*
 * Created on Dec 11, 2003 by jpvl
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.degree.execution;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author jpvl
 */
public class ReadExecutionDegreesByExecutionYearAndDegreeType extends Service {

    public List run(String executionYear, DegreeType degreeType)
            throws ExcepcaoPersistencia {

        List infoExecutionDegreeList = null;

        List executionDegrees = null;

        if (degreeType == null) {
            executionDegrees = ExecutionDegree.getAllByExecutionYear(executionYear);
        } else {
            executionDegrees = ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear,
                    degreeType);
        }

        infoExecutionDegreeList = (ArrayList) CollectionUtils.collect(executionDegrees,
                new Transformer() {

                    public Object transform(Object input) {
                        ExecutionDegree executionDegree = (ExecutionDegree) input;
                        InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                                .newInfoFromDomain(executionDegree);

                        InfoExecutionYear infoExecutionYear = InfoExecutionYear
                                .newInfoFromDomain(executionDegree.getExecutionYear());
                        infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);

                        InfoDegreeCurricularPlan infoDegreeCurricularPlan = InfoDegreeCurricularPlan
                                .newInfoFromDomain(executionDegree.getDegreeCurricularPlan());
                        infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);

                        return infoExecutionDegree;
                    }
                });

        return infoExecutionDegreeList;
    }
}