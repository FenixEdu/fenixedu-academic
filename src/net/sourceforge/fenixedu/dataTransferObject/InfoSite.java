package net.sourceforge.fenixedu.dataTransferObject;

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

    private final Site site;

    private InfoExecutionCourse infoExecutionCourse;

    public InfoSite(final Site site) {
        this.site = site;
    }

    /**
     * @return the dynamicMailDistribution
     */
    public Boolean getDynamicMailDistribution() {
        return site.getDynamicMailDistribution();
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
        return obj instanceof InfoSite && site == ((InfoSite) obj).site;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return site.toString();
    }

    /**
     * @return String
     */
    public String getAlternativeSite() {
        return site.getAlternativeSite();
    }

    /**
     * @return String
     */
    public String getMail() {
        return site.getMail();
    }

    /**
     * @return String
     */
    public String getInitialStatement() {
        return site.getInitialStatement();
    }

    /**
     * @return String
     */
    public String getIntroduction() {
        return site.getIntroduction();
    }

    /**
     * @return String
     */
    public String getStyle() {
        return site.getStyle();
    }

    public static InfoSite newInfoFromDomain(Site site) {
        return site == null ? null : new InfoSite(site);
    }

    @Override
    public Integer getIdInternal() {
        return site.getIdInternal();
    }

}