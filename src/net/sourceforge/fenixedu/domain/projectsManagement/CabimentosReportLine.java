/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public class CabimentosReportLine implements Serializable, ICabimentosReportLine {

	private String projectCode;

	private Double cabimentos;

	private Double justifications;

	private Double total;

	@Override
	public Double getCabimentos() {
		return cabimentos;
	}

	@Override
	public void setCabimentos(Double cabimentos) {
		this.cabimentos = cabimentos;
	}

	@Override
	public Double getJustifications() {
		return justifications;
	}

	@Override
	public void setJustifications(Double justifications) {
		this.justifications = justifications;
	}

	@Override
	public String getProjectCode() {
		return projectCode;
	}

	@Override
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	@Override
	public Double getTotal() {
		return total;
	}

	@Override
	public void setTotal(Double total) {
		this.total = total;
	}
}
