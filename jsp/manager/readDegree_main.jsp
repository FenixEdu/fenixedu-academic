<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<bean:define id="degreeId" name="internalId" />
<ul style="list-style-type: square;">
	<li><html:link page="/readDegrees.do"><bean:message key="label.manager.readDegrees" /></html:link></li>
	<br>
	<br>
	<li><html:link page="/editDegree.do" paramId="idInternal" paramName="degreeId"><bean:message key="label.manager.editDegree" /></html:link></li>
	<br>
	<br>
	<li><html:link page="/readCurricularPlans.do" paramId="idInternal" paramName="degreeId"><bean:message key="label.manager.readCurricularPlans" /></html:link></li>			
</ul>