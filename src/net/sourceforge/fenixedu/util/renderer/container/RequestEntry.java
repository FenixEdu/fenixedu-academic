/*
 * Created on Jul 5, 2004
 *
 */
package net.sourceforge.fenixedu.util.renderer.container;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Luis Cruz
 *  
 */
public class RequestEntry {
    private String requestPath = null;

    private int executionTime = 0;

    private int numberCalls = 0;

    private List logTimes = new ArrayList();

    private List executionTimes = new ArrayList();

    public RequestEntry(String requestPath) {
        super();
        this.requestPath = requestPath;
    }

    public int getExecutionTime() {
        return executionTime;
    }

    public int getNumberCalls() {
        return numberCalls;
    }

    public int getAverageExecutionTime() {
        if (numberCalls != 0) {
            return executionTime / numberCalls;
        }
        return 0;

    }

    public String getRequestPath() {
        return requestPath;
    }

    public List getLogTimes() {
        return logTimes;
    }

    public List getExecutionTimes() {
        return executionTimes;
    }

    public void addEntry(Integer executionTime, Date logTime) {
        executionTimes.add(executionTime);
        logTimes.add(logTime);
        this.executionTime = this.executionTime + executionTime.intValue();
        this.numberCalls++;
    }
}