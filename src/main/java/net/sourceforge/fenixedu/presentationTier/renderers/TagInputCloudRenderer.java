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
import java.util.Properties;

import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit.UnitFileBean;
import pt.ist.fenixWebFramework.renderers.InputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlScript;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.components.HtmlTextInput;
import pt.ist.fenixWebFramework.renderers.contexts.PresentationContext;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;
import pt.ist.fenixWebFramework.renderers.utils.RenderKit;
import pt.ist.fenixWebFramework.renderers.utils.RenderMode;
import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class TagInputCloudRenderer extends InputRenderer {

    private String size;

    private String tagClasses;

    private String tagSort;

    private String textClasses;

    public String getTextClasses() {
        return textClasses;
    }

    public void setTextClasses(String textClasses) {
        this.textClasses = textClasses;
    }

    public String getTagSort() {
        return tagSort;
    }

    public void setTagSort(String tagSort) {
        this.tagSort = tagSort;
    }

    public String getTagClasses() {
        return tagClasses;
    }

    public void setTagClasses(String tagClasses) {
        this.tagClasses = tagClasses;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    protected Layout getLayout(Object object, Class type) {

        return new Layout() {

            @Override
            public HtmlComponent createComponent(Object object, Class type) {
                MetaSlot slot = (MetaSlot) getContext().getMetaObject();

                HtmlBlockContainer container = new HtmlBlockContainer();

                String tags = (String) object;
                HtmlTextInput input = new HtmlTextInput();
                if (getSize() != null) {
                    input.setSize(getSize());
                }
                input.setValue(tags);
                input.bind(slot);
                input.setId(input.getName());

                TagCloudRenderer tagCloud = new TagCloudRenderer();
                tagCloud.setClasses(getTagClasses());
                tagCloud.setSortBy(getTagSort());
                tagCloud.setLinkFormat("#");
                tagCloud.setOnClick(getAction(input.getName()));
                tagCloud.setOnDblClick(getAction(input.getName()));
                Collection<UnitFileTag> tagList = getTags();

                PresentationContext newContext = getContext().createSubContext(getContext().getMetaObject());
                newContext.setProperties(new Properties());
                newContext.setRenderMode(RenderMode.getMode("output"));

                container.addChild(getScript());
                container.addChild(input);
                container.addChild(new HtmlText("<br/>", false));
                HtmlText text =
                        new HtmlText(RenderUtils.getResourceString("RENDERER_RESOURCES",
                                "renderers.label.tags.are.space.separated"));
                text.setClasses(getTextClasses());
                container.addChild(text);
                container.addChild(RenderKit.getInstance().renderUsing(tagCloud, newContext, tagList, tagList.getClass()));

                return container;

            }

            private HtmlComponent getScript() {
                HtmlScript script = new HtmlScript();

                script.setContentType("text/javascript");
                script.setConditional(true);
                script.setScript("\n" + "function addTag(field, tag) {\n" + "var element = document.getElementById(field);\n"
                        + "var tags = element.value;\n"
                        + "if (!tags.match('^' + tag + '$|\\\\s' + tag + '\\\\s|\\\\s' + tag + '$')) {\n"
                        + "if (tags.length > 0) {\n" + "element.value = tags + ' ' + tag;\n" + "}\n" + "else {\n"
                        + "element.value = tag;\n" + "}\n" + "element.focus();\n" + "}\n" + "}\n");
                return script;
            }

            private Collection<UnitFileTag> getTags() {
                UnitFileBean bean = (UnitFileBean) getContext().getParentContext().getMetaObject().getObject();
                return bean.getUnit().getUnitFileTags();
            }

            private String getAction(String name) {
                return "addTag('" + name + "','${name}');";
            }

        };
    }

}
