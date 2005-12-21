<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="message.editDegree" /></h2>
<br>

<span class="error"><html:errors/></span>

<table>
<html:form action="/editDegree" method="get">
	<html:hidden property="page" value="1"/>
	
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.degreeName"/>
	</td>
	<td>
		<html:text size="60" property="name" />
		
	</td>
</tr>
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.degreeNameEn"/>
	</td>
	<td>
		<html:text size="60" property="nameEn" />
	</td>
</tr>
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.degreeCode"/>
	</td>
	<td>
		<html:text size="13" property="code"  />
	</td>
</tr>
				
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degreeType"/>
	</td>
	<td>
		
		<html:select property="degreeType">
    		<html:option key="option.editDegree.degree" value="DEGREE"/>
    		<html:option key="option.editDegree.masterDegree" value="MASTER_DEGREE"/>
    	</html:select>
		
	</td>
</tr>
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.manager.gradeType"/>
	</td>
	<td>
		<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.GradeScale" bundle="ENUMERATION_RESOURCES"/>
		<html:select property="gradeType">
			<html:option bundle="ENUMERATION_RESOURCES" key="dropDown.Default" value=""/>
			<html:options collection="values" property="value" labelProperty="label"/>
		</html:select>
	</td>
</tr>		
</table>
<html:hidden property="method" value="edit" />
<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>

	

<br>
<html:submit styleClass="inputbutton">
<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
</html:reset>			


</html:form>