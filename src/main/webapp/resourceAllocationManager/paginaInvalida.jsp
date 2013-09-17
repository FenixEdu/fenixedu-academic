<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<%! String myAction; %>
<%  
    myAction = (String)request.getAttribute("lastMainActionPerformed");
%>

<html:html xhtml="true">
    <head>
        <title> Página Inválida </title>
    </head>
    <body>
        <br/>
        <h3 align="center"> A página anterior expirou e, portanto, não pode ser processada. </h3>
        <br/>
        <table align="center">
            <tr align="center">
                <td>
                    <html:link page="<%=myAction%>"> <i> Voltar </i> </html:link>
                </td>
            </tr>
        </table>
        <br/>
    </body>
</html:html>
