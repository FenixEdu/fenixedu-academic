<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.candidacies" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>

<jsp:include flush="true" page="/caseHandling/listProcesses.jsp" />

<br/>
<br/>
<bean:define id="processName" name="processName"/>
<html:form action='<%= "/caseHandling" + processName.toString() + ".do?method=intro"%>'>
	<html:cancel><bean:message key='label.back' bundle="APPLICATION_RESOURCES"/></html:cancel>			
</html:form>
