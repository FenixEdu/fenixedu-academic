<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="announcements">
	<h2><bean:message key="message.announcements.not.available" /></h2>
	</logic:empty>
<table border="0" style="text-align: left;">
        <tbody>
            <logic:iterate id="announcement" name="component" property="announcements" >
                <tr>
                    <td>
                        <h2>
                        <img src="<%= request.getContextPath() %>/images/icon_anuncio.gif" alt="" />&nbsp;
                        <bean:write name="announcement" property="title"/>&nbsp;&nbsp;
                       <span class="post-date">(<dt:format pattern="dd-MM-yyyy HH:mm"><bean:write name="announcement" property="lastModifiedDate.time"/></dt:format>)</span></h2>
                    </td>
                </tr>
                <tr>
                    <td>
						<bean:write name="announcement" property="information" filter="false"/>
                        <br><br>
                    </td>
                </tr>
          
            </logic:iterate>
        </tbody>
</table>
</logic:present>
<logic:notPresent name="siteView" property="component">
<h4>
<bean:message key="message.announcements.not.available" />
</h4>
</logic:notPresent>