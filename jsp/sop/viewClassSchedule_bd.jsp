<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected">
    		<p>O curso seleccionado &eacute;:</p>
    		<strong><jsp:include page="context.jsp"/></strong>
		</td>
	</tr>
</table>

<br />
<h2>Manipular Turma</h2>

<br />
<table cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td>
			<html:form action="/manageClass">
				<html:hidden property="method" value="prepareAddShifts"/>
				<html:hidden property="page" value="0"/>
			
				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
							 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
							 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
							 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CLASS_VIEW_OID %>"
							 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
			
				<html:submit styleClass="inputbutton">
					<bean:message key="label.shifts.add"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10"></td>
		<td>
			<html:form action="/manageClass">
				<html:hidden property="method" value="prepare"/>
				<html:hidden property="page" value="0"/>
			
				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
							 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
							 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
							 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CLASS_VIEW_OID %>"
							 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
			
				<html:submit styleClass="inputbutton">
					<bean:message key="label.view.shifts"/>
				</html:submit>			
			</html:form>
		</td>
		<td width="10"></td>
		<td>
			<html:form action="/manageClasses">
				<html:hidden property="method" value="listClasses"/>
				<html:hidden property="page" value="0"/>

				<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
						 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
						 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
				<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
						 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>

				<html:submit styleClass="inputbutton">
					<bean:message key="label.return"/>
				</html:submit>			
			</html:form>
		</td>
	</tr>
</table>

<br />
<html:form action="/manageClass" focus="className">

	<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
				 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
				 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
	<html:hidden property="<%= SessionConstants.CLASS_VIEW_OID %>"
				 value="<%= pageContext.findAttribute("classOID").toString() %>"/>

	<html:hidden property="method" value="edit"/>
	<html:hidden property="page" value= "1"/>

	<span class="error"><html:errors/></span>
   	<br />
   	<html:text property="className"/>
   	<br />
   	<html:submit styleClass="inputbuttonSmall">
   		<bean:message key="label.change"/>
   	</html:submit>
</html:form>

<br />
	<h2>Horário da Turma</h2>
	<div align="center">
		<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>"
						  type="<%= TimeTableType.SOP_CLASS_TIMETABLE %>"/>
	</div>
<br />