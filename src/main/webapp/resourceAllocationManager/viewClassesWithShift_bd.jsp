<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ page import="java.util.List"%>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			  <strong><jsp:include page="context.jsp"/></strong>
     	</td>
   	</tr>
</table>
<br />
<h2><bean:message key="title.shift.classes"/></h2>
<br />
<table>
  	<tr>
      	<th class="listClasses-header"><bean:message key="property.class.name"/></th>
<%--      	<th class="listClasses-header"><bean:message key="label.class"/></th> --%>
   	</tr>
	<logic:iterate id="infoClass" name="classesWithShift">
    <tr>
   		<td class="listClasses"><bean:write name="infoClass" property="nome"/></td>
<%--        <td class="listClasses">
			<html:link page="/ClassManagerDA.do?method=viewClass" paramId="className" paramName="infoClass" paramProperty="nome">
		    	<bean:message key="link.view"/>							
			</html:link>
         </td> --%>
  	</tr>
	</logic:iterate>
</table>