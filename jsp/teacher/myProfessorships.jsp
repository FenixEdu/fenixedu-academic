<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<tiles:importAttribute />
<logic:notEmpty name="professorshipList" >	
	<bean:define id="titleKey">
		<tiles:getAsString name="title"/>
	</bean:define>
	<h2><bean:message key="<%= titleKey %>" /></h2>
	
<table border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td nowrap="nowrap">
			<bean:message key="property.executionPeriod"/>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
    	</td>
    
    	
			<html:form action="/manageExecutionCourses" >
			<td nowrap="nowrap">
				<html:hidden property="method" value="perform"/>
				<html:hidden property="page" value="1"/>
				<html:select property="selectedExecutionPeriodId" size="1" onchange="document.executionPeriodForm.submit();">
				<html:options	property="value" 
     						labelProperty="label" 
							collection="<%= SessionConstants.LABELLIST_EXECUTIONPERIOD %>" />
				</html:select>
				</td>
			</html:form>
		
	</tr>
</table>
	<br />
	<br />
	
		<table width="90%"cellpadding="5" border="0">
			<tr>
				<td class="listClasses-header"><bean:message key="label.professorships.acronym" />
				</td>
				<td class="listClasses-header" style="text-align:left"><bean:message key="label.professorships.name" />
				</td>
				<td class="listClasses-header" style="text-align:left"><bean:message key="label.professorships.degrees" />
				</td>
				
				<td class="listClasses-header"><bean:message key="label.semester" />
				</td>
			</tr>
			<bean:define id="link">
				<tiles:getAsString name="link"/>
			</bean:define>
			<logic:iterate id="detailedProfessorship" name="professorshipList" >
				<bean:define id="professorship" name="detailedProfessorship" property="infoProfessorship"/>
				<bean:define id="infoExecutionCourse" name="professorship" property="infoExecutionCourse"/>
				<tr>
					<td class="listClasses">
						<html:link page="<%= link %>" paramId="objectCode" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="sigla"/>
						</html:link>
					</td>			
					<td class="listClasses" style="text-align:left">
						<html:link page="<%= link %>" paramId="objectCode" paramName="infoExecutionCourse" paramProperty="idInternal">
							<bean:write name="infoExecutionCourse" property="nome"/>
						</html:link>
					</td>
					<td class="listClasses" style="text-align:left">
						<bean:define id="infoDegreeList" name="detailedProfessorship" property="infoDegreeList" />
						<bean:size id="degreeSizeList" name="infoDegreeList"/>
						<logic:iterate id="infoDegree" name="infoDegreeList" indexId="index">
							<bean:write name="infoDegree" property="sigla" /> 
							<logic:notEqual name="degreeSizeList" value="<%= String.valueOf(index.intValue() + 1) %>">
							,
							</logic:notEqual>
						</logic:iterate>
					</td>						
					<td class="listClasses">
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>&nbsp;-&nbsp;
						<bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/>
					</td>
					
				</tr>
			</logic:iterate>
	 	</table>
</logic:notEmpty>