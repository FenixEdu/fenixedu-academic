/*
 * Created on 7/Abr/2003
 *
 * 
 */
package ServidorApresentacao.TagLib.publico.sectionMenu;

import java.util.List;

import DataBeans.InfoSection;

/**
 * @author João Mota
 * 
 *  
 */
public class SectionMenuMap {
    private List sections;

    private InfoSection activeSection;

    /**
     *  
     */
    public SectionMenuMap(List infoSections, InfoSection activeSection) {
        setSections(infoSections);
        setActiveSection(activeSection);
    }

    public SectionMenuMap(List infoSections) {
        setSections(infoSections);
    }

    /**
     * @return List
     */
    public List getSections() {
        return sections;
    }

    /**
     * Sets the sections.
     * 
     * @param sections
     *            The sections to set
     */
    public void setSections(List sections) {
        this.sections = sections;
    }

    /**
     * @return
     */
    public InfoSection getActiveSection() {
        return activeSection;
    }

    /**
     * @param section
     */
    public void setActiveSection(InfoSection section) {
        InfoSection tempSection = section;
        while (tempSection.calculateDepth().intValue() != 0) {
            tempSection = tempSection.getSuperiorInfoSection();
        }
        activeSection = tempSection;
    }

}