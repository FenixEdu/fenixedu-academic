<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/print.css" rel="stylesheet" media="print" type="text/css" />
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<title>Lista de Professores do <bean:message key="institution.name.abbreviation" bundle="GLOBAL_RESOURCES" /></title>

<body bgcolor="white">


<strong><p align="center"><bean:message key="label.grant.contract.teachersList"/></p></strong><br/>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error"><!-- Error messages go here -->
	<html:errors/>
</span><br/>
</logic:messagesPresent>

<logic:messagesNotPresent>


<logic:present name="teachersList">
<center>
    <table border="0" cellspacing="1" cellpadding="1">
    <%-- Table with teachers description rows --%>
    <tr>
        <th class="listClasses-header">
            <bean:message key="label.grant.contract.teacherId"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.contract.teacherName"/>
        </th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoTeacher" name="teachersList">
        <tr>
            <td class="listClasses">
                <bean:write name="infoTeacher" property="teacherId"/>
            </td>
            <td class="listClasses">
                <bean:write name="infoTeacher" property="infoPerson.nome"/>
            </td>       
        </tr>
    </logic:iterate>
    </table>
</center>
</logic:present>

</logic:messagesNotPresent>

</body>

</html:html>