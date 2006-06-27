<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:form action="/guidesManagement">
	<html:hidden property="method" value="searchEventsForCandidacy" />
	
	<h2><bean:message key="link.masterDegree.administrativeOffice.guides" /></h2>
	<hr>
	<h3><bean:message key="label.masterDegree.administrativeOffice.payments.searchCandidacy" /></h3>
	
	<logic:messagesPresent message="true">
	<ul>
	<html:messages id="messages" message="true">
		<li><span class="error0"><bean:write name="messages" /></span></li>
	</html:messages>
	</ul>
	<br/>
	</logic:messagesPresent>
		
	<bean:message key="label.masterDegree.administrativeOffice.payments.candidacyNumber" />:
	<html:text property="candidacyNumber"/>
	<br/><br/>
	<html:submit styleClass="inputbutton"><bean:message key="label.masterDegree.administrativeOffice.payments.search"/></html:submit>

</html:form>