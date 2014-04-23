package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.ist.fenixframework.DomainObject;

/**
 * @author Nuno & Joana
 */
public class InfoExecutionPeriod extends InfoObject implements Comparable {

    public static final Comparator<InfoExecutionPeriod> COMPARATOR_BY_YEAR_AND_SEMESTER = new Comparator<InfoExecutionPeriod>() {

        @Override
        public int compare(InfoExecutionPeriod o1, InfoExecutionPeriod o2) {
            final int c = o2.getInfoExecutionYear().getYear().compareTo(o1.getInfoExecutionYear().getYear());
            return c == 0 ? o2.getSemester().compareTo(o1.getSemester()) : c;
        }

    };

    private ExecutionSemester executionPeriodDomainReference;

    private String qualifiedName;

    public InfoExecutionPeriod(final ExecutionSemester executionSemester) {
        executionPeriodDomainReference = executionSemester;
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InfoExecutionPeriod) {
            InfoExecutionPeriod infoExecutionPeriod = (InfoExecutionPeriod) obj;
            return (getInfoExecutionYear().equals(infoExecutionPeriod.getInfoExecutionYear()) && getName().equals(
                    infoExecutionPeriod.getName()));

        }
        return false;
    }

    @Override
    public String toString() {
        return getExecutionPeriod().toString();
    }

    public PeriodState getState() {
        return getExecutionPeriod().getState();
    }

    public Integer getSemester() {
        return getExecutionPeriod().getSemester();
    }

    @Override
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
        if (getName() != null) {
            buffer.append(getName());
        }
        if (getInfoExecutionYear() != null) {
            buffer.append(" - ").append(getInfoExecutionYear().getYear());
        }
        return buffer.toString();
    }

    public InfoExecutionPeriod getPreviousInfoExecutionPeriod() {
        final ExecutionSemester previousInfoExecutionPeriod = getExecutionPeriod().getPreviousExecutionPeriod();
        return previousInfoExecutionPeriod == null ? null : new InfoExecutionPeriod(previousInfoExecutionPeriod);
    }

    public static InfoExecutionPeriod newInfoFromDomain(ExecutionSemester executionSemester) {
        return executionSemester == null ? null : new InfoExecutionPeriod(executionSemester);
    }

    public String getQualifiedName() {
        return getDescription();
    }

    public Date getInquiryResponseBegin() {
        return getExecutionPeriod().getInquiryResponsePeriod().getBegin().toDate();
    }

    public Date getInquiryResponseEnd() {
        return getExecutionPeriod().getInquiryResponsePeriod().getEnd().toDate();
    }

    @Override
    public void copyFromDomain(DomainObject domainObject) {
        throw new Error("Method should not be called!");
    }

    @Override
    public String getExternalId() {
        return getExecutionPeriod().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    public ExecutionSemester getExecutionPeriod() {
        return executionPeriodDomainReference;
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        executionPeriodDomainReference = executionSemester;
    }

}
