package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InarService.gwt")
public interface InarService extends RemoteService{
    
    public String getExecutionYear(String eyId);
    
    public int[] getInar(String eyId, String dcpId);
    
    public int[][] getInarByCurricularYears(String eyId, String dcpId);
    
    public int getNumberOfCurricularYears(String dcpId);
    
    public double[] getAverageByCurricularYears(String eyId, String dcpId);
    
    public Map<Integer, Map<Integer, List<String>>> getDCPCourses(String eyId, String dcpId, String heuristic);
    
    public String getCourseName(String ecId);
    
    public String[] getCourseInarLabel(String ecId);
    
    public int[] getInarByExecutionCourse(String ecId);
    
    public int[] getGradesDistribution(String ecId);

}
