<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="participationList" name="participations" scope="request" type="java.util.List"/>
<bean:define id="forwardTo" name="forwardTo" scope="request" type="java.lang.String"/>
<bean:define id="schema" name="schema" scope="request" type="java.lang.String"/>

<div>
<ul class="listresearch">
	<logic:iterate id="participation" name="participations">
		<bean:define id="participationId" name="participation" property="externalId"/>
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
