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

import net.sourceforge.fenixedu.domain.elections.DelegateElectionCandidacyPeriod;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionPeriod;
import net.sourceforge.fenixedu.domain.elections.DelegateElectionVotingPeriod;
import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.state.ViewDestination;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class DelegateElectionPeriodRenderer extends OutputRenderer {

    private static final String DEFAULT_FORMAT = "dd/MM/yyyy";

    private String bundle;

    private String dateFormat;

    private String linkFormat;

    private boolean contextRelative;

    private boolean moduleRelative;

    private String destination;

    private String currentPeriodClasses;

    private String dateHtmlSeparator;

    private String periodPostLabel;

    private boolean isPeriodPostLabelKey;

    public String getDateHtmlSeparator() {
        return dateHtmlSeparator;
    }

    public void setDateHtmlSeparator(String dateHtmlSeparator) {
        this.dateHtmlSeparator = dateHtmlSeparator;
    }

    public String getDateFormat() {
        return (dateFormat != null ? dateFormat : DEFAULT_FORMAT);
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * This property allows you to specify the period post label. (Ex.:
     * candidatos ou votos)
     * 
     * @property
     */
    public String getPeriodPostLabel() {
        return periodPostLabel;
    }

    public void setPeriodPostLabel(String periodPostLabel) {
        this.periodPostLabel = periodPostLabel;
    }

    public boolean isPeriodPostLabelKey() {
        return isPeriodPostLabelKey;
    }

    public void setIsPeriodPostLabelKey(boolean isPeriodPostLabelKey) {
        this.isPeriodPostLabelKey = isPeriodPostLabelKey;
    }

    public String getLinkFormat() {
        return this.linkFormat;
    }

    /**
     * This property allows you to specify the format of the final link. In this
     * format you can use properties of the object being presented. For example:
     * 
     * <code>
     *  format="/some/action.do?oid=${id}"
     * </code>
     * 
     * @see RenderUtils#getFormattedProperties(String, Object)
     * @property
     */
    public void setLinkFormat(String linkFormat) {
        this.linkFormat = linkFormat;
    }

    public boolean isContextRelative() {
        return this.contextRelative;
    }

    /**
     * Indicates that the link specified should be relative to the context of
     * the application and not to the current module. This also overrides the
     * module if a destination is specified.
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
     * Allows you to choose if the generated link is relative to the current
     * module. Note that if the link is not context relative then it also isn't
     * module relative.
     * 
     * @property
     */
    public void setModuleRelative(boolean moduleRelative) {
        this.moduleRelative = moduleRelative;
    }

    public String getDestination() {
        return this.destination;
    }

    /**
     * This property is an alternative to the use of the {@link #setLinkFormat(String) linkFormat}. With this property you can
     * specify the name of the view state destination that will be used. This
     * property allows you to select the concrete destination in each context
     * were this configuration is used.
     * 
     * @property
     */
    public void setDestination(String destination) {
        this.destination = destination;
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

    public String getCurrentPeriodClasses() {
        return currentPeriodClasses;
    }

    public void setCurrentPeriodClasses(String currentPeriodClasses) {
        this.currentPeriodClasses = currentPeriodClasses;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                DelegateElectionPeriod period = (DelegateElectionPeriod) object;

                if (period == null) {
                    return new HtmlText();
                }

                String postLabel = getElectionPeriodPostLabel(period);

                if (isLink() && getContext().getParentContext() != null) {
                    Object targetObject = getContext().getParentContext().getMetaObject().getObject(); // Election

                    HtmlLink link = getLink(targetObject);

                    HtmlBlockContainer container = new HtmlBlockContainer();
                    String periodResume = getPeriodResume(period, postLabel, true);
                    link.setBody(new HtmlText(periodResume, false));
                    link.setClasses(getCurrentPeriodClasses()); // Classes for
                    // current
                    // periods
                    container.addChild(link);
                    return container;
                } else {
                    String periodResume = getPeriodResume(period, postLabel, false);
                    return new HtmlText(periodResume, false);
                }
            }

            private boolean isLink() {
                return (getLinkFormat() != null ? true : false);
            }

            private HtmlLink getLink(Object usedObject) {
                HtmlLink link = new HtmlLink();

                String url;

                if (getDestination() != null) {
                    ViewDestination destination = getContext().getViewState().getDestination(getDestination());

                    if (destination != null) {
                        link.setModule(destination.getModule());
                        url = destination.getPath();
                    } else {
                        url = "#";
                    }
                } else {
                    if (getLinkFormat() != null) {
                        url = getLinkFormat();
                    } else {
                        url = "#";
                    }
                }

                link.setUrl(RenderUtils.getFormattedProperties(url, usedObject));

                link.setModuleRelative(isModuleRelative());
                link.setContextRelative(isContextRelative());

                return link;
            }

            private String getPeriodResume(DelegateElectionPeriod electionPeriod, String postLabel, boolean isLongResume) {
                postLabel = (postLabel != null ? "&nbsp;" + postLabel : "");

                String shortResume =
                        electionPeriod.getStartDate().toString(getDateFormat()) + getDateHtmlSeparator()
                                + electionPeriod.getEndDate().toString(getDateFormat());

                String longResume = null;
                if (electionPeriod instanceof DelegateElectionCandidacyPeriod) {
                    longResume =
                            "("
                                    + ((DelegateElectionCandidacyPeriod) electionPeriod).getDelegateElection().getCandidatesSet()
                                            .size() + postLabel + ")";
                } else {
                    longResume = "(" + ((DelegateElectionVotingPeriod) electionPeriod).getVotesSet().size() + postLabel + ")";
                }

                return (isLongResume ? shortResume + "<br/>" + longResume : shortResume);
            }

            private String getElectionPeriodPostLabel(DelegateElectionPeriod electionPeriod) {
                return (isPeriodPostLabelKey() ? RenderUtils.getResourceString(getBundle(), getPeriodPostLabel()) : getPeriodPostLabel());
            }

        };
    }

}