package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.helloFenix.client;

import net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.helloFenix.client.widget.HelloWidget;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class HelloFenix implements EntryPoint {

    @Override
    public void onModuleLoad() {
        // set widget on "content" element
        RootPanel content = RootPanel.get("gwt_content");
        if (content != null) {
            content.add(new HelloWidget());
        }
    }

}
