package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import org.apache.commons.collections.Transformer;

import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IExecutionDegree;

import net.sourceforge.fenixedu.commons.CollectionUtils;

/**
 * @author Fernanda Quitério Created on 27/Jul/2004
 *  
 */
public class InfoExecutionDegreeWithCoordinators extends InfoExecutionDegreeWithCampus {

    public void copyFromDomain(IExecutionDegree executionDegree) {
        super.copyFromDomain(executionDegree);
        if (executionDegree != null) {
            setCoordinatorsList(copyICoordinator2InfoCoordinator(executionDegree.getCoordinatorsList()));
        }
    }

    /**
     * @param coordinatorsList
     * @return
     */
    private List copyICoordinator2InfoCoordinator(List coordinatorsList) {
        List infoCoordinators = null;

        infoCoordinators = (List) CollectionUtils.collect(coordinatorsList, new Transformer() {
            public Object transform(Object arg0) {
                ICoordinator coordinator = (ICoordinator) arg0;

                return InfoCoordinatorWithInfoPerson.newInfoFromDomain(coordinator);
            }
        });
        return infoCoordinators;
    }

    public static InfoExecutionDegree newInfoFromDomain(IExecutionDegree executionDegree) {
        InfoExecutionDegreeWithCoordinators infoExecutionDegreeWithCoordinators = null;
        if (executionDegree != null) {
            infoExecutionDegreeWithCoordinators = new InfoExecutionDegreeWithCoordinators();
            infoExecutionDegreeWithCoordinators.copyFromDomain(executionDegree);
        }
        return infoExecutionDegreeWithCoordinators;
    }

}