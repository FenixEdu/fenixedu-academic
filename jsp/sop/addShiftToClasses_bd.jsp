<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<span class="error"><html:errors/></span>
<logic:present name="siteView">
<bean:define id="component" name="siteView" property="component"/>
<br/>
<br/>
<bean:message key="message.addshift.toclasses.warning"/>
<br/>
<br/>
<table>
<tr>
	<td class="listClasses-header">
				&nbsp;
	</td>
	<td class="listClasses-header">
				<bean:message key="label.class" /> 
	</td>
	<td class="listClasses-header">
				<bean:message key="label.degree" />
	</td>			
</tr>  
<html:form action="/addShiftToClasses">

<html:hidden property="method" value="addShiftToClasses"/>  	
<bean:define id="classesList" name="component" property="infoClasses"/>
<logic:iterate id="infoClass" name="component" property="infoClasses" type="DataBeans.InfoClass">
<bean:define id="idInternal" name="infoClass" property="idInternal"/>
<tr>
	<td class="listClasses">
			<html:multibox  property="classesList" value="<%= idInternal.toString() %>" />
	</td>
	<td class="listClasses">
		<bean:write name="infoClass" property="nome"/>			
	</td>
	<bean:define id="infoExecutionDegree" name="infoClass" property="infoExecutionDegree"/>
	<bean:define id="infoDegreeCurricularPlan" name="infoExecutionDegree" property="infoDegreeCurricularPlan"/>
	<bean:define id="infoDegree" name="infoDegreeCurricularPlan" property="infoDegree"/>

	<td class="listClasses">
		<bean:write name="infoDegree" property="sigla"/>			
	</td>
</tr>
</logic:iterate>

</table>
<br/>
<br/>
<br/>
<html:submit/><html:reset/>
</html:form>
</logic:present>