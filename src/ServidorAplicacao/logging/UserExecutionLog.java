/*
 * Created on Dec 24, 2003
 *
 */
package ServidorAplicacao.logging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.FastHashMap;

import ServidorAplicacao.IUserView;

/**
 * @author Luis Cruz
 *  
 */
public class UserExecutionLog implements Serializable {

    private IUserView userView;

    private int numberCalls;

    private FastHashMap mapServicesLog;

    /**
     *  
     */
    public UserExecutionLog(IUserView userView) {
        super();
        this.userView = userView;
        this.numberCalls = 0;
        this.mapServicesLog = new FastHashMap();
        this.mapServicesLog.setFast(true);
    }

    /**
     * @return Returns the numberCalls.
     */
    public int getNumberCalls() {
        return numberCalls;
    }

    /**
     * @return Returns the userView.
     */
    public IUserView getUserView() {
        return userView;
    }

    /**
     * @return Returns the mapServicesLog.
     */
    public FastHashMap getMapServicesLog() {
        return mapServicesLog;
    }

    /**
     * @param string
     * @param serviceStartTime
     */
    public void addServiceCall(String serviceFootPrint, Calendar serviceStartTime) {
        List listCallTimes = (List) mapServicesLog.get(serviceFootPrint);
        if (listCallTimes == null) {
            listCallTimes = new ArrayList();
            mapServicesLog.put(serviceFootPrint, listCallTimes);
        }
        listCallTimes.add(Calendar.getInstance());
        numberCalls++;
    }

}