<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="activityId" name="researchActivity" property="externalId"/>
<bean:define id="parameter" value="<%= "activityId=" + activityId %>"/>
<bean:define id="activityType" name="researchActivity" property="class.simpleName" />
<bean:define id="schema" value="<%= activityType + ".view-defaults" %>" type="java.lang.String" scope="request" />

<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
<h2><bean:message bundle="RESEARCHER_RESOURCES" key="<%= "researcher.activity.editResearchActivity." + activityType %>"/></h2>

<ul class="mvert2 list5">
	<li>
		<html:link page="/activities/activitiesManagement.do?method=listActivities">
			<bean:message bundle="RESEARCHER_RESOURCES" key="label.back" />
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

 


<%-- DATA --%>		
<fr:view name="researchActivity" schema="<%= schema %>">
    <fr:layout name="tabular-nonNullValues">
	    <fr:property name="classes" value="tstyle2 thlight thtop thleft"/>
	    <fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,"/>
		<fr:property name="columnClasses" value="width12em, width50em"/>
		<fr:property name="rowClasses" value="tdbold,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,"/>
    </fr:layout>
</fr:view>
