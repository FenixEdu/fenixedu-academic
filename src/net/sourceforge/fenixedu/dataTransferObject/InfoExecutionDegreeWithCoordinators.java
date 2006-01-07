package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.ExecutionDegree;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author Fernanda Quitério Created on 27/Jul/2004
 *  
 */
public class InfoExecutionDegreeWithCoordinators extends InfoExecutionDegreeWithCampus {

    public void copyFromDomain(ExecutionDegree executionDegree) {
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
                Coordinator coordinator = (Coordinator) arg0;

                return InfoCoordinatorWithInfoPerson.newInfoFromDomain(coordinator);
            }
        });
        return infoCoordinators;
    }

    public static InfoExecutionDegree newInfoFromDomain(ExecutionDegree executionDegree) {
        InfoExecutionDegreeWithCoordinators infoExecutionDegreeWithCoordinators = null;
        if (executionDegree != null) {
            infoExecutionDegreeWithCoordinators = new InfoExecutionDegreeWithCoordinators();
            infoExecutionDegreeWithCoordinators.copyFromDomain(executionDegree);
        }
        return infoExecutionDegreeWithCoordinators;
    }

}