package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.curricularPlans;

import java.io.Serializable;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Coordinator;
import net.sourceforge.fenixedu.domain.Person;

public class ExecutionDegreeCoordinatorsBean implements Serializable{
    
    private ExecutionDegree executionDegree;
    private Person newCoordinator;

    public ExecutionDegree getExecutionDegree() {
        return executionDegree;
    }

    public void setExecutionDegree(ExecutionDegree executionDegree) {
        this.executionDegree = executionDegree;
    }
    
    public Person getNewCoordinator(){
	return newCoordinator;
    }
    
    public void setNewCoordinator(Person newCoordinator){
	this.newCoordinator = newCoordinator;
    }
    
    public Set<Coordinator> getCoordinators(){
	return executionDegree.getCoordinatorsListSet();
    }
    
    public ExecutionDegreeCoordinatorsBean(ExecutionDegree executionDegree){
	this.executionDegree = executionDegree;
    }
    
    public ExecutionDegreeCoordinatorsBean(){
	
    }

}
