/*
 * Created on 6/Jul/2004
 *
 */
package DataBeans;

import Dominio.ISection;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoSectionWithSuperiorSection extends InfoSection {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoSection#copyFromDomain(Dominio.ISection)
     */
    public void copyFromDomain(ISection section) {
        super.copyFromDomain(section);
        if (section != null && section.getSuperiorSection() != null) {
            setSuperiorInfoSection(InfoSectionWithSuperiorSection.newInfoFromDomain(section
                    .getSuperiorSection()));
        }
    }

    public static InfoSection newInfoFromDomain(ISection section) {
        InfoSectionWithSuperiorSection infoSection = null;
        if (section != null) {
            infoSection = new InfoSectionWithSuperiorSection();
            infoSection.copyFromDomain(section);
        }
        return infoSection;
    }
}