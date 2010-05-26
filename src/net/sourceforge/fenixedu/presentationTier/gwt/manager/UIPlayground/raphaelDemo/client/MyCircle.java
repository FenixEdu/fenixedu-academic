package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.raphaelDemo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.Window;

public class MyCircle implements EntryPoint {
  public void onModuleLoad() {
    MyDrawing d = new MyDrawing(Window.getClientWidth(),
        Window.getClientHeight());
    RootPanel.get("gwt_content").add(d);
  }
}
