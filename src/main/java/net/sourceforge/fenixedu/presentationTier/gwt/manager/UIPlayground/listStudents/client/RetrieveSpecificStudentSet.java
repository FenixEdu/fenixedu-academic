package net.sourceforge.fenixedu.presentationTier.gwt.manager.UIPlayground.listStudents.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("RetrieveSpecificStudentSet.gwt")
public interface RetrieveSpecificStudentSet extends RemoteService {

    public List<String> getStudentNames();

}
