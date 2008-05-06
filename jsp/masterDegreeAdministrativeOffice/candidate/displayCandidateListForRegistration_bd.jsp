<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>


	<table>
		<tr>
			<td>
				<strong><bean:message key="label.masterDegree.administrativeOffice.degree" /></strong>
			</td>
			<td>
				<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome" />
			</td>
		</tr>
		<tr>
			<td>
				<strong><bean:message key="label.masterDegree.administrativeOffice.executionYear" /></strong>
			</td>
			<td>
				<bean:write name="infoExecutionDegree" property="infoExecutionYear.year" />
			</td>
		</tr>
	</table>

	<br />
	<br />


	<bean:define id="link">/candidateRegistration.do?method=prepareCandidateRegistration<%= "&" %>candidateID=
	</bean:define>


    <logic:present name="candidateList">
    	<br />
        <h2><bean:message key="title.masterDegree.administrativeOffice.listCandidates" /></h2>
    	<br />
    	<table>
			<tr>
				<td class="listClasses">
					<bean:message key="label.masterDegree.administrativeOffice.number" />
				</td>
				<td class="listClasses">
					<bean:message key="label.masterDegree.administrativeOffice.name" />
				</td>
				<td class="listClasses">
					<bean:message key="label.specialization" />
				</td>
			</tr>
    	
	    	<logic:iterate id="candidate" name="candidateList" >
        		<bean:define id="candidateLink">
    				<bean:write name="link"/><bean:write name="candidate" property="idInternal"/>
    			</bean:define>
    			<tr>
					<td class="listClasses">
						<bean:write name="candidate" property="candidateNumber"/>
					</td>
					<td class="listClasses">
		      			<html:link page="<%= pageContext.findAttribute("candidateLink").toString() %>">
		    				<bean:write name="candidate" property="infoPerson.nome" />
		    			</html:link>
					</td>
					<td class="listClasses">
						<bean:message name="candidate" property="specialization.name" bundle="ENUMERATION_RESOURCES"/>
					</td>
    			</tr>
    		</logic:iterate>
    	</table>
   </logic:present>