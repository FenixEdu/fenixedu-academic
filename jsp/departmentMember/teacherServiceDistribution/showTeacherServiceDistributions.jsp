<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.domain.teacherServiceDistribution.TeacherServiceDistribution" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<h3>
	<html:link page='/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution'>
		<bean:message key="link.teacherServiceDistribution"/>
	</html:link>
	>
	<bean:message key="link.teacherServiceDistribution.teacherServiceDistributionEdition"/>
</h3>

<html:form action="/teacherServiceDistribution">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareForTeacherServiceDistributionEdition"/>

<table class='vtsbc'>
<tr>
	<th colspan="2">
		<b><bean:write name="departmentName"/></b>
	</th>
</tr>
<tr>
	<td align="left">
		<b><bean:message key="label.teacherServiceDistribution.executionYear"/>:</b>
		&nbsp;&nbsp;
		<html:select property="executionYear" onchange="this.form.submit();">
			<html:option value="-1">&nbsp;</html:option>
			<html:options collection="executionYearList" property="idInternal" labelProperty="year"/>
		</html:select>
	</td>
	<td>
		<b><bean:message key="label.teacherServiceDistribution.semester"/>:</b>
		&nbsp;&nbsp;
		<html:select property="executionPeriod" onchange="this.form.submit();">
			<html:option value="-1">&nbsp;</html:option>
			<html:options collection="executionPeriodsList" property="idInternal" labelProperty="semester"/>
		</html:select>
	</td>
</tr>
</table>

<br/>
<br/>
<logic:empty name="teacherServiceDistributionList">
<span class="error">
	<bean:message key="label.teacherServiceDistribution.noTeacherServiceDistributionsForExecutionPeriod"/>
</span>
</logic:empty>
<logic:notEmpty name="teacherServiceDistributionList">
	<b><bean:message key="label.teacherServiceDistribution.availableTeacherServiceDistributions"/>:</b>
	<br/>

	<table class='vtsbc'>
		<tr>
			<th>
				<bean:message key="label.teacherServiceDistribution.name"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.executionYear"/>
			</th>
			<th>
				<bean:message key="label.teacherServiceDistribution.semesters"/>
			</th>
		</tr>
	<logic:iterate name="teacherServiceDistributionList" id="teacherServiceDistribution">	
		<tr>
		 	<td class="courses">
		 		<bean:define id="teacherServiceDistributionId" name="teacherServiceDistribution" property="idInternal"/>
				<html:link page='<%= "/teacherServiceDistribution.do?method=showTeacherServiceDistributionServices&amp;teacherServiceDistribution=" + teacherServiceDistributionId %>'>
					<bean:write name="teacherServiceDistribution" property="name"/>
				</html:link>
			</td> 
			<td align="center">
				<bean:write name="teacherServiceDistribution" property="executionYear.year" />
			</td>
			<td align="center">
				<logic:iterate id="executionPeriod" name="teacherServiceDistribution" property="orderedExecutionPeriods">
					<bean:write name="executionPeriod" property="semester"/>�&nbsp;
				</logic:iterate>
			</td>
		</tr>
	</logic:iterate> 
	</table>	
</logic:notEmpty> 
</html:form>

	
<br/>
<br/>
<html:link page="/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution">
	<bean:message key="link.back"/>
</html:link>	

