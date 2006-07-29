<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><!-- Error messages go here --><html:errors /></span> 

<logic:present name="siteView">

		<html:form action="/objectivesManagerDA">

		<logic:present name="siteView" property="component">
			<bean:define id="curriculum" name="siteView" property="component"/>
			<h3><bean:write name="curriculum" property="infoCurricularCourse.name"/> -- <bean:write name="curriculum" property="infoCurricularCourse.infoDegreeCurricularPlan.infoDegree.nome"/></h3>
			<bean:define id="curricularCourseCode" name="curriculum" property="infoCurricularCourse.idInternal"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseCode" property="curricularCourseCode" value="<%= curricularCourseCode.toString() %>"/>
		</logic:present>
	
		<logic:notPresent name="siteView" property="component">
			<bean:define id="curricularCourseCode" name="curricularCourseCode"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseCode" property="curricularCourseCode" value="<%= curricularCourseCode.toString() %>"/>
		</logic:notPresent> 
		
		<h2><bean:message key="title.objectives"/></h2>	

		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<table>	
			<tr>
				<td><strong><bean:message key="label.generalObjectives"/></strong></td>
			</tr>
			<tr>
				<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectives"  property="generalObjectives" cols="50" rows="8"/></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.generalObjectives.eng"/></strong></td>
			</tr>	
			<tr>
				<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectivesEn"  property="generalObjectivesEn" cols="50" rows="8"/></td>
			</tr>
		</table>   
		</br>
		<table>	
			<tr>
				<td><strong><bean:message key="label.operacionalObjectives"/></strong></td>
			</tr>
			<tr>
				<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectives"  property="operacionalObjectives" cols="50" rows="8"/></td>
			</tr>
			<tr>
				<td><strong><bean:message key="label.operacionalObjectives.eng"/></strong></td>
			</tr>	
			<tr>
				<td><html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectivesEn"  property="operacionalObjectivesEn" cols="50" rows="8"/></td>
			</tr>
			<tr>
				<td>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
						<bean:message key="button.save"/>
					</html:submit>
					<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
						<bean:message key="label.clear"/>
					</html:reset>
				</td>
			</tr>
		</table>   
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editObjectives" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		
		</html:form>
</logic:present>