<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="message.editDegree" /></h2>
<br/>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<table>
<html:form action="/editDegree" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.degreeName"/>
	</td>
	<td>
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="60" property="name" />
		
	</td>
</tr>
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.degreeNameEn"/>
	</td>
	<td>
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.nameEn" size="60" property="nameEn" />
	</td>
</tr>
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.degreeCode"/>
	</td>
	<td>
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.code" size="13" property="code"  />
	</td>
</tr>
				
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degreeType"/>
	</td>
	<td>
		
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeType" property="degreeType">
    		<html:option bundle="MANAGER_RESOURCES" key="option.editDegree.degree" value="DEGREE"/>
    		<html:option bundle="MANAGER_RESOURCES" key="option.editDegree.masterDegree" value="MASTER_DEGREE"/>
    	</html:select>
		
	</td>
</tr>
<tr>
	<td>
		<bean:message bundle="MANAGER_RESOURCES" key="message.manager.gradeType"/>
	</td>
	<td>
		<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.GradeScale" bundle="ENUMERATION_RESOURCES"/>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.gradeType" property="gradeType">
			<html:option bundle="ENUMERATION_RESOURCES" key="dropDown.Default" value=""/>
			<html:options collection="values" property="value" labelProperty="label"/>
		</html:select>
	</td>
</tr>		
</table>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>

	

<br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
</html:reset>			


</html:form>