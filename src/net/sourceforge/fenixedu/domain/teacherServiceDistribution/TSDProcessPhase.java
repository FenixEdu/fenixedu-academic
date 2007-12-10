package net.sourceforge.fenixedu.domain.teacherServiceDistribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.injectionCode.Checked;

import org.apache.commons.beanutils.BeanComparator;

public class TSDProcessPhase extends TSDProcessPhase_Base {

    protected TSDProcessPhase() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public TSDProcessPhase(TSDProcess tsdProcess, String name,
	    TSDProcessPhase previousTSDProcessPhase, TSDProcessPhase nextTSDProcessPhase,
	    TSDProcessPhaseStatus status, Map<String, Object> omissionValues) {
	this();

	this.setTSDProcess(tsdProcess);
	this.setName(name);
	this.setPreviousTSDProcessPhase(previousTSDProcessPhase);
	this.setNextTSDProcessPhase(nextTSDProcessPhase);
	this.setStatus(status);
	this.setIsPublished(false);

	this.setStudentsPerTheoreticalShift((Integer) omissionValues.get("studentsPerTheoreticalShift"));
	this.setStudentsPerPraticalShift((Integer) omissionValues.get("studentsPerPraticalShift"));
	this.setStudentsPerTheoPratShift((Integer) omissionValues.get("studentsPerTheoPratShift"));
	this.setStudentsPerLaboratorialShift((Integer) omissionValues
		.get("studentsPerLaboratorialShift"));
	this.setWeightFirstTimeEnrolledStudentsPerTheoShift((Double) omissionValues
		.get("weightFirstTimeEnrolledStudentsPerTheoShift"));
	this.setWeightFirstTimeEnrolledStudentsPerPratShift((Double) omissionValues
		.get("weightFirstTimeEnrolledStudentsPerPratShift"));
	this.setWeightFirstTimeEnrolledStudentsPerTheoPratShift((Double) omissionValues
		.get("weightFirstTimeEnrolledStudentsPerTheoPratShift"));
	this.setWeightFirstTimeEnrolledStudentsPerLabShift((Double) omissionValues
		.get("weightFirstTimeEnrolledStudentsPerLabShift"));
	this.setWeightSecondTimeEnrolledStudentsPerTheoShift((Double) omissionValues
		.get("weightSecondTimeEnrolledStudentsPerTheoShift"));
	this.setWeightSecondTimeEnrolledStudentsPerPratShift((Double) omissionValues
		.get("weightSecondTimeEnrolledStudentsPerPratShift"));
	this.setWeightSecondTimeEnrolledStudentsPerTheoPratShift((Double) omissionValues
		.get("weightSecondTimeEnrolledStudentsPerTheoPratShift"));
	this.setWeightSecondTimeEnrolledStudentsPerLabShift((Double) omissionValues
		.get("weightSecondTimeEnrolledStudentsPerLabShift"));

	TSDFactory tsdFactory = TSDFactory.getInstance();
	tsdFactory.createTSDTreeStructure(this);
    }

    public TSDProcessPhase getCurrentTSDProcessPhase() {
	for (TSDProcessPhase tsdProcessPhase = getFirstTSDProcessPhase(); tsdProcessPhase != null; tsdProcessPhase = tsdProcessPhase
		.getNextTSDProcessPhase()) {
	    if (tsdProcessPhase.getStatus() == TSDProcessPhaseStatus.CURRENT) {
		return tsdProcessPhase;
	    }
	}

	return null;
    }

    public TSDProcessPhase getFirstTSDProcessPhase() {
	TSDProcessPhase tsdProcessPhase = this;

	while (tsdProcessPhase.getPreviousTSDProcessPhase() != null) {
	    tsdProcessPhase = tsdProcessPhase.getPreviousTSDProcessPhase();
	}

	return tsdProcessPhase;
    }

    public TSDProcessPhase getLastTSDProcessPhase() {
	TSDProcessPhase tsdProcessPhase = this;

	while (tsdProcessPhase.getNextTSDProcessPhase() != null) {
	    tsdProcessPhase = tsdProcessPhase.getNextTSDProcessPhase();
	}

	return tsdProcessPhase;
    }
    
    public List<TSDProcessPhase> getPreviousTSDProcessPhases() {
		List<TSDProcessPhase> previousTSDProcessPhaseList = new ArrayList<TSDProcessPhase>();
		TSDProcessPhase phase = getPreviousTSDProcessPhase();
		
		while(phase != null){
			previousTSDProcessPhaseList.add(phase);
			phase = phase.getPreviousTSDProcessPhase();
		}
		
		return previousTSDProcessPhaseList;
	}


    public Integer getNumberOfTeacherServiceDistributions() {
	return getGroupings().size();
    }

    public Boolean getIsPrevious(TSDProcessPhase tsdProcessPhase) {
	if (tsdProcessPhase == this) {
	    return false;
	}

	for (; (tsdProcessPhase != null); tsdProcessPhase = tsdProcessPhase.getNextTSDProcessPhase()) {
	    if (tsdProcessPhase.getNextTSDProcessPhase() == this) {
		return false;
	    }
	}

	return true;
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setCurrent() {
	if (getStatus() == TSDProcessPhaseStatus.OPEN) {
	    getCurrentTSDProcessPhase().setStatus(TSDProcessPhaseStatus.OPEN);
	    setStatus(TSDProcessPhaseStatus.CURRENT);
	}
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setClosed() {
	if (getStatus() == TSDProcessPhaseStatus.OPEN) {
	    setStatus(TSDProcessPhaseStatus.CLOSED);
	}
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setOpen() {
	if (getStatus() == TSDProcessPhaseStatus.CLOSED) {
	    setStatus(TSDProcessPhaseStatus.OPEN);
	}
    }

    public TeacherServiceDistribution getRootTSD() {
	return getGroupings().get(0).getRootTSD();
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void delete() {
	for (TeacherServiceDistribution tsd : getGroupings()) {
	    removeGroupings(tsd);
	}
	
	removeNextTSDProcessPhase();
	removePreviousTSDProcessPhase();
	removeTSDProcess();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void deleteTSDProcessPhaseData() {
	deleteTeacherServiceDistributions();
    }

    private void deleteTeacherServiceDistributions() {
	for (TeacherServiceDistribution tsd : getGroupings()) {
	    for (TSDTeacher tsdTeacher : tsd.getTSDTeachers()) {
		tsdTeacher.delete();
	    }
	    for (TSDCourse course : tsd.getTSDCourses()) {
	    	course.delete();
	    }

	    tsd.delete();
	}
    }

    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void deleteDataAndPhase() {
	deleteTSDProcessPhaseData();
	delete();
    }

    public List<TeacherServiceDistribution> getTeacherServiceDistributionsOrderedByName() {
	List<TeacherServiceDistribution> orderedGrouping = new ArrayList<TeacherServiceDistribution>(getGroupings());

	Collections.sort(orderedGrouping, new BeanComparator("name"));

	return orderedGrouping;
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setIsPublished(Boolean isPublished) {
	super.setIsPublished(isPublished);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setKeyNextTSDProcessPhase(Integer keyNextTSDProcessPhase) {

	super.setKeyNextTSDProcessPhase(keyNextTSDProcessPhase);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setKeyPreviousTSDProcessPhase(Integer keyPreviousTSDProcessPhase) {

	super.setKeyPreviousTSDProcessPhase(keyPreviousTSDProcessPhase);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setKeyTSDProcess(Integer keyTSDProcess) {

	super.setKeyTSDProcess(keyTSDProcess);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setName(String name) {

	super.setName(name);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setNextTSDProcessPhase(TSDProcessPhase nextTSDProcessPhase) {

	super.setNextTSDProcessPhase(nextTSDProcessPhase);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setPreviousTSDProcessPhase(TSDProcessPhase previousTSDProcessPhase) {

	super.setPreviousTSDProcessPhase(previousTSDProcessPhase);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStatus(TSDProcessPhaseStatus status) {

	super.setStatus(status);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerLaboratorialShift(Integer studentsPerLaboratorialShift) {

	super.setStudentsPerLaboratorialShift(studentsPerLaboratorialShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerPraticalShift(Integer studentsPerPraticalShift) {

	super.setStudentsPerPraticalShift(studentsPerPraticalShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerTheoPratShift(Integer studentsPerTheoPratShift) {

	super.setStudentsPerTheoPratShift(studentsPerTheoPratShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setStudentsPerTheoreticalShift(Integer studentsPerTheoreticalShift) {

	super.setStudentsPerTheoreticalShift(studentsPerTheoreticalShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerLabShift(
	    Double weightFirstTimeEnrolledStudentsPerLabShift) {

	super.setWeightFirstTimeEnrolledStudentsPerLabShift(weightFirstTimeEnrolledStudentsPerLabShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerPratShift(
	    Double weightFirstTimeEnrolledStudentsPerPratShift) {

	super
		.setWeightFirstTimeEnrolledStudentsPerPratShift(weightFirstTimeEnrolledStudentsPerPratShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerTheoPratShift(
	    Double weightFirstTimeEnrolledStudentsPerTheoPratShift) {

	super
		.setWeightFirstTimeEnrolledStudentsPerTheoPratShift(weightFirstTimeEnrolledStudentsPerTheoPratShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightFirstTimeEnrolledStudentsPerTheoShift(
	    Double weightFirstTimeEnrolledStudentsPerTheoShift) {

	super
		.setWeightFirstTimeEnrolledStudentsPerTheoShift(weightFirstTimeEnrolledStudentsPerTheoShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerLabShift(
	    Double weightSecondTimeEnrolledStudentsPerLabShift) {

	super
		.setWeightSecondTimeEnrolledStudentsPerLabShift(weightSecondTimeEnrolledStudentsPerLabShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerPratShift(
	    Double weightSecondTimeEnrolledStudentsPerPratShift) {

	super
		.setWeightSecondTimeEnrolledStudentsPerPratShift(weightSecondTimeEnrolledStudentsPerPratShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerTheoPratShift(
	    Double weightSecondTimeEnrolledStudentsPerTheoPratShift) {

	super
		.setWeightSecondTimeEnrolledStudentsPerTheoPratShift(weightSecondTimeEnrolledStudentsPerTheoPratShift);
    }

    @Override
    @Checked("TSDProcessPhasePredicates.writePredicate")
    public void setWeightSecondTimeEnrolledStudentsPerTheoShift(
	    Double weightSecondTimeEnrolledStudentsPerTheoShift) {

	super
		.setWeightSecondTimeEnrolledStudentsPerTheoShift(weightSecondTimeEnrolledStudentsPerTheoShift);
    }
    
    
    public Integer getStudentsPerShift(ShiftType type){
    	Integer studentsNumber = 0;
    	
    	switch(type) {
    	case TEORICA: 
    		studentsNumber = getStudentsPerTheoreticalShift();
    		break;
    	case PRATICA: 
    		studentsNumber = getStudentsPerPraticalShift();
    		break;
    	case TEORICO_PRATICA: 
    		studentsNumber = getStudentsPerTheoPratShift();
    		break;
    	case LABORATORIAL: 
    		studentsNumber = getStudentsPerLaboratorialShift();
    		break;
    	}
    	
    	return studentsNumber;
    }
    
    public Double getWeightFirstTimeEnrolledStudentsPerShift(ShiftType type){
    	Double weight = 0d;
    	
    	switch(type) {
    	case TEORICA: 
    		weight = getWeightFirstTimeEnrolledStudentsPerTheoShift();
    		break;
    	case PRATICA: 
    		weight = getWeightFirstTimeEnrolledStudentsPerPratShift();
    		break;
    	case TEORICO_PRATICA: 
    		weight = getWeightFirstTimeEnrolledStudentsPerTheoPratShift();
    		break;
    	case LABORATORIAL: 
    		weight = getWeightFirstTimeEnrolledStudentsPerLabShift();
    		break;
    	}
    	
    	return weight;
    }
    
    public Double getWeightSecondTimeEnrolledStudentsPerShift(ShiftType type){
    	Double weight = 0d;
    	
    	switch(type) {
    	case TEORICA: 
    		weight = getWeightSecondTimeEnrolledStudentsPerTheoShift();
    		break;
    	case PRATICA: 
    		weight = getWeightSecondTimeEnrolledStudentsPerPratShift();
    		break;
    	case TEORICO_PRATICA: 
    		weight = getWeightSecondTimeEnrolledStudentsPerTheoPratShift();
    		break;
    	case LABORATORIAL: 
    		weight = getWeightSecondTimeEnrolledStudentsPerLabShift();
    		break;
    	}
    	
    	return weight;
    }

    

}
