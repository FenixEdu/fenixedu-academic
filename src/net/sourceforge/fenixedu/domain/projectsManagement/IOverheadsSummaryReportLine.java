package net.sourceforge.fenixedu.domain.projectsManagement;

public interface IOverheadsSummaryReportLine {

    public abstract Double getBalance();

    public abstract void setBalance(Double balance);

    public abstract String getCostCenter();

    public abstract void setCostCenter(String costCenter);

    public abstract Integer getExplorationUnit();

    public abstract void setExplorationUnit(Integer explorationUnit);

    public abstract Double getOAOverhead();

    public abstract void setOAOverhead(Double overhead);

    public abstract Double getOARevenue();

    public abstract void setOARevenue(Double revenue);

    public abstract Double getOEOverhead();

    public abstract void setOEOverhead(Double overhead);

    public abstract Double getOERevenue();

    public abstract void setOERevenue(Double revenue);

    public abstract Double getOGOverhead();

    public abstract void setOGOverhead(Double overhead);

    public abstract Double getOGRevenue();

    public abstract void setOGRevenue(Double revenue);

    public abstract Double getOOOverhead();

    public abstract void setOOOverhead(Double overhead);

    public abstract Double getOORevenue();

    public abstract void setOORevenue(Double revenue);

    public abstract Double getTotalOverheads();

    public abstract void setTotalOverheads(Double totalOverheads);

    public abstract Double getTransferedOverheads();

    public abstract void setTransferedOverheads(Double transferedOverheads);

    public abstract Integer getYear();

    public abstract void setYear(Integer year);

}