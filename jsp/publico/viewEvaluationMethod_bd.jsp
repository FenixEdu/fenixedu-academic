<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<br />

<logic:present name="siteView">
	<bean:define id="component" name="siteView" property="component"/>
	
	<blockquote>
			
		<h2><bean:message key="title.evaluationMethod"/></h2>				
		<table>
			<tr>
				<td > 
					<bean:write name="component" property="evaluationElements" filter="false"/>
	 			</td>
			</tr>
		</table>
		
		<br />
		
		<logic:notEmpty name="component" property="evaluationElementsEn">	
			<h2><bean:message key="title.evaluationMethod.eng"/></h2>
			<table>	
				<tr>
					<td> 
						<bean:write name="component" property="evaluationElementsEn" filter="false"/>
					</td>
				</tr>
			</table>
			<br/>	
		</logic:notEmpty>
			
		</blockquote>				
		<br />
		<br />			
		<br />		

</logic:present> 