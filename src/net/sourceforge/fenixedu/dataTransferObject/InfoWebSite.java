package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.IWebSite;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author Fernanda Quitério 24/09/2003
 *  
 */
public class InfoWebSite extends InfoObject {

    private String name;

    private List sections;

    public InfoWebSite() {
    }

    public InfoWebSite(String name, List sections) {
        setName(name);
        setSections(sections);

    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof InfoTeacher) {
            InfoWebSite infoWebSite = (InfoWebSite) obj;

            result = ((infoWebSite != null)
                    && (CollectionUtils.isEqualCollection(getSections(), infoWebSite.getSections())) && ((infoWebSite
                    .getName() == null && this.getName() == null) || (infoWebSite.getName() != null
                    && this.getName() != null && infoWebSite.getName().equals(this.getName()))));
        }
        return result;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public List getSections() {
        return sections;
    }

    /**
     * @param sections
     */
    public void setSections(List sections) {
        this.sections = sections;
    }

    public void copyFromDomain(final IWebSite webSite) {
        super.copyFromDomain(webSite);
        if (webSite != null) {
            setName(webSite.getName());
        }
    }

    public static InfoWebSite newInfoFromDomain(final IWebSite webSite) {
        if (webSite != null) {
            final InfoWebSite infoWebSite = new InfoWebSite();
            infoWebSite.copyFromDomain(webSite);
            return infoWebSite;
        }
        return null;
    }
}