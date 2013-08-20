package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionCourseSite;

/**
 * This is the view class that contains information about the site domain
 * objects.
 * 
 * @author Joao Pereira
 * @author João Mota
 * @author Ivo Brandão
 */

public class InfoSite extends InfoObject implements ISiteComponent {

    private final ExecutionCourseSite site;

    private InfoExecutionCourse infoExecutionCourse;

    public InfoSite(final ExecutionCourseSite site) {
        this.site = site;
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

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoSite && getSite() == ((InfoSite) obj).getSite();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
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

    public static InfoSite newInfoFromDomain(ExecutionCourseSite site) {
        return site == null ? null : new InfoSite(site);
    }

    @Override
    public String getExternalId() {
        return getSite().getExternalId();
    }

    private ExecutionCourseSite getSite() {
        return site;
    }
}