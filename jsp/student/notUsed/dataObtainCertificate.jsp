<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">

<html:html>

      <tr>
	    		<td><BR>
          		</td>
        </tr>
		<tr>
				<td>
					<B> <bean:message key="label.obtain.certificate.sitio"/>, 
					<bean:write name="Data" property="dia"/> de <bean:write name="Data" property="mes"/> de  <bean:write name="Data" property="ano"/></B>
				</td>
		</tr>
        <tr>
	    		<td>
          		</td>
        </tr>
        <tr>
	    		<td align="right">
	    			<B><bean:message key="label.chefe.artigo"/> Chefe de Secção</B>
          		</td>
        </tr>

  </html:html>
