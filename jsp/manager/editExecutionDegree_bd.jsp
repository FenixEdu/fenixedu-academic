<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.executionDegree" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/editExecutionDegree" method ="get">
	    
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%= request.getParameter("executionDegreeId") %>"/>
	
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.executionYear"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearId" property="executionYearId">
					<html:options collection="infoExecutionYearsList" property="idInternal" labelProperty="year"/>
				</html:select>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.campus"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.campusId" property="campusId">
					<html:options collection="infoCampusList" property="idInternal" labelProperty="name"/>
				</html:select>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionDegree.temporaryExamMap"/>
			</td>
			<td>
				<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.tempExamMap" property="tempExamMap" value="true"/>
			</td>
		</tr>
	</table>
	
	<br>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>
<br/>
<html:form action="/editExecutionDegree">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editPeriods"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%= request.getParameter("executionDegreeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.periodToEdit" property="periodToEdit"/>
	
<table>
		<tr>
			<td colspan=4>
				Periodo Aulas 1� Semestre:
			</td>
		</tr>

		<logic:iterate indexId="i" id="xpto" name="executionDegreeForm" property="lessonsFirstStartDay">		
			<tr>
				<td>
				    &nbsp;&nbsp;
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsFirstStartDay" maxlength="2" size="2" property="lessonsFirstStartDay" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsFirstStartDay"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsFirstStartMonth" maxlength="2" size="2" property="lessonsFirstStartMonth" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsFirstStartMonth"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsFirstStartYear" maxlength="4" size="4" property="lessonsFirstStartYear" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsFirstStartYear"))[i.intValue()].toString()%>"/>			
				</td>
				<td>
				    &nbsp;a&nbsp;
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsFirstEndDay" maxlength="2" size="2" property="lessonsFirstEndDay" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsFirstEndDay"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsFirstEndMonth" maxlength="2" size="2" property="lessonsFirstEndMonth" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsFirstEndMonth"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsFirstEndYear" maxlength="4" size="4" property="lessonsFirstEndYear" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsFirstEndYear"))[i.intValue()].toString()%>"/>			
				</td>
			</tr>
		</logic:iterate>
		<tr>
			<td colspan=4>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='addLine';this.form.page.value=1;this.form.periodToEdit.value='lessonsFirst';">
					<bean:message bundle="MANAGER_RESOURCES" key="button.add"/>
				</html:submit>
				<bean:define id="size">
					<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsFirstStartDay")).length %>
				</bean:define>
				<logic:notEqual name="size" value="1">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='removeLine';this.form.page.value=1;this.form.periodToEdit.value='lessonsFirst';">
						<bean:message bundle="MANAGER_RESOURCES" key="button.remove"/>
					</html:submit>
				</logic:notEqual>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td colspan=4>
				Periodo Exames 1� Semestre:
			</td>
		</tr>	
		<logic:iterate indexId="i" id="xpto" name="executionDegreeForm" property="examsFirstStartDay">		
			<tr>
				<td>
				    &nbsp;&nbsp;
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsFirstStartDay" maxlength="2" size="2" property="examsFirstStartDay" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsFirstStartDay"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsFirstStartMonth" maxlength="2" size="2" property="examsFirstStartMonth" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsFirstStartMonth"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsFirstStartYear" maxlength="4" size="4" property="examsFirstStartYear" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsFirstStartYear"))[i.intValue()].toString()%>"/>			
				</td>
				<td>
				    &nbsp;a&nbsp;
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsFirstEndDay" maxlength="2" size="2" property="examsFirstEndDay" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsFirstEndDay"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsFirstEndMonth" maxlength="2" size="2" property="examsFirstEndMonth" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsFirstEndMonth"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsFirstEndYear" maxlength="4" size="4" property="examsFirstEndYear" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsFirstEndYear"))[i.intValue()].toString()%>"/>			
				</td>
			</tr>
		</logic:iterate>
		<tr>
			<td colspan=4>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='addLine';this.form.periodToEdit.value='examsFirst';this.form.page.value=1;">
					<bean:message bundle="MANAGER_RESOURCES" key="button.add"/>
				</html:submit>
				<bean:define id="size">
					<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsFirstStartDay")).length %>
				</bean:define>
				<logic:notEqual name="size" value="1">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='removeLine';this.form.page.value=1;this.form.periodToEdit.value='examsFirst';">
						<bean:message bundle="MANAGER_RESOURCES" key="button.remove"/>
					</html:submit>
				</logic:notEqual>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td colspan=4>
				Periodo Aulas 2� Semestre:
			</td>
		</tr>

		<logic:iterate indexId="i" id="xpto" name="executionDegreeForm" property="lessonsSecondStartDay">		
			<tr>
				<td>
				    &nbsp;&nbsp;
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsSecondStartDay" maxlength="2" size="2" property="lessonsSecondStartDay" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsSecondStartDay"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsSecondStartMonth" maxlength="2" size="2" property="lessonsSecondStartMonth" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsSecondStartMonth"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsSecondStartYear" maxlength="4" size="4" property="lessonsSecondStartYear" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsSecondStartYear"))[i.intValue()].toString()%>"/>			
				</td>
				<td>
				    &nbsp;a&nbsp;
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsSecondEndDay" maxlength="2" size="2" property="lessonsSecondEndDay" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsSecondEndDay"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsSecondEndMonth" maxlength="2" size="2" property="lessonsSecondEndMonth" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsSecondEndMonth"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.lessonsSecondEndYear" maxlength="4" size="4" property="lessonsSecondEndYear" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsSecondEndYear"))[i.intValue()].toString()%>"/>			
				</td>
			</tr>
		</logic:iterate>
		<tr>
			<td colspan=4>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='addLine';this.form.page.value=1;this.form.periodToEdit.value='lessonsSecond';">
					<bean:message bundle="MANAGER_RESOURCES" key="button.add"/>
				</html:submit>
				<bean:define id="size">
					<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("lessonsSecondStartDay")).length %>
				</bean:define>
				<logic:notEqual name="size" value="1">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='removeLine';this.form.page.value=1;this.form.periodToEdit.value='lessonsSecond';">
						<bean:message bundle="MANAGER_RESOURCES" key="button.remove"/>
					</html:submit>
				</logic:notEqual>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
		<tr>
			<td colspan=4>
				Periodo Exames 2� Semestre:
			</td>
		</tr>	
		<logic:iterate indexId="i" id="xpto" name="executionDegreeForm" property="examsSecondStartDay">		
			<tr>
				<td>
				    &nbsp;&nbsp;
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsSecondStartDay" maxlength="2" size="2" property="examsSecondStartDay" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsSecondStartDay"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsSecondStartMonth" maxlength="2" size="2" property="examsSecondStartMonth" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsSecondStartMonth"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsSecondStartYear" maxlength="4" size="4" property="examsSecondStartYear" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsSecondStartYear"))[i.intValue()].toString()%>"/>			
				</td>
				<td>
				    &nbsp;a&nbsp;
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsSecondEndDay" maxlength="2" size="2" property="examsSecondEndDay" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsSecondEndDay"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsSecondEndMonth" maxlength="2" size="2" property="examsSecondEndMonth" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsSecondEndMonth"))[i.intValue()].toString()%>"/>			
					/
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.examsSecondEndYear" maxlength="4" size="4" property="examsSecondEndYear" 
						value="<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsSecondEndYear"))[i.intValue()].toString()%>"/>			
				</td>
			</tr>
		</logic:iterate>
		<tr>
			<td colspan=4>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='addLine';this.form.page.value=1;this.form.periodToEdit.value='examsSecond';">
					<bean:message bundle="MANAGER_RESOURCES" key="button.add"/>
				</html:submit>
				<bean:define id="size">
					<%= ((String[])((org.apache.struts.validator.DynaValidatorForm) pageContext.findAttribute("executionDegreeForm")).get("examsSecondStartDay")).length %>
				</bean:define>
				<logic:notEqual name="size" value="1">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='removeLine';this.form.page.value=1;this.form.periodToEdit.value='examsSecond';">
						<bean:message bundle="MANAGER_RESOURCES" key="button.remove"/>
					</html:submit>
				</logic:notEqual>
			</td>
		</tr>
		<tr><td>&nbsp;</td></tr>
</table>
<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='editPeriods';this.form.page.value=2;">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>	
</html:form>
