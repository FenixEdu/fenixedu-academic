/*
 * Created on 26/Set/2003, 20:02:21
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Util;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 *
 * 
 * Created at 26/Set/2003, 20:02:21
 * 
 */
public class EmailReporter implements TransportListener, Serializable 
{
	private List invalid;
	private List validUndelivered;
	private List delivered;
	//
	//
    public EmailReporter()
    {
        invalid = new LinkedList();
        validUndelivered = new LinkedList();
        delivered = new LinkedList();
    }
    //
    //
	private void fillLists(TransportEvent e)
	{
		for (int i= 0; i < e.getInvalidAddresses().length; i++)
			this.invalid.add(e.getInvalidAddresses()[i]);
		for (int i= 0; i < e.getValidUnsentAddresses().length; i++)
			this.validUndelivered.add(e.getValidUnsentAddresses()[i]);
		for (int i= 0; i < e.getValidSentAddresses().length; i++)
			this.delivered.add(e.getValidSentAddresses()[i]);
	}
	public void messageDelivered(TransportEvent arg0)
	{
        System.out.println("Message delivered !!");
		this.fillLists(arg0);
	}
	/* (non-Javadoc)
	 * @see javax.mail.event.TransportListener#messageNotDelivered(javax.mail.event.TransportEvent)
	 */
	public void messageNotDelivered(TransportEvent arg0)
	{
        System.out.println("Message not delivered !!");
        this.fillLists(arg0);
	}
	/* (non-Javadoc)
	 * @see javax.mail.event.TransportListener#messagePartiallyDelivered(javax.mail.event.TransportEvent)
	 */
	public void messagePartiallyDelivered(TransportEvent arg0)
	{
        System.out.println("Messaged partially delivered !!");
        this.fillLists(arg0);
	}
    
    public List getAllUndelivered()
    {
        List allUndelivered = new LinkedList();
        allUndelivered.addAll(this.invalid);
        allUndelivered.addAll(this.validUndelivered);
        return allUndelivered;
    }
}
