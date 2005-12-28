package net.sourceforge.fenixedu.renderers.taglib;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class DefineTei extends TagExtraInfo {

    public DefineTei() {
        super();
    }

    public VariableInfo[] getVariableInfo(TagData data) {

        String type = (String) data.getAttribute("type");

        if (type == null) {
            type = "java.lang.Object";
        }

        return new VariableInfo[] { new VariableInfo(data.getAttributeString("id"), type, true,
                VariableInfo.AT_END) };
    }
}
