/*
 * Created on 16/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.teacher;

import net.sourceforge.fenixedu.util.ProviderRegimeType;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class ServiceProviderRegime extends ServiceProviderRegime_Base {
    private ProviderRegimeType providerRegimeType;

    /**
     * @return Returns the providerRegimeType.
     */
    public ProviderRegimeType getProviderRegimeType() {
        return providerRegimeType;
    }

    /**
     * @param providerRegimeType
     *            The providerRegimeType to set.
     */
    public void setProviderRegimeType(ProviderRegimeType providerRegimeType) {
        this.providerRegimeType = providerRegimeType;
    }

}