/*
 * Created on Nov 15, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package net.sourceforge.fenixedu.applicationTier.Filtro.student;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.AccessControlFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.AuthorizationUtils;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.domain.ICoordinator;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroup;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IGroupProposal;
import net.sourceforge.fenixedu.domain.finalDegreeWork.IProposal;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.persistenceTier.IPersistentCoordinator;
import net.sourceforge.fenixedu.persistenceTier.IPersistentExecutionDegree;
import net.sourceforge.fenixedu.persistenceTier.IPersistentFinalDegreeWork;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.utl.ist.berserk.ServiceRequest;
import pt.utl.ist.berserk.ServiceResponse;
import pt.utl.ist.berserk.logic.filterManager.exceptions.FilterException;

/**
 * @author André Fernandes / João Brito
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StudentDegreeCoordinatorAuthorizationFilter extends AccessControlFilter
{
    public StudentDegreeCoordinatorAuthorizationFilter(){}
    
    public void execute(ServiceRequest request, ServiceResponse response)
    	throws FilterException, Exception
	{
		IUserView id = (IUserView) request.getRequester();
		String messageException;
		
		if (id == null || id.getRoles() == null
		        || !AuthorizationUtils.containsRole(id.getRoles(), getRoleType()))
		{
		    throw new NotAuthorizedFilterException();
		}
        messageException = authorizedCoordinator(id, request.getServiceParameters().parametersArray());
        if (messageException != null)
            throw new NotAuthorizedFilterException(messageException);	
	}
    
    /*
    (String username, StudentCurricularPlanIDDomainType curricularPlanID, EnrollmentStateSelectionType criterio)
    */
    //devolve null se tudo OK
    // noAuthorization se algum prob
    private String authorizedCoordinator(IUserView id, Object[] arguments)
	{
	    try 
	    {
	        String username = (String)arguments[0];
	        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
	        IPersistentFinalDegreeWork persistentFinalDegreeWork = sp.getIPersistentFinalDegreeWork();
	        
	        IStudent student1 = sp.getIPersistentStudent().readByUsername(username);
	        
	        List students = sp.getIPersistentStudent().readbyPerson(student1.getPerson());
	        
	        // for each of the Person's Student roles
	        for (Iterator studentsIterator = students.iterator(); studentsIterator.hasNext();)
	        {
	            IStudent student = (IStudent)studentsIterator.next();
	
	            IGroup group = persistentFinalDegreeWork.readFinalDegreeWorkGroupByUsername(student.getPerson().getUsername());
	            
	            if (group != null)
	            {
	                IExecutionDegree executionDegree = group.getExecutionDegree();
	                List coordinators = executionDegree.getCoordinatorsList();
	                
	                for (Iterator it = coordinators.iterator(); it.hasNext();)
	                {
	                    ICoordinator coordinator = (ICoordinator)it.next();
	                    if (coordinator.getTeacher().getPerson().getUsername().equals(id.getUtilizador()))
	                    {
	                        // The student is a candidate for a final degree work of
	                        // the degree of the
	                        // coordinator making the request. Allow access.
	                        return null;
	                    }
	                }
	                
	                List groupProposals = group.getGroupProposals();
	
	                for (Iterator it = groupProposals.iterator(); it.hasNext(); )
	                {
	                    IGroupProposal groupProposal = (IGroupProposal)it.next();
	                    IProposal proposal = groupProposal.getFinalDegreeWorkProposal();
	                    ITeacher teacher = proposal.getOrientator();
	
	                    if (teacher.getPerson().getUsername().equals(id.getUtilizador())) {
	                        // The student is a candidate for a final degree work of
	                        // oriented by the
	                        // teacher making the request. Allow access.
	                        return null;
	                    }
	
	                    teacher = proposal.getCoorientator();
	                    if (teacher != null && teacher.getPerson().getUsername().equals(id.getUtilizador())) {
	                        // The student is a candidate for a final degree work of
	                        // cooriented by the
	                        // teacher making the request. Allow access.
	                        return null;
	                    }
	                }
	            }
	            /*-----*/
	            
	            IPersistentExecutionDegree persistentExecutionDegree = sp.getIPersistentExecutionDegree();
	            List studentCurricularPlans = student.getStudentCurricularPlans();
	            
	            for (Iterator scpIterator = studentCurricularPlans.iterator(); scpIterator.hasNext();)
	            {
	                IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan)scpIterator.next();
	                
	                List executionDegrees = persistentExecutionDegree.readByDegreeCurricularPlan(studentCurricularPlan.getDegreeCurricularPlan());
	                
	                if (executionDegrees == null || executionDegrees.isEmpty())
	                {
	                    continue;
	                }
	                
	                IPersistentCoordinator persistentCoordinator = sp.getIPersistentCoordinator();
	                
	                for(Iterator executionDegreeIterator = executionDegrees.iterator(); executionDegreeIterator.hasNext();)
	                {
	                    IExecutionDegree executionDegree = (IExecutionDegree)executionDegreeIterator.next();
		                List coordinatorsList = persistentCoordinator.readCoordinatorsByExecutionDegree(executionDegree);
		                
		                if (coordinatorsList == null || coordinatorsList.isEmpty())
		                {
		                    continue;
		                }
		
		                final String coordinatorUsername = id.getUtilizador();
		                
		                ICoordinator coordinator = (ICoordinator) CollectionUtils.find(coordinatorsList,
		                        new Predicate() {
		
		                            public boolean evaluate(Object input) {
		                                ICoordinator coordinatorTemp = (ICoordinator) input;
		                                if (coordinatorUsername.equals(coordinatorTemp.getTeacher().getPerson()
		                                        .getUsername()))
		                                {
		                                    return true;
		                                }
		                                return false;
		                            }
		                        });
		                if (coordinator == null)
		                {
		                    continue;
		                }
		
		                //if this is a coordinator of the Degree for this Student
		                if (coordinator.getExecutionDegree().getCurricularPlan().getDegree().getIdInternal().equals(
		                                studentCurricularPlan.getDegreeCurricularPlan().getDegree().getIdInternal()))
		                {
		                    return null;
		                }
	                }
	            }
	        }
	    }
	    catch (Exception exception)
	    {
	        exception.printStackTrace();
	        return "noAuthorization";
	    }
	    return "noAuthorization";
	}
    
    protected RoleType getRoleType()
	{
	    return RoleType.COORDINATOR;
	}
}
