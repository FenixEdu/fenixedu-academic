<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="datePattern" value="dd-MM-yyyy"/>

<bean:define id="link">
	/CRUDManagementPosition.do?teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;teacherNumber=<bean:write name="infoTeacher" property="teacherNumber"/>
</bean:define>

<tiles:insert definition="teacherContextTile">
	<tiles:put name="infoTeacher" beanName="infoTeacher" />
</tiles:insert>

<html:link page="/prepareServiceExemptionsManagement.do?method=doSearch" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
	<bean:message key="link.serviceExemptions.management"/>
</html:link>
<br />

<h3><bean:message key="label.managementPositions.management"/></h3>

<logic:notEmpty name="infoManagementPositions">
	<table width="100%" cellpadding="3">
		<tr>
			<td class="listClasses-header" style="text-align:left">
				<bean:message key="label.managementPosition.position" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.managementPosition.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.managementPosition.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.managementPosition.edit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.managementPosition.delete" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
		</tr>
		<logic:iterate id="creditLine" name="infoManagementPositions">
			<tr>
				<td class="listClasses" style="text-align:left"><bean:write name="creditLine" property="position"/></td>
				<td class="listClasses">
					<dt:format patternId="datePattern">
						<bean:write name="creditLine" property="start.time"/>
					</dt:format>
				</td>
				<td class="listClasses">
					<dt:format patternId="datePattern">
						<bean:write name="creditLine" property="end.time"/>
					</dt:format>
				</td>
				<td class="listClasses">
					<html:link page='<%= link + "&amp;page=0&amp;method=prepareEdit" %>' paramId="idInternal" paramName="creditLine" paramProperty="idInternal">
						<bean:message key="link.managementPosition.edit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</html:link>
				</td>
				<td class="listClasses">
					<html:link page='<%= link + "&amp;page=0&amp;method=delete" %>' paramId="idInternal" paramName="creditLine" paramProperty="idInternal">
						<bean:message key="link.managementPosition.delete" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
<logic:empty name="infoManagementPositions">
	<span class="error"><bean:message key="message.managementPositions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span>
</logic:empty>
<br />
<br />
<html:link page='<%= link + "&amp;page=0&amp;method=prepareEdit" %>' >
	<bean:message key="link.managementPosition.create" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>	
</html:link>

