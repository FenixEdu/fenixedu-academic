package net.sourceforge.fenixedu.presentationTier.gwt.coordinator.xviews.XviewsYear.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface InarServiceAsync {
    
    public void getExecutionYear(String eyId, AsyncCallback<String> callback);
    
    public void getInar(String eyId, String dcpId, AsyncCallback<int[]> callback);
    
    public void getInarByCurricularYears(String eyId, String dcpId, AsyncCallback<int[][]> callback);
    
    public void getNumberOfCurricularYears(String dcpId, AsyncCallback<Integer> callback);
    
    public void getAverageByCurricularYears(String eyId, String dcpId, AsyncCallback<double[]> callback);

}
