/*
 * InfoStudent.java
 *
 * Created on 13 de Dezembro de 2002, 16:04
 */

package DataBeans;


/**
 *
 * @author  tfc130
 */

public class InfoMark {
	protected String mark;
	protected String publishedMark;
	private InfoExam infoExam;
	private InfoFrequenta infoFrequenta;
	
	
	public InfoMark() {}

	public InfoMark(String mark, String publishedMark, InfoFrequenta frequenta, InfoExam exam) {
			setInfoFrequenta(frequenta);
			setInfoExam(exam);
			setPublishedMark(mark);
			setPublishedMark(publishedMark);			
	}

	

//  public boolean equals(Object obj) {
//    boolean resultado = false;
//    if (obj instanceof InfoMark) {
//      InfoMark infoMark = (InfoMark)obj;
//      resultado = getMark().equals(infoMark.getPublishedMark());
//    }
//    return resultado;
//  }
  
  public String toString() {
    String result = "[InfoStudent";
    result += ", mark=" + mark;
    result += ", published mark=" + publishedMark;

    if (infoExam != null)
    	result += ", exam" + infoExam.toString();
    result += "]";
	if (infoFrequenta != null)
		   result += ", exam" + infoFrequenta.toString();
	   result += "]";
    return result;
  }
  


	/**
	 * @return
	 */
	public InfoExam getInfoExam() {
		return infoExam;
	}

	/**
	 * @return
	 */
	public InfoFrequenta getInfoFrequenta() {
		return infoFrequenta;
	}

	/**
	 * @return
	 */
	public String getMark() {
		return mark;
	}

	/**
	 * @return
	 */
	public String getPublishedMark() {
		return publishedMark;
	}

	/**
	 * @param exam
	 */
	public void setInfoExam(InfoExam exam) {
		infoExam = exam;
	}

	/**
	 * @param frequenta
	 */
	public void setInfoFrequenta(InfoFrequenta frequenta) {
		infoFrequenta = frequenta;
	}

	/**
	 * @param string
	 */
	public void setMark(String string) {
		mark = string;
	}

	/**
	 * @param string
	 */
	public void setPublishedMark(String string) {
		publishedMark = string;
	}

}