<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<span class="error"><!-- Error messages go here --><html:errors /></span>

		<bean:define id="link">/displayCourseListToStudyPlan.do?method=prepareSelectCourseList<%= "&" %>candidateID=
		</bean:define>
		<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request" />
	<br />
<logic:present name="jspTitle">
	<h2><bean:write name="jspTitle" /></h2>
	<br />
</logic:present>

	<logic:present name="candidateList">
            <bean:message key="title.masterDegree.administrativeOffice.listCandidates" />
        <br /><br />
            <table>	        
            <tr>
            	<th class="listClasses-header"><bean:message key="label.person.name" /></th>
            </tr>
        	<!-- Candidate List -->
        	<logic:iterate id="candidate" name="candidateList" indexId="indexCandidate">
        		<bean:define id="candidateLink">
    				<bean:write name="link"/><bean:write name="candidate" property="idInternal"/>
    			</bean:define>
    		<tr>
    		<td class="listClasses">
      			<html:link page="<%= pageContext.findAttribute("candidateLink").toString() + "&amp;executionYear=" + pageContext.findAttribute("executionYear") + "&amp;degreeCurricularPlanID=" + degreeCurricularPlanID %>">
    				<bean:write name="candidate" property="infoPerson.nome" />
    			</html:link>
    		</td>
    		</tr>
    		</logic:iterate>
        	</table>
	</logic:present> 
	
