package net.sourceforge.fenixedu.dataTransferObject.log.profiling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;

public class ServerBean implements Serializable {

    LocalDate date;
    String serverName;
    int minTimeSpent;
    int maxTimeSpent;
    int invocationCount;
    int averageTimeSpent;
    int totalTimeSpent;

    List<ModuleBean> modules;

    public ServerBean() {
        modules = new ArrayList<ModuleBean>();
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
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

    public int getInvocationCount() {
        return invocationCount;
    }

    public void setInvocationCount(int invocationCount) {
        this.invocationCount = invocationCount;
    }

    public int getAverageTimeSpent() {
        return averageTimeSpent;
    }

    public void setAverageTimeSpent(int averageTimeSpent) {
        this.averageTimeSpent = averageTimeSpent;
    }

    public int getTotalTimeSpent() {
        return totalTimeSpent;
    }

    public void setTotalTimeSpent(int totalTimeSpent) {
        this.totalTimeSpent = totalTimeSpent;
    }

    public List<ModuleBean> getModules() {
        return modules;
    }

    public void addModuleBean(ModuleBean module) {
        module.setServerName(getServerName());
        this.modules.add(module);
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
