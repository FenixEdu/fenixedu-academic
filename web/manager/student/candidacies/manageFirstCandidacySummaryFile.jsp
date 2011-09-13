<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.domain.StudentCurricularPlan"%><html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<logic:present role="MANAGER">
	<h2>Gerir Ficheiro de Sumário</h2>

	<fr:hasMessages for="student-number-bean" type="conversion">
		<ul class="nobullet list6">
			<fr:messages>
				<li><span class="error0"><fr:message /></span></li>
			</fr:messages>
		</ul>
	</fr:hasMessages>
	
	<logic:messagesPresent message="true" property="success">
		<div class="success5 mbottom05" style="width: 700px;">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES" property="success">
				<p class="mvert025"><bean:write name="messages" /></p>
			</html:messages>
		</div>
	</logic:messagesPresent>

	<fr:form action="/candidacySummary.do?method=searchCandidacy">

		<fr:edit id="student-number-bean" name="studentNumberBean" schema="StudentNumberBean.edit">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thright mtop05" />
			</fr:layout>
		</fr:edit>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit" /></html:submit>
	</fr:form>
	
	<logic:present name="candidacy">
		<bean:define id="candidacyID" name="candidacy" property="idInternal" />
		
		<br/><br/>
		<html:link	module="/candidate" action="<%= "/degreeCandidacyManagement.do?method=generateSummaryFile&amp;candidacyID=" + candidacyID%>">
			Gerar PDF de Sumário
		</html:link>
		<br/>
		<logic:present name="hasPDF">
			<html:link	module="/candidate" action="<%= "/degreeCandidacyManagement.do?method=showSummaryFile&amp;candidacyID=" + candidacyID%>">
				Mostrar PDF de Sumário
			</html:link>
		</logic:present>
	</logic:present>
</logic:present>