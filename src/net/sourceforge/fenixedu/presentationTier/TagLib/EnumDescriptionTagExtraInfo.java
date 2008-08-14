/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.TagLib;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 16:35:19,23/Set/2005
 * @version $Id: EnumDescriptionTagExtraInfo.java 15663 2005-11-15 07:56:11Z
 *          gedl $
 */
public class EnumDescriptionTagExtraInfo extends TagExtraInfo {
    public VariableInfo[] getVariableInfo(TagData arg0) {
	return new VariableInfo[] { new VariableInfo("id", "java.lang.String", true, VariableInfo.AT_END) };

    }

}
