<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<logic:present name="employee">
	<bean:define id="month" value='<%=request.getParameter("month")%>'/>
	<bean:define id="year" value='<%=request.getParameter("year")%>'/>
	<bean:define id="employeeNumber" name="employee" property="employeeNumber" />
	<bean:define id="personID" name="employee" property="person.idInternal" />
	
	<p><bean:message key="label.show" bundle="ASSIDUOUSNESS_RESOURCES"/>: <html:link
		page="<%="/assiduousnessResponsible.do?method=showEmployeeWorkSheet&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.workSheet" bundle="ASSIDUOUSNESS_RESOURCES"/>
	</html:link>, <html:link
		page="<%="/assiduousnessResponsible.do?method=showSchedule&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="label.schedule" bundle="ASSIDUOUSNESS_RESOURCES"/>
	</html:link>, <html:link
		page="<%="/assiduousnessResponsible.do?method=showClockings&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.clockings" bundle="ASSIDUOUSNESS_RESOURCES"/>
	</html:link>, <html:link
		page="<%="/assiduousnessResponsible.do?method=showJustifications&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.justifications" bundle="ASSIDUOUSNESS_RESOURCES"/>
	</html:link>
	<%-- , <html:link
		page="<%="/assiduousnessResponsible.do?method=showVacations&month="+month.toString()+"&year="+year.toString()+"&employeeNumber="+employeeNumber.toString()%>">
		<bean:message key="link.vacations" bundle="ASSIDUOUSNESS_RESOURCES"/>
	</html:link>
	--%></p>
		
	<span class="toprint"><br />
	</span>

	<table border="0">
		<tr><td class="invisible">
		<html:img src="<%= request.getContextPath() +"/departmentMember/assiduousnessResponsible.do?method=showPhoto&amp;personID="+personID.toString() %>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
		</td><td>
		<fr:view name="employee" schema="show.employeeInformation">
			<fr:layout name="tabular">
				<fr:property name="classes" value="showinfo1 thbold" />
			</fr:layout>
		</fr:view>
		</td></tr>
	</table>

	<p>
		<strong>
			<bean:message key="<%=month.toString()%>" bundle="ENUMERATION_RESOURCES"/>
			<bean:write name="year"/>
		</strong>
	</p>
</logic:present>	