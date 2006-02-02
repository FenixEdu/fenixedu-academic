/**
 * 
 */


package net.sourceforge.fenixedu.accessControl;


import net.sourceforge.fenixedu.accessControl.injector.AccessControlCodeGenerator;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 14:51:43,25/Nov/2005
 * @version $Id$
 */
public class FenixAccessControlCallsGenerator implements AccessControlCodeGenerator
{

	public String getCode(String groupAccessorMethodName)
	{
		StringBuilder buffer = new StringBuilder();
        
        buffer.append("{net.sourceforge.fenixedu.accessControl.AccessControl").append(".").append("check").append("(").append("this, ");
        buffer.append("net.sourceforge.fenixedu.predicates.").append(groupAccessorMethodName).append(");}");         
    
		//buffer.append("{System.out.println(\"I've been injected. That's great.\");}");
        
		return buffer.toString();

	}

}
