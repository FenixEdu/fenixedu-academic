package Dominio;

/**
 * @author Tânia Pousão
 * 24 de Junho de 2003	
 */
public abstract class Evaluation extends DomainObject implements IEvaluation {
	private String ojbConcreteClass;
	protected String publishmentMessage;

	public Evaluation() {
		super();
		this.ojbConcreteClass = this.getClass().getName();
	}
	
	/**
	 * @return String
	 */
	public String getOjbConcreteClass() {
		return ojbConcreteClass;
	}

	/**
	 * @param string
	 */
	public void setOjbConcreteClass(String string) {
		ojbConcreteClass = string;
	}

	/**
	 * @return String
	 */
	public String getPublishmentMessage() {
		return publishmentMessage;
	}

	/**
	 * @param string
	 */
	public void setPublishmentMessage(String string) {
		publishmentMessage = string;
	}

}
