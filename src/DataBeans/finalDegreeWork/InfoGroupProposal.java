/*
 * Created on 2004/04/15
 *
 */
package DataBeans.finalDegreeWork;

import DataBeans.InfoObject;

/**
 * @author Luis Cruz
 *
 */
public class InfoGroupProposal extends InfoObject {

    private Integer orderOfPreference;
    private InfoProposal finalDegreeWorkProposal;
    private InfoGroup infoGroup;

	public InfoGroupProposal() {
		super();
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoGroupProposal) {
			InfoGroupProposal group = (InfoGroupProposal) obj;

			if (group.getIdInternal() != null && getIdInternal() != null)
			{	
				result = group.getIdInternal().equals(getIdInternal());
			}
		}
		return result;
	}

	public String toString() {
		String result = "[InfoGroupProposal";
		result += ", idInternal=" + getIdInternal();
		result += "]";
		return result;
	}

    /**
     * @return Returns the finalDegreeWorkProposal.
     */
    public InfoProposal getFinalDegreeWorkProposal()
    {
        return finalDegreeWorkProposal;
    }
    /**
     * @param finalDegreeWorkProposal The finalDegreeWorkProposal to set.
     */
    public void setFinalDegreeWorkProposal(InfoProposal finalDegreeWorkProposal)
    {
        this.finalDegreeWorkProposal = finalDegreeWorkProposal;
    }
    /**
     * @return Returns the orderOfPreference.
     */
    public Integer getOrderOfPreference()
    {
        return orderOfPreference;
    }
    /**
     * @param orderOfPreference The orderOfPreference to set.s
     */
    public void setOrderOfPreference(Integer orderOfPreference)
    {
        this.orderOfPreference = orderOfPreference;
    }
    /**
     * @return Returns the infoGroup.
     */
    public InfoGroup getInfoGroup()
    {
        return infoGroup;
    }
    /**
     * @param infoGroup The infoGroup to set.
     */
    public void setInfoGroup(InfoGroup infoGroup)
    {
        this.infoGroup = infoGroup;
    }
}