<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt"%>
<bean:define id="datePattern" value="dd-MM-yyyy"/>

<bean:define id="link">
	/CRUDServiceExemption.do?teacherId=<bean:write name="infoTeacher" property="idInternal"/>&amp;teacherNumber=<bean:write name="infoTeacher" property="teacherNumber"/>
</bean:define>

<tiles:insert definition="teacherContextTile">
	<tiles:put name="infoTeacher" beanName="infoTeacher" />
</tiles:insert>

<html:link page="/prepareManagementPositionsManagement.do?method=doSearch" paramId="teacherNumber" paramName="infoTeacher" paramProperty="teacherNumber">
	<bean:message key="link.managementPositions.management"/>
</html:link>
<br />
<logic:notEmpty name="infoServiceExemptions">
	<table width="100%" cellpadding="3">
		<tr>
			<td class="listClasses-header" style="text-align:left">
				<bean:message key="label.serviceExemption.type" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.serviceExemption.start" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.serviceExemption.end" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.serviceExemption.edit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
			<td class="listClasses-header" width="10%">
				<bean:message key="label.serviceExemption.delete" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</td>
		</tr>
		<logic:iterate id="creditLine" name="infoServiceExemptions">
			<tr>
				<td class="listClasses" style="text-align:left"><bean:message name="creditLine" property="type.name" bundle="ENUMERATION_RESOURCES"/></td>
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
						<bean:message key="link.serviceExemption.edit" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</html:link>
				</td>
				<td class="listClasses">
					<html:link page='<%= link + "&amp;page=0&amp;method=delete" %>' paramId="idInternal" paramName="creditLine" paramProperty="idInternal">
						<bean:message key="link.serviceExemption.delete" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
					</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>
<logic:empty name="infoServiceExemptions">
	<span class="error"><bean:message key="message.serviceExemptions.noRegists" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></span>
</logic:empty>
<br />
<br />
<html:link page='<%= link + "&amp;page=0&amp;method=prepareEdit" %>' >
	<bean:message key="link.serviceExemption.create" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>	
</html:link>

