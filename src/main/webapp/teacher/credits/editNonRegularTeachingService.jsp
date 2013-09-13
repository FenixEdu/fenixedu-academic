<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<logic:present name="person">
	<fr:view name="person" schema="PersonNameAndUsername">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight mtop05"/>
	   		<fr:property name="columnClasses" value="aleft,"/>
		</fr:layout>
	</fr:view>
	<br/>
</logic:present>

<logic:present  name="professorship">
	<strong><bean:write name="professorship" property="executionCourse.nome"/>				
	(<bean:write name="professorship" property="degreeSiglas"/>)</strong>
</logic:present>

<logic:messagesPresent message="true">
	<html:messages id="message" message="true" bundle="SCIENTIFIC_COUNCIL_RESOURCES">
		<p><span class="error0"><bean:write name="message"/></span></p>
	</html:messages>
</logic:messagesPresent>

<logic:present name="nonRegularTeachingServiceBean">
	<fr:form action="/manageNonRegularTeachingService.do?method=editNonRegularTeachingService">
		<fr:edit id="nonRegularTeachingServiceBeans" name="nonRegularTeachingServiceBean">
			<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.teacher.credits.NonRegularTeachingServiceBean" bundle="APPLICATION_RESOURCES">
				<fr:slot name="shift.presentationName" key="label.class.name" readOnly="true"/>
				<fr:slot name="percentage">
					<fr:property name="size" value="3"/>
				</fr:slot>
				<fr:slot name="teachingServicePercentages" key="label.teacher.applied" readOnly="true" layout="flowLayout">
					<fr:property name="eachLayout" value="values-dash"/>
					<fr:property name="htmlSeparator" value="<br/>"/>
					
				</fr:slot>
			</fr:schema>
			<fr:layout name="tabular-editable">
				<fr:property name="classes" value="tstyle2 thlight mtop15" />
				<fr:property name="sortBy" value="shift.nome" />
			</fr:layout>
		</fr:edit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
			<bean:message key="button.confirm" bundle="APPLICATION_RESOURCES"/>
		</html:submit>
	</fr:form>
</logic:present>

