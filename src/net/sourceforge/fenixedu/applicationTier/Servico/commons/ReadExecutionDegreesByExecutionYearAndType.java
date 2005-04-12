/*
 * Created on 2004/04/04
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionDegree;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.TipoCurso;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Luis Cruz
 * 
 */
public class ReadExecutionDegreesByExecutionYearAndType implements IService {

    public List run(Integer executionYearOID, TipoCurso typeOfCourse) throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

        final List executionDegrees = executionDegreeDAO.readByExecutionYearOIDAndDegreeType(executionYearOID,
                typeOfCourse);
        return getInfoExecutiionDegrees(executionDegrees);
    }

    public List run(IDegree degree, IExecutionYear executionYear, String tmp)
            throws ExcepcaoPersistencia {
        final ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        final IPersistentExecutionDegree executionDegreeDAO = sp.getIPersistentExecutionDegree();

        final List executionDegrees = executionDegreeDAO.readByDegreeAndExecutionYear(degree, executionYear);
        return getInfoExecutiionDegrees(executionDegrees);
    }

    private List getInfoExecutiionDegrees(final List executionDegrees) {
        final List infoExecutionDegrees = new ArrayList(executionDegrees.size());
        for (int i = 0; i < executionDegrees.size(); i++) {
            final IExecutionDegree executionDegree = (IExecutionDegree) executionDegrees.get(i);
            final InfoExecutionDegree infoExecutionDegree = InfoExecutionDegree
                    .newInfoFromDomain(executionDegree);
            infoExecutionDegrees.add(infoExecutionDegree);
        }
        return infoExecutionDegrees;
    }

}