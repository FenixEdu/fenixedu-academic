package net.sourceforge.fenixedu.dataTransferObject;

import java.io.Serializable;
import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author Nuno & Joana
 */
public class InfoExecutionPeriod extends InfoObject implements Serializable {

    private String name;
    
    private String qualifiedName;

    private InfoExecutionYear infoExecutionYear;

    private PeriodState state;

    private Integer semester;

    private Date beginDate;

    private Date endDate;

    private Date inquiryResponseBegin;

    private Date inquiryResponseEnd;

    private InfoExecutionPeriod previousInfoExecutionPeriod;

    public InfoExecutionPeriod() {

    }

    public InfoExecutionPeriod(String name, InfoExecutionYear infoExecutionYear) {
        setName(name);
        setInfoExecutionYear(infoExecutionYear);
    }

    /**
     * @param integer
     */
    public InfoExecutionPeriod(Integer idInternal) {
        super(idInternal);
    }

    /**
     * Returns the infoExecutionYear.
     * 
     * @return InfoExecutionYear
     */
    public InfoExecutionYear getInfoExecutionYear() {
        return infoExecutionYear;
    }

    /**
     * Returns the name.
     * 
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the infoExecutionYear.
     * 
     * @param infoExecutionYear
     *            The infoExecutionYear to set
     */
    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
        this.infoExecutionYear = infoExecutionYear;
    }

    /**
     * Sets the name.
     * 
     * @param name
     *            The name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        if (obj instanceof InfoExecutionPeriod) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) obj;
            return (getInfoExecutionYear().equals(infoExecutionPeriod.getInfoExecutionYear()) && getName()
                    .equals(infoExecutionPeriod.getName()));

        }
        return false;
    }

    public String toString() {
        String result = "[INFOEXECUTIONPERIOD";
        result += ", id=" + getIdInternal();
        result += ", name=" + name;
        result += ", infoExecutionYear=" + infoExecutionYear;
        result += ", begin Date=" + beginDate;
        result += ", end Date=" + endDate;
        result += "]\n";
        return result;
    }

    /**
     * @return PeriodState
     */
    public PeriodState getState() {
        return state;
    }

    /**
     * Sets the periodState.
     * 
     * @param periodState
     *            The periodState to set
     */
    public void setState(PeriodState state) {
        this.state = state;
    }

    /**
     * @return
     */
    public Integer getSemester() {
        return semester;
    }

    /**
     * @param integer
     */
    public void setSemester(Integer integer) {
        semester = integer;
    }

    public int compareTo(Object arg0) {
        InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) arg0;
        int yearCmp = this.getInfoExecutionYear().compareTo(infoExecutionPeriod.getInfoExecutionYear());
        if (yearCmp != 0) {
            return yearCmp;
        } else {
            return this.getSemester().intValue() - infoExecutionPeriod.getSemester().intValue();
        }
    }

    /**
     * @return
     */
    public Date getBeginDate() {
        return beginDate;
    }

    /**
     * @param beginDate
     */
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    /**
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Method created for presentation matters. Concatenates execution period
     * name with execution year name.
     */
    public String getDescription() {
        StringBuilder buffer = new StringBuilder();

        // these ifs are needed due to cloner converting strategy (it looks to
        // all
        // properties).
        if (this.getName() != null) {
            buffer.append(this.getName());
        }
        if (this.getInfoExecutionYear() != null) {
            buffer.append(" - ").append(this.getInfoExecutionYear().getYear());
        }
        return buffer.toString();
    }

    /**
     * @return Returns the previousInfoExecutionPeriod.
     */
    public InfoExecutionPeriod getPreviousInfoExecutionPeriod() {
        return previousInfoExecutionPeriod;
    }

    /**
     * @param previousInfoExecutionPeriod
     *            The previousInfoExecutionPeriod to set.
     */
    public void setPreviousInfoExecutionPeriod(InfoExecutionPeriod previousInfoExecutionPeriod) {
        this.previousInfoExecutionPeriod = previousInfoExecutionPeriod;
    }

    public void copyFromDomain(ExecutionPeriod period) {
        super.copyFromDomain(period);
        if (period != null) {
            setName(period.getName());
            setState(period.getState());
            setBeginDate(period.getBeginDate());
            setEndDate(period.getEndDate());
            setSemester(period.getSemester());
            setInfoExecutionYear(InfoExecutionYear.newInfoFromDomain(period.getExecutionYear()));
            setInquiryResponseBegin(period.getInquiryResponseBegin());
            setInquiryResponseEnd(period.getInquiryResponseEnd());
        }
    }

    /**
     * @param period
     * @return
     */
    public static InfoExecutionPeriod newInfoFromDomain(ExecutionPeriod period) {
        InfoExecutionPeriod infoExecutionPeriod = null;
        if (period != null) {
            infoExecutionPeriod = new InfoExecutionPeriod();
            infoExecutionPeriod.copyFromDomain(period);
            infoExecutionPeriod.setQualifiedName(period.getName() + " " + period.getExecutionYear().getYear());
        }
        
        return infoExecutionPeriod;
    }

    public void copyToDomain(InfoExecutionPeriod infoExecutionPeriod, ExecutionPeriod executionPeriod) {
        super.copyToDomain(infoExecutionPeriod, executionPeriod);
        executionPeriod.setBeginDate(infoExecutionPeriod.getBeginDate());
        executionPeriod.setEndDate(infoExecutionPeriod.getEndDate());
        executionPeriod.setName(infoExecutionPeriod.getName());
        executionPeriod.setSemester(infoExecutionPeriod.getSemester());
        executionPeriod.setState(executionPeriod.getState());
    }

	public String getQualifiedName()
	{
		return qualifiedName;
	}

	public void setQualifiedName(String qualifiedName)
	{
		this.qualifiedName = qualifiedName;
	}

    public Date getInquiryResponseBegin() {
        return inquiryResponseBegin;
    }

    public void setInquiryResponseBegin(Date inquiryResponseBegin) {
        this.inquiryResponseBegin = inquiryResponseBegin;
    }

    public Date getInquiryResponseEnd() {
        return inquiryResponseEnd;
    }

    public void setInquiryResponseEnd(Date inquiryResponseEnd) {
        this.inquiryResponseEnd = inquiryResponseEnd;
    }

}
