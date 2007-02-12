package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules.Rule;

/**
 * A <tt>Resource</tt> represents something that needs to be retrieved by the
 * {@link net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Fetcher}.
 * 
 * <p>
 * A <tt>Resource</tt> defines a local name for the content being retrieved
 * and the url from where the content will be retrieved. The name must not start
 * with '/' but can represent a complex path like <tt>"parent/dir/file"</tt>.
 * 
 * <p>
 * Additionaly you can add rules to the resource. This rules are to be applyed
 * by the
 * {@link net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Fetcher}
 * to the content of the resource.
 * 
 * @author cfgi
 */
public class Resource {

    private String url;
    private String name;
    
    private List<Rule> rules;

    /**
     * Creates a new resource with the given local name and the given url.
     * 
     * @param name
     *            the resource local name
     * @param url
     *            the url from where the resource wil lbe retrieved
     */
    public Resource(String name, String url) {
        super();
    
        this.url = url;
        this.name = name;
        
        this.rules = new ArrayList<Rule>();
    }

    public String getName() {
        return this.name;
    }

    public String getUrl() {
        return this.url;
    }

    public List<Rule> getRules() {
        return this.rules;
    }

    public void addRule(Rule rule) {
        this.rules.add(rule);
    }
    
    public void addAllRules(Collection<Rule> rules) {
        this.rules.addAll(rules);
    }

    @Override
    public String toString() {
        return String.format("Resource['%s' -> '%s']", getUrl(), getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        
        if (! (obj instanceof Resource)) {
            return false;
        }
        
        Resource other = (Resource) obj;
        return getName().equals(other.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
    
}
