<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html>
  <head>
    <title><bean:message key="title.error"/></title>
  </head>
  <body>
    <html:errors/>
   <center> <b><bean:message key="message.error.ocurred"/></b> </center>
     <br/> <bean:message key="message.error.sendEmail"/>
     <br/><bean:message key="message.error.goBack"/>
     <br/>
    <html:link page="/exceptionHandling.do?method=goBack">
   	 <center><b><bean:message key="link.goBack"/></b></center>
    </html:link>
    <html:form action="/exceptionHandling.do?method=sendEmail">
    <table align="center"  cellpadding='20' cellspacing='10'>
    	<tr><td>
    	<bean:message key="property.email"/></td><td>
    	<html:text bundle="HTMLALT_RESOURCES" altKey="text.email" property="email" value=""/> </td>
    	</tr>
    	<tr><td>
    	<bean:message key="property.subject"/></td><td>
   		<html:text bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" value=""/></td>
   		</tr>
    	<tr><td>
   		<bean:message key="property.message"/></td><td>
    	<html:textarea bundle="HTMLALT_RESOURCES" property="body" value=""/></td>
    	</tr>
    	
      </table>	
      <center>
    	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" >
   		 <bean:message key="label.submit"/>
    	</html:submit>
   	  </center>
    </html:form>
  </body>
</html>
