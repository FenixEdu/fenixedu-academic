<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="java.util.List"%>
   	<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected"><p>O curso seleccionado
              &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
            </td>
          </tr>
    </table>
    <br />
    <center><font color='#034D7A' size='5'> <b> <bean:message key="title.manage.turnos"/> </b> </font></center>
    <br/>
    <br/>
    <center><span class="error"><html:errors/></span>
    <html:form action="/manipularTurnosForm">
        <!-- Cria a tabela das turnos -->
        <center> <b> <bean:message key="listTurnos.existing"/> <b> </center>
        <br/>
		<logic:present name="infoTurnosDeDisciplinaExecucao" scope="session">
        <table align="center" border=1 cellpadding='5'>
        	<%! int i; %>
            <% i = 0; %>
            <tr>
            	<th>
            		&nbsp;
            	</th>
            	<th>
            		<bean:message key="property.shift.name"/>
            	</th>
            	<th>
            		<bean:message key="property.shift.type"/>
            	</th>
            	<th>
            		<bean:message key="property.shift.capacity"/>
            	</th>
            	<th>
            		<bean:message key="label.link.shift.classes"/>
            	</th>
            </tr>
            <logic:iterate id="infoTurno" name="infoTurnosDeDisciplinaExecucao">
            	<tr align="center">
	                <td>
    	                <html:radio property="indexTurno" value="<%= (new Integer(i)).toString() %>"/>
                    </td>
                    <td class="listClasses">
                        <bean:write name="infoTurno" property="nome"/>
                    </td>
                    <td class="listClasses">
                        <bean:write name="infoTurno" property="tipo"/>
                    </td>
                    <td class="listClasses">
                        <bean:write name="infoTurno" property="lotacao"/>
                    </td>
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
        <br/>    
        <table align="center">
            <tr align="center">
                <td height='60'>
                    <html:submit property="operation">
                        <bean:message key="label.view.Turno"/>
                    </html:submit>
                </td>
                <td> </td>
                <td>
                    <html:submit property="operation">
                    	<bean:message key="label.edit.Turno"/>
                    </html:submit>
                </td>
                <td> </td>
                <td>
                    <html:submit property="operation">
                        <bean:message key="label.delete.Turno"/>
                    </html:submit>
                </td>
            </tr>
            <tr align="center">
                <td colspan='5' height='60'>
                    <html:submit property="operation">
                        <bean:message key="label.view.StudentsInroled"/>
                    </html:submit>
                </td>
            </tr>
        </table>
		</logic:present>
		
        <logic:notPresent name="infoTurnosDeDisciplinaExecucao" scope="session">
        	<br/>
        	<br/>
			<table align="center" border="1" cellpadding='5'>
	        	<tr align="center">
    	        	<td>
        	        	<font color='red'> <bean:message key="errors.existTurnos"/> </font>
                	</td>
            	</tr>
            </table>
            <br/>
            <br/>
            <table align="center">
                <tr align="center">
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
