/*
 * Created on Apr 3, 2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.MessageResources;

import net.sourceforge.fenixedu.dataTransferObject.InfoExamsMap;
import net.sourceforge.fenixedu.dataTransferObject.InfoRoomExamsMap;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.renderers.ExamsMapContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.sop.examsMap.renderers.ExamsMapSlotContentRenderer;

/**
 * @author Luis Cruz & Sara Ribeiro
 *  
 */
public class RenderExamsMapTag extends TagSupport {

    // Name of atribute containing ExamMap
    private String name;

    private String user;

    private ExamsMapSlotContentRenderer examsMapSlotContentRenderer = new ExamsMapContentRenderer();

    public int doStartTag() throws JspException {
        // Obtain InfoExamMap
        InfoExamsMap infoExamsMap = null;
        InfoRoomExamsMap infoRoomExamsMap = null;
        ExamsMap examsMap = null;
        IExamsMapRenderer renderer = null;
        String typeUser = "";

        try {
            infoExamsMap = (InfoExamsMap) pageContext.findAttribute(name);
            typeUser = user;
            examsMap = new ExamsMap(infoExamsMap);
            renderer = new ExamsMapRenderer(examsMap, this.examsMapSlotContentRenderer, typeUser);
        } catch (ClassCastException e) {
            infoExamsMap = null;
        }
        try {
            infoRoomExamsMap = (InfoRoomExamsMap) pageContext.findAttribute(name);
            typeUser = user;
            examsMap = new ExamsMap(infoRoomExamsMap);
            renderer = new ExamsMapForRoomRenderer(examsMap, this.examsMapSlotContentRenderer, typeUser);
        } catch (ClassCastException e) {
            infoRoomExamsMap = null;
        }
        if (infoExamsMap == null && infoRoomExamsMap == null) {
            throw new JspException(messages.getMessage("generateExamsMap.infoExamsMap.notFound", name));
        }

        // Generate Map from infoExamsMap
        JspWriter writer = pageContext.getOut();
        //ExamsMap examsMap = new ExamsMap(infoExamsMap);

        //		ExamsMapRenderer renderer =
        //			new ExamsMapRenderer(
        //				examsMap,
        //				this.examsMapSlotContentRenderer,
        //				typeUser);

        try {
            writer.print(renderer.render());
        } catch (IOException e) {
            throw new JspException(messages.getMessage("generateExamsMap.io", e.toString()));
        }

        return (SKIP_BODY);
    }

    public int doEndTag() {
        return (EVAL_PAGE);
    }

    public void release() {
        super.release();
    }

    // Error Messages
    protected static MessageResources messages = MessageResources
            .getMessageResources("ApplicationResources");

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String string) {
        user = string;
    }

}