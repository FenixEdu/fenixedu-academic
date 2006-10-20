/**
 * 
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Collection;
import java.util.Set;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:54:06,17/Mar/2006
 * @version $Id$
 */
public class DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup extends DegreeCurricularPlanGroup
{
	private static final long serialVersionUID = 1052397518994080993L;

	private class StudentCurricularStateIsActiveOrSchoolPartConcluded implements Predicate
	{

		public boolean evaluate(Object arg0)
		{
			boolean result = false;
			if (arg0 instanceof StudentCurricularPlan)
			{
				StudentCurricularPlanState state = ((StudentCurricularPlan) arg0).getCurrentState();
				result |= (state == StudentCurricularPlanState.SCHOOLPARTCONCLUDED);
				result |= (state == StudentCurricularPlanState.ACTIVE);
			}
			
			return result;
		}		
	}
	
	 private class StudentCurricularPlanPersonTransformer implements Transformer {

	        public Object transform(Object arg0) {
	            StudentCurricularPlan scp = (StudentCurricularPlan) arg0;

	            return scp.getRegistration().getPerson();
	        }
	    }
	 
	  public DegreeCurricularPlanActiveOrSchoolPartConcludedStudentsGroup(DegreeCurricularPlan degreeCurricularPlan) {
	        super(degreeCurricularPlan);
	    }

	    @Override
	    public int getElementsCount() {
	        int elementsCount = 0;
	        for (StudentCurricularPlan scp : this.getDegreeCurricularPlan().getStudentCurricularPlans())
			{
				if (scp.getCurrentState() == StudentCurricularPlanState.ACTIVE || scp.getCurrentState() == StudentCurricularPlanState.SCHOOLPARTCONCLUDED)
					elementsCount++;
			}
	        
	        
	        return elementsCount;
	    }

	    @Override
	    public Set<Person> getElements() {
	    	Set<Person> elements = super.buildSet();
	    	Collection<StudentCurricularPlan> studentCurricularPlans = this.getDegreeCurricularPlan().getStudentCurricularPlans();
	    	Collection<StudentCurricularPlan> activeOrSchoolPartConcludedStudentCurricularPlans = CollectionUtils.select(studentCurricularPlans,new StudentCurricularStateIsActiveOrSchoolPartConcluded());
	    	Collection<Person> activeOrSchoolPartConcludedPersons = CollectionUtils.collect(activeOrSchoolPartConcludedStudentCurricularPlans,new StudentCurricularPlanPersonTransformer());
	    	elements.addAll(activeOrSchoolPartConcludedPersons);
	    		    	
	    	return super.freezeSet(elements);
	    }

}