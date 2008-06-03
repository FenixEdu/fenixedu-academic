package net.sourceforge.fenixedu.presentationTier.renderers;

import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.injectionCode.IGroup;
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

	    private HtmlComponent processGroupUnion(GroupUnion group) {
		HtmlContainer container = new HtmlInlineContainer();
		container.setIndented(false);

		int i = group.getChildren().size();

		for (IGroup child : group.getChildren()) {
		    if (child instanceof GroupUnion) {
			container.addChild(processGroupUnion((GroupUnion) child));
		    } else {
			container.addChild(new HtmlText(child.getName()));
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

		if (object instanceof GroupUnion) {
		    container.addChild(processGroupUnion((GroupUnion) object));
		} else {
		    IGroup group = (IGroup) object;
		    container.addChild(new HtmlText(group.getName()));
		}

		return container;
	    }

	};
    }

}
