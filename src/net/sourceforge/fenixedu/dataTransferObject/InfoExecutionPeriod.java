package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Date;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.util.PeriodState;

/**
 * @author Nuno & Joana
 */
public class InfoExecutionPeriod extends InfoObject {

    private final DomainReference<ExecutionPeriod> executionPeriodDomainReference;

    private String qualifiedName;

    public InfoExecutionPeriod(final ExecutionPeriod executionPeriod) {
        executionPeriodDomainReference = new DomainReference<ExecutionPeriod>(executionPeriod);
    }


    private InfoExecutionYear infoExecutionYear = null;
    public InfoExecutionYear getInfoExecutionYear() {
        if (infoExecutionYear == null) {
            infoExecutionYear = new InfoExecutionYear(getExecutionPeriod().getExecutionYear());
        }
        return infoExecutionYear;
    }

    public String getName() {
        return getExecutionPeriod().getName();
    }

    public boolean equals(Object obj) {
        if (obj instanceof InfoExecutionPeriod) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) obj;
            return (getInfoExecutionYear().equals(infoExecutionPeriod.getInfoExecutionYear()) && getName()
                    .equals(infoExecutionPeriod.getName()));

        }
        return false;
    }

    public String toString() {
        return getExecutionPeriod().toString();
    }

    public PeriodState getState() {
        return getExecutionPeriod().getState();
    }

    public Integer getSemester() {
        return getExecutionPeriod().getSemester();
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

    public Date getBeginDate() {
        return getExecutionPeriod().getBeginDate();
    }

    public Date getEndDate() {
        return getExecutionPeriod().getEndDate();
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
        if (getName() != null) {
            buffer.append(getName());
        }
        if (getInfoExecutionYear() != null) {
            buffer.append(" - ").append(getInfoExecutionYear().getYear());
        }
        return buffer.toString();
    }

    public InfoExecutionPeriod getPreviousInfoExecutionPeriod() {
        final ExecutionPeriod previousInfoExecutionPeriod = getExecutionPeriod().getPreviousExecutionPeriod();
        return previousInfoExecutionPeriod == null ? null : new InfoExecutionPeriod(previousInfoExecutionPeriod);
    }

    public static InfoExecutionPeriod newInfoFromDomain(ExecutionPeriod executionPeriod) {
        return executionPeriod == null ? null : new InfoExecutionPeriod(executionPeriod);
    }

	public String getQualifiedName() {
		return getDescription();
	}

    public Date getInquiryResponseBegin() {
        return getExecutionPeriod().getInquiryResponseBegin();
    }

    public Date getInquiryResponseEnd() {
        return getExecutionPeriod().getInquiryResponseEnd();
    }

    @Override
    public void copyFromDomain(DomainObject domainObject) {
        throw new Error("Method should not be called!");
    }

    @Override
    public void copyToDomain(InfoObject infoObject, DomainObject domainObject) {
        throw new Error("Method should not be called!");
    }

    @Override
    public Integer getIdInternal() {
        return getExecutionPeriod().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
        throw new Error("Method should not be called!");
    }

    public ExecutionPeriod getExecutionPeriod() {
        return executionPeriodDomainReference == null ? null : executionPeriodDomainReference.getObject();
    }

}
