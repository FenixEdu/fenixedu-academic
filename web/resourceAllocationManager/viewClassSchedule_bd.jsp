<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="link.manage.turmas"/></h2>

<p class="mbottom05">O curso seleccionado &eacute;:</p>
<strong><jsp:include page="context.jsp"/></strong>


<h3>Manipular Turma</h3>

<table class="mbottom1">
	<tr>
		<td>
			<html:form action="/manageClass">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareAddShifts"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
			
				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
							 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
							 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
							 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CLASS_VIEW_OID %>" property="<%= SessionConstants.CLASS_VIEW_OID %>"
							 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
			
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.shifts.add"/>
				</html:submit>			
			</html:form>
		</td>
		<td></td>
		<td>
			<html:form action="/manageClass">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepare"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
			
				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
							 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
							 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
							 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CLASS_VIEW_OID %>" property="<%= SessionConstants.CLASS_VIEW_OID %>"
							 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
			
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.view.shifts"/>
				</html:submit>			
			</html:form>
		</td>
		<td></td>
		<td>
			<html:form action="/manageClasses">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="listClasses"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>

				<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="label.return"/>
				</html:submit>			
			</html:form>
		</td>
	</tr>
</table>


<html:form action="/manageClass" focus="className">

	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_DEGREE_OID %>" property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CURRICULAR_YEAR_OID %>" property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden alt="<%= SessionConstants.CLASS_VIEW_OID %>" property="<%= SessionConstants.CLASS_VIEW_OID %>"
				 value="<%= pageContext.findAttribute("classOID").toString() %>"/>

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value= "1"/>

	<p>
		<span class="warning"><!-- Error messages go here --><html:errors /></span>
	</p>

   	<html:text bundle="HTMLALT_RESOURCES" altKey="text.className" property="className"/>

   	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbuttonSmall">
   		<bean:message key="label.change"/>
   	</html:submit>
</html:form>


	<h3 class="mtop15">Horário da Turma</h3>
	<div align="center">
		<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>"
						  type="<%= TimeTableType.SOP_CLASS_TIMETABLE %>"/>
	</div>
