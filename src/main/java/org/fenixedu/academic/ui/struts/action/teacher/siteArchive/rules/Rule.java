/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.rules;

import net.sourceforge.fenixedu.presentationTier.Action.teacher.siteArchive.Resource;

/**
 * A <tt>Rule</tt> has the responsability of transforming urls present in a page
 * and possibly generate a new resource to be dowloaded. This allows rules to
 * download required resources and update the url present in the page to the new
 * local location of the downloaded resource.
 * 
 * @author cfgi
 */
public interface Rule {

    /**
     * Verifies if this rule matches the given url, that is, if it will
     * transform the url. If a rule answers <code>false</code> then calling {@link #transform(String)} will result in a url that
     * is equal to the
     * given on.
     * 
     * @param url
     *            the url found in the processed page
     * @return <code>true</code> if this rule will transform the given url
     */
    public boolean matches(String url);

    /**
     * Transforms the url. If {@link #matches(String)} returned <code>false</code> then this will not change the given url.
     * 
     * @param url
     *            the url found in the page
     * @return the transformed url
     */
    public String transform(String url);

    /**
     * Creates a new resource to be retrieved. This allows the rule to transform
     * a url to match a local path and then scheduçe the retrieval of a resource
     * to that local path.
     * 
     * @param url
     *            the url found in the page
     * @param transformedUrl
     *            the url returned by {@link #transform(String)}
     * 
     * @return a new resource to be downloaded or <code>null</code> if no extra
     *         resource needs to be downloaded
     */
    public Resource getResource(String url, String transformedUrl);

}
