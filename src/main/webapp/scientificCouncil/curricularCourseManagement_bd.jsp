
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoop"><bean:message key="message.curricularCourseManagement.instructions"/></td>
          </tr>
        </table>
	<br />
<li><html:link page="/curricularCourseManager.do?method=prepareSelectDegree" ><bean:message key="link.curricularInformationManagement"/></html:link></li>
<br/>
<br/>
<li><html:link page="/basicCurricularCourseManager.do?method=prepareSelectDegree" ><bean:message key="link.basicCurricularCourseManagement"/></html:link></li>
