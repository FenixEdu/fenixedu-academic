package net.sourceforge.fenixedu.renderers.layouts;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.HtmlList;
import net.sourceforge.fenixedu.renderers.components.HtmlListItem;

public abstract class ListLayout extends FlowLayout {

    public ListLayout() {
        super();
    }

    @Override
    protected HtmlComponent getContainer() {
        return new HtmlList();
    }

    @Override
    protected void addComponent(HtmlComponent container, HtmlComponent component) {
        HtmlList list = (HtmlList) container;
        
        HtmlListItem item = list.createItem();
        item.setBody(component);
    }
}
