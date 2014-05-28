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

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Site;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlLink;
import pt.ist.fenixWebFramework.renderers.components.HtmlLinkWithPreprendedComment;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;

/**
 * 
 * Renderers a Unit name with it's website if available. Can also display it's
 * parent Unit and it's like.
 * 
 * @author pcma
 * 
 */
public class UnitSiteRenderer extends OutputRenderer {

    private String classes;

    private String unitSchema;

    private String unitLayout;

    private boolean parenteShown;

    private boolean targetBlank;

    private boolean moduleRelative;

    private boolean contextRelative;

    private String separator;

    private String style;

    public boolean isContextRelative() {
        return contextRelative;
    }

    public void setContextRelative(boolean contextRelative) {
        this.contextRelative = contextRelative;
    }

    public boolean isModuleRelative() {
        return moduleRelative;
    }

    public void setModuleRelative(boolean moduleRelative) {
        this.moduleRelative = moduleRelative;
    }

    @Override
    public String getClasses() {
        return classes;
    }

    @Override
    public void setClasses(String classes) {
        this.classes = classes;
    }

    public boolean isParenteShown() {
        return parenteShown;
    }

    public void setParenteShown(boolean parenteShown) {
        this.parenteShown = parenteShown;
    }

    public boolean isTargetBlank() {
        return targetBlank;
    }

    public void setTargetBlank(boolean targetBlank) {
        this.targetBlank = targetBlank;
    }

    public String getUnitSchema() {
        return unitSchema;
    }

    public void setUnitSchema(String unitSchema) {
        this.unitSchema = unitSchema;
    }

    public String getUnitLayout() {
        return unitLayout;
    }

    public void setUnitLayout(String unitLayout) {
        this.unitLayout = unitLayout;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new UnitSiteLayout();
    }

    private class UnitSiteLayout extends Layout {

        private Schema findSchema() {
            return RenderKit.getInstance().findSchema(getUnitSchema());
        }

        @Override
        public HtmlComponent createComponent(Object object, Class type) {
            Unit unit = (Unit) object;
            HtmlBlockContainer unitPresentation = new HtmlBlockContainer();
            unitPresentation.addChild(getUnitComponent(unit));
            if (isParenteShown()) {

                Collection<Unit> parentUnits = CollectionUtils.select(unit.getParentUnits(), new Predicate() {
                    @Override
                    public boolean evaluate(Object arg0) {
                        return !((Unit) arg0).isAggregateUnit();
                    }

                });

                if (!parentUnits.isEmpty()) {
                    unitPresentation.addChild(new HtmlText(getSeparator(), false));
                    int i = 0;
                    int size = parentUnits.size();
                    for (Unit parentUnit : parentUnits) {
                        unitPresentation.addChild(getUnitComponent(parentUnit));
                        if (i < size - 1) {
                            unitPresentation.addChild(new HtmlText(","));
                        }
                        i++;
                    }
                }
            }
            return unitPresentation;
        }

        private HtmlComponent getUnitComponent(Unit unit) {
            HtmlComponent component;
            if (unitHasSite(unit)) {
                HtmlLink link = new HtmlLinkWithPreprendedComment(GenericChecksumRewriter.NO_CHECKSUM_PREFIX);

                link.setUrl(resolveUnitURL(unit));
                link.setBody(renderValue(unit, findSchema(), getUnitLayout()));
                if (isTargetBlank()) {
                    link.setTarget("_blank");
                }
                link.setModuleRelative(isModuleRelative());
                link.setContextRelative(isContextRelative());
                component = link;
            } else {
                component = renderValue(unit, findSchema(), getUnitLayout());
            }
            return component;
        }

        private boolean unitHasSite(Unit unit) {
            return (unit.isDegreeUnit()) ? unit.getDegree().hasSite() : unit.hasSite();
        }

        private String resolveUnitURL(Unit unit) {
            Site site = unit.getSite();
            if (site == null) {
                return "";
            }

            return site.getReversePath();
        }
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    @Override
    public String getStyle() {
        return style;
    }

    @Override
    public void setStyle(String style) {
        this.style = style;
    }

}