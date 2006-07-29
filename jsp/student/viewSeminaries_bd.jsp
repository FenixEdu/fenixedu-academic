<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %> 
<bean:define id="listSeminaries" type="java.util.List" scope="request" name="seminaries"/> 
<logic:empty name="listSeminaries" >
		<h2><bean:message key="message.no.seminaries.to.enroll"/></h2>
</logic:empty>
<logic:notEmpty name="listSeminaries" >
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<h2 class="redtxt">Informações de utilização:</h2>
	<p>Existem várias modalidades de inscrição. Para mais informações consultar regulamento do seminário.</p>
	<h2><bean:message key="label.seminariesToEnroll"/></h2>
	<table width="90%" align="center">
	<tr>
		<th class="listClasses-header" ><bean:message key="label.seminaryTitle"/></th>
		<th class="listClasses-header" ><bean:message key="label.enroll" /></th>
		<th class="listClasses-header" ><bean:message key="label.deadline" /></th>
	</tr>	
		<logic:iterate id="seminary" name="listSeminaries" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoSeminary">
		<tr>
			<td class="listClasses"><bean:write name="seminary" property="name"/></td>		
			<td class="listClasses">
				<html:link page="/candidacyOptions.do" 
						   paramName="seminary" 
						   paramProperty="idInternal" 
						   paramId="objectCode">
								<bean:message key="label.seminaryEnroll" />
				</html:link>
			</td>
			<td class="listClasses">
				<%=seminary.printDeadline()%>
			</td>
		</tr>
	</logic:iterate>
	</table>
</logic:notEmpty>
<logic:notEmpty name="currentCandidacies" >
	<h2><bean:message key="label.currentCandidacies"/></h2>
	<bean:define id="currentCandidacies" type="java.util.List" scope="request" name="currentCandidacies"/>
	<table width="90%" align="center">
		<tr>
			<th class="listClasses-header" ><bean:message key="label.seminaryTitle"/></th>
			<th class="listClasses-header" ><bean:message key="label.seminaryCandidacyDetails" /></th>
			<th class="listClasses-header" ><bean:message key="label.cancelCandidacy" /></th>
		</tr>	
			<logic:iterate id="candidacy" name="currentCandidacies">
			<tr>
				<td class="listClasses"><bean:write name="candidacy" property="seminaryName"/></td>		
				<td class="listClasses">
					<html:link page="/candidacyDetails.do" 
							   paramName="candidacy" 
							   paramProperty="idInternal" 
							   paramId="objectCode">
									<bean:message key="label.seminaryCandidacyDetails" />
					</html:link>
				</td>
				<td class="listClasses">
				<html:link page="/cancelCandidacy.do" 
							   paramName="candidacy" 
							   paramProperty="idInternal" 
							   paramId="objectCode">
									<bean:message key="label.cancelCandidacy" />
				</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>