<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<span class="error"><html:errors/></span>

<html:form action="/editAnnouncement" focus="title" >
	<html:hidden property="page" value="1"/>
	<table border="0" >
        	<tr>
        		<td>
					<bean:message key="label.title" />
				</td>
				<td>
					<html:text name="Announcement" property="title" >
					</html:text>
				</td>
			</tr>
        	<tr>
        		<td>
					<bean:message key="label.information" />	
				</td>
				<td>
					<html:text name="Announcement" property="information" >
					</html:text>
				</td>
			</tr>
        </table>

	<br>
	<html:reset ><bean:message key="label.clear"/>
	</html:reset>
	<html:hidden property="method" value="editAnnouncement" />
	<html:submit> <bean:message key="button.save" />
	</html:submit>    

</html:form>