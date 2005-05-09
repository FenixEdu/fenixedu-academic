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
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionDegreesByExecutionYearAndType implements IService {

    public List run(Integer executionYearOID, DegreeType typeOfCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

        final List executionDegrees = executionDegreeDAO.readByExecutionYearOIDAndDegreeType(executionYearOID,
                typeOfCourse);
        return getInfoExecutionDegrees(executionDegrees);
    }

    public List run(IDegree degree, IExecutionYear executionYear, String tmp)
            throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

        final List executionDegrees = executionDegreeDAO.readByDegreeAndExecutionYear(degree, executionYear);
        return getInfoExecutionDegrees(executionDegrees);
    }

    private List getInfoExecutionDegrees(final List executionDegrees) {
        final List infoExecutionDegrees = new ArrayList(executionDegrees.size());
        for (int i = 0; i < executionDegrees.size(); i++) {
            final IExecutionDegree executionDegree = (IExecutionDegree) executionDegrees.get(i);
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                    .newInfoFromDomain(executionDegree);

            infoExecutionDegree.setInfoDegreeCurricularPlan(InfoDegreeCurricularPlan
                    .newInfoFromDomain(executionDegree.getDegreeCurricularPlan()));

            infoExecutionDegree.getInfoDegreeCurricularPlan().setInfoDegree(
                    InfoDegree.newInfoFromDomain(executionDegree.getDegreeCurricularPlan().getDegree()));

            infoExecutionDegrees.add(infoExecutionDegree);
        }
        return infoExecutionDegrees;
    }

}