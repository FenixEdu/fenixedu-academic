<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>

<h2><bean:message key="label.certificate.create" /></h2>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<table>
	<html:form action="/chooseCertificateInfoAction.do?method=chooseStudent">
  	  	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
  	  	<html:hidden property="graduationType" value="STUDENT_CURRICULAR_PLAN_MASTER_DEGREE"/>  
		<tr>
	       	<td><bean:message key="label.masterDegree.administrativeOffice.requesterNumber"/>: </td>
        	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.requesterNumber" property="requesterNumber" value=""/></td>
      	</tr>
      	<tr>
      		<td>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.ok" value="Submeter" styleClass="inputbutton" property="ok"/> 
      		</td>
		</tr>
	</html:form>
</table>