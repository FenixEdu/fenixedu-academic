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
public class InfoSectionWithAll extends InfoSection {

    public void copyFromDomain(Section section) {
        super.copyFromDomain(section);
        if (section != null) {
            final InfoSite infoSite = InfoSite.newInfoFromDomain(section.getSite());
            infoSite.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(section.getSite().getExecutionCourse()));
            setInfoSite(infoSite);
            if (section.getSuperiorSection() != null) {
                setSuperiorInfoSection(InfoSectionWithAll
                        .newInfoFromDomain(section.getSuperiorSection()));
            }
        }
    }

    public static InfoSection newInfoFromDomain(Section section) {
        InfoSectionWithAll infoSection = null;
        if (section != null) {
            infoSection = new InfoSectionWithAll();
            infoSection.copyFromDomain(section);
        }
        return infoSection;
    }
}