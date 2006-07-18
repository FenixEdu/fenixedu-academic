
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoop"><bean:message key="message.curricularCourseManagement.instructions"/></td>
          </tr>
        </table>
	<br />
<li><html:link page="/curricularCourseManager.do?method=prepareSelectDegree" ><bean:message key="link.curricularInformationManagement"/></html:link></li>
<br/>
<br/>
<li><html:link page="/basicCurricularCourseManager.do?method=prepareSelectDegree" ><bean:message key="link.basicCurricularCourseManagement"/></html:link></li>
