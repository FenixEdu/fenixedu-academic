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
package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.File;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

/**
 * This render is used to create a link to a File object. The form of the link
 * depends on the location of the contents of the file.
 * 
 * @author Pedro Santos
 */
public class FileLinkRenderer extends OutputRenderer {

    private String key;

    private String bundle;

    private String text;

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object != null && object instanceof File) {
                    File file = (File) object;
                    HtmlBlockContainer container = new HtmlBlockContainer();
                    HtmlLink link = getLink(file);
                    container.addChild(link);
                    link.setIndented(false);
                    link.setText(getLinkText(file));
                    return container;
                }
                return new HtmlLink();
            }

            private HtmlLink getLink(File file) {
                HtmlLink link = new HtmlLinkWithPreprendedComment(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);
//		if (file.hasLocalContent()) {
//		    link.setContextRelative(true);
//		} else {
                link.setContextRelative(false);
//		}
                link.setModuleRelative(false);
                link.setUrl(file.getDownloadUrl());
                return link;
            }

            private String getLinkText(File file) {
                if (getKey() != null) {
                    return RenderUtils.getResourceString(getBundle(), getKey());
                }
                if (!StringUtils.isEmpty(getText())) {
                    return getText();
                }
                return file.getDisplayName();
            }
        };
    }

    public String getKey() {
        return this.key;
    }

    /**
     * Instead of specifying thr {@link #setText(String) text} property you can
     * specify a key, with this property, and a bundle with the {@link #setBundle(String) bundle}.
     * 
     * @property
     */
    public void setKey(String key) {
        this.key = key;
    }

    public String getBundle() {
        return this.bundle;
    }

    /**
     * The bundle were the {@link #setKey(String) key} will be fetched.
     * 
     * @property
     */
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
