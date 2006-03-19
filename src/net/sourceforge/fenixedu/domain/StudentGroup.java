/*
 * Created on 9/Mai/2003
 *
 */
package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

/**
 * @author asnr and scpo
 */
public class StudentGroup extends StudentGroup_Base {

    public StudentGroup() {
    	super();
    	setRootDomainObject(RootDomainObject.getInstance());
    }

    public StudentGroup(Integer groupNumber, Grouping grouping) {
    	this();
        super.setGroupNumber(groupNumber);
        super.setGrouping(grouping);
    }

    public StudentGroup(Integer groupNumber, Grouping grouping, Shift shift) {
    	this();
        super.setGroupNumber(groupNumber);
        super.setGrouping(grouping);
        super.setShift(shift);
    }

    public void delete(){
        List attendsList = this.getAttends();

        if (attendsList.size() != 0) {
            throw new DomainException(this.getClass().getName(), "");
        }
        
        this.setShift(null);
        this.setGrouping(null);
    }
    
    public void editShift(Shift shift){
        if (this.getGrouping().getShiftType() == null
                || (!this.getGrouping().getShiftType().equals(shift.getTipo()))) {
            throw new DomainException(this.getClass().getName(), "");
        }

        this.setShift(shift);
    }
    
    public boolean checkStudentUsernames(List<String> studentUsernames) {
        boolean found;
        for (final String studentUsername :  studentUsernames) {
            found = false;
            for (final Attends attend : this.getAttends()) {
                if (attend.getAluno().getPerson().getUsername() == studentUsername) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return true;
            }
        }
        return false;
    }
}
