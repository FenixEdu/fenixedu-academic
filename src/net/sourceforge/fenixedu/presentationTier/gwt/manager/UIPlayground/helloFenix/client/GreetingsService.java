package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.helloFenix.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("greetings.gwt")
public interface GreetingsService extends RemoteService {

    public String getGreetings();

}
