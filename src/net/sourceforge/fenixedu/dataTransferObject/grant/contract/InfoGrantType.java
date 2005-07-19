/*
 * Created on 19/Nov/2003
 */
package net.sourceforge.fenixedu.dataTransferObject.grant.contract;

import java.util.Date;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.grant.contract.IGrantType;

/**
 * @author Barbosa
 * @author Pica
 */

public class InfoGrantType extends InfoObject {

    private String name;

    private String sigla;

    private Integer minPeriodDays;

    private Integer maxPeriodDays;

    private Double indicativeValue;

    private String source;

    private Date state;

    /**
     * @return
     */
    public Double getIndicativeValue() {
        return indicativeValue;
    }

    /**
     * @param indicativeValue
     */
    public void setIndicativeValue(Double indicativeValue) {
        this.indicativeValue = indicativeValue;
    }

    /**
     * @return
     */
    public Integer getMaxPeriodDays() {
        return maxPeriodDays;
    }

    /**
     * @param maxPeriodDays
     */
    public void setMaxPeriodDays(Integer maxPeriodDays) {
        this.maxPeriodDays = maxPeriodDays;
    }

    /**
     * @return
     */
    public Integer getMinPeriodDays() {
        return minPeriodDays;
    }

    /**
     * @param minPeriodDays
     */
    public void setMinPeriodDays(Integer minPeriodDays) {
        this.minPeriodDays = minPeriodDays;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getSigla() {
        return sigla;
    }

    /**
     * @param sigla
     */
    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    /**
     * @return
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return
     */
    public Date getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(Date state) {
        this.state = state;
    }

    /**
     * @param GrantType
     */
    public void copyFromDomain(IGrantType grantType) {
        super.copyFromDomain(grantType);
        if (grantType != null) {
            setName(grantType.getName());
            setSigla(grantType.getSigla());
            setMinPeriodDays(grantType.getMinPeriodDays());
            setMaxPeriodDays(grantType.getMaxPeriodDays());
            setIndicativeValue(grantType.getIndicativeValue());
            setSource(grantType.getSource());
            setState(grantType.getState());
        }
    }

    /**
     * @param GrantType
     * @return
     */
    public static InfoGrantType newInfoFromDomain(IGrantType grantType) {
        InfoGrantType infoGrantType = null;
        if (grantType != null) {
            infoGrantType = new InfoGrantType();
            infoGrantType.copyFromDomain(grantType);
        }
        return infoGrantType;
    }

    public void copyToDomain(InfoGrantType infoGrantType, IGrantType grantType) {
        super.copyToDomain(infoGrantType, grantType);

        grantType.setName(infoGrantType.getName());
        grantType.setSigla(infoGrantType.getSigla());
        grantType.setMinPeriodDays(infoGrantType.getMinPeriodDays());
        grantType.setMaxPeriodDays(infoGrantType.getMaxPeriodDays());
        grantType.setIndicativeValue(infoGrantType.getIndicativeValue());
        grantType.setSource(infoGrantType.getSource());
        grantType.setState(infoGrantType.getState());
    }

}
