/*
 * Created on Dec 24, 2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.logging;

import java.io.Serializable;

/**
 * @author Luis Cruz
 *  
 */
public class ServiceExecutionLog implements Serializable {

    private String serviceFootPrint;

    private long totalExecutionTime;

    private long lastExecutionTime;

    private int numberCalls;

    /**
     *  
     */
    public ServiceExecutionLog(String serviceFootPrint) {
        super();
        this.serviceFootPrint = serviceFootPrint;
        this.numberCalls = 0;
    }

    /**
     * @return Returns the serviceFootPrint.
     */
    public String getServiceFootPrint() {
        return serviceFootPrint;
    }

    /**
     * @return Returns the lastExecutionTime.
     */
    public long getLastExecutionTime() {
        return lastExecutionTime;
    }

    /**
     * @return Returns the totalExecutionTime.
     */
    public long getTotalExecutionTime() {
        return totalExecutionTime;
    }

    /**
     * @return Returns the numberCalls.
     */
    public int getNumberCalls() {
        return numberCalls;
    }

    public void addExecutionTime(long lastExecutionTime) {
        this.lastExecutionTime = lastExecutionTime;
        this.totalExecutionTime += lastExecutionTime;
        this.numberCalls++;
    }

    /**
     * @return Calculates the average execution time of the service.
     */
    public long getAverageExecutionTime() {
        return this.totalExecutionTime / this.numberCalls;
    }

}