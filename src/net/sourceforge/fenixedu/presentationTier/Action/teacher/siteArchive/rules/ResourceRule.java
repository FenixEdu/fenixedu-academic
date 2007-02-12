package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Resource;

/**
 * A <tt>ResourceRule</tt> transforms url and schedules a new resource for retrieval.
 * 
 * @author cfgi
 */
public class ResourceRule extends SimpleTransformRule {

    private String name;
    private List<Rule> rules;

    /**
     * Creates a new <tt>ResourceRule</tt> that transforms urls that match
     * <tt>pattern</tt> to <tt>replacement</tt> and schedules the
     * corresponding resource, named <tt>name</tt>, to be retrieved.
     * 
     * <p>
     * The resource name can be a pattern too and will be applyied to the
     * matching url. So this rule can create resources based on the matched url
     * and parameterized their name.
     * 
     * @param pattern
     *            the matching pattern
     * @param replacement
     *            the replacement pattern for the url
     * @param name
     *            the local name of the new resource
     */
    public ResourceRule(String pattern, String replacement, String name) {
        super(pattern, replacement);
        
        this.name = name;
        this.rules = new ArrayList<Rule>();
    }

    /**
     * Convenience constructor just like the previous but considers <tt></tt> to be 
     * the same as <tt>replacement</tt>
     * 
     * @param pattern the matching pattern
     * @param replacement the replacement pattern
     */
    public ResourceRule(String pattern, String replacement) {
        this(pattern, replacement, replacement);
    }
    
    public String getResourceName() {
        return this.name;
    }

    /**
     * Adds a new rule to be used by the resource created in {@link #getResource(String, String)}
     * 
     * @param rule a rule
     */
    public void addRule(Rule rule) {
        this.rules.add(rule);
    }
    
    /**
     * Adds all rules link in {@link #addRule(Rule)}. 
     */
    public void addAllRules(Collection<Rule> rules) {
        this.rules.addAll(rules);
    }
    
    /**
     * Creates a new resource with a name that results from applying the given
     * name pattern to the url. The <tt>transformedUrl</tt> is not used.
     */
    @Override
    public Resource getResource(String url, String transformedUrl) {
        Resource resource = new Resource(transform(url, getResourceName()), url);
        resource.addAllRules(this.rules);
        
        return resource;
    }
    
}
