<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="RESEARCHER">	
	<bean:define id="activityId" name="researchActivity" property="idInternal" />

	<bean:define id="parameter" value="<%="activityId=" + activityId %>" />
	<bean:define id="activityType" name="researchActivity" property="class.simpleName" />
	<bean:define id="schema" value="<%= activityType + ".edit-defaults" %>" type="java.lang.String" scope="request" />
	<em><bean:message bundle="RESEARCHER_RESOURCES" key="label.researchPortal"/></em> 
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="<%= "researcher.activity.editResearchActivityData." + activityType %>"/></h2>
	
	<ul class="list5 mtop2 mbottom1">
		<li>
			<html:link page="<%="/activities/editResearchActivity.do?method=prepare" + activityType + "&amp;" + parameter %>">
				<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.goBack" />
			</html:link>
		</li>
	</ul>
  	
	<p class="mtop2 mbottom05"><strong><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.activity.editResearchActivityData.explanation"/></strong></p>
	<fr:form action="<%="/activities/editResearchActivity.do?method=prepare" + activityType + "&amp;" + parameter %>">
		<fr:edit name="researchActivity" schema="<%= schema %>">
		    <fr:layout name="tabular">
	    	    <fr:property name="classes" value="tstyle1 thlight thleft thtop mtop05"/>
	        	<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		    </fr:layout>
		    <fr:destination name="invalid" path="<%="/activities/editResearchActivity.do?method=prepareEdit" + activityType + "Data&" + parameter%>"/>
		    <fr:destination name="cancel" path="<%="/activities/editResearchActivity.do?method=prepare" + activityType + "&amp;" + parameter %>"/>
		</fr:edit>
		<html:submit><bean:message key="button.change" bundle="RESEARCHER_RESOURCES"/></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="RESEARCHER_RESOURCES"/></html:cancel>
	</fr:form>
</logic:present>
