<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e" %>




<html:form action="/generatePasswordsForCandidacies.do?method=showCandidacies">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	
	<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES"/></em>
	<h2><bean:message key="label.operator.candidacy.passwords" /></h2>
	
	<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
	
	<table class="tstyle5 thlight thright">
	  <tr>
	    <th>
	    	<bean:message key="label.operator.candidacy.passwords.chooseExecutionDegree" />:
	    </th>
	    <td>	
	    	<html:select property="executionDegreeId">
				<html:option value="0">&nbsp;</html:option>
				<html:options collection="executionDegrees" property="externalId" labelProperty="degree.presentationName"/>
			</html:select>
		</td>
	  </tr>
	  <tr>
	  	<th>
			<bean:message key="label.operator.candidacy.passwords.chooseEntryPhase" />:
	    </th>
	    <td>
		 	<html:select property="entryPhase">
		 		<logic:iterate id="entryPhase" name="entryPhases">
		 			<bean:define id="entryPhaseName" name="entryPhase" property="localizedName" />
		 			<bean:define id="entryPhaseOrdinal" name="entryPhase" property="name" />
		 			<html:option value="<%=entryPhaseOrdinal.toString()%>"><bean:write name="entryPhaseName" /></html:option>
				</logic:iterate>
			</html:select>
		</td>
	  </tr>
	</table>


	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.OK" value="Ok" styleClass="inputbutton" property="OK"/>
</html:form> 