/**
 * 
 */

package net.sourceforge.fenixedu.injectionCode;

import java.util.Map;

import net.sourceforge.fenixedu.injectionCode.injector.CodeGenerator;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 14:51:43,25/Nov/2005
 * @version $Id: FenixAccessControlCallsGenerator.java,v 1.3 2006/02/02 17:08:57
 *          sana Exp $
 */
public class FenixAccessControlCallsGenerator implements CodeGenerator {

    public String getCode(Map<String, Object> annotationParameters) {
	StringBuilder buffer = new StringBuilder();

	buffer.append("{net.sourceforge.fenixedu.injectionCode.AccessControl.check(this,");
	buffer.append("net.sourceforge.fenixedu.predicates.").append(
		((String) annotationParameters.get("value")).trim()).append(");}");

	return buffer.toString();
    }
}
