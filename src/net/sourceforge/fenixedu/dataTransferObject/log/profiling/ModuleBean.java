package net.sourceforge.fenixedu.dataTransferObject.log.profiling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

public class ModuleBean implements Serializable {

    LocalDate date;
    String serverName;
    String moduleName;
    int invocationCount;
    int minTimeSpent;
    int maxTimeSpent;
    int totalTimeSpent;

    List<RequestBean> requests;

    public ModuleBean() {
        this.requests = new ArrayList<RequestBean>();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getInvocationCount() {
        return invocationCount;
    }

    public void setInvocationCount(int invocationCount) {
        this.invocationCount = invocationCount;
    }

    public int getMinTimeSpent() {
        return minTimeSpent;
    }

    public void setMinTimeSpent(int minTimeSpent) {
        this.minTimeSpent = minTimeSpent;
    }

    public int getMaxTimeSpent() {
        return maxTimeSpent;
    }

    public void setMaxTimeSpent(int maxTimeSpent) {
        this.maxTimeSpent = maxTimeSpent;
    }

    public int getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public void setTotalTimeSpent(int totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    public List<RequestBean> getRequests() {
        return requests;
    }

    public void addRequestBean(RequestBean bean) {
        this.requests.add(bean);
        bean.setModuleName(getModuleName());
        bean.setServerName(getServerName());
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
