<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="java.util.List"%>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
     	</td>
    </tr>
</table>
<br />
<h2><bean:message key="title.manage.turnos"/></h2>
<br />
<span class="error"><html:errors/></span>
    <html:form action="/manipularTurnosForm">
        <%-- Cria a tabela das turnos --%>
        <b><bean:message key="listTurnos.existing"/></b>
        <br />
        <br />
		<logic:present name="infoTurnosDeDisciplinaExecucao" scope="request">
        <table>
        	<%! int i; %>
            <% i = 0; %>
            <tr>
            	<td class="listClasses-header">&nbsp;</td>
            	<td class="listClasses-header"><bean:message key="property.shift.name"/></td>
		        <td class="listClasses-header"><bean:message key="property.shift.type"/></td>
				<td class="listClasses-header"><bean:message key="property.lessons"/></td>
	          	<td class="listClasses-header"><bean:message key="property.shift.capacity"/></td>
            	<td class="listClasses-header"><bean:message key="property.shift.ocupation"/></td>
            	<td class="listClasses-header"><bean:message key="property.shift.percentage"/></td>
            	<td class="listClasses-header"><bean:message key="label.link.shift.classes"/></td>
            </tr>
    	<logic:iterate id="infoTurno" name="infoTurnosDeDisciplinaExecucao">
          	<tr align="center">
	            <td class="listClasses"><html:radio property="indexTurno" value="<%= (new Integer(i)).toString() %>"/></td>
              	<td class="listClasses"><bean:write name="infoTurno" property="nome"/></td>
              	<td class="listClasses"><bean:write name="infoTurno" property="tipo"/></td>
              	<td class="listClasses"><bean:write name="infoTurno" property="lessons"/></td> 
              	<td class="listClasses"><bean:write name="infoTurno" property="lotacao"/></td>
    	       	<td class="listClasses"><bean:write name="infoTurno" property="ocupation"/></td>
              	<td class="listClasses"><bean:write name="infoTurno" property="percentage"/> %</td>
			   	<td class="listClasses">
			   		<bean:define id="shiftOID" name="infoTurno" property="idInternal"/>
               		<html:link page="<%= "/viewClassesWithShift.do?"
               							+ SessionConstants.SHIFT_OID
			  							+ "="
               				   			+ pageContext.findAttribute("shiftOID")
               				   			+ "&amp;"
			  							+ SessionConstants.EXECUTION_COURSE_OID
  										+ "="
  										+ pageContext.findAttribute("executionCourseOID")
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
						<bean:message key="link.view"/>
					</html:link>
             	</td>
          	</tr>
	  	        <% i++; %>
        </logic:iterate>
		</table>
        <br/>
<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>

<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.view.Turno"/></html:submit>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.edit.Turno"/></html:submit>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.delete.Turno"/></html:submit>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.view.StudentsInroled"/></html:submit>
		</logic:present>
        <logic:notPresent name="infoTurnosDeDisciplinaExecucao" scope="request">
        	<br />
        	<br />
			<table>
	        	<tr>
    	        	<td>
        	        	<span class="error"> <bean:message key="errors.existTurnos"/></span>
                	</td>
            	</tr>
            </table>
            <br />
            <br />
            <table>
                <tr>
                    <td>
<html:hidden property="page" value="0"/>
<html:hidden property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
			 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.EXECUTION_DEGREE_OID %>"
			 value="<%= pageContext.findAttribute("executionDegreeOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.CURRICULAR_YEAR_OID %>"
			 value="<%= pageContext.findAttribute("curricularYearOID").toString() %>"/>
<html:hidden property="<%= SessionConstants.EXECUTION_COURSE_OID %>"
			 value="<%= pageContext.findAttribute("executionCourseOID").toString() %>"/>
                        <html:submit property="operation">
							<bean:message key="label.back"/>
<%--                           <bean:write name="elem" property="label.back"/> --%>
                        </html:submit>
                    </td>
                </tr>
            </table>        
        </logic:notPresent>
   </html:form>