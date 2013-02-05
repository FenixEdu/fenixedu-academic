package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.helloFenix.client.widget;

import net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.helloFenix.client.GreetingsService;
import net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.helloFenix.client.GreetingsServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class HelloWidget extends Composite {

    public HelloWidget() {
        // obtain a reference to the service
        service = (GreetingsServiceAsync) GWT.create(GreetingsService.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) service;
        endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "greetings.gwt");

        initWidget(panel);
        panel.add(label, DockPanel.CENTER);
        panel.add(button, DockPanel.SOUTH);

        // click listener to get data from server
        button.addClickListener(new ButtonClickListener());
    }

    private class ButtonClickListener implements ClickListener {
        @Override
        public void onClick(Widget sender) {
            // call servlet to get data
            service.getGreetings(new AsyncCallback() {

                @Override
                public void onFailure(Throwable e) {
                    label.setText("Server call failed");
                }

                @Override
                public void onSuccess(Object obj) {
                    if (obj != null) {
                        label.setText(obj.toString());
                    } else {
                        label.setText("Server call returned nothing");
                    }
                }
            });
        }
    }

    private final GreetingsServiceAsync service;
    private final DockPanel panel = new DockPanel();
    private final Button button = new Button("Push me");
    private final Label label = new Label("Gweetenix example:");
}
