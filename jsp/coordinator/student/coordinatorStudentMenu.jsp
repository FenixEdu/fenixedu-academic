<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html>
  <head>
    <title><bean:message key="title.coordinator.main" /></title>
  </head>
  <body>
  
    <html:link page="/candidateSection.do"><bean:message key="link.coordinator.candidate" />
    </html:link><br>

	<br>
  
  	<bean:message key="label.coordinator.student" /><br>
        &nbsp;&nbsp;- <html:link page="/visualizeStudents.do?method=prepare&action=visualize&page=0"><bean:message key="link.coordinator.visualizeStudent" /></html:link><br>
        &nbsp;&nbsp;- <html:link page="/setEvaluations.do?method=prepare&action=edit&page=0"><bean:message key="link.coordinator.setEvaluations" /></html:link><br>
	<br>
	
	<html:link forward="logoff">
	<bean:message key="link.logout"/>
</html:link><br>
  </body>
</html>