<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
  <head>
    <title><bean:message key="title.error"/></title>
  </head>
  <body>
    <html:errors/>
   <center> <b><bean:message key="message.error.notAuthorized"/></b> </center>
     <br/>
     <br/>
     <br/>
     <br/>
     <br/>
    <html:link page="/">
   	 <center><b><bean:message key="link.login"/></b></center>
    </html:link>
   
  </body>
</html>
