/*
 * Created on: 2004/02/25
 * 
 */
 
package ServidorAplicacao.Servico.exceptions;

/**
 * @author  Luis Cruz
 */
public class FenixRemoteServiceException extends FenixServiceException {

	private String causeClassName;
	private String causePackageName;
	
    public FenixRemoteServiceException() {
    	super();
    }
    
    public FenixRemoteServiceException(String message){
    	super(message);
    }
    
	public FenixRemoteServiceException(Throwable cause) {
		super(cause);
	}

	public FenixRemoteServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @return Returns the causeClassName.
	 */
	public String getCauseClassName()
	{
		return causeClassName;
	}

	/**
	 * @param causeClassName The causeClassName to set.
	 */
	public void setCauseClassName(String causeClassName)
	{
		this.causeClassName = causeClassName;
	}

	/**
	 * @return Returns the causePackage.
	 */
	public String getCausePackageName()
	{
		return causePackageName;
	}

	/**
	 * @param causePackage The causePackage to set.
	 */
	public void setCausePackageName(String causePackageName)
	{
		this.causePackageName = causePackageName;
	}

}