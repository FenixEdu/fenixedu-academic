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
public class InfoSectionWithSuperiorSection extends InfoSection {

    /*
     * (non-Javadoc)
     * 
     * @see net.sourceforge.fenixedu.dataTransferObject.InfoSection#copyFromDomain(Dominio.Section)
     */
    public void copyFromDomain(Section section) {
        super.copyFromDomain(section);
        if (section != null && section.getSuperiorSection() != null) {
            setSuperiorInfoSection(InfoSectionWithSuperiorSection.newInfoFromDomain(section
                    .getSuperiorSection()));
        }
    }

    public static InfoSection newInfoFromDomain(Section section) {
        InfoSectionWithSuperiorSection infoSection = null;
        if (section != null) {
            infoSection = new InfoSectionWithSuperiorSection();
            infoSection.copyFromDomain(section);
        }
        return infoSection;
    }
}