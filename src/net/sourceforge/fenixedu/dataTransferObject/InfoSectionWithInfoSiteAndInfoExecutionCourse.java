/*
 * Created on 6/Jul/2004
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionCourseSite;
import net.sourceforge.fenixedu.domain.Section;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoSectionWithInfoSiteAndInfoExecutionCourse extends InfoSection {

    public void copyFromDomain(Section section) {
        super.copyFromDomain(section);
        if (section != null) {
            ExecutionCourseSite site = (ExecutionCourseSite) section.getSite();
            final InfoSite infoSite = InfoSite.newInfoFromDomain(site);
            infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(site.getExecutionCourse()));
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