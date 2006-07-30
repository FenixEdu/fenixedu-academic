<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected">
    		<p>O curso seleccionado &eacute;:</p>
    		<strong><jsp:include page="context.jsp"/></strong>
		</td>
	</tr>
</table>

<br />
<h2>Adicionar Turmas</h2>

<br />
<logic:present name="<%= SessionConstants.CLASSES %>" scope="request">
	<html:form action="/addClasses" focus="selectedItems">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="add"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

		<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.EXECUTION_COURSE_OID %>" property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
					 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
		<html:hidden alt="<%= SessionConstants.SHIFT_OID %>" property="<%= SessionConstants.SHIFT_OID %>"
					 value="<%= pageContext.findAttribute("shiftOID").toString() %>"/>

		<table>
			<tr>
				<th class="listClasses-header">
				</th>
				<th class="listClasses-header">
					<bean:message key="label.name"/>
				</th>
				<th class="listClasses-header">
					<bean:message key="label.degree"/>
				</th>
			</tr>
			<logic:iterate id="infoClass" name="<%= SessionConstants.CLASSES %>">
				<bean:define id="infoClassOID" name="infoClass" property="idInternal"/>
				<bean:define id="infoExecutionDegreeOID" name="infoClass" property="infoExecutionDegree.idInternal"/>
				<tr align="center">
	              	<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selectedItems" property="selectedItems">
							<bean:write name="infoClass" property="idInternal"/>
						</html:multibox>
					</td>
					<td class="listClasses">
						<bean:write name="infoClass" property="nome"/>
					</td>
					<td class="listClasses">
						<bean:write name="infoClass" property="infoExecutionDegree.infoDegreeCurricularPlan.infoDegree.sigla"/>
					</td>
				</tr>
			</logic:iterate>
		</table>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="label.add"/>
		</html:submit>			
	</html:form>
</logic:present>

<logic:notPresent name="<%= SessionConstants.CLASSES %>" scope="request">
	<span class="error"><!-- Error messages go here --><bean:message key="errors.classes.none"/></span>	
</logic:notPresent>