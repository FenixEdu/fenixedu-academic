/**
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.messaging;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.messaging.ExecutionCourseForum;
import net.sourceforge.fenixedu.domain.messaging.Forum;

/**
 * @author <a href="mailto:goncalo@ist.utl.pt"> Goncalo Luiz</a><br/>
 * Created on May 5, 2006, 9:15:52 AM
 *
 */
public class WriteExecutionCourseForum extends Service {
   
    static public class WriteExecutionCourseForumParameters
    {
	public String name;
	public String description;
	public Person owner;
    }
    
    public Forum run(WriteExecutionCourseForumParameters p)
    {
	Forum forum = new ExecutionCourseForum(p.owner,p.name,p.description);
	
	return forum;
    }
}
