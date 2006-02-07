/*
 * Created on 7/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers;

import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.fenixedu.dataTransferObject.InfoSection;

/**
 * @author João Mota
 * 
 *  
 */
public class SectionMenuTeacherContentRenderer extends TagSupport implements
        ISectionMenuSlotContentRenderer {

    private InfoSection infoSection;

    public SectionMenuTeacherContentRenderer() {
    }

    public SectionMenuTeacherContentRenderer(InfoSection infoSection) {
        setInfoSection(infoSection);
    }

    public StringBuilder renderSectionLabel(int i, String path, boolean hasChilds) {
        StringBuilder strBuffer = new StringBuilder();
        strBuffer.append(renderDepthContent(getInfoSection(), i, path, hasChilds));
        return strBuffer;
    }

    private StringBuilder renderDepthIdent(InfoSection infoSection) {
        StringBuilder strBuffer = new StringBuilder();
        int depth = infoSection.getSectionDepth().intValue();
        while (depth > 1) {
            strBuffer.append("&nbsp&nbsp");
            depth--;
        }
        return strBuffer;
    }

    private StringBuilder renderDepthContent(InfoSection infoSection, int i, String path,
            boolean hasChilds) {
        StringBuilder strBuffer = new StringBuilder();
        int depth = infoSection.getSectionDepth().intValue();
        if (depth == 0) {
            //adds the info

            strBuffer.append("<li>\n");
            strBuffer.append(renderDepthIdent(getInfoSection()));

            strBuffer.append("<a href=\"" + path
                    + "/sectionManagement.do?method=viewSection&objectCode="
                    + getInfoSection().getInfoSite().getInfoExecutionCourse().getIdInternal()
                    + "&currentSectionCode=" + getInfoSection().getIdInternal() + "\">\n");
            //" onclick=\"houdini('"+infoSection.getName()+"');\

            strBuffer.append(infoSection.getName());

            strBuffer.append("</a>");
            strBuffer.append("</li>\n");
        } else {
            //adds the info
            strBuffer.append("<dd>");

            strBuffer.append(renderDepthIdent(getInfoSection()));
            strBuffer.append("<a href=\"sectionManagement.do?method=viewSection&objectCode="
                    + getInfoSection().getInfoSite().getInfoExecutionCourse().getIdInternal()
                    + "&currentSectionCode=" + getInfoSection().getIdInternal() + "\">");
            //falta o index

            strBuffer.append(infoSection.getName());

            strBuffer.append("</a>");
            strBuffer.append("</dd>\n");
        }

        return strBuffer;
    }

    /**
     * @return InfoSection
     */
    public InfoSection getInfoSection() {
        return infoSection;
    }

    /**
     * Sets the infoSection.
     * 
     * @param infoSection
     *            The infoSection to set
     */
    public void setInfoSection(InfoSection infoSection) {
        this.infoSection = infoSection;
    }

}