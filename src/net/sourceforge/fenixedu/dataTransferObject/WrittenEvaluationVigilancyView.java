package net.sourceforge.fenixedu.dataTransferObject;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.WrittenEvaluation;
import net.sourceforge.fenixedu.domain.vigilancy.OtherCourseVigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.Vigilancy;
import net.sourceforge.fenixedu.domain.vigilancy.VigilantWrapper;

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

	public List<VigilantWrapper> getTeachersVigilants() {
		List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
		for (Vigilancy vigilancy : getWrittenEvaluation().getTeachersVigilancies()) {
			vigilants.add(vigilancy.getVigilantWrapper());
		}
		return vigilants;
	}

	public List<VigilantWrapper> getOtherVigilants() {
		List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
		for (Vigilancy vigilancy : getWrittenEvaluation().getOthersVigilancies()) {
			vigilants.add(vigilancy.getVigilantWrapper());
		}
		return vigilants;
	}

	public List<VigilantWrapper> getCancelledConvokes() {
		List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
		for (Vigilancy vigilancy : getWrittenEvaluation().getVigilancies()) {
			if (!vigilancy.isActive()) {
				vigilants.add(vigilancy.getVigilantWrapper());
			}
		}
		return vigilants;
	}

	public int getNumberOfCancelledConvokes() {
		return getCancelledConvokes().size();
	}

	public List<VigilantWrapper> getConfirmedConvokes() {
		List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
		for (Vigilancy vigilancy : getWrittenEvaluation().getOthersVigilancies()) {
			OtherCourseVigilancy otherCourseVigilancy = (OtherCourseVigilancy) vigilancy;
			if (otherCourseVigilancy.isConfirmed()) {
				vigilants.add(otherCourseVigilancy.getVigilantWrapper());
			}
		}
		return vigilants;
	}

	public int getNumberOfConfirmedConvokes() {
		return getConfirmedConvokes().size();
	}

	public List<VigilantWrapper> getAttendedConvokes() {
		List<VigilantWrapper> vigilants = new ArrayList<VigilantWrapper>();
		for (Vigilancy vigilancy : getWrittenEvaluation().getVigilancies()) {
			if (vigilancy.isAttended()) {
				vigilants.add(vigilancy.getVigilantWrapper());
			}
		}
		return vigilants;
	}

	public int getNumberOfAttendedConvokes() {
		return getAttendedConvokes().size();
	}

}
