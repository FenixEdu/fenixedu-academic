<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul style="list-style-type: square;">
	<li><html:link page="/readDegrees.do"><bean:message key="label.manager.readDegrees" /></html:link></li>
	<br>
	<br>
	<li><html:link page="/editDegree.do"><bean:message key="label.manager.editDegree" /></html:link></li>
	<br>
	<br>
	<li><html:link page="/readCurricularPlans.do"><bean:message key="label.manager.readCurricularPlans" /></html:link></li>			
</ul>