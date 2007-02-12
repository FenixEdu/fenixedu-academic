<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="activitiesList" name="activities" scope="request" type="java.util.List"/>
<bean:define id="forwardTo" name="forwardTo" scope="request" type="java.lang.String"/>
<bean:define id="schema" name="schema" scope="request" type="java.lang.String"/>


<ul style="width: 600px;">
	<logic:iterate id="activity" name="activitiesList">
		<bean:define id="researchActivityId" name="activity" property="idInternal"/>
		<bean:define id="parameters" value="<%= "researchActivityId=" + researchActivityId + "&forwardTo=" +  forwardTo%>" toScope="request" />
		<li class="mtop0">
			<fr:view name="activity" layout="nonNullValues" schema="<%= schema %>">
				<fr:layout>
					<fr:property name="htmlSeparator" value=", "/>
					<fr:property name="indentation" value="false"/>
				</fr:layout>
			</fr:view>
		</li>
	</logic:iterate>
</ul>