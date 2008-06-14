<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.SessionConstants" %>

<em><bean:message key="title.resourceAllocationManager.management"/></em>
<h2><bean:message key="title.manage.schedule"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<p class="mtop15 mbottom05">
	<bean:message key="label.chooseDegreeAndYear" />:
</p>


<html:form action="/chooseContext" focus="executionDegreeOID">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="choose"/>
	<html:hidden alt="<%= SessionConstants.EXECUTION_PERIOD_OID %>" property="<%= SessionConstants.EXECUTION_PERIOD_OID %>"
				 value="<%= pageContext.findAttribute("executionPeriodOID").toString() %>"/>


	<table class="tstyle5 thlight thright mtop05">
		<tr>
			<th>
				<bean:message key="property.context.degree"/>:
			</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="executionDegreeOID" size="1">
		       		<html:options collection="licenciaturas"
		       					  property="value"
		       					  labelProperty="label"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<th>
				<bean:message key="property.context.curricular.year"/>:
			</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="curricularYear" size="1">
		       		<html:options collection="anosCurriculares"
		       					  property="value"
		       					  labelProperty="label"/>
		       	</html:select>
		    </td>
		</tr>
	</table>


	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter" styleClass="inputbutton">
             <bean:message key="label.next"/>
       </html:submit>
	</p>
</html:form>