package Dominio;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class Mark extends DomainObject implements IMark {

	private Integer keyAttend;
	private Integer keyExam;

	private String mark;
	private String publishedMark;
	private IFrequenta attend;
	private IExam exam;

	public Mark(Integer idInternal) {
		setIdInternal(idInternal);
	}
	public Mark() {

	}
	
	public Mark(
		String mark,
		String publishedMark,
		IFrequenta attend,
		IExam exam) {
		setAttend(attend);
		setExam(exam);
		setPublishedMark(publishedMark);
	}

	public boolean equals(Object obj) {
		boolean resultado = false;
		if (obj instanceof Mark) {
			Mark dcp = (Mark) obj;
			resultado =
				this.getMark().equals(dcp.getMark())
					&& this.getPublishedMark().equals(dcp.getPublishedMark());
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + getIdInternal() + "; ";
		result += "mark = " + this.mark + "; ";
		result += "published mark = " + this.publishedMark + "; ";
		result += "Exam = " + this.exam + "; ";
		result += "attend = " + this.attend + "; ";

		return result;
	}

	/**
	 * @return
	 */
	public IExam getExam() {
		return exam;
	}

	/**
	 * @return
	 */
	public IFrequenta getAttend() {
		return attend;
	}

	/**
	 * @return
	 */
	public Integer getKeyAttend() {
		return keyAttend;
	}

	/**
	 * @return
	 */
	public Integer getKeyExam() {
		return keyExam;
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
	public void setExam(IExam exam) {
		this.exam = exam;
	}

	/**
	 * @param attend
	 */
	public void setAttend(IFrequenta attend) {
		this.attend = attend;
	}

	/**
	 * @param integer
	 */
	public void setKeyAttend(Integer integer) {
		keyAttend = integer;
	}

	/**
	 * @param integer
	 */
	public void setKeyExam(Integer integer) {
		keyExam = integer;
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