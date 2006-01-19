/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.applicationTier.IService;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionDegreesByExecutionYearAndType implements IService {

    public List run(Integer executionYearOID, DegreeType typeOfCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

        final ExecutionYear executionYear = (ExecutionYear) sp.getIPersistentExecutionYear()
                .readByOID(ExecutionYear.class, executionYearOID);

        final List executionDegrees = executionDegreeDAO.readByExecutionYearAndDegreeType(executionYear
                .getYear(), typeOfCourse);
        return getInfoExecutionDegrees(executionDegrees);
    }

    public List run(final DegreeType typeOfCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionYear persistentExecutionYear = persistentSupport.getIPersistentExecutionYear();

        final ExecutionYear executionYear = persistentExecutionYear.readCurrentExecutionYear();
        final List<ExecutionDegree> executionDegrees = executionYear.getExecutionDegrees();
        final List<InfoExecutionDegree> infoExecutionDegrees = new ArrayList();
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
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

        final List executionDegrees = executionDegreeDAO.readByDegreeAndExecutionYear(degree
                .getIdInternal(), executionYear.getYear(), CurricularStage.OLD);
        return getInfoExecutionDegrees(executionDegrees);
    }

    private List getInfoExecutionDegrees(final List executionDegrees) {
        final List infoExecutionDegrees = new ArrayList(executionDegrees.size());
        for (int i = 0; i < executionDegrees.size(); i++) {
            final ExecutionDegree executionDegree = (ExecutionDegree) executionDegrees.get(i);
            final InfoExecutionDegree infoExecutionDegree = getInfoExecutionDegree(executionDegree);

            infoExecutionDegree.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree.getDegreeCurricularPlan()));

            infoExecutionDegree.getInfoDegreeCurricularPlan().setInfoDegree(
                    InfoDegree.newInfoFromDomain(executionDegree.getDegreeCurricularPlan().getDegree()));

            infoExecutionDegrees.add(infoExecutionDegree);
        }
        return infoExecutionDegrees;
    }

    private InfoExecutionDegree getInfoExecutionDegree(final ExecutionDegree executionDegree) {
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                    .newInfoFromDomain(executionDegree);

            infoExecutionDegree.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree.getDegreeCurricularPlan()));

            infoExecutionDegree.getInfoDegreeCurricularPlan().setInfoDegree(
                    InfoDegree.newInfoFromDomain(executionDegree.getDegreeCurricularPlan().getDegree()));

            return infoExecutionDegree;
    }

}