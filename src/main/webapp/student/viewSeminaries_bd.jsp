<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %> 
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %> 
<bean:define id="listSeminaries" type="java.util.List" scope="request" name="seminaries"/> 

<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="link.seminaries.enrolment"/></h2>
		
<logic:empty name="listSeminaries" >
	<p><em><bean:message key="message.no.seminaries.to.enroll"/></em></p>
</logic:empty>

<logic:notEmpty name="listSeminaries" >
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<p class="redtxt">Informações de utilização:</p>
	<p>Existem várias modalidades de inscrição. Para mais informações consultar regulamento do seminário.</p>
	<p><bean:message key="label.seminariesToEnroll"/></p>
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
						   paramProperty="externalId" 
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
	<p><bean:message key="label.currentCandidacies"/></p>
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
							   paramProperty="externalId" 
							   paramId="objectCode">
									<bean:message key="label.seminaryCandidacyDetails" />
					</html:link>
				</td>
				<td class="listClasses">
				<html:link page="/cancelCandidacy.do" 
							   paramName="candidacy" 
							   paramProperty="externalId" 
							   paramId="objectCode">
									<bean:message key="label.cancelCandidacy" />
				</html:link>
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:notEmpty>