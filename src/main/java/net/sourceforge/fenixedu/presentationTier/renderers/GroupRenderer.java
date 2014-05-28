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

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;

import pt.ist.fenixWebFramework.renderers.OutputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlInlineContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlText;
import pt.ist.fenixWebFramework.renderers.layouts.Layout;

public class GroupRenderer extends OutputRenderer {

    @Override
    protected Layout getLayout(Object object, Class type) {
        return new Layout() {

            private HtmlComponent processUnionGroup(UnionGroup object) {
                HtmlContainer container = new HtmlInlineContainer();
                container.setIndented(false);

                int i = object.getChildren().size();

                for (Group child : object.getChildren()) {
                    if (child instanceof UnionGroup) {
                        container.addChild(processUnionGroup((UnionGroup) child));
                    } else {
                        container.addChild(new HtmlText(child.getPresentationName()));
                    }
                    i--;
                    if (i > 0) {
                        container.addChild(new HtmlText(", "));
                    }
                }

                return container;
            }

            @Override
            public HtmlComponent createComponent(Object object, Class type) {

                HtmlInlineContainer container = new HtmlInlineContainer();

                if (object instanceof UnionGroup) {
                    container.addChild(processUnionGroup((UnionGroup) object));
                } else {
                    Group group = (Group) object;
                    container.addChild(new HtmlText(group.getPresentationName()));
                }

                return container;
            }

        };
    }

}
