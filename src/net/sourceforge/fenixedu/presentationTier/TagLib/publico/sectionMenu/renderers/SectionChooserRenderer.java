/*
 * Created on 11/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers;

import net.sourceforge.fenixedu.dataTransferObject.InfoSection;

/**
 * @author Ivo Brandão
 */
public class SectionChooserRenderer implements ISectionMenuSlotContentRenderer {

    private InfoSection infoSection;

    public SectionChooserRenderer(InfoSection infoSection) {
        setInfoSection(infoSection);
    }

    public SectionChooserRenderer() {
    }

    /**
     * @see ServidorApresentacao.TagLib.publico.sectionMenu.renderers.ISectionMenuSlotContentRenderer#renderSectionLabel(int,
     *      java.lang.String)
     */
    public StringBuffer renderSectionLabel(int i, String path, boolean hasChilds) {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(renderDepthContent(getInfoSection(), i, path, hasChilds));
        return strBuffer;
    }

    private StringBuffer renderDepthContent(InfoSection infoSection, int i, String path,
            boolean hasChilds) {
        StringBuffer strBuffer = new StringBuffer();

        strBuffer.append(renderDepthIdent(getInfoSection()));
        strBuffer.append("<a href=\"editSection.do?method=changeParent&amp;sectionIndex=" + i + "\" >");

        strBuffer.append(infoSection.getName());

        strBuffer.append("</a><br>");

        return strBuffer;
    }

    private StringBuffer renderDepthIdent(InfoSection infoSection) {
        StringBuffer strBuffer = new StringBuffer();
        int depth = infoSection.getSectionDepth().intValue();

        while (depth > 0) {
            strBuffer.append("&nbsp&nbsp");
            depth--;
        }
        return strBuffer;
    }

    /**
     * @return
     */
    public InfoSection getInfoSection() {
        return infoSection;
    }

    /**
     * @param section
     */
    public void setInfoSection(InfoSection section) {
        infoSection = section;
    }
}