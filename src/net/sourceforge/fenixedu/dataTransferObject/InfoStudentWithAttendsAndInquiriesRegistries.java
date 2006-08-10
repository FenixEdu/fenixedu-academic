/*
 * Created on 3/Jun/2005 - 16:22:30
 * 
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRegistry;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InfoStudentWithAttendsAndInquiriesRegistries extends InfoStudent {
	
	private List<InfoFrequenta> attends;
	private List<InfoInquiriesRegistry> inquiriesRegistries;
	
	public InfoStudentWithAttendsAndInquiriesRegistries() {
//		this.attends = new ArrayList<InfoFrequenta>();
//		this.inquiriesRegistries = new ArrayList<InfoInquiriesRegistry>();
	}

	public InfoStudentWithAttendsAndInquiriesRegistries(InfoStudent aluno) {
		this.setIdInternal(aluno.getIdInternal());
	    this.setNumber(aluno.getNumber());
		this.setState(aluno.getState());
		this.setInfoPerson(aluno.getInfoPerson());
		this.setDegreeType(aluno.getDegreeType());
		this.setInfoStudentKind(aluno.getInfoStudentKind());
		this.setPayedTuition(aluno.getPayedTuition());
//		this.attends = new ArrayList<InfoFrequenta>();
	}

	/**
	 * @return Returns the attends.
	 */
	public List<InfoFrequenta> getAttends() {
		return attends;
	}
	

	/**
	 * @param attends The attends to set.
	 */
	public void setAttends(List<InfoFrequenta> attends) {
		this.attends = attends;
	}

	/**
	 * @return Returns the inquiriesRegistries.
	 */
	public List<InfoInquiriesRegistry> getInquiriesRegistries() {
		return inquiriesRegistries;
	}
	

	/**
	 * @param inquiriesRegistries The inquiriesRegistries to set.
	 */
	public void setInquiriesRegistries(
			List<InfoInquiriesRegistry> inquiriesRegistries) {
		this.inquiriesRegistries = inquiriesRegistries;
	}
	
//    public void copyFromDomain(Registration student) {
//
//        if (student != null) {
//	        super.copyFromDomain(student);
//            this.setNumber(student.getNumber());
//			this.setDegreeType(student.getDegreeType());
//			this.setState(student.getState());
//			this.setPayedTuition(student.getPayedTuition());
//			this.setInfoPerson(InfoPerson.newInfoFromDomain(student.getPerson()));
//			this.
//			
//        }
//    }
//
//    public static InfoStudent newInfoFromDomain(Registration student) {
//        InfoStudent infoStudent = null;
//        if (student != null) {
//            infoStudent = new InfoStudent();
//            infoStudent.copyFromDomain(student);
//        }
//        return infoStudent;
//    }
//
	
	

}
