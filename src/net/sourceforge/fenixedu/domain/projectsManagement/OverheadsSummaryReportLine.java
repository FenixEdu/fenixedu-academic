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

    public Double getBalance() {
	return balance;
    }

    public void setBalance(Double balance) {
	this.balance = balance;
    }

    public String getCostCenter() {
	return costCenter;
    }

    public void setCostCenter(String costCenter) {
	this.costCenter = costCenter;
    }

    public Integer getExplorationUnit() {
	return explorationUnit;
    }

    public void setExplorationUnit(Integer explorationUnit) {
	this.explorationUnit = explorationUnit;
    }

    public Double getOAOverhead() {
	return OAOverhead;
    }

    public void setOAOverhead(Double overhead) {
	OAOverhead = overhead;
    }

    public Double getOARevenue() {
	return OARevenue;
    }

    public void setOARevenue(Double revenue) {
	OARevenue = revenue;
    }

    public Double getOEOverhead() {
	return OEOverhead;
    }

    public void setOEOverhead(Double overhead) {
	OEOverhead = overhead;
    }

    public Double getOERevenue() {
	return OERevenue;
    }

    public void setOERevenue(Double revenue) {
	OERevenue = revenue;
    }

    public Double getOGOverhead() {
	return OGOverhead;
    }

    public void setOGOverhead(Double overhead) {
	OGOverhead = overhead;
    }

    public Double getOGRevenue() {
	return OGRevenue;
    }

    public void setOGRevenue(Double revenue) {
	OGRevenue = revenue;
    }

    public Double getOOOverhead() {
	return OOOverhead;
    }

    public void setOOOverhead(Double overhead) {
	OOOverhead = overhead;
    }

    public Double getOORevenue() {
	return OORevenue;
    }

    public void setOORevenue(Double revenue) {
	OORevenue = revenue;
    }

    public Double getTotalOverheads() {
	return totalOverheads;
    }

    public void setTotalOverheads(Double totalOverheads) {
	this.totalOverheads = totalOverheads;
    }

    public Double getTransferedOverheads() {
	return transferedOverheads;
    }

    public void setTransferedOverheads(Double transferedOverheads) {
	this.transferedOverheads = transferedOverheads;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

}