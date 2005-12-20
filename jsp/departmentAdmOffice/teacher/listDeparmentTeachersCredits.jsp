<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3>
	<logic:iterate id="department" name="departmentsList" indexId="nrIter">
		<logic:equal name="nrIter" value="0">
			<bean:write name="department" property="name"/>
		</logic:equal>
		<logic:notEqual name="nrIter" value="0">
			, <bean:write name="department" property="name"/>
		</logic:notEqual>
	</logic:iterate>
</h3>
<br />

<tiles:insert definition="creditsLegend"/>

<bean:define id="executionPeriodId" name="executionPeriodId"/>
<bean:define id="teachersCreditsListSize" name="teachersCreditsListSize"/>
<u><bean:message key="label.departmentTeachersList.teachersFound" arg0="<%= teachersCreditsListSize.toString() %>"/></u>
<br />
<table width="100%">
	<tr>
		<td class="listClasses-header" width="10%">
			<html:link page="/prepareListDepartmentTeachersCredits.do?sortBy=number" paramId="executionPeriodId" paramName="executionPeriodId">
				<bean:message key="label.departmentTeachersList.teacherNumber" />
			</html:link>
		</td>
		<td class="listClasses-header" style="text-align:left">
			<html:link page="/prepareListDepartmentTeachersCredits.do?sortBy=name" paramId="executionPeriodId" paramName="executionPeriodId">
				<bean:message key="label.departmentTeachersList.teacherName" />
			</html:link>
		</td>
		<td class="listClasses-header" width="10%">			
			<bean:message key="label.departmentTeachersList.teacherCategory" />
		</td>
		<td class="listClasses-header">
			<bean:message key="label.credits.resume" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</td>
		<td class="listClasses-header" width="10%">
			<bean:message key="label.departmentTeachersList.teacherCreditsSheet.details" />
		</td>
		
	</tr>
	<logic:iterate id="teachersCredits" name="teachersCreditsList">
		<tr>	
			<td class="listClasses">
				<bean:write name="teachersCredits" property="teacher.teacherNumber"/>
			</td>
			<td class="listClasses" style="text-align:left">
				<bean:write name="teachersCredits" property="teacher.person.nome"/>			
			</td>
			<td class="listClasses">
				<logic:present name="teachersCredits" property="category">
					<label title='<bean:write name="teachersCredits" property="category.longName" />'>
						<bean:write name="teachersCredits" property="category.code"/>
					</label>						
				</logic:present>
				<logic:notPresent name="teachersCredits" property="category">
					--
				</logic:notPresent>
			</td>
			<td class="listClasses">
				<font size="-5">
					<tiles:insert definition="creditsResumeLine" flush="false">
						<tiles:put name="creditLineDTO" beanName="teachersCredits" beanProperty="creditLineDTO"/>
					</tiles:insert>
				</font>
			</td>
			<td class="listClasses">
				<html:link page='<%= "/showTeacherCredits.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teachersCredits" paramProperty="teacher.idInternal">
					<bean:message key="link.view"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>