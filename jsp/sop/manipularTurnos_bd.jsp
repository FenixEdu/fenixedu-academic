<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
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
        <!-- Cria a tabela das turnos -->
        <b><bean:message key="listTurnos.existing"/></b>
        <br />
        <br />
		<logic:present name="infoTurnosDeDisciplinaExecucao" scope="session">
        <table>
        	<%! int i; %>
            <% i = 0; %>
            <tr>
            	<td class="listClasses-header">&nbsp;</td>
            	<td class="listClasses-header"><bean:message key="property.shift.name"/></td>
            	<td class="listClasses-header"><bean:message key="property.shift.type"/></td>
            	<td class="listClasses-header"><bean:message key="property.shift.capacity"/></td>
            	<td class="listClasses-header"><bean:message key="label.link.shift.classes"/></td>
            </tr>
    	<logic:iterate id="infoTurno" name="infoTurnosDeDisciplinaExecucao">
          	<tr align="center">
	            <td class="listClasses"><html:radio property="indexTurno" value="<%= (new Integer(i)).toString() %>"/></td>
              	<td class="listClasses"><bean:write name="infoTurno" property="nome"/></td>
              	<td class="listClasses"><bean:write name="infoTurno" property="tipo"/></td>
              	<td class="listClasses"><bean:write name="infoTurno" property="lotacao"/></td>
               	<td class="listClasses">
               		<html:link page="/viewClassesWithShift.do" paramId="name" paramName="infoTurno" paramProperty="nome">
						<bean:message key="link.view"/>							
					</html:link>
             	</td>
          	</tr>
	  	        <% i++; %>
        </logic:iterate>
		</table>
        <br/>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.view.Turno"/></html:submit>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.edit.Turno"/></html:submit>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.delete.Turno"/></html:submit>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="label.view.StudentsInroled"/></html:submit>
		</logic:present>
        <logic:notPresent name="infoTurnosDeDisciplinaExecucao" scope="session">
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
                        <html:submit property="operation">
							<bean:message key="label.back"/>
<%--                           <bean:write name="elem" property="label.back"/> --%>
                        </html:submit>
                    </td>
                </tr>
            </table>        
        </logic:notPresent>
   </html:form>