/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
