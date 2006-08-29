package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Site;

/**
 * This is the view class that contains information about the site domain
 * objects.
 * 
 * @author Joao Pereira
 * @author João Mota
 * @author Ivo Brandão
 */

public class InfoSite extends InfoObject implements ISiteComponent {

    private final DomainReference<Site> site;

    private InfoExecutionCourse infoExecutionCourse;

    public InfoSite(final Site site) {
        this.site = new DomainReference<Site>(site);
    }

    /**
     * @return the dynamicMailDistribution
     */
    public Boolean getDynamicMailDistribution() {
        return getSite().getDynamicMailDistribution();
    }

    /**
     * @return InfoExecutionCourse
     */
    public InfoExecutionCourse getInfoExecutionCourse() {
        return infoExecutionCourse;
    }

    /**
     * Sets the infoExecutionCourse.
     * 
     * @param infoExecutionCourse
     *            The infoExecutionCourse to set
     */
    public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
        this.infoExecutionCourse = infoExecutionCourse;
    }

    public boolean equals(Object obj) {
        return obj instanceof InfoSite && getSite() == ((InfoSite) obj).getSite();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getSite().toString();
    }

    /**
     * @return String
     */
    public String getAlternativeSite() {
        return getSite().getAlternativeSite();
    }

    /**
     * @return String
     */
    public String getMail() {
        return getSite().getMail();
    }

    /**
     * @return String
     */
    public String getInitialStatement() {
        return getSite().getInitialStatement();
    }

    /**
     * @return String
     */
    public String getIntroduction() {
        return getSite().getIntroduction();
    }

    /**
     * @return String
     */
    public String getStyle() {
        return getSite().getStyle();
    }

    public static InfoSite newInfoFromDomain(Site site) {
        return site == null ? null : new InfoSite(site);
    }

    @Override
    public Integer getIdInternal() {
        return getSite().getIdInternal();
    }

    private Site getSite() {
        return site == null ? null : site.getObject();
    }
}