/**
 * Project Sop 
 * 
 * Package DataBeans
 * 
 * Created on 16/Jan/2003
 *
 */
package DataBeans;

/**
 * @author tfc130
 *
 * 
 */
public final class InfoLessonServiceResult {
	public static final int SUCESS = 0;
	public static final int INVALID_TIME_INTERVAL = 1;

	
	private int _messageType;
	
	public InfoLessonServiceResult() { }
	
	public InfoLessonServiceResult(int messageType){
		switch (messageType) {
			case SUCESS :
			case INVALID_TIME_INTERVAL:
				_messageType = messageType;
				break;				
			default :
				throw new IllegalArgumentException("Message type not recognized!");
		} 
	}
	
	public int getMessageType() {
		return _messageType;
	}

	public void setMessageType(int messageType) {
		_messageType = messageType;
	}

	public boolean isSUCESS() {
		return (_messageType == SUCESS);
	}

}
