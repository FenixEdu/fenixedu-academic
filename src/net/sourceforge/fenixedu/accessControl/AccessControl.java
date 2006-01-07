/**
 * 
 */


package net.sourceforge.fenixedu.accessControl;


import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.UserGroup;
import net.sourceforge.fenixedu.domain.cms.Content;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt">Goncalo Luiz</a> <br/> <br/>
 *         <br/> Created on 17:31:53,23/Nov/2005
 * @version $Id$
 */
public class AccessControl
{

	public static class IllegalDataAccessException extends RuntimeException
	{
		private static final long serialVersionUID = 2264135195805915798L;

		public IllegalDataAccessException()
		{
			super();
		}

		public IllegalDataAccessException(String msg, Person person)
		{
			super(msg);
		}
	}

	private static InheritableThreadLocal<IUserView> userView = new InheritableThreadLocal<IUserView>();

	public static IUserView getUserView()
	{
		return AccessControl.userView.get();
	}

	public static void setUserView(IUserView userView)
	{
		AccessControl.userView.set(userView);
	}

	public static void check(DomainObject c, UserGroup group)
	{
		Person requester = AccessControl.getUserView().getPerson();
		boolean result = false;
		
        if (c instanceof Content) {
            result = ((Content)c).getOwners().contains(requester);
        }
		result |= (group!=null && group.isMember(requester));
		
		if (!result)
		{
			StringBuffer message = new StringBuffer();
			message.append("User ").append(requester.getUsername()).append(" tried to execute access content instance number").append(c.getIdInternal());
			message.append("but he/she is not authorized to do so");

			throw new IllegalDataAccessException(message.toString(),requester);
		}
	}
}
