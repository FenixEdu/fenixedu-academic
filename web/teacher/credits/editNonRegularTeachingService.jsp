<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

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
			<bean:message key="button.confirm" />
		</html:submit>
	</fr:form>
</logic:present>

