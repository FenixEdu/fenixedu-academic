<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
       <table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td bgcolor="#FFFFFF" class="infoselected">
			  <strong><bean:message key="property.name"/></strong> <bean:write name="infoStudent" property="infoPerson.nome"/>
			  <br/>
			  <strong><bean:message key="property.number"/></strong> <bean:write name="infoStudent" property="number"/>
            </td>
          </tr>
		</table>
        <br />
		<p>
            <img src="../images/portalEstudante.gif" width="289" height="45" alt="">
        </p>
		<br/>
        <table cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td>
                    <html:link page="/student/viewEnrolment.do"> <bean:message key="title.student.enrolment"/> </html:link>
                </td>
            </tr>
            <tr>
                <td>
                    Aqui podem ser colocadas outras operações disponíveis no portal do aluno.
                </td>
            </tr>
        </table>