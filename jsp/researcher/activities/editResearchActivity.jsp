<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">
	<bean:define id="activityId" name="researchActivity" property="idInternal"/>
	<bean:define id="parameter" value="<%= "activityId=" + activityId %>"/>
	<bean:define id="activityType" name="researchActivity" property="class.simpleName" />
	<bean:define id="schema" value="<%= activityType + ".view-defaults" %>" type="java.lang.String" scope="request" />
	
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="<%= "researcher.activity.editResearchActivity." + activityType %>"/></h2>
	
	<ul class="mvert2 list5">
		<li>
			<html:link page="/activities/activitiesManagement.do?method=listActivities">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.goBack" />
			</html:link>
		</li>
		<li>
			<html:link page="<%="/activities/activitiesManagement.do?method=prepareDelete" + activityType + "Participations&forwardTo=Edit" + activityType +"&amp;" + parameter%>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.activitiesManagement.delete" />
			</html:link> 
		</li>
	</ul>
	
	<logic:equal name="confirm" value="yes">
		<p class="mbottom1 mtop2"><span class="warning0"><bean:message key="researcher.activity.activitiesManagement.delete.useCase"/></span></p>
		<fr:form action="<%="/activities/activitiesManagement.do?method=delete" + activityType +"Participations&forwardTo=Edit" + activityType + "&amp;" + parameter%>">
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.confirm" property="confirm">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.delete"/>
			</html:submit>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.cancel" property="cancel">
				<bean:message bundle="RESEARCHER_RESOURCES" key="button.cancel"/>
			</html:submit>
		</fr:form>
		<br />
	</logic:equal>

	<logic:messagesPresent message="true">
		<p>
			<html:messages id="messages" message="true" bundle="RESEARCHER_RESOURCES">
				<span class="error0"><bean:write name="messages"/></span>
			</html:messages>
		</p>
	</logic:messagesPresent>

	<bean:message key="link.edit" bundle="RESEARCHER_RESOURCES"/>: 
	<html:link page="<%="/activities/editResearchActivity.do?method=prepareEdit" + activityType + "Data&" + parameter %>">
			<bean:message bundle="RESEARCHER_RESOURCES" key="label.activityData" />
	</html:link>, 

	<html:link page="<%="/activities/editResearchActivity.do?method=prepareEdit" + activityType + "Participants&" + parameter %>">
		<logic:equal name="activityType" value="Cooperation">
				<bean:message bundle="RESEARCHER_RESOURCES" key="label.cooperation.colaborationForm" />
		</logic:equal>
		<logic:notEqual name="activityType" value="Cooperation">
				<bean:message bundle="RESEARCHER_RESOURCES" key="label.activityRoles" />
		</logic:notEqual>
	</html:link>

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