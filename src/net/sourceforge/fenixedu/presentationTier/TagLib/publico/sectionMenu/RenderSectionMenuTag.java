/*
 * Created on 7/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers.ISectionMenuSlotContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers.SectionMenuContentRenderer;

import org.apache.struts.util.MessageResources;

/**
 * @author João Mota
 * 
 *  
 */
public class RenderSectionMenuTag extends TagSupport {

    private String name;

    private String path;

    private String activeSectionName;

    private String renderer;

    private ISectionMenuSlotContentRenderer sectionMenuSlotContentRenderer = new SectionMenuContentRenderer();

    public int doStartTag() throws JspException {
        //		Obtain InfoSection
        List sections = null;
        InfoSection activeSection = null;
        try {
            sections = (List) pageContext.findAttribute(name);
        } catch (ClassCastException e) {
            sections = null;
        }
        if (getActiveSectionName() != null) {

            try {
                activeSection = (InfoSection) pageContext.findAttribute(getActiveSectionName());
            } catch (ClassCastException e) {
                activeSection = null;
            }
        }
        //TODO: change the message
        if (sections == null) {
            throw new JspException(messages.getMessage("generateExamsMap.infoExamsMap.notFound", name));
        }

        //		Generate Map from sections
        JspWriter writer = pageContext.getOut();
        SectionMenuMap sectionMenuMap = null;
        if (activeSection == null) {
            sectionMenuMap = new SectionMenuMap(sections);
        } else {
            sectionMenuMap = new SectionMenuMap(sections, activeSection);
        }

        SectionMenuMapRenderer renderer = new SectionMenuMapRenderer(sectionMenuMap,
                this.sectionMenuSlotContentRenderer, getPath(), getRenderer());

        try {
            writer.print(renderer.render());
        } catch (IOException e) {
            e.printStackTrace();
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

    public String getName() {
        return (this.name);
    }

    public void setName(String name) {
        this.name = name;
    }

    //	Error Messages
    protected static MessageResources messages = MessageResources
            .getMessageResources("ApplicationResources");

    /**
     * @return
     */
    public String getActiveSectionName() {
        return activeSectionName;
    }

    /**
     * @param string
     */
    public void setActiveSectionName(String string) {
        activeSectionName = string;
    }

    /**
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * @param string
     */
    public void setPath(String string) {
        path = string;
    }

    /**
     * @return
     */
    public String getRenderer() {
        return renderer;
    }

    /**
     * @param string
     */
    public void setRenderer(String string) {
        renderer = string;
    }

}