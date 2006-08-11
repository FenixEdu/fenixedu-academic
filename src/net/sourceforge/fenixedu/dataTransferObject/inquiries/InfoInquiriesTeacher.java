/*
 * Created on Mar 18, 2005
 * 
 */
package net.sourceforge.fenixedu.dataTransferObject.inquiries;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.dataTransferObject.InfoTeacher;
import net.sourceforge.fenixedu.dataTransferObject.teacher.InfoNonAffiliatedTeacher;
import net.sourceforge.fenixedu.domain.ShiftType;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesTeacher;
import net.sourceforge.fenixedu.util.InquiriesUtil;

/**
 * @author João Fialho & Rita Ferreira
 * 
 */
public class InfoInquiriesTeacher extends InfoObject implements Comparable {

    private Integer keyInquiriesCourse;
	private InfoInquiriesCourse inquiriesCourse;
    
	private Integer keyTeacher;
	private InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes teacherOrNonAffiliatedTeacher;
    
	final private List<ShiftType> classTypes = new ArrayList<ShiftType>();
    
	private Integer studentAssiduity;
	private Integer teacherAssiduity;
	private Double teacherPunctuality;
	private Double teacherClarity;
	private Double teacherAssurance;
	private Double teacherInterestStimulation;
	private Double teacherAvailability;
	private Double teacherReasoningStimulation;
	private Double globalAppreciation;
    
    
    /**
     * @return Returns the classTypes.
     */
    public List<ShiftType> getClassTypes() {
        return classTypes;
    }
    /**
     * @return Returns the globalAppreciation.
     */
    public Double getGlobalAppreciation() {
        return globalAppreciation;
    }
    /**
     * @param globalAppreciation The globalAppreciation to set.
     */
    public void setGlobalAppreciation(Double globalAppreciation) {
		if(InquiriesUtil.isValidAnswer(globalAppreciation))
	        this.globalAppreciation = globalAppreciation;
		else
			this.globalAppreciation = null;
    }
    /**
     * @return Returns the inquiriesCourse.
     */
    public InfoInquiriesCourse getInquiriesCourse() {
        return inquiriesCourse;
    }
    /**
     * @param inquiriesCourse The inquiriesCourse to set.
     */
    public void setInquiriesCourse(InfoInquiriesCourse inquiriesCourse) {
        this.inquiriesCourse = inquiriesCourse;
    }
    /**
     * @return Returns the keyInquiriesCourse.
     */
    public Integer getKeyInquiriesCourse() {
        return keyInquiriesCourse;
    }
    /**
     * @param keyInquiriesCourse The keyInquiriesCourse to set.
     */
    public void setKeyInquiriesCourse(Integer keyInquiriesCourse) {
        this.keyInquiriesCourse = keyInquiriesCourse;
    }
    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher() {
        return keyTeacher;
    }
    /**
     * @param keyTeacher The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher) {
        this.keyTeacher = keyTeacher;
    }
    /**
     * @return Returns the studentAssiduity.
     */
    public Integer getStudentAssiduity() {
        return studentAssiduity;
    }
    /**
     * @param studentAssiduity The studentAssiduity to set.
     */
    public void setStudentAssiduity(Integer studentAssiduity) {
		if(InquiriesUtil.isValidAnswer(studentAssiduity))
	        this.studentAssiduity = studentAssiduity;
		else
			this.studentAssiduity = null;
    }
    /**
     * @return Returns the teacherOrNonAffiliatedTeacher.
     */
    public InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes getTeacherOrNonAffiliatedTeacher() {
        return teacherOrNonAffiliatedTeacher;
    }
    /**
     * @param teacherOrNonAffiliatedTeacher The teacherOrNonAffiliatedTeacher to set.
     */
    public void setTeacherOrNonAffiliatedTeacher(InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes teacher) {
        this.teacherOrNonAffiliatedTeacher = teacher;
    }
    /**
     * @return Returns the teacherAssiduity.
     */
    public Integer getTeacherAssiduity() {
        return teacherAssiduity;
    }
    /**
     * @param teacherAssiduity The teacherAssiduity to set.
     */
    public void setTeacherAssiduity(Integer teacherAssiduity) {
		if(InquiriesUtil.isValidAnswer(teacherAssiduity))
	        this.teacherAssiduity = teacherAssiduity;
		else
			this.teacherAssiduity = null;
    }
    /**
     * @return Returns the teacherAssurance.
     */
    public Double getTeacherAssurance() {
        return teacherAssurance;
    }
    /**
     * @param teacherAssurance The teacherAssurance to set.
     */
    public void setTeacherAssurance(Double teacherAssurance) {
		if(InquiriesUtil.isValidAnswer(teacherAssurance))
	        this.teacherAssurance = teacherAssurance;
		else
			this.teacherAssurance = null;
    }
    /**
     * @return Returns the teacherAvailability.
     */
    public Double getTeacherAvailability() {
        return teacherAvailability;
    }
    /**
     * @param teacherAvailability The teacherAvailability to set.
     */
    public void setTeacherAvailability(Double teacherAvailability) {
		if(InquiriesUtil.isValidAnswer(teacherAvailability))
	        this.teacherAvailability = teacherAvailability;
		else
			this.teacherAvailability = null;
    }
    /**
     * @return Returns the teacherClarity.
     */
    public Double getTeacherClarity() {
        return teacherClarity;
    }
    /**
     * @param teacherClarity The teacherClarity to set.
     */
    public void setTeacherClarity(Double teacherClarity) {
		if(InquiriesUtil.isValidAnswer(teacherClarity))
			this.teacherClarity = teacherClarity;
		else
			this.teacherClarity = null;
    }
    /**
     * @return Returns the teacherInterestStimulation.
     */
    public Double getTeacherInterestStimulation() {
        return teacherInterestStimulation;
    }
    /**
     * @param teacherInterestStimulation The teacherInterestStimulation to set.
     */
    public void setTeacherInterestStimulation(Double teacherInterestStimulation) {
		if(InquiriesUtil.isValidAnswer(teacherInterestStimulation))
	        this.teacherInterestStimulation = teacherInterestStimulation;
		else
			this.teacherInterestStimulation = null;
    }
    /**
     * @return Returns the teacherPunctuality.
     */
    public Double getTeacherPunctuality() {
        return teacherPunctuality;
    }
    /**
     * @param teacherPunctuality The teacherPunctuality to set.
     */
    public void setTeacherPunctuality(Double teacherPunctuality) {
		if(InquiriesUtil.isValidAnswer(teacherPunctuality))
			this.teacherPunctuality = teacherPunctuality;
		else
			this.teacherPunctuality = null;
    }
    /**
     * @return Returns the teacherReasoningStimulation.
     */
    public Double getTeacherReasoningStimulation() {
        return teacherReasoningStimulation;
    }
    /**
     * @param teacherReasoningStimulation The teacherReasoningStimulation to set.
     */
    public void setTeacherReasoningStimulation(Double teacherReasoningStimulation) {
		if(InquiriesUtil.isValidAnswer(teacherReasoningStimulation))
	        this.teacherReasoningStimulation = teacherReasoningStimulation;
		else
			this.teacherReasoningStimulation = null;
    }
    
		
	public int compareTo(Object arg0) {
        return 0;
    }

    public static InfoInquiriesTeacher newInfoFromDomain(InquiriesTeacher inquiriesTeacher) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        InfoInquiriesTeacher newInfo = null;
        if(inquiriesTeacher != null) {
            newInfo = new InfoInquiriesTeacher();
            newInfo.copyFromDomain(inquiriesTeacher);
        }
        return newInfo;
    }
       
    public void copyFromDomain(InquiriesTeacher inquiriesTeacher) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (inquiriesTeacher != null) {
            super.copyFromDomain(inquiriesTeacher);

            setGlobalAppreciation(inquiriesTeacher.getGlobalAppreciation());
            setIdInternal(inquiriesTeacher.getIdInternal());
            setStudentAssiduity(inquiriesTeacher.getStudentAssiduity());
            setTeacherAssiduity(inquiriesTeacher.getTeacherAssiduity());
            setTeacherAssurance(inquiriesTeacher.getTeacherAssurance());
            setTeacherAvailability(inquiriesTeacher.getTeacherAvailability());
            setTeacherClarity(inquiriesTeacher.getTeacherClarity());
            setTeacherInterestStimulation(inquiriesTeacher.getTeacherInterestStimulation());
            setTeacherPunctuality(inquiriesTeacher.getTeacherPunctuality());
            setTeacherReasoningStimulation(inquiriesTeacher.getTeacherReasoningStimulation());
	        this.setInquiriesCourse(InfoInquiriesCourse.newInfoFromDomain(inquiriesTeacher.getInquiriesCourse()));
			this.getClassTypes().add(inquiriesTeacher.getShiftType());
			
			InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes teacher =
				new InfoTeacherOrNonAffiliatedTeacherWithRemainingClassTypes();
			teacher.setTeacher(InfoTeacher.newInfoFromDomain(inquiriesTeacher.getTeacher()));
			InfoNonAffiliatedTeacher infoNonAffiliatedTeacher = new InfoNonAffiliatedTeacher();
			infoNonAffiliatedTeacher.copyFromDomain(inquiriesTeacher.getNonAffiliatedTeacher());
			teacher.setNonAffiliatedTeacher(infoNonAffiliatedTeacher);
			this.setTeacherOrNonAffiliatedTeacher(teacher);

        }
        
		
    }
        
	public String toString() {
        String result = "[INFOINQUIRIESTEACHER";
        result += ", id=" + getIdInternal();
		if(teacherOrNonAffiliatedTeacher != null)
			result += ", teacherOrNonAffiliatedTeacher=" + teacherOrNonAffiliatedTeacher.toString();
		result += ", classTypes=" + classTypes;
		result += ", studentAssiduity=" + studentAssiduity;
		result += ", teacherAssiduity=" + teacherAssiduity;
		result += ", teacherPunctuality=" + teacherPunctuality;
		result += ", teacherClarity=" + teacherClarity;
		result += ", teacherAssurance=" + teacherAssurance;
		result += ", teacherInterestStimulation=" + teacherInterestStimulation;
		result += ", teacherAvailability=" + teacherAvailability;
		result += ", teacherReasoningStimulation=" + teacherReasoningStimulation;
		result += ", globalAppreciation=" + globalAppreciation;
        result += "]\n";
        return result;
	}
	
	public boolean hasClassType(String classType) {
		return classTypes.contains(ShiftType.valueOf(classType));
	}
}
