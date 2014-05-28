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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sourceforge.fenixedu.domain.Section;
import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.cms.OldCmsSemanticURLHandler;
import net.sourceforge.fenixedu.domain.cms.TemplatedSection;
import net.sourceforge.fenixedu.domain.cms.TemplatedSectionInstance;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.Face;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlList;
import pt.ist.fenixWebFramework.renderers.components.HtmlListItem;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * This renderer is responsible for presenting a {@link net.sourceforge.fenixedu.domain.Site} as a menu. It uses all the
 * sections defined in the site and it's template to generate the menu and
 * expands some sections if they were selected. The renderer uses parameters to
 * interact with the environment, so some parameters like <tt>sectionID</tt> are
 * appended automatically and others, like the one indicated by {@link #setContextParam(String)}, are appened to mantain the
 * required context
 * for the target site.
 */
public class SiteMenuRenderer extends OutputRenderer {

    private boolean contextRelative;

    private boolean moduleRelative;

    private String sectionUrl;

    private String contextParam;

    private String empty;

    private String depthStyle;

    private String selectedTopItemClass = "menu-top-item-selected";

    private String selectedChildItemClass = "menu-child-item-selected";

    public SiteMenuRenderer() {
        super();

        setModuleRelative(true);
        setContextRelative(true);
    }

    public String getDepthStyle() {
        return depthStyle;
    }

    public void setDepthStyle(String depthStyle) {
        this.depthStyle = depthStyle;
    }

    public String getSelectedTopItemClass() {
        return selectedTopItemClass;
    }

    public void setSelectedTopItemClass(String selectedTopItemClass) {
        this.selectedTopItemClass = selectedTopItemClass;
    }

    public String getSelectedChildItemClass() {
        return selectedChildItemClass;
    }

    public void setSelectedChildItemClass(String selectedChildItemClass) {
        this.selectedChildItemClass = selectedChildItemClass;
    }

    public boolean isContextRelative() {
        return this.contextRelative;
    }

    /**
     * Indicates that the url for the sections ir not relative to the
     * applications context. This can be usefull to redirect to another
     * application or the remove dependencies on Struts.
     * 
     * @property
     */
    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    public boolean isModuleRelative() {
        return this.moduleRelative;
    }

    /**
     * Indicates that the link for sections is not relative to the module.
     * 
     * @property
     */
    public void setModuleRelative(boolean moduleRelative) {
        this.moduleRelative = moduleRelative;
    }

    public String getSectionUrl() {
        return this.sectionUrl;
    }

    /**
     * The url of the action responsible for showing the section content.
     * 
     * @property
     */
    public void setSectionUrl(String sectionUrl) {
        this.sectionUrl = sectionUrl;
    }

    public String getContextParam() {
        return this.contextParam;
    }

    /**
     * The name of the parameters that provides a context for this site.
     * 
     * @property
     */
    public void setContextParam(String contextParam) {
        this.contextParam = contextParam;
    }

    public String getEmpty() {
        return empty;
    }

    /**
     * Decides what to show when there are no sections to include in the menu.
     * 
     * @property
     */
    public void setEmpty(String empty) {
        this.empty = empty;
    }

    protected boolean accessTemplate() {
        return true;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                if (object == null) {
                    return new HtmlText();
                }

                HtmlList list = new HtmlList();
                HttpServletRequest request = getContext().getViewState().getRequest();

                List<CmsContent> selectedPath = getSelectedPath(request);

                Site site = (Site) object;

                Collection<? extends CmsContent> entries = getInitialEntries(site);

                if (entries.isEmpty()) {
                    return generateEmpty();
                }

                createList(site, list, entries, selectedPath, 0);
                return list;
            }

            private List<CmsContent> getSelectedPath(HttpServletRequest request) {
                List<CmsContent> contents = new ArrayList<>();
                CmsContent current = OldCmsSemanticURLHandler.getContent(request);
                while (current != null) {
                    contents.add(0, current);
                    current = current.getParent();
                }
                return contents;
            }

            public void createList(Site site, HtmlList list, Collection<? extends CmsContent> entries,
                    List<CmsContent> selectedPath, Integer depth) {
                for (CmsContent entry : entries) {
                    if (!entry.getVisible()) {
                        continue;
                    }

                    if (entry instanceof Section) {
                        Section section = (Section) entry;
                        HtmlListItem item = list.createItem();
                        item.addChild(generateComponent(site, entry, true, selectedPath, depth));
                        if (depth > 0) {
                            item.setStyle(getDepthStyle());
                        }

                        if (allowsSubMenus() && selectedPath.contains(section) && !section.getChildSet().isEmpty()) {
                            HtmlList subMenu = new HtmlList();
                            item.addChild(subMenu);
                            createList(site, subMenu, section.getOrderedSubSections(), selectedPath, depth + 1);
                        }
                    }
                }
            }

            private HtmlLink generateLink(Site site, CmsContent content, final HtmlComponent body, List<CmsContent> selectedPath,
                    Integer depth) {

                final String url =
                        content instanceof TemplatedSection ? site.getFullPath() + "/" + content.getNormalizedName().getContent() : content
                                .getFullPath();
                HtmlLink link = new HtmlLinkWithPreprendedComment(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);

                link.setContextRelative(false);
                link.setUrl(url);
                link.setBody(body);

                if (selectedPath.contains(content)
                        || (content instanceof TemplatedSectionInstance && selectedPath
                                .contains(((TemplatedSectionInstance) content).getSectionTemplate()))) {
                    if (depth == 0) {
                        link.addClass(getSelectedTopItemClass());
                    } else if (depth > 0) {
                        link.addClass(getSelectedChildItemClass());
                    }
                }

                String contextParamValue = getContextParamValue();
                if (contextParamValue != null) {
                    link.setParameter(getContextParam(), contextParamValue);
                }

                return link;
            }

            public HtmlComponent generateComponent(Site site, CmsContent content, boolean canMakeLink,
                    List<CmsContent> selectedPath, Integer depth) {

                HtmlText text = new HtmlText(content.getName().getContent());
                text.setFace(Face.STANDARD);
                HtmlComponent component = generateLink(site, content, text, selectedPath, depth);

                MultiLanguageString title = content.getName();
                if (title != null && !title.isEmpty()) {
                    component.setTitle(title.getContent());
                }

                return component;
            }

            private HtmlComponent generateEmpty() {
                return new HtmlText(getEmpty(), false);
            }

            private String getContextParamValue() {
                String contextParam = getContextParam();

                if (contextParam == null) {
                    return null;
                }

                HttpServletRequest request = getContext().getViewState().getRequest();
                return request.getParameter(contextParam);
            }

        };
    }

    protected Site getSite(Object object) {
        return (Site) object;
    }

    protected boolean allowsSubMenus() {
        return true;
    }

    protected Collection<? extends CmsContent> getInitialEntries(Site site) {
        return site.getOrderedSections();
    }

}