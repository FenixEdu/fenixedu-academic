/*
 * Created on Jul 7, 2004
 *
 */
package DataBeans.grant.stat;

import DataBeans.InfoObject;

/**
 * @author Pica
 * @author Barbosa
 *  
 */
public class InfoStatResultGrantOwner extends InfoObject {

    private Integer totalNumberOfGrantOwners;

    private Integer numberOfGrantOwnerByCriteria;

    private Integer totalNumberOfGrantContracts;

    private Integer numberOfGrantContractsByCriteria;

    public InfoStatResultGrantOwner() {
    }

    /**
     * @return Returns the numberOfGrantContractsByCriteria.
     */
    public Integer getNumberOfGrantContractsByCriteria() {
        return this.numberOfGrantContractsByCriteria;
    }

    /**
     * @param numberOfGrantContractsByCriteria
     *            The numberOfGrantContractsByCriteria to set.
     */
    public void setNumberOfGrantContractsByCriteria(Integer numberOfGrantContractsByCriteria) {
        this.numberOfGrantContractsByCriteria = numberOfGrantContractsByCriteria;
    }

    /**
     * @return Returns the numberOfGrantOwnerByCriteria.
     */
    public Integer getNumberOfGrantOwnerByCriteria() {
        return this.numberOfGrantOwnerByCriteria;
    }

    /**
     * @param numberOfGrantOwnerByCriteria
     *            The numberOfGrantOwnerByCriteria to set.
     */
    public void setNumberOfGrantOwnerByCriteria(Integer numberOfGrantOwnerByCriteria) {
        this.numberOfGrantOwnerByCriteria = numberOfGrantOwnerByCriteria;
    }

    /**
     * @return Returns the totalNumberOfGrantContracts.
     */
    public Integer getTotalNumberOfGrantContracts() {
        return this.totalNumberOfGrantContracts;
    }

    /**
     * @param totalNumberOfGrantContracts
     *            The totalNumberOfGrantContracts to set.
     */
    public void setTotalNumberOfGrantContracts(Integer totalNumberOfGrantContracts) {
        this.totalNumberOfGrantContracts = totalNumberOfGrantContracts;
    }

    /**
     * @return Returns the totalNumberOfGrantOwners.
     */
    public Integer getTotalNumberOfGrantOwners() {
        return this.totalNumberOfGrantOwners;
    }

    /**
     * @param totalNumberOfGrantOwners
     *            The totalNumberOfGrantOwners to set.
     */
    public void setTotalNumberOfGrantOwners(Integer totalNumberOfGrantOwners) {
        this.totalNumberOfGrantOwners = totalNumberOfGrantOwners;
    }
    
    public Integer getPercentageGrantOwnerResult() {
        if(numberOfGrantOwnerByCriteria.equals(new Integer(0))) {
            return new Integer(0);
        }
        double totalGrantOwner = totalNumberOfGrantOwners.intValue();
        double resultGrantOwner = numberOfGrantOwnerByCriteria.intValue();
        return new Integer( (int)((resultGrantOwner / totalGrantOwner) * 100 ));
    }
    
    public Integer getPercentageGrantContractResult() {
        if(numberOfGrantContractsByCriteria.equals(new Integer(0))) {
            return new Integer(0);
        }
        double totalGrantContract = totalNumberOfGrantContracts.intValue();
        double resultGrantContract = numberOfGrantContractsByCriteria.intValue();
        return new Integer( (int)((resultGrantContract / totalGrantContract) * 100 ));
    }
}