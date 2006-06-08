<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="label.departmentTeachersList.title"/></h2>
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

<html:form action="/prepareListDepartmentTeachersCredits">
	<logic:notEmpty name="executionPeriods">
		<p><html:select property="executionPeriodId" onchange="this.form.submit()">
			<html:option key="choose.execution.period" value=""/>
			<html:options collection="executionPeriods" property="value" labelProperty="label"/>
		</html:select></p>		
	</logic:notEmpty>
</html:form>

<br />

<tiles:insert definition="creditsLegend"/>

<bean:define id="executionPeriodId" name="executionPeriodForm" property="executionPeriodId"/>
<bean:define id="teachersCreditsListSize" name="teachersCreditsListSize"/>
<u><bean:message key="label.departmentTeachersList.teachersFound" arg0="<%= teachersCreditsListSize.toString() %>"/></u>
<br />
<table class="tstyle1c">
	<tr>
		<th>
			<html:link page="/prepareListDepartmentTeachersCredits.do?sortBy=number" paramProperty="executionPeriodId">
				<bean:message key="label.departmentTeachersList.teacherNumber" />
			</html:link>
		</th>
		<th class="aleft">
			<html:link page="/prepareListDepartmentTeachersCredits.do?sortBy=name" paramProperty="executionPeriodId">
				<bean:message key="label.departmentTeachersList.teacherName" />
			</html:link>
		</th>
		<th>			
			<bean:message key="label.departmentTeachersList.teacherCategory" />
		</th>
		<th>
			<bean:message key="label.credits.resume" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		</th>
		<th>
			<bean:message key="label.departmentTeachersList.teacherCreditsSheet.details" />
		</th>
		
	</tr>
	<logic:iterate id="teachersCredits" name="teachersCreditsList">
		<tr>	
			<td>
				<bean:write name="teachersCredits" property="teacher.teacherNumber"/>
			</td>
			<td style="text-align:left">
				<bean:write name="teachersCredits" property="teacher.person.nome"/>			
			</td>
			<td>
				<logic:present name="teachersCredits" property="category">
					<label title='<bean:write name="teachersCredits" property="category.longName" />'>
						<bean:write name="teachersCredits" property="category.code"/>
					</label>						
				</logic:present>
				<logic:notPresent name="teachersCredits" property="category">
					--
				</logic:notPresent>
			</td>
			<td>
				<tiles:insert definition="creditsResumeLine" flush="false">
					<tiles:put name="creditLineDTO" beanName="teachersCredits" beanProperty="creditLineDTO"/>
				</tiles:insert>
			</td>
			<td>
				<html:link page='<%= "/showFullTeacherCreditsSheet.do?method=showTeacherCredits&page=1&amp;executionPeriodId=" + executionPeriodId %>' paramId="teacherId" paramName="teachersCredits" paramProperty="teacher.idInternal">
					<bean:message key="link.view"/>
				</html:link>
			</td>
		</tr>
	</logic:iterate>
</table>