package Dominio;

/**
 * Created on 11/Fev/2003
 * @author João Mota
 * ciapl 
 * Dominio
 * 
 */
public class ExecutionPeriod implements IExecutionPeriod {

	protected IExecutionYear executionYear;
	protected String name;
	private Integer internalCode;
	private Integer keyExecutionYear;
	/**
	 * Constructor for ExecutionPeriod.
	 */
	public ExecutionPeriod() {
			super();
		
		}
	public ExecutionPeriod(String name,IExecutionYear executionYear) {
		setName(name);
		setExecutionYear(executionYear);
		
	}

	/**
	 * Returns the executionYear.
	 * @return ExecutionYear
	 */
	public IExecutionYear getExecutionYear() {
		return executionYear;
	}

	/**
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the internalCode.
	 * @return Integer
	 */
	public Integer getInternalCode() {
		return internalCode;
	}

	/**
	 * Sets the internalCode.
	 * @param internalCode The internalCode to set
	 */
	public void setInternalCode(Integer internalCode) {
		this.internalCode = internalCode;
	}

	/**
	 * Returns the keyExecutionYear.
	 * @return Integer
	 */
	public Integer getKeyExecutionYear() {
		return keyExecutionYear;
	}

	/**
	 * Sets the keyExecutionYear.
	 * @param keyExecutionYear The keyExecutionYear to set
	 */
	public void setKeyExecutionYear(Integer keyExecutionYear) {
		this.keyExecutionYear = keyExecutionYear;
	}

	/**
	 * Sets the executionYear.
	 * @param executionYear The executionYear to set
	 */
	public void setExecutionYear(IExecutionYear executionYear) {
		this.executionYear = executionYear;
	}


	public String toString() {
		String result = "[EXECUTION_PERIOD";
		result += ", internalCode=" + internalCode;
		result += ", name=" + name;
		result += ", executionYear=" + getExecutionYear();
		result += "]";
		return result;
	}
	
	

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj instanceof IExecutionPeriod){
			IExecutionPeriod executionPeriod = (IExecutionPeriod) obj;
			return getName().equals(executionPeriod.getName())
				&& getExecutionYear().equals(executionPeriod.getExecutionYear());		
		}
		return super.equals(obj);
	}

}
