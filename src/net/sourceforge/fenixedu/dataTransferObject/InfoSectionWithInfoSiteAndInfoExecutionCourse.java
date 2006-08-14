/*
 * Created on 6/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Section;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoSectionWithInfoSiteAndInfoExecutionCourse extends InfoSection {

    public void copyFromDomain(Section section) {
        super.copyFromDomain(section);
        if (section != null) {
            final InfoSite infoSite = InfoSite.newInfoFromDomain(section.getSite());
            infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(section.getSite().getExecutionCourse()));
            setInfoSite(infoSite);
        }
    }

    public static InfoSection newInfoFromDomain(Section section) {
        InfoSectionWithInfoSiteAndInfoExecutionCourse infoSection = null;
        if (section != null) {
            infoSection = new InfoSectionWithInfoSiteAndInfoExecutionCourse();
            infoSection.copyFromDomain(section);
        }
        return infoSection;
    }
}