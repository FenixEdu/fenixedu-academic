<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<logic:notPresent name="publico.infoShifts" scope="request">
<table align="center" border='1' cellpadding='10'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.shifts"/> </font>
				</td>
			</tr>
		</table>
</logic:notPresent>

<logic:present name="publico.infoShifts" scope="request">
<table>
		<tr>
			<th>
				<bean:message key="property.name"/>
			</th>
			<th>
				<bean:message key="property.shift.type"/>
			</th>
				<th>
				<bean:message key="property.shift.capacity"/> 
			</th>
			
		</tr>		
<logic:iterate id="infoShift" name="publico.infoShifts" scope="request">
<tr>
			<td><html:link page="/viewShiftTimeTable.do" paramId="shiftName" paramName="infoShift" paramProperty="nome">
				<jsp:getProperty name="infoShift" property="nome"/>
				</html:link>
			</td>
			<td>
				<jsp:getProperty name="infoShift" property="tipo"/>
			</td>
				<td>
				<jsp:getProperty name="infoShift" property="lotacao"/>
			</td>
			
		</tr>		
</logic:iterate>
</logic:present>
