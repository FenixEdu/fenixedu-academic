/*
 * Created on Jul 31, 2004
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author joaosa-rmalo
 */

public class AttendsSet extends AttendsSet_Base {

	
	public AttendsSet () {}
	
	
	public AttendsSet (String name) {
		super.setName(name);
	}
	
	
	public String toString() {
		String result = "[ATTENDSSET";
		result += ", name=" + getName();
		result += "]";
		return result;
	}
	
	
	public void addAttendInAttendsSet (IAttendInAttendsSet attendInAttendsSet) {
		if( attendInAttendsSet==null){
			super.setAttendInAttendsSet(new ArrayList());
			super.getAttendInAttendsSet().add(attendInAttendsSet);
		}
		else{
			super.getAttendInAttendsSet().add(attendInAttendsSet);	
		}
	}
	
	
	public void removeAttendInAttendsSet (IAttendInAttendsSet attendInAttendsSet) {
		super.getAttendInAttendsSet().remove(attendInAttendsSet);
	}
	
		
	public boolean existsAttendInAttendsSet (IAttendInAttendsSet attendInAttendsSet) {
		return super.getAttendInAttendsSet().contains(attendInAttendsSet);
	}

	public void addStudentGroup (IStudentGroup studentGroup) {
		super.getStudentGroups().add(studentGroup);
	}
	
	
	public void removeStudentGroup (IStudentGroup studentGroup) {
	    super.getStudentGroups().remove(studentGroup);
	}
	
		
	public boolean existsStudentGroup (IStudentGroup studentGroup) {
		return super.getStudentGroups().contains(studentGroup);
	}
	
	public IStudentGroup getStudentGroup(Integer groupNumber){
		Iterator iter=super.getStudentGroups().iterator();
		while(iter.hasNext()){
			IStudentGroup sg=(IStudentGroup)iter.next();
			if(sg.getGroupNumber()==groupNumber)
				return sg;
				} 
		return null;
		
	}
	
	
	public List getAttends() {
		List attends = new ArrayList();
		Iterator iterAttendInAttendsSet = 
			getAttendInAttendsSet().iterator();
		IAttendInAttendsSet attendInAttendsSet = null;
		while(iterAttendInAttendsSet.hasNext()){
			attendInAttendsSet = (IAttendInAttendsSet)iterAttendInAttendsSet.next();
			attends.add(attendInAttendsSet.getAttend());
		}
		return attends;
	}
	
	
	public IAttends getStudentAttend(IStudent student){
		IAttends attend=null;
		boolean found = false;
		Iterator iterAttends = getAttendInAttendsSet().iterator();
		while(iterAttends.hasNext() && !found)
		{
			attend = ((IAttendInAttendsSet)iterAttends.next()).getAttend();
			if(attend.getAluno().getIdInternal().equals(student.getIdInternal())){
				return attend;
			}
			attend = null;
		}
		return attend;
	}
	
	public List getStudentGroupsWithoutShift(){
		List result = new ArrayList();
		Iterator iter=super.getStudentGroups().iterator();
		while(iter.hasNext()){
			IStudentGroup sg=(IStudentGroup)iter.next();
			if(sg.getShift()==null){
				result.add(sg);
			}
		} 
		return result;
	}
	
	public List getStudentGroupsWithShift(){
		List result = new ArrayList();
		Iterator iter=super.getStudentGroups().iterator();
		while(iter.hasNext()){
			IStudentGroup sg=(IStudentGroup)iter.next();
			if(sg.getShift()!=null){
				result.add(sg);
			}
		} 
		return result;
	}
	
	public Integer getNumberOfStudentsInAttendsSet(){
	    return new Integer(getAttendInAttendsSet().size());
	}
	
	public Integer getNumberOfStudentsNotInAttendsSet(){
	    
	    List frequentasPossiveis = new ArrayList();
        List frequentasAttendsSet = new ArrayList();
        List frequentasStudentNumbersInsert = new ArrayList();
        frequentasAttendsSet.addAll(getAttendInAttendsSet()); 
	    List allGroupPropertiesExecutionCourse = super.getGroupProperties().getGroupPropertiesExecutionCourse();            
        
	    // Create List with all attends related with possible ExecutionCourses 
	    
        Iterator iterator = allGroupPropertiesExecutionCourse.iterator();
        while (iterator.hasNext()) {
            IGroupPropertiesExecutionCourse groupPropertiesExecutionCourse = (IGroupPropertiesExecutionCourse)iterator.next(); 
        	if(groupPropertiesExecutionCourse.getProposalState().getState().intValue()==1
        		|| groupPropertiesExecutionCourse.getProposalState().getState().intValue()==2){
            IExecutionCourse executionCourse = groupPropertiesExecutionCourse.getExecutionCourse();
            List allExecutionCourseAttends = executionCourse.getAttends();
            frequentasPossiveis.addAll(allExecutionCourseAttends);
            }
        }
        
        // Delete students in attendsSet from frequentasPossiveis
        
        Iterator iterator2 = frequentasAttendsSet.iterator();
        IAttends frequenta = null;
        while (iterator2.hasNext()) {
            IAttendInAttendsSet attendInAttendsSet = (IAttendInAttendsSet)iterator2.next();
        	frequenta = attendInAttendsSet.getAttend();
        	frequentasStudentNumbersInsert.add(frequenta.getAluno().getNumber());
        	
        	if (frequentasPossiveis.contains(frequenta)) {
        		frequentasPossiveis.remove(frequenta);
        	}
        }
        
        // Delete duplicate students from frequentasPossiveis
        
        List frequentasOutside = new ArrayList();
        Iterator iteratorFreqPoss = frequentasPossiveis.iterator();
        while (iteratorFreqPoss.hasNext()) {
        	frequenta = (IAttends)iteratorFreqPoss.next();
        	if(!frequentasStudentNumbersInsert.contains(frequenta.getAluno().getNumber())){
        		
        		frequentasStudentNumbersInsert.add(frequenta.getAluno().getNumber());
                frequentasOutside.add(frequenta);
        	}
        }
    return new Integer(frequentasOutside.size());    
	}
	
}