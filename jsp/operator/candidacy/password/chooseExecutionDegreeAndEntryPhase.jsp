<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<br />
<html:form action="/generatePasswordsForCandidacies.do?method=showCandidacies">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	
	<h2><bean:message key="label.operator.candidacy.passwords" /></h2>
	
	<br/>
	
	<table>
	  <tr>
	    <td>
	    	<strong><bean:message key="label.operator.candidacy.passwords.chooseExecutionDegree" /></strong>
	    </td>
	    <td>	
	    	<html:select property="executionDegreeId">
				<html:option value="0">&nbsp;</html:option>
				<html:options collection="executionDegrees" property="idInternal" labelProperty="degree.presentationName"/>
			</html:select>
		</td>
	  </tr>
	  <tr>
	  	<td>
			<strong><bean:message key="label.operator.candidacy.passwords.chooseEntryPhase" /></strong>
	    </td>
	    <td>
		 	<html:select property="entryPhase">
		 		<logic:iterate id="entryPhase" name="entryPhases">
		 			<bean:define id="entryPhaseName" name="entryPhase" property="name" />
		 			<bean:define id="entryPhaseOrdinal" name="entryPhase" property="entryPhase" />
		 			<html:option value="<%=entryPhaseOrdinal.toString()%>"><bean:write name="entryPhase" property="name"/></html:option>
				</logic:iterate>
			</html:select>
		</td>
	  </tr>
	</table>


	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.OK" value="Ok" styleClass="inputbutton" property="OK"/>
</html:form> 