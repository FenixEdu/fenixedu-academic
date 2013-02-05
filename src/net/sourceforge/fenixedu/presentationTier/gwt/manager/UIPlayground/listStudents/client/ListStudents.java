package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.listStudents.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

public class ListStudents implements EntryPoint {

    @Override
    public void onModuleLoad() {
        RootPanel content = RootPanel.get("gwt_content");
        if (content != null) {
            StudentCollectionWidget pupilsList = new StudentCollectionWidget();
            content.add(pupilsList);
        }
    }

}
