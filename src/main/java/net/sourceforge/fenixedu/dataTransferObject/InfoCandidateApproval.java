package net.sourceforge.fenixedu.dataTransferObject;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InfoCandidateApproval extends InfoObject {

    protected String externalId;

    protected Integer orderPosition;

    protected String candidateName;

    protected String remarks;

    protected String situationName;

    public InfoCandidateApproval() {
    }

    /**
     * @return
     */
    public String getCandidateName() {
        return candidateName;
    }

    /**
     * @return
     */
    @Override
    public String getExternalId() {
        return externalId;
    }

    /**
     * @return
     */
    public Integer getOrderPosition() {
        return orderPosition;
    }

    /**
     * @return
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * @return
     */
    public String getSituationName() {
        return situationName;
    }

    /**
     * @param string
     */
    public void setCandidateName(String string) {
        candidateName = string;
    }

    /**
     * @param integer
     */
    @Override
    public void setExternalId(String integer) {
        externalId = integer;
    }

    /**
     * @param integer
     */
    public void setOrderPosition(Integer integer) {
        orderPosition = integer;
    }

    /**
     * @param string
     */
    public void setRemarks(String string) {
        remarks = string;
    }

    /**
     * @param string
     */
    public void setSituationName(String string) {
        situationName = string;
    }

}