package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

public class OverheadsSummaryReportLine implements Serializable, IOverheadsSummaryReportLine {

	private Integer year;

	private Integer explorationUnit;

	private String costCenter;

	private Double OGRevenue;

	private Double OGOverhead;

	private Double OARevenue;

	private Double OAOverhead;

	private Double OORevenue;

	private Double OOOverhead;

	private Double OERevenue;

	private Double OEOverhead;

	private Double totalOverheads;

	private Double transferedOverheads;

	private Double balance;

	@Override
	public Double getBalance() {
		return balance;
	}

	@Override
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	@Override
	public String getCostCenter() {
		return costCenter;
	}

	@Override
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	@Override
	public Integer getExplorationUnit() {
		return explorationUnit;
	}

	@Override
	public void setExplorationUnit(Integer explorationUnit) {
		this.explorationUnit = explorationUnit;
	}

	@Override
	public Double getOAOverhead() {
		return OAOverhead;
	}

	@Override
	public void setOAOverhead(Double overhead) {
		OAOverhead = overhead;
	}

	@Override
	public Double getOARevenue() {
		return OARevenue;
	}

	@Override
	public void setOARevenue(Double revenue) {
		OARevenue = revenue;
	}

	@Override
	public Double getOEOverhead() {
		return OEOverhead;
	}

	@Override
	public void setOEOverhead(Double overhead) {
		OEOverhead = overhead;
	}

	@Override
	public Double getOERevenue() {
		return OERevenue;
	}

	@Override
	public void setOERevenue(Double revenue) {
		OERevenue = revenue;
	}

	@Override
	public Double getOGOverhead() {
		return OGOverhead;
	}

	@Override
	public void setOGOverhead(Double overhead) {
		OGOverhead = overhead;
	}

	@Override
	public Double getOGRevenue() {
		return OGRevenue;
	}

	@Override
	public void setOGRevenue(Double revenue) {
		OGRevenue = revenue;
	}

	@Override
	public Double getOOOverhead() {
		return OOOverhead;
	}

	@Override
	public void setOOOverhead(Double overhead) {
		OOOverhead = overhead;
	}

	@Override
	public Double getOORevenue() {
		return OORevenue;
	}

	@Override
	public void setOORevenue(Double revenue) {
		OORevenue = revenue;
	}

	@Override
	public Double getTotalOverheads() {
		return totalOverheads;
	}

	@Override
	public void setTotalOverheads(Double totalOverheads) {
		this.totalOverheads = totalOverheads;
	}

	@Override
	public Double getTransferedOverheads() {
		return transferedOverheads;
	}

	@Override
	public void setTransferedOverheads(Double transferedOverheads) {
		this.transferedOverheads = transferedOverheads;
	}

	@Override
	public Integer getYear() {
		return year;
	}

	@Override
	public void setYear(Integer year) {
		this.year = year;
	}

}