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

import pt.ist.fenixWebFramework.rendererExtensions.AutoCompleteInputRenderer;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;
import pt.ist.fenixWebFramework.renderers.components.HtmlComponent;
import pt.ist.fenixWebFramework.renderers.components.HtmlHiddenField;
import pt.ist.fenixWebFramework.renderers.components.controllers.HtmlController;
import pt.ist.fenixWebFramework.renderers.components.state.IViewState;
import pt.ist.fenixWebFramework.renderers.components.state.ViewDestination;
import pt.ist.fenixWebFramework.renderers.model.MetaSlot;

/**
 * This renderer extends the {@link AutoCompleteInputRenderer} and allows to
 * perform a postback whenever a valid option listed as auto-completion is
 * chosen.
 * 
 * @author João Neves - JoaoRoxoNeves@ist.utl.pt
 */
public class AutoCompleteInputRendererWithPostBack extends AutoCompleteInputRenderer {

    private final String HIDDEN_NAME = "postback";

    private String destination;

    public String getDestination() {
        return destination;
    }

    /**
     * Allows to choose the postback destination. If this property is not
     * specified the default "postback" destination is used.
     * 
     * @property
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Override
    public HtmlComponent render(Object object, Class type) {
        String prefix =
                HtmlComponent.getValidIdOrName(((MetaSlot) getInputContext().getMetaObject()).getKey().toString()
                        .replaceAll("\\.", "_").replaceAll("\\:", "_"));

        HtmlHiddenField hidden = new HtmlHiddenField(prefix + HIDDEN_NAME, "");

        HtmlBlockContainer container = (HtmlBlockContainer) super.render(object, type);
        HtmlHiddenField hiddenValue = null;
        for (HtmlComponent childComponent : container.getChildren()) {
            if ((childComponent instanceof HtmlHiddenField) && (childComponent.getId().endsWith("_AutoComplete"))) {
                hiddenValue = (HtmlHiddenField) childComponent;
            }
        }
        hiddenValue.setOnChange("this.form." + prefix + HIDDEN_NAME + ".value='true';this.form.submit();");
        hiddenValue.setController(new PostBackController(hidden, getDestination()));

        container.addChild(hidden);

        return container;
    }

    private static class PostBackController extends HtmlController {

        private final HtmlHiddenField hidden;

        private final String destination;

        public PostBackController(HtmlHiddenField hidden, String destination) {
            this.hidden = hidden;
            this.destination = destination;
        }

        @Override
        public void execute(IViewState viewState) {
            if (hidden.getValue() != null && hidden.getValue().equalsIgnoreCase("true")) {
                String destinationName = this.destination == null ? "postback" : this.destination;
                ViewDestination destination = viewState.getDestination(destinationName);

                if (destination != null) {
                    viewState.setCurrentDestination(destination);
                } else {
                    viewState.setCurrentDestination("postBack");
                }

                hidden.setValue("false");
                viewState.setSkipValidation(true);
            }

        }

    }
}
