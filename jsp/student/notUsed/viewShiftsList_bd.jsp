<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="java.util.ArrayList" %>
       <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected">
			  <strong><bean:message key="property.name"/></strong> <bean:write name="infoStudent" property="infoPerson.nome"/>
			  <br/>
			  <strong><bean:message key="property.number"/></strong> <bean:write name="infoStudent" property="number"/>
            </td>
          </tr>
		</table>
        <br />
		<center><font color='#034D7A' size='5'> <b> <bean:message key="title.shifts.available"/> </b> </font></center>
		<br/>
	<logic:present name="shiftsList" scope="request">
		<table>
			<tr>
				<th>
					<bean:message key="property.name"/>
				</th>
				<th>
					<bean:message key="property.type"/>
				</th>
				<th>
					<bean:message key="property.turno.capacity"/>
				</th>
				<th>
					<bean:message key="label.vacancies"/> 
				</th>
				<th>
					<bean:message key="label.enrol"/> 
				</th>
				<th>
					<bean:message key="label.shift.view.schedule"/> 
				</th>			
			</tr>		
		<% ArrayList vacancies = (ArrayList) request.getAttribute("vacancies"); %>	
	    <% int xpto=0;%>
	<logic:iterate id="shift" name="shiftsList" scope="request">
		<tr>
			<td>
				<jsp:getProperty name="shift" property="nome"/>
			</td>
			<td>
				<jsp:getProperty name="shift" property="tipo"/>
			</td>
			<td>
				<jsp:getProperty name="shift" property="lotacao"/>		
			</td>
			<td>
				<%= vacancies.get(xpto) %>
			</td>
			<td>
				<html:link paramId="shiftName" paramName="shift" paramProperty="nome"  page="/shiftEnrolment.do">
				<bean:message key="label.enrol"/> 
				</html:link>	
			</td>
			<td>
				<html:link paramId="shiftName" paramName="shift" paramProperty="nome"  page="/viewShiftSchedule.do">
				<bean:message key="link.view"/>
				</html:link>	
			</td>		
		</tr>
		<%xpto++;%>
	</logic:iterate>
	</table>
	</logic:present>
	<logic:notPresent name="shiftsList" scope="request">
		<table align="center" border='1' cellpadding='10'>
					<tr align="center">
						<td>
							<font color='red'> <bean:message key="message.student.enrolment.shifts.not.available"/> </font>
						</td>
					</tr>
		</table>
	</logic:notPresent>
