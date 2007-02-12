package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules;

import java.util.regex.Pattern;

import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Resource;

/**
 * This rule simple transform url based on a matching pattern and a replacement
 * pattern. This rule never schedules a resource for retrieval.
 * 
 * @author cfgi
 */
public class SimpleTransformRule implements Rule {

    private String pattern;
    private String replacement;
    
    private Pattern compiled;

    public SimpleTransformRule(String pattern, String replacement) {
        this.pattern = pattern;
        this.replacement = replacement;
    }

    /**
     * @return the matching pattern given in the construction
     */
    public String getPattern() {
        return this.pattern;
    }

    /**
     * @return the replacement pattern given in the construction
     */
    public String getReplacement() {
        return this.replacement;
    }

    /**
     * Obtains the a compiled pattern from the current matching pattern. This
     * method does caching so that the pattern only gets compiled once.
     * 
     * @return a compiled pattern
     */
    protected Pattern getCompiledPattern() {
        if (this.compiled == null) {
            this.compiled = Pattern.compile(getPattern());
        }

        return this.compiled;
    }

    /**
     * @return <code>true</code> if url matches the pattern in {@link #getPattern()} 
     */
    public boolean matches(String url) {
        return getCompiledPattern().matcher(url).matches();
    }
    
    protected String transform(String url, String replacement) {
        return getCompiledPattern().matcher(url).replaceFirst(replacement);
    }
    
    /**
     * Transform the given url by applying {@link #getPattern()} to it.
     * 
     * @param url
     *            the url found in the page
     * @return the transformed url
     */
    public String transform(String url) {
        return transform(url, getReplacement());
    }

    /**
     * @return <code>null</code>
     */
    public Resource getResource(String url, String transformedUrl) {
        return null;
    }

    @Override
    public String toString() {
        return String.format("Rule['%s' -> '%s']", getPattern(), getReplacement());
    }

}
