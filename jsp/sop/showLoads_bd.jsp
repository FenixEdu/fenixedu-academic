<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="infoExecutionCourse" scope="request">
	<bean:define id="labHours" name="infoExecutionCourse" property="labHours"/>
	<bean:define id="theoreticalHours" name="infoExecutionCourse" property="theoreticalHours"/>
	<bean:define id="theoPratHours" name="infoExecutionCourse" property="theoPratHours" type="java.lang.Double" />
	<bean:define id="praticalHours" name="infoExecutionCourse" property="praticalHours" type="java.lang.Double" />

	<h2><bean:message key="label.executionCourse" /></h2>
	<table width="100%" cellpadding="1" cellspacing="1">
		<tr>
			<td class="listClasses-header" width="30%" rowspan="2">
				<bean:message key="label.name"/>
			</td>
			<td class="listClasses-header" width="10%" rowspan="2"> 
				<bean:message key="label.code"/>
			</td>
			<td class="listClasses-header" colspan="4" width="60%">
				Carga
			</td>
		</tr>
		<tr>
			<td class="listClasses-header">
				<bean:message key="label.hours.load.theoretical"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.hours.load.theoretical_practical"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.hours.load.practical"/>
			</td>
			<td class="listClasses-header">
				<bean:message key="label.hours.load.laboratorial"/>
			</td>			
		</tr>	

		<tr>
			<td class="listClasses">
				<bean:write name="infoExecutionCourse" property="nome"/>
			</td>
			<td class="listClasses">
				<bean:write name="infoExecutionCourse" property="sigla"/>
			</td>
			<td class="listClasses">
				<bean:write name="infoExecutionCourse" property="theoreticalHours"/>
			</td>
			<td class="listClasses">
				<bean:write name="infoExecutionCourse" property="theoPratHours"/>
			</td>
			<td class="listClasses">
				<bean:write name="infoExecutionCourse" property="praticalHours"/>
			</td>
			<td class="listClasses">
				<bean:write name="infoExecutionCourse" property="labHours"/>
			</td>
		</tr>

	</table>


	<logic:present name="curricularCourses" >

		<h2><bean:message key="label.curricularCourses" /></h2>
			<br />
			<logic:iterate id="curricularCourse" name="curricularCourses" >
				<h3>
					<bean:message key="label.curricularCourse"/>: <bean:write name="curricularCourse" property="name" /> 
					<br/>
					Curso :	<bean:write name="curricularCourse" property="infoDegreeCurricularPlan.infoDegree.nome" />
				</h3>
								
				<table width="70%" cellpadding="0" border="0">
					<tr>
						<td class="listClasses-header"><bean:message key="message.manager.theoreticalHours" />
						</td>
						<td class="listClasses-header"><bean:message key="message.manager.praticalHours" />
						</td>
						<td class="listClasses-header"><bean:message key="message.manager.theoPratHours" />
						</td>
						<td class="listClasses-header"><bean:message key="message.manager.labHours" />
						</td>
					</tr>
					<tr>
						<td class="listClasses">
							<logic:equal  name="curricularCourse" property="theoreticalHours"  value="<%= theoreticalHours.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="theoreticalHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="theoreticalHours" value="<%= theoreticalHours.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="theoreticalHours"/></font>
							</logic:notEqual>
						</td>
						<td class="listClasses">
							<logic:equal name="curricularCourse" property="theoPratHours" value="<%= theoPratHours.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="theoPratHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="theoPratHours" value="<%= theoPratHours.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="theoPratHours"/></font>
							</logic:notEqual>							
						</td>
						<td class="listClasses">
							<logic:equal name="curricularCourse" property="praticalHours" value="<%= praticalHours.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="praticalHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="praticalHours" value="<%= praticalHours.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="praticalHours"/></font>
							</logic:notEqual>								
						</td>
						<td class="listClasses">
							<logic:equal name="curricularCourse" property="labHours" value="<%= labHours.toString() %>" >
								<font color="#008000"><bean:write name="curricularCourse" property="labHours"/></font>
							</logic:equal>
							<logic:notEqual name="curricularCourse" property="labHours" value="<%= labHours.toString() %>">
								<font color="red"><bean:write name="curricularCourse" property="labHours"/></font>
							</logic:notEqual>								
						</td>
					</tr>
				</table>
				<br />
				<br />
				<table width="50%" cellpadding="0" border="0">				
					<tr>				
						<td class="listClasses-header">
							<bean:message key="label.manager.curricularCourseScope.branch"/>
						</td>
						<td class="listClasses-header">
							<bean:message key="label.manager.curricularCourseScope.curricularYear"/>
						</td>
						<td class="listClasses-header">
							Sem.
							<%--<bean:message key="label.manager.curricularCourseScope.curricularSemester"/> --%>
						</td>	
						<td>
							&nbsp;
						</td>				
					</tr>		
					<%-- Scopes --%>
					<logic:iterate id="scope" name="curricularCourse" property="infoScopes" >
						<tr>
							<td class="listClasses">
								<logic:equal name="scope" property="infoBranch.name" value="" >
									<bean:message key="label.commonBranch" />
								</logic:equal>
								<logic:notEqual name="scope" property="infoBranch.name" value="" >
									<bean:write name="scope" property="infoBranch.name"/>
								</logic:notEqual>
							</td>
							<td class="listClasses">
								<bean:write name="scope" property="infoCurricularSemester.infoCurricularYear.year"/>
							</td>
							<td class="listClasses">
								<bean:write name="scope" property="infoCurricularSemester.semester"/>
							</td>								
							<td>
								&nbsp;
							</td>	
						</tr>
					</logic:iterate>
				</table>
				<br />
				<br />
			</logic:iterate>				
	</logic:present>
</logic:present>

