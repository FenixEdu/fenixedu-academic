<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="researchActivityId" name="researchActivity" property="idInternal" />
	<bean:define id="parameter" value="<%="researchActivityId=" + researchActivityId %>" />
	<bean:define id="schema" name="schema" type="java.lang.String" scope="request" />
	<bean:define id="activityType" name="researchActivity" property="class.simpleName" />
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.superTitle"/></em>
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="<%= "researcher.activity.editResearchActivity." + activityType %>"/></h2>
	
	<ul class="mvert2 list5">
		<li>
			<html:link page="/activities/activitiesManagement.do?method=listActivities">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.goBack" />
			</html:link>
		</li>
		<li>
			<html:link page="<%="/activities/activitiesManagement.do?method=prepareDelete&forwardTo=EditResearchActivity&" + parameter%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.delete" />
			</html:link> 
		</li>
	</ul>
	
	<logic:equal name="confirm" value="yes">
		<p class="mbottom1 mtop2"><span class="warning0"><bean:message key="researcher.activity.activitiesManagement.delete.useCase"/></span></p>
		<fr:form action="<%="/activities/activitiesManagement.do?method=delete&forwardTo=EditResearchActivity&" + parameter%>">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
			</html:submit>
		</fr:form>
		<br />
	</logic:equal>

	<bean:message key="link.edit" bundle="RESEARCHER_RESOURCES"/>: 
	<html:link page="<%="/activities/editResearchActivity.do?method=prepareEditData&" + parameter %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="label.activityData" />
	</html:link>, 
	<logic:equal name="activityType" value="Cooperation">
		<html:link page="<%="/activities/editResearchActivity.do?method=prepareEditParticipants&" + parameter %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="label.cooperation.colaborationForm" />
		</html:link>
	</logic:equal>
	<logic:notEqual name="activityType" value="Cooperation">
		<html:link page="<%="/activities/editResearchActivity.do?method=prepareEditParticipants&" + parameter %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="label.activityRoles" />
		</html:link>
	</logic:notEqual>

	<%-- DATA --%>		
	<fr:view name="editionBean" schema="<%= schema %>">
	    <fr:layout name="tabular-nonNullValues">
    	    <fr:property name="classes" value="tstyle2 thlight thtop thleft"/>
    	    <fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,"/>
			<fr:property name="columnClasses" value="width12em, width50em"/>
			<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
	    </fr:layout>
	</fr:view>

</logic:present>