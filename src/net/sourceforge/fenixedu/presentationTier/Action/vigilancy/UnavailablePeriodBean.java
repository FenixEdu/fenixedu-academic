package net.sourceforge.fenixedu.presentationTier.Action.vigilancy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.vigilancy.ExamCoordinator;
import net.sourceforge.fenixedu.domain.vigilancy.UnavailablePeriod;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantGroup;

import org.joda.time.DateTime;

public class UnavailablePeriodBean implements Serializable {

    private DateTime beginDate;

    private DateTime endDate;

    private String justification;

    private DomainReference<Vigilant> vigilant;

    private DomainReference<ExamCoordinator> coordinator;

    private List<DomainReference<UnavailablePeriod>> unavailablePeriods = new ArrayList<DomainReference<UnavailablePeriod>>();;

    private DomainReference<VigilantGroup> selectedVigilantGroup;
    
    private Integer idInternal;

    public UnavailablePeriodBean() {
        setVigilant(null);
        setCoordinator(null);
        setSelectedVigilantGroup(null);
    }

    public DateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(DateTime beginDate) {
        this.beginDate = beginDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public Vigilant getVigilant() {
        return (this.vigilant != null) ? this.vigilant.getObject() : null;
    }

    public void setVigilant(Vigilant vigilant) {
        this.vigilant = (vigilant != null) ? new DomainReference<Vigilant>(vigilant) : null;
    }

    public ExamCoordinator getCoordinator() {
        return coordinator.getObject();
    }

    public void setCoordinator(ExamCoordinator coordinator) {
        this.coordinator = new DomainReference<ExamCoordinator>(coordinator);
    }

    public Integer getIdInternal() {
        return idInternal;
    }

    public void setIdInternal(Integer idInternal) {
        this.idInternal = idInternal;
    }

    public Collection getUnavailablePeriods() {
        Collection periods = new ArrayList<UnavailablePeriod>();
        for (DomainReference<UnavailablePeriod> unavailablePeriod : this.unavailablePeriods) {
            if (unavailablePeriod != null)
                periods.add(unavailablePeriod.getObject());
        }
        return periods;
    }

    public void setUnavailablePeriods(List<UnavailablePeriod> unavailablePeriods) {
        for (UnavailablePeriod unavailablePeriod : unavailablePeriods) {
            if (unavailablePeriod != null) {
                this.unavailablePeriods.add(new DomainReference(unavailablePeriod));
            }
        }
    }

    public VigilantGroup getSelectedVigilantGroup() {
    	return this.selectedVigilantGroup.getObject();
    }
    
    public void setSelectedVigilantGroup(VigilantGroup group) {
    	this.selectedVigilantGroup = new DomainReference<VigilantGroup>(group);
    }
}
