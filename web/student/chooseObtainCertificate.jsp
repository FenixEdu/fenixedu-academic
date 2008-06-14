<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:html xhtml="true">
    <head>
        <title> <bean:message key="title.choose.certificate"/> </title>
        <html:base/>
    </head>
    <body>
        <center><font color='#034D7A' size='5'> <b> <bean:message key="title.choose.certificate"/> </b> </font></center>
        <br/>
        <br/>
        <html:form action="/escolherCertidaoForm.do">
        <html:errors />
            <table align="center" border="5" cellpadding='20' cellspacing='10'>
                <tr valign='center'>
                	<td>
                		<table border="0">
                		<tr>
                    		<td align='center' colspan='2' nowrap>
                        		<bean:message key="label.get.certificate"/>		
                    		</td>
                		</tr>
                		<tr valign='center'>
                    		<td align='center' colspan='2' nowrap>
                        		<br/>
                    		</td>
                		</tr>
                		<tr valign='center'>
                    		<td align='center' colspan='2' nowrap>
                 	    		<html:select bundle="HTMLALT_RESOURCES" altKey="select.certidao" property="certidao" size="1">
                        		<html:options collection="certidoes" property="value" labelProperty="label"/>
                        		</html:select>
                    		</td>
                		</tr>
                	</table>
                </td>
             </tr>
            </table>
            <br/>
            <br/>
            <table align="center">
                <tr align="center">
                    <td>
                        <html:submit>
                            <bean:message key="label.next"/>
                        </html:submit>
                    </td>
                </tr>
            </table>
        </html:form>
    </body>
</html:html>

