/*
 * Created on Dec 25, 2003
 *
 */
package ServidorAplicacao.logging;

import java.io.Serializable;
import java.util.Properties;

/**
 * @author Luis Cruz
 *  
 */
public class SystemInfo implements Serializable {

    private Integer availableProcessors;

    private Long freeMemory;

    private Long maxMemory;

    private Long totalMemory;

    private Properties properties;

    /**
     *  
     */
    public SystemInfo() {
        super();
        Runtime rt = Runtime.getRuntime();
        this.availableProcessors = new Integer(rt.availableProcessors());
        this.freeMemory = new Long(rt.freeMemory());
        this.maxMemory = new Long(rt.maxMemory());
        this.totalMemory = new Long(rt.totalMemory());
        this.properties = System.getProperties();
    }

    /**
     * @return Returns the availableProcessors.
     */
    public Integer getAvailableProcessors() {
        return availableProcessors;
    }

    /**
     * @return Returns the freeMemory.
     */
    public Long getFreeMemory() {
        return freeMemory;
    }

    /**
     * @return Returns the maxMemory.
     */
    public Long getMaxMemory() {
        return maxMemory;
    }

    /**
     * @return Returns the totalMemory.
     */
    public Long getTotalMemory() {
        return totalMemory;
    }

    /**
     * @return Returns the properties.
     */
    public Properties getProperties() {
        return properties;
    }

}