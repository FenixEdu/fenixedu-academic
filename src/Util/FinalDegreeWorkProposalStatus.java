/*
 * Created on Apr 4, 2004
 *  
 */
package Util;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author Luis Cruz
 */
public class FinalDegreeWorkProposalStatus extends FenixUtil {

	public static final int APPROVED = 1;
	public static final int PUBLISHED = 2;

	public static final FinalDegreeWorkProposalStatus APPROVED_STATUS = new FinalDegreeWorkProposalStatus(APPROVED);
	public static final FinalDegreeWorkProposalStatus PUBLISHED_STATUS = new FinalDegreeWorkProposalStatus(PUBLISHED);

	public static final String APPROVED_STRING = "Aprovado";
	public static final String PUBLISHED_STRING = "Publicado";

	private Integer status;

	public FinalDegreeWorkProposalStatus() {
	}

	public FinalDegreeWorkProposalStatus(int status) {
		this.status = new Integer(status);
	}

	public FinalDegreeWorkProposalStatus(Integer status) {
		this.status = status;
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof FinalDegreeWorkProposalStatus) {
			FinalDegreeWorkProposalStatus ds = (FinalDegreeWorkProposalStatus) obj;
			resultado = this.getStatus().equals(ds.getStatus());
		}
		return resultado;
	}

	/**
	 * @return
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param integer
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKey() {
		if (status.intValue() == APPROVED) { return APPROVED_STRING; }
		if (status.intValue() == PUBLISHED) { return PUBLISHED_STRING; }
		 return null; 
	}

	public static List getLabelValueList() {
		List labelValueList = new ArrayList();
		labelValueList.add(new LabelValueBean(APPROVED_STRING, "" + APPROVED));
		labelValueList.add(new LabelValueBean(PUBLISHED_STRING, "" + PUBLISHED));
		return labelValueList;
	}

}