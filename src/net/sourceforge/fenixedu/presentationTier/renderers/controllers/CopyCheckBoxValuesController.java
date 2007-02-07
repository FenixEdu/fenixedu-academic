/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.renderers.controllers;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.renderers.components.HtmlCheckBox;
import net.sourceforge.fenixedu.renderers.components.HtmlMultipleValueComponent;
import net.sourceforge.fenixedu.renderers.components.controllers.HtmlController;
import net.sourceforge.fenixedu.renderers.components.state.IViewState;

public class CopyCheckBoxValuesController extends HtmlController {

    private List<HtmlCheckBox> checkboxes;

    private boolean copyTrueValues;

    public CopyCheckBoxValuesController() {
	super();

	this.checkboxes = new ArrayList<HtmlCheckBox>();
	this.copyTrueValues = true;
    }

    public CopyCheckBoxValuesController(final boolean copyTrueValues) {
	this();
	this.copyTrueValues = copyTrueValues;
    }

    public void addCheckBox(HtmlCheckBox checkBox) {
	this.checkboxes.add(checkBox);
    }

    @Override
    public void execute(IViewState viewState) {
	HtmlMultipleValueComponent component = (HtmlMultipleValueComponent) getControlledComponent();

	List<String> values = new ArrayList<String>();

	for (HtmlCheckBox checkBox : this.checkboxes) {
	    if (checkBox.isChecked() == copyTrueValues) {
		values.add(checkBox.getValue());
	    }
	}

	component.setValues(values.toArray(new String[0]));
    }
}