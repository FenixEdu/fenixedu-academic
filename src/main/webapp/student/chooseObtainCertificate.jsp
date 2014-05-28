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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
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

