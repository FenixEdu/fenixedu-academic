package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InarServiceAsync {
    
    public void getExecutionYear(String eyId, AsyncCallback<String> callback);
    
    public void getInar(String eyId, String dcpId, AsyncCallback<int[]> callback);
    
    public void getInarByCurricularYears(String eyId, String dcpId, AsyncCallback<int[][]> callback);
    
    public void getNumberOfCurricularYears(String dcpId, AsyncCallback<Integer> callback);
    
    public void getAverageByCurricularYears(String eyId, String dcpId, AsyncCallback<double[]> callback);
    
    public void getProblematicCourses(String eyId, String dcpId, AsyncCallback<Map<Integer, Map<Integer, List<String>>>> callback);
    
    public void getCourseName(String ecId, AsyncCallback<String> callback);
    
    public void getCourseInarLabel(String ecId, AsyncCallback<String[]> callback);
    
    public void getInarByExecutionCourse(String ecId, AsyncCallback<int[]> callback);
    
    public void getGradesDistribution(String ecId, AsyncCallback<int[]> callback);

}
