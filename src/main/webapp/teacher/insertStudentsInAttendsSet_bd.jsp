<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message key="title.insertStudentsInAttendsSet"/></h2>

<script language="Javascript" type="text/javascript">
<!--
var select = false;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}
	for (var i=0; i<document.forms[0].selected.length; i++){
		var e = document.forms[0].selected[i];
		if (select == true) { e.checked = true; } else { e.checked = false; }
	}
}

function cleanSelect() { 
	select = false; 
	document.forms[0].selected[0].checked = false; 
}
// -->
</script>



<div class="infoop2">
	<bean:message key="label.teacher.InsertStudentsInAttendsSet.description" />
</div>

<p>	
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<ul>
	<li>
		<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
	    	<bean:message key="link.backToShiftsAndGroups"/>
    	</html:link>
    </li>
</ul>

<logic:present name="infoStudentList"> 
		
<html:form action="/insertStudentsInAttendsSet" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertStudentsInAttendsSet"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />


<logic:empty name="infoStudentList"> 
	<h2><bean:message key="message.insertStudentsInAttendsSet.NoMembersToAdd" /></h2>
</logic:empty>

<logic:notEmpty name="infoStudentList"> 
<p>
	<bean:message key="message.editAttendsSetMembers.InsertMembers"/>
</p>




	<table class="tstyle5 thlight thright">
		<tr>
			<td>
				<bean:message key="label.allStudents"/>:
			</td>
			<td>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selected" property="selected" onclick="invertSelect()">
				    <bean:message key="label.allStudents"/>
				</html:multibox> 
			</td>
		</tr>
	</table>

	
<table class="tstyle4">	
	<tr>
		<th>
		</th>
		<th><bean:message key="label.teacher.StudentNumber" />
		</th>
		<th><bean:message key="label.teacher.StudentName" />
		</th>
		<th><bean:message key="label.teacher.StudentEmail" />
		</th>
	</tr>


	<logic:iterate id="infoStudent" name="infoStudentList">			
		<tr>	
			<td>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.selected" property="selected" onclick="cleanSelect()">
			<bean:write name="infoStudent" property="externalId"/>
			</html:multibox>
			</td>
			<td><bean:write name="infoStudent" property="number"/>
			</td>	
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>		
			<td><bean:write name="infoPerson" property="nome"/>
			</td>
			<td><bean:write name="infoPerson" property="email"/>
			</td>
	 	</tr>	
	 </logic:iterate>

</table>
<br/>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.insertAluno"/>                    		         	
</html:submit>       

<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  

</logic:notEmpty>

</html:form>
</logic:present>

<logic:notPresent name="infoStudentList">
<h2>
<bean:message key="message.editAttendsSetMembers.NoMembersToAdd" />
</h2>
</logic:notPresent>