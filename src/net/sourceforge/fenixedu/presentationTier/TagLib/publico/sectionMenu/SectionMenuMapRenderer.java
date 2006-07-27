/*
 * Created on 7/Abr/2003
 *
 * 
 */
package net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoSection;
import net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers.ISectionMenuSlotContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers.SectionChooserRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers.SectionMenuContentRenderer;
import net.sourceforge.fenixedu.presentationTier.TagLib.publico.sectionMenu.renderers.SectionMenuTeacherContentRenderer;

/**
 * @author João Mota
 * 
 *  
 */
public class SectionMenuMapRenderer {
    private SectionMenuMap sectionMenuMap;

    private ISectionMenuSlotContentRenderer sectionMenuSlotContentRenderer;

    private String path;

    private String renderer;

    /**
     *  
     */
    public SectionMenuMapRenderer(SectionMenuMap sectionMenuMap,
            ISectionMenuSlotContentRenderer sectionMenuSlotContentRenderer, String path, String renderer) {
        setSectionMenuMap(sectionMenuMap);
        setSectionMenuSlotContentRenderer(sectionMenuSlotContentRenderer);
        setPath(path);
        setRenderer(renderer);
    }

    /**
     * @return SectionMenuMap
     */
    public SectionMenuMap getSectionMenuMap() {
        return sectionMenuMap;
    }

    /**
     * @return SectionMenuSlotContentRenderer
     */
    public ISectionMenuSlotContentRenderer getSectionMenuSlotContentRenderer() {
        return sectionMenuSlotContentRenderer;
    }

    /**
     * Sets the sectionMenuMap.
     * 
     * @param sectionMenuMap
     *            The sectionMenuMap to set
     */
    public void setSectionMenuMap(SectionMenuMap sectionMenuMap) {
        this.sectionMenuMap = sectionMenuMap;
    }

    /**
     * Sets the sectionMenuSlotContentRenderer.
     * 
     * @param sectionMenuSlotContentRenderer
     *            The sectionMenuSlotContentRenderer to set
     */
    public void setSectionMenuSlotContentRenderer(
            ISectionMenuSlotContentRenderer sectionMenuSlotContentRenderer) {
        this.sectionMenuSlotContentRenderer = sectionMenuSlotContentRenderer;
    }

    public StringBuilder render() {
        StringBuilder strBuffer = new StringBuilder("");
        List sections = getSectionMenuMap().getSections();
        if (sections != null && !sections.isEmpty()) {
            int i = 0;

            while (i != sections.size()) {

                InfoSection infoSection = (InfoSection) sections.get(i);

                ISectionMenuSlotContentRenderer sectionMenuSlot = getContentRenderer(infoSection,
                        getRenderer());

                boolean hasChilds = false;
                if ((i + 1) < sections.size()) {
                    InfoSection nextInfoSection = (InfoSection) sections.get(i + 1);
                    hasChilds = nextInfoSection.getSectionDepth().intValue() > infoSection
                            .getSectionDepth().intValue();
                }

                strBuffer.append(sectionMenuSlot.renderSectionLabel(i, getPath(), hasChilds));
                strBuffer.append(renderSuffix(sections, i));

                i++;
            }
        }
        return strBuffer;
    }

    private StringBuilder renderSuffix(List sections, int iterator) {
        StringBuilder strBuffer = new StringBuilder("");

        //section management
        if (renderer != null && renderer.equals("sectionChooser")) {
        }
        //main menu
        else {
            if (((InfoSection) sections.get(iterator)).getSectionDepth().intValue() != 0
                    && iterator == sections.size() - 1) {
                strBuffer.append("</dl></li>\n");

            } else {
                if (iterator != sections.size() - 1) {

                    if (((InfoSection) sections.get(iterator + 1)).getSectionDepth().intValue() == 0
                            && ((InfoSection) sections.get(iterator)).getSectionDepth().intValue() != 0) {
                        strBuffer.append("</dl></li>\n");
                    }
                    if (((InfoSection) sections.get(iterator + 1)).getSectionDepth().intValue() != 0
                            && ((InfoSection) sections.get(iterator)).getSectionDepth().intValue() == 0) {
                        strBuffer.append("<li><dl style=\"display:" + getStyle(sections, iterator) + ";\">\n");
                    }
                }
            }
        }

        return strBuffer;
    }

    private StringBuilder getStyle(List sections, int iterator) {
        StringBuilder strBuffer = new StringBuilder("");
        if (getSectionMenuMap().getActiveSection() == null
                || !((InfoSection) sections.get(iterator))
                        .equals(getSectionMenuMap().getActiveSection())) {
            strBuffer.append("none");
        }
        return strBuffer;
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

    private ISectionMenuSlotContentRenderer getContentRenderer(InfoSection infoSection, String renderer) {
        ISectionMenuSlotContentRenderer slotRenderer = new SectionMenuContentRenderer(infoSection);
        if (renderer == null) {
        }//do nothing
        else {
            if (renderer.equals("teacher")) {
                slotRenderer = new SectionMenuTeacherContentRenderer(infoSection);
            }
            if (renderer.equals("sectionChooser")) {
                slotRenderer = new SectionChooserRenderer(infoSection);
            }
        }
        return slotRenderer;
    }

}