package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftType;

public class ShiftToEnrol extends DataTranferObject {
    
    private ExecutionCourse executionCourse;
    
    private ShiftType theoricType;
    private ShiftType praticType;
    private ShiftType laboratoryType;
    private ShiftType theoricoPraticType;
    
    private Shift theoricShift;
    private Shift praticShift;
    private Shift laboratoryShift;
    private Shift theoricoPraticShift;
    
    private boolean enrolled;

	public boolean isEnrolled() {
		return enrolled;
	}

	public void setEnrolled(boolean enrolled) {
		this.enrolled = enrolled;
	}

	public ExecutionCourse getExecutionCourse() {
		return executionCourse;
	}

	public void setExecutionCourse(ExecutionCourse executionCourse) {
		this.executionCourse = executionCourse;
	}

	public Shift getLaboratoryShift() {
		return laboratoryShift;
	}

	public void setLaboratoryShift(Shift laboratoryShift) {
		this.laboratoryShift = laboratoryShift;
	}

	public ShiftType getLaboratoryType() {
		return laboratoryType;
	}

	public void setLaboratoryType(ShiftType laboratoryType) {
		this.laboratoryType = laboratoryType;
	}

	public Shift getPraticShift() {
		return praticShift;
	}

	public void setPraticShift(Shift praticShift) {
		this.praticShift = praticShift;
	}

	public ShiftType getPraticType() {
		return praticType;
	}

	public void setPraticType(ShiftType praticType) {
		this.praticType = praticType;
	}

	public Shift getTheoricoPraticShift() {
		return theoricoPraticShift;
	}

	public void setTheoricoPraticShift(Shift theoricoPraticShift) {
		this.theoricoPraticShift = theoricoPraticShift;
	}

	public ShiftType getTheoricoPraticType() {
		return theoricoPraticType;
	}

	public void setTheoricoPraticType(ShiftType theoricoPraticType) {
		this.theoricoPraticType = theoricoPraticType;
	}

	public Shift getTheoricShift() {
		return theoricShift;
	}

	public void setTheoricShift(Shift theoricShift) {
		this.theoricShift = theoricShift;
	}

	public ShiftType getTheoricType() {
		return theoricType;
	}

	public void setTheoricType(ShiftType theoricType) {
		this.theoricType = theoricType;
	}
        
}


