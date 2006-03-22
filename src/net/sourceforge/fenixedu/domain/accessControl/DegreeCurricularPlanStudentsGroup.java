/**
 * 
 */
package net.sourceforge.fenixedu.domain.accessControl;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Professorship;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.iterators.FilterIterator;
import org.apache.commons.collections.iterators.TransformIterator;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a>
 * <br/>
 * <br/>
 * <br/>
 * Created on 16:54:06,17/Mar/2006
 * @version $Id$
 */
public class DegreeCurricularPlanStudentsGroup extends DegreeCurricularPlanGroup
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

	            return scp.getStudent().getPerson();
	        }
	    }
	 
	  public DegreeCurricularPlanStudentsGroup(DegreeCurricularPlan degreeCurricularPlan) {
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
	    public Iterator<Person> getElementsIterator() {	    	
	    	Iterator scpIterator = this.getDegreeCurricularPlan().getStudentCurricularPlansIterator();
	    	Iterator<StudentCurricularPlan> filterIterator =  new FilterIterator(scpIterator,new StudentCurricularStateIsActiveOrSchoolPartConcluded());
	    	Iterator<Person> transformIterator = new TransformIterator(filterIterator,new StudentCurricularPlanPersonTransformer());
	    	
	    	return transformIterator;
	    }

}