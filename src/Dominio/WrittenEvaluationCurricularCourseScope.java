package Dominio;


/**
 * @author Fernanda Quitério
 * created on 16/06/2004
 */
public class WrittenEvaluationCurricularCourseScope	extends DomainObject implements IWrittenEvaluationCurricularCourseScope {

	private Integer keyWrittenEvaluation;
	private Integer keyCurricularCourseScope;
	
	private IWrittenEvaluation writtenEvaluation;
	private ICurricularCourseScope curricularCourseScope;
	
	public WrittenEvaluationCurricularCourseScope() {
	}

	public boolean equals(Object obj) {
		return (
			(obj instanceof WrittenEvaluationCurricularCourseScope) && 
			(((WrittenEvaluationCurricularCourseScope) obj).getCurricularCourseScope().equals(getCurricularCourseScope())) &&
			(((WrittenEvaluationCurricularCourseScope) obj).getWrittenEvaluation().equals(getWrittenEvaluation())));
	}

	public String toString() {
		return "[WRITTEN EVALUATION CURRICULAR COURSE SCOPE:"
			+ " WrittenEvaluation= '"
			+ this.getWrittenEvaluation()
			+ "'\n"
			+ " CurricularCourseScope= '"
			+ this.getCurricularCourseScope()
			+ "'\n"
			+ "";
	}

	/**
	 * @return
	 */
	public Integer getKeyWrittenEvaluation() {
		return keyWrittenEvaluation;
	}

	/**
	 * @return
	 */
	public Integer getKeyCurricularCourseScope() {
		return keyCurricularCourseScope;
	}

	/**
	 * @return
	 */
	public IWrittenEvaluation getWrittenEvaluation() {
		return writtenEvaluation;
	}

	/**
	 * @return
	 */
	public ICurricularCourseScope getCurricularCourseScope() {
		return curricularCourseScope;
	}

	/**
	 * @param integer
	 */
	public void setKeyWrittenEvaluation(Integer integer) {
		keyWrittenEvaluation = integer;
	}

	/**
	 * @param integer
	 */
	public void setKeyCurricularCourseScope(Integer integer) {
		keyCurricularCourseScope = integer;
	}

	/**
	 * @param writtenEvaluation
	 */
	public void setWrittenEvaluation(IWrittenEvaluation writtenEvaluation) {
		this.writtenEvaluation = writtenEvaluation;
	}

	/**
	 * @param curricularCourseScope
	 */
	public void setCurricularCourseScope(ICurricularCourseScope curricularCourseScope) {
		this.curricularCourseScope = curricularCourseScope;
	}



}