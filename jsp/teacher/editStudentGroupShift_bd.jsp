<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.editStudentGroupShift"/></h2>

<logic:present name="siteView" property="component">
 <bean:define id="component" name="siteView" property="component" />


<div class="infoop2">
	<bean:message key="label.teacher.EditStudentGroupShift.description" />
</div>

<div class="dinline forminline"> 
<html:form action="/editStudentGroupShift" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<p class="mvert15">
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>



<table class="tstyle5 thlight thright dinline mtop15">
		<tr>
			<th><bean:message key="label.editStudentGroupShift.oldShift"/></th>
			<td><bean:write name="shift" property="nome"/></td>
			<td class="tdclear tderror1"><span class="error"><!-- Error messages go here --><html:errors property="shiftType"/></span></td>
		</tr>
		
		<tr>
		 	<th><bean:message key="message.editStudentGroupShift"/></th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.shift" property="shift" size="1">
		    	<html:options collection="shiftsList" property="value" labelProperty="label"/>
		    	</html:select>
	    	</td>
			<td class="tdclear tderror1"><span class="error"><!-- Error messages go here --><html:errors property="shiftType"/></span></td>
		</tr>	
</table>

<br/>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.change"/>                    		         	
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editStudentGroupShift"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
</html:form>


	<html:form action="/viewStudentGroupInformation" method="get">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewStudentGroupInformation"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
	</html:form>

</div>

</logic:present>