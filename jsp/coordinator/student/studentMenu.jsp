<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

    <html:link page="/candidateSection.do"><bean:message key="link.coordinator.candidate" />
    </html:link><br>

	<br>
  
  	<bean:message key="label.coordinator.student" /><br>
        &nbsp;&nbsp;- <html:link page="/listStudentsForCoordinator.do?method=getStudentsFromDCP&page=0"><bean:message key="link.coordinator.studentListByDegree" /></html:link><br>
	<br>
	
