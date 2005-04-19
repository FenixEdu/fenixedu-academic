/*
 * Created on Apr 18, 2005
 */
package net.sourceforge.fenixedu.presentationTier.TagLib;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class CollectionTagExtraInfo extends TagExtraInfo{

    public VariableInfo[] getVariableInfo(TagData arg0) {
        return new VariableInfo[] {
                new VariableInfo("id",
                                 "java.util.Collection",
                                 true,
                                 VariableInfo.AT_END)           
              };

    }

}
