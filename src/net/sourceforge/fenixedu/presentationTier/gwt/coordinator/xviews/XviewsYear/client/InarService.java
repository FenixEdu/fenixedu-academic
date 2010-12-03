package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("InarService.gwt")
public interface InarService extends RemoteService{
    
    public String getExecutionYear(String eyId);
    
    public int[] getInar(String eyId, String dcpId);
    
    public int[][] getInarByCurricularYears(String eyId, String dcpId);
    
    public int getNumberOfCurricularYears(String dcpId);
    
    public double[] getAverageByCurricularYears(String eyId, String dcpId);

}
