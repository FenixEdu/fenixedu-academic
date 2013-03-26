<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>	

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="curricularCourse" name="component" property="infoCurricularCourse"/>

<table align="center" cellspacing="1" cellpadding="5" >
			<tr>
				<th class="listClasses-header">
					<bean:message key="property.curricularCourse.name"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.degree.initials"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.curricularCourse.branch"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.curricularCourse.curricularYear"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="property.curricularCourse.semester"/>
				</th>
			</tr>
			
				<logic:iterate id="infoCurricularCourseScope" name="curricularCourse" property="infoScopes">
					<%-- FIXME: hardcoded semester 2 
					<logic:equal name="infoCurricularCourseScope" property="infoCurricularSemester.semester" value="2">
						--%>
						<tr>
							<td class="listClasses">
								<bean:write name="curricularCourse" property="name"/>
							</td>
							<td class="listClasses">
								<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.sigla"/>
							</td>
							<td class="listClasses">
								<bean:write name="infoCurricularCourseScope" property="infoBranch.name"/>&nbsp;
							</td>
							<td class="listClasses">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.infoCurricularYear.year"/>&nbsp;
							</td>
							<td class="listClasses">
								<bean:write name="infoCurricularCourseScope" property="infoCurricularSemester.semester"/>&nbsp;
							</td>
						</tr>
						
					<%--</logic:equal>--%>
					
				</logic:iterate>	
		</table>
<br/>
<br/>
<logic:present name="component" property="infoCurriculum">
<bean:define id="curriculum" name="component" property="infoCurriculum"/>

	<logic:notEmpty name="curriculum" property="generalObjectives">
		<logic:notEqual name="curriculum" property="generalObjectives" value="">
		<h2><bean:message key="label.generalObjectives" />	</h2>
		<p>
			<bean:write name="curriculum" property="generalObjectives" filter="false"/>
		</p>
		</logic:notEqual>
	</logic:notEmpty>
	 <logic:notEmpty name="curriculum" property="operacionalObjectives">
		<logic:notEqual name="curriculum" property="operacionalObjectives" value="">
		<h2><bean:message key="label.operacionalObjectives" /></h2>
		<p>
			<bean:write name="curriculum" property="operacionalObjectives" filter="false"/>
		</p>
		</logic:notEqual>
	</logic:notEmpty> 
<br/>
<br/>
	<logic:notEmpty name="curriculum" property="program">
		<h2><bean:message key="label.program" /></h2>	
		<p>
			<bean:write name="curriculum" property="program" filter="false" />
		</p>	
	</logic:notEmpty>
<br/>
<br/>
	
<%--	<logic:notEmpty name="curriculum" property="evaluationElements">
		<h2><bean:message key="label.evaluation" /></h2>	
		<p>
			<bean:write name="curriculum" property="evaluationElements" filter="false" />
		</p>	
	</logic:notEmpty> --%>
	
</logic:present >

