/*
 * Created on 6/Jul/2004
 *
 */
package DataBeans;

import Dominio.ISite;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoSiteWithInfoExecutionCourse extends InfoSite {

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.InfoSite#copyFromDomain(Dominio.ISite)
     */
    public void copyFromDomain(ISite site) {
        super.copyFromDomain(site);
        if (site != null) {
            setInfoExecutionCourse(InfoExecutionCourseWithExecutionPeriod.newInfoFromDomain(site
                    .getExecutionCourse()));
        }
    }

    public static InfoSite newInfoFromDomain(ISite site) {
        InfoSiteWithInfoExecutionCourse infoSite = null;
        if (site != null) {
            infoSite = new InfoSiteWithInfoExecutionCourse();
            infoSite.copyFromDomain(site);
        }
        return infoSite;
    }
}