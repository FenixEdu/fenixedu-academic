<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />

<title>Lista de Professores do IST</title>

<body bgcolor="white">

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<strong><p align="center"><bean:message key="label.grant.contract.teachersList"/></p></strong><br/>

<%-- Presenting errors --%>
<logic:messagesPresent>
<span class="error">
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
            <bean:message key="label.grant.contract.teacherNumber"/>
        </th>
        <th class="listClasses-header">
            <bean:message key="label.grant.contract.teacherName"/>
        </th>
    </tr>   
    <%-- Table with result of search --%>
    <logic:iterate id="infoTeacher" name="teachersList">
        <tr>
            <td class="listClasses">
                <bean:write name="infoTeacher" property="teacherNumber"/>
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