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
public class InfoSectionWithAll extends InfoSection {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoSection#copyFromDomain(Dominio.ISection)
     */
    public void copyFromDomain(ISection section) {
        super.copyFromDomain(section);
        if (section != null) {
            setInfoSite(InfoSiteWithInfoExecutionCourse.newInfoFromDomain(section.getSite()));
            if (section.getSuperiorSection() != null) {
                setSuperiorInfoSection(InfoSectionWithAll
                        .newInfoFromDomain(section.getSuperiorSection()));
            }
        }
    }

    public static InfoSection newInfoFromDomain(ISection section) {
        InfoSectionWithAll infoSection = null;
        if (section != null) {
            infoSection = new InfoSectionWithAll();
            infoSection.copyFromDomain(section);
        }
        return infoSection;
    }
}