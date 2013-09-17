<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.student.Registration" %>


<bean:define id="student" name="<%= PresentationConstants.STUDENT %>" scope="request"/>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.title"/></h2>
<center>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<br/>

<table border="0" width="100%" cellspacing="3" cellpadding="10">
	<tr>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/></th>
		<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentName"/></th>
	</tr>

	<tr>
		<td align="left">
			<bean:write name="student" property="number"/>
		</td>
		<td align="left">
			<bean:write name="student" property="person.name"/>
		</td>			
	</tr>
	
</table>