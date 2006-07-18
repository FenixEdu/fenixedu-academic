<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2>
	<bean:message key="title.manage.schedule"/>
</h2>

<span class="error"><html:errors/></span>

<html:form action="/chooseContext" focus="executionDegreeOID">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>

	<table width="100%" border="0" cellpadding="0" cellspacing="0">
    	<tr>
        	<td bgcolor="#FFFFFF" class="infoop">
        		Por favor, proceda &agrave; escolha do curso pretendido.
        	</td>
        </tr>
    </table>

	<br />
	<p>
		<bean:message key="property.context.degree"/>:
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeOID" property="executionDegreeOID" size="1">
       		<html:options collection="licenciaturas"
       					  property="value"
       					  labelProperty="label"/>
       </html:select>
	</p>

	<br />
	<table width="100%" border="0" cellpadding="0" cellspacing="0">
		<tr>
			<td bgcolor="#FFFFFF" class="infoop"><bean:message key="label.chooseYear" /></td>
		</tr>
	</table>

	<br />
	<br />   
	<table border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td nowrap class="formTD">
				<bean:message key="property.context.curricular.year"/>:
			</td>
			<td nowrap class="formTD">
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularYear" property="curricularYear" size="1">
		       		<html:options collection="anosCurriculares"
		       					  property="value"
		       					  labelProperty="label"/>
		       	</html:select>
		    </td>
		</tr>
	</table>

	<br />
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
             <bean:message key="label.next"/>
       </html:submit>
	</p>
</html:form>