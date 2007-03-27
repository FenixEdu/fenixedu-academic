<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="participationList" name="participations" scope="request" type="java.util.List"/>
<bean:define id="forwardTo" name="forwardTo" scope="request" type="java.lang.String"/>
<bean:define id="schema" name="schema" scope="request" type="java.lang.String"/>

<div style="width: 600px;">
<ul>
	<logic:iterate id="participation" name="participations">
		<bean:define id="participationId" name="participation" property="idInternal"/>
		<bean:define id="parameters" value="<%= "participationId=" + participationId + "&forwardTo=" +  forwardTo%>" toScope="request" />
		<li class="mtop1">
			<fr:view name="participation" layout="nonNullValues" schema="<%= schema %>">
				<fr:layout>
					<fr:property name="htmlSeparator" value=", "/>
					<fr:property name="indentation" value="false"/>
				</fr:layout>
			</fr:view>
		</li>
	</logic:iterate>
</ul>
</div>