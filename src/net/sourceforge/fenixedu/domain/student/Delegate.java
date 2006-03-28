/*
 * Created on Feb 18, 2004
 *  
 */
package net.sourceforge.fenixedu.domain.student;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.DelegateYearType;


/**
 * @author <a href="mailto:lesa@mega.ist.utl.pt">Leonor Almeida </a>
 * @author <a href="mailto:shmc@mega.ist.utl.pt">Sergio Montelobo </a>
 * 
 */
public class Delegate extends Delegate_Base {

	public Delegate() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public void delete() {
		removeExecutionYear();
		removeStudent();
		removeDegree();
		deleteDomainObject();
	}

    public static List getAllByDegreeAndExecutionYearAndYearType(Degree degree, ExecutionYear executionYear, DelegateYearType yearType) {
        List<Delegate> delegates = new ArrayList<Delegate>();
        
        for (Delegate delegate : RootDomainObject.getInstance().getDelegates()) {
            if (! degree.equals(delegate.getDegree())) {
                continue;
            }
            
            if (! executionYear.equals(delegate.getExecutionYear())) {
                continue;
            }
            
            if (yearType != null && !yearType.equals(delegate.getYearType())) {
                continue;
            }
            
            delegates.add(delegate);
        }
        
        return delegates;
    }

    public static List getAllByDegreeAndExecutionYear(Degree degree, ExecutionYear executionYear) {
        return getAllByDegreeAndExecutionYearAndYearType(degree, executionYear, null);
    }
    

}
