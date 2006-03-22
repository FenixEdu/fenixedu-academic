/**
 * 
 */


package net.sourceforge.fenixedu.domain.accessControl;


import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:50:53,17/Mar/2006
 * @version $Id$
 */
public abstract class DegreeCurricularPlanGroup extends DomainBackedGroup<DegreeCurricularPlan>
{

	private static final long serialVersionUID = 5503741043940549135L;

	public DegreeCurricularPlanGroup(DegreeCurricularPlan degreeCurricularPlan)
	{
		super(degreeCurricularPlan);
	}

	public DegreeCurricularPlan getDegreeCurricularPlan()
	{
		return getObject();
	}

}
