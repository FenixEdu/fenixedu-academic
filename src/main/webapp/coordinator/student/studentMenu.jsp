<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

    <html:link page="/candidateSection.do"><bean:message key="link.coordinator.candidate" />
    </html:link><br/>

	<br/>
  
  	<bean:message key="label.coordinator.student" /><br/>
    <li><html:link page="/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0"><bean:message key="link.coordinator.studentListByDegree" /></html:link><br/></li>
	<br/>
	
