<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
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
<h2>Adicionar Turnos</h2>


<br />
<logic:present name="<%= SessionConstants.SHIFTS %>" scope="request">
	<html:form action="/addShifts">
		<html:hidden property="method" value="add"/>
		<html:hidden property="page" value="1"/>

		<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
					 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
					 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
					 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
		<html:hidden property="<%= SessionConstants.CLASS_VIEW_OID %>"
					 value="<%= pageContext.findAttribute("classOID").toString() %>"/>
		<table>
			<tr>
				<td class="listClasses-header">
		        </td>
				<td class="listClasses-header">
					<bean:message key="property.executionCourse"/>
				</td>
				<td class="listClasses-header">
					<bean:message key="property.shift.name"/>
				</td>
		        <td class="listClasses-header">
		        	<bean:message key="property.shift.type"/>
		        </td>
				<td class="listClasses-header">
					<bean:message key="property.lessons"/>
				</td>
				<td class="listClasses-header">
		        	<bean:message key="property.shift.capacity"/>
		        </td>
			</tr>
			<logic:iterate id="infoShift" name="<%= SessionConstants.SHIFTS %>">
				<bean:define id="infoShiftOID" name="infoShift" property="idInternal"/>
				<bean:define id="infoExecutionCourseOID" name="infoShift" property="infoDisciplinaExecucao.idInternal"/>
				<tr align="center">
	              	<td class="listClasses">
						<html:multibox property="selectedItems">
							<bean:write name="infoShift" property="idInternal"/>
						</html:multibox>
					</td>
					<td class="listClasses">
						<bean:write name="infoShift" property="infoDisciplinaExecucao.sigla"/>
					</td>
					<td class="listClasses">
	               		<html:link page="<%= "/manageShift.do?method=prepareEditShift&amp;page=0&amp;"
	               							+ SessionConstants.SHIFT_OID
				  							+ "="
	               				   			+ pageContext.findAttribute("infoShiftOID")
	               				   			+ "&amp;"
				  							+ SessionConstants.EXECUTION_COURSE_OID
	  										+ "="
	  										+ pageContext.findAttribute("infoExecutionCourseOID")
	               				   			+ "&amp;"
				  							+ SessionConstants.EXECUTION_PERIOD_OID
	  										+ "="
	  										+ pageContext.findAttribute("executionPeriodOID")
	  										+ "&amp;"
	  										+ SessionConstants.CURRICULAR_YEAR_OID
				  							+ "="
	  										+ pageContext.findAttribute("curricularYearOID")
	  										+ "&amp;"
				  							+ SessionConstants.EXECUTION_DEGREE_OID
	  										+ "="
	  										+ pageContext.findAttribute("executionDegreeOID") %>">
							<bean:write name="infoShift" property="nome"/>
						</html:link>
					</td>
	              	<td class="listClasses">
	              		<bean:write name="infoShift" property="tipo"/>
	              	</td>
	              	<td class="listClasses">
						<logic:iterate id="infoLesson" name="infoShift" property="infoLessons">
							<bean:write name="infoLesson" property="diaSemana"/> 
							<dt:format pattern="HH:mm">
								<bean:write name="infoLesson" property="inicio.timeInMillis"/>
							</dt:format> -
							<dt:format pattern="HH:mm">
								<bean:write name="infoLesson" property="fim.timeInMillis"/>
							</dt:format>
							<bean:write name="infoLesson" property="infoSala.nome"/>;
			        </logic:iterate>      		
    	          	</td> 
	              	<td class="listClasses">
	              		<bean:write name="infoShift" property="lotacao"/>
	              	</td>
				</tr>
			</logic:iterate>
		</table>

		<html:submit styleClass="inputbutton">
			<bean:message key="label.add"/>
		</html:submit>			
	</html:form>
</logic:present>

<logic:notPresent name="<%= SessionConstants.SHIFTS %>" scope="request">
	<span class="error"><bean:message key="errors.shifts.none"/></span>	
</logic:notPresent>