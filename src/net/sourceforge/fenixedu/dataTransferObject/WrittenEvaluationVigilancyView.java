package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.OtherCourseVigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilant;

public class WrittenEvaluationVigilancyView {

	private WrittenEvaluation writtenEvaluation;
	
	
	public WrittenEvaluationVigilancyView(WrittenEvaluation evaluation) {
		writtenEvaluation = evaluation;
	}


	public WrittenEvaluation getWrittenEvaluation() {
		return writtenEvaluation;
	}


	public void setWrittenEvaluation(WrittenEvaluation writtenEvaluation) {
		this.writtenEvaluation = writtenEvaluation;
	}
	
	public int getTotalVigilancies() {
		return getWrittenEvaluation().getVigilanciesCount();
	}
	
	public int getVigilanciesFromTeachers() {
		return getTotalVigilancies() - getVigilanciesFromOthers();
	}
	
	public int getVigilanciesFromOthers() {
		return getWrittenEvaluation().getOthersVigilancies().size();
	}
	
	public List<Vigilant> getTeachersVigilants() {
		List<Vigilant> vigilants = new ArrayList<Vigilant>();
		for(Vigilancy vigilancy : getWrittenEvaluation().getTeachersVigilancies()) {
				vigilants.add(vigilancy.getVigilant());
		}
		return vigilants;
	}
	
	public List<Vigilant> getOtherVigilants() {
		List<Vigilant> vigilants = new ArrayList<Vigilant>();
		for(Vigilancy vigilancy : getWrittenEvaluation().getOthersVigilancies()) {
			vigilants.add(vigilancy.getVigilant());
		}
		return vigilants;
	}
	
	public List<Vigilant> getCancelledConvokes() {
		List<Vigilant> vigilants = new ArrayList<Vigilant>();
		for(Vigilancy vigilancy : getWrittenEvaluation().getVigilancies()) {
			if(!vigilancy.isActive()) {
				vigilants.add(vigilancy.getVigilant());
			}
		}
		return vigilants;
	}
	
	public int getNumberOfCancelledConvokes() {
		return getCancelledConvokes().size();
	}
	
	public List<Vigilant> getConfirmedConvokes() {
		List<Vigilant> vigilants = new ArrayList<Vigilant>();
		for(Vigilancy vigilancy : getWrittenEvaluation().getOthersVigilancies()) {
			OtherCourseVigilancy otherCourseVigilancy = (OtherCourseVigilancy) vigilancy;
			if(otherCourseVigilancy.isConfirmed()) {
				vigilants.add(otherCourseVigilancy.getVigilant());
			}
		}
		return vigilants;
	}
	
	public int getNumberOfConfirmedConvokes() {
		return getConfirmedConvokes().size();
	}
	
	public List<Vigilant> getAttendedConvokes() {
		List<Vigilant> vigilants = new ArrayList<Vigilant>();
		for(Vigilancy vigilancy : getWrittenEvaluation().getVigilancies()) {
			if(vigilancy.isAttended()) {
				vigilants.add(vigilancy.getVigilant());
			}
		}
		return vigilants;
	}
	
	public int getNumberOfAttendedConvokes() {
		return getAttendedConvokes().size();
	}
	
}
