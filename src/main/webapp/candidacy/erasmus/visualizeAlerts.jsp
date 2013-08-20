<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusAlert" %>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<html:xhtml/>


<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="label.erasmus.visualize.alerts.title" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />

<p>
	<html:link action='<%= f("/caseHandlingMobilityIndividualApplicationProcess.do?method=listProcessAllowedActivities&amp;processId=%s", processId.toString()) %>'>
		« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p><span class="error0"><bean:write name="message" /></span></p>
</html:messages>


<fr:hasMessages for="individualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>



<script src="<%= request.getContextPath() + "/javaScript/jquery/jquery.js" %>" type="text/javascript" ></script>


<logic:empty name="individualCandidacyProcessBean" property="individualCandidacyProcess.alert">
	<p class="mbottom05"><em><bean:message key="label.erasmus.visualize.alerts.empty" bundle="ACADEMIC_OFFICE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:iterate name="individualCandidacyProcessBean" property="individualCandidacyProcess.alert" id="alert" type="ErasmusAlert">
	<logic:equal name="alert" property="toFire" value="true">
		<logic:present role="INTERNATIONAL_RELATION_OFFICE" >
			<html:link action='<%= f("/caseHandlingMobilityIndividualApplicationProcess.do?method=markAlertAsViewed&amp;erasmusAlertId=%s&amp;processId=%s", alert.getExternalId(), processId.toString()) %>'>
				<bean:message key="label.eramus.alert.mark.as.viewed" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</html:link>
		</logic:present>
	</logic:equal>
	<fr:view name="alert" schema="ErasmusAlert.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
		</fr:layout>		
	</fr:view>
</logic:iterate>


