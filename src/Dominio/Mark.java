package Dominio;

/**
 * @author dcs-rjao
 *
 * 19/Mar/2003
 */

public class Mark extends DomainObject implements IMark {

	private Integer keyAttend;
	private Integer keyEvaluation;

	private String mark;
	private String publishedMark;
	private IFrequenta attend;
	private IEvaluation evaluation;

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
		setEvaluation(evaluation);
		setPublishedMark(publishedMark);
		setMark(mark);
	}

	public boolean equals(Object obj) {

		boolean resultado = false;
		if (obj instanceof Mark) {
			Mark mark = (Mark) obj;
			resultado = this.getAttend().equals(mark.getAttend()) && this.getEvaluation().equals(mark.getEvaluation());
//				(this.getMark() == null
//					&& mark == null
//					&& this.getPublishedMark() == null
//					&& mark.getPublishedMark() == null)
//					|| (this.getMark() != null
//						&& this.getPublishedMark() != null
//						&& this.getMark().equals(mark.getMark())
//						&& this.getPublishedMark().equals(
//							mark.getPublishedMark()));
		}
		return resultado;
	}

	public String toString() {
		String result = "[" + this.getClass().getName() + ": ";
		result += "idInternal = " + getIdInternal() + "; ";
		result += "mark = " + this.mark + "; ";
		result += "published mark = " + this.publishedMark + "; ";
		result += "evaluation= " + this.evaluation.getIdInternal() + "; ";
		result += "attend = " + this.attend.getIdInternal() + "; ]";

		return result;
	}

	/**
	 * @return
	 */
	public IEvaluation getEvaluation() {
		return evaluation;
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
	public Integer getKeyEvaluation() {
		return keyEvaluation;
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
	public void setEvaluation(IEvaluation evaluation) {
		this.evaluation = evaluation;
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
	public void setKeyEvaluation(Integer integer) {
		keyEvaluation = integer;
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