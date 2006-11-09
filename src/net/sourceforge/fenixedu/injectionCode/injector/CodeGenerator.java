/**
 * 
 */
package net.sourceforge.fenixedu.injectionCode.injector;

import java.util.Map;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 15:01:06,25/Nov/2005
 * @version $Id: AccessControlCodeGenerator.java,v 1.2 2005/12/16 16:05:59 lepc
 *          Exp $
 */
public interface CodeGenerator {
    public abstract String getCode(Map<String, Object> annotationParameters);
}
