<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>

<logic:messagesPresent>
	<html:errors/>
</logic:messagesPresent>

<tiles:insert definition="teacherContextTile">
	<tiles:put name="infoTeacher" beanName="infoTeacher" />
</tiles:insert>

<html:form action="/CRUDServiceExemption">
	<html:hidden property="idInternal"/>
	<html:hidden property="teacherId"/>
	<html:hidden property="page" value="1"/>	
	<html:hidden property="method" value="edit"/>
	<html:hidden property="teacherNumber" name="infoTeacher"/>
	<table>
		<tr>
			<td width="10%">
				<strong><bean:message key="label.serviceExemption.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES" /></strong>
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.credits.ServiceExemptionType" bundle="ENUMERATION_RESOURCES"/>
				<html:select property="type">
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<td style="vertical-align:top">
				<strong><bean:message key="label.serviceExemption.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>
			</td>
			<td>
				<html:text property="start" size="12"/>
			</td>
		</tr>
		<tr>
			<td style="vertical-align:top">
				<strong><bean:message key="label.serviceExemption.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>
			</td>
			<td>
				<html:text property="end" size="12"/>
			</td>
		</tr>
	</table>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.submit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
	</html:submit>
</html:form>
