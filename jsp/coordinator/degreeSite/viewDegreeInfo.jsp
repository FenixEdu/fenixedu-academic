<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<p><span class="error"><html:errors/></span></p>

<html:form action="/degreeSiteManagement">

	<bean:define id="degreeCurricularPlanID" name="degreeCurricularPlanID" scope="request"/>
	
	<bean:define id="infoDegreeInfoID" name="infoDegreeInfoID"/>
	<html:hidden property="infoDegreeInfoID" value="<%=  infoDegreeInfoID.toString() %>"/>
	<html:hidden property="degreeCurricularPlanID" value="<%=  degreeCurricularPlanID.toString() %>"/>

	<html:hidden property="method" value="editDegreeInformation" />
	
	<html:hidden property="page" value="1" />

	<logic:notPresent name="inEnglish">
		<h2><bean:message key="title.coordinator.degreeSite.edit"/></h2>
	</logic:notPresent>
	<logic:present name="inEnglish">
		<bean:define id="inEnglish" name="inEnglish" />
		<html:hidden property="inEnglish" value="<%=  inEnglish.toString() %>"/>
		
		<h2><bean:message key="title.coordinator.degreeSite.editEnglish"/></h2>
	</logic:present>

	<logic:present name="inEnglish">		
		<div class="gen-button">
			<logic:present name="info" >
			<logic:equal name="info" value="description" >		
			<img src="<%= request.getContextPath() %>/images/portugal-flag.gif" alt="Icon: English version!" width="16" height="12" />
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;info=description&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>">
				<bean:message key="link.coordinator.degreeSite.editPortuguese"/>
			</html:link>
			</logic:equal>
			</logic:present>			
			
			<logic:present name="info" >
			<logic:equal name="info" value="acess" >		
			<img src="<%= request.getContextPath() %>/images/portugal-flag.gif" alt="Icon: English version!" width="16" height="12" />
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;info=acess&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>">
				<bean:message key="link.coordinator.degreeSite.editPortuguese"/>
			</html:link>
			</logic:equal>
			</logic:present>				
		</div>
	</logic:present>	

	<logic:notPresent name="inEnglish">		
		<div class="gen-button">
			<logic:present name="info" >
			<logic:equal name="info" value="description" >
			<img src="<%= request.getContextPath() %>/images/england-flag.gif" alt="Icon: English version!" width="16" height="12" />
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;info=description&amp;inEnglish=true&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>">
				<bean:message key="link.coordinator.degreeSite.editEnglish"/>
			</html:link>
			</logic:equal>
			</logic:present>
				
			<logic:present name="info" >
			<logic:equal name="info" value="acess" >			
			<img src="<%= request.getContextPath() %>/images/england-flag.gif" alt="Icon: English version!" width="16" height="12" />
			<html:link page="<%= "/degreeSiteManagement.do?method=viewInformation&amp;info=acess&amp;inEnglish=true&amp;degreeCurricularPlanID=" + degreeCurricularPlanID.toString()%>">
				<bean:message key="link.coordinator.degreeSite.editEnglish"/>
			</html:link>			
			</logic:equal>
			</logic:present>			
		</div>
	</logic:notPresent>
	<table>	
		<logic:notPresent name="inEnglish">	
				<logic:present name="info" >
				<logic:equal name="info" value="description" >
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.description"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="description" cols="80" rows="8"/></td>
				</tr>
						
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.objectives"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="objectives" cols="80" rows="8"/></td>
				</tr>
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.history"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="history" cols="80" rows="8"/></td>
				</tr>		

				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.professionalExits"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="professionalExits" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.additionalInfo"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="additionalInfo" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.links"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="links" cols="80" rows="8"/></td>
				</tr>	
				<html:hidden property="testIngression" />
				<html:hidden property="driftsInitial" />	
				<html:hidden property="driftsFirst" />	
				<html:hidden property="driftsSecond" />	
				<html:hidden property="classifications" />
				<html:hidden property="markMin" />	
				<html:hidden property="markMax" />	
				<html:hidden property="markAverage" />					
				</logic:equal>
				</logic:present>
				
				<logic:present name="info" >
				<logic:equal name="info" value="acess" >
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.testIngression"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="testIngression" cols="80" rows="3"/></td>
				</tr>	

				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.classifications"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="classifications" cols="80" rows="3"/></td>
				</tr>	
				
				<tr>
					<td>
						<table>	
							<tr>			
								<td>
									<p align="left"><strong><bean:message key="label.coordinator.degreeSite.drifts"/>:&nbsp;</strong></p>
								</td>								
								<td>
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.drifts.initial"/></strong></p>
								</td>
								<td>
									<html:text property="driftsInitial" size="5"/>
								</td>
								<td>
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.drifts.first"/></strong></p>
								</td>
								<td>
									<html:text property="driftsFirst" size="5"/>
								</td>
								<td>					
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.drifts.second"/></strong></p>
								</td>
								<td>
									<html:text property="driftsSecond" size="5"/>
								</td>
							</tr>	
						
							<tr>
								<td>
									<p align="left"><strong><bean:message key="label.coordinator.degreeSite.marks"/>:&nbsp;</strong></p>
								</td>							
								<td>
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.mark.min"/></strong></p>
								</td>
								<td>
									<html:text property="markMin" size="5"/>
								</td>	
								<td>
									<p align="right"><strong><bean:message key="label.coordinator.degreeSite.mark.max"/></strong></p>
								</td>
								<td>
								  <html:text property="markMax" size="5"/>
								</td>
								<td>
								  <p align="right"><strong><bean:message key="label.coordinator.degreeSite.mark.average"/></strong></p>
								</td>
								<td>
									<html:text property="markAverage" size="5"/>
							  </td>
						  </tr>
						  
					  </table>
				  </td>
				</tr>		
				<html:hidden property="description" />			
				<html:hidden property="objectives" />	
				<html:hidden property="history" />
				<html:hidden property="professionalExits" />
				<html:hidden property="additionalInfo" />
				<html:hidden property="links" />				
				</logic:equal>
				</logic:present>
				
				<html:hidden property="descriptionEn" />	
				<html:hidden property="objectivesEn" />	
				<html:hidden property="historyEn" />
				<html:hidden property="professionalExitsEn" />
				<html:hidden property="additionalInfoEn" />
				<html:hidden property="linksEn" />
				<html:hidden property="testIngressionEn" />
				<html:hidden property="classificationsEn" />
		</logic:notPresent>																																																
		<logic:present name="inEnglish">	
				<html:hidden property="description" />			
				<html:hidden property="objectives" />	
				<html:hidden property="history" />
				<html:hidden property="professionalExits" />
				<html:hidden property="additionalInfo" />
				<html:hidden property="links" />
				<html:hidden property="testIngression" />
				<html:hidden property="driftsInitial" />	
				<html:hidden property="driftsFirst" />	
				<html:hidden property="driftsSecond" />	
				<html:hidden property="classifications" />
				<html:hidden property="markMin" />	
				<html:hidden property="markMax" />	
				<html:hidden property="markAverage" />	

				<logic:present name="info" >
				<logic:equal name="info" value="description" >
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.description"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="descriptionEn" cols="80" rows="8"/></td>
				</tr>
										
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.objectives"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="objectivesEn" cols="80" rows="8"/></td>
				</tr>
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.history"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="historyEn" cols="80" rows="8"/></td>
				</tr>		

				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.professionalExits"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="professionalExitsEn" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.additionalInfo"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="additionalInfoEn" cols="80" rows="8"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.links"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="linksEn" cols="80" rows="8"/></td>
				</tr>	
				<html:hidden property="testIngressionEn" />
				<html:hidden property="classificationsEn" />				
				</logic:equal>
				</logic:present>
				
				<logic:present name="info" >
				<logic:equal name="info" value="acess" >				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.testIngression"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="testIngressionEn" cols="80" rows="3"/></td>
				</tr>	
				
				<tr>
					<td><strong><bean:message key="label.coordinator.degreeSite.classifications"/></strong></td>
				</tr>
				<tr>
					<td><html:textarea property="classificationsEn" cols="80" rows="3"/></td>
				</tr>			
				<html:hidden property="descriptionEn" />	
				<html:hidden property="objectivesEn" />	
				<html:hidden property="historyEn" />
				<html:hidden property="professionalExitsEn" />
				<html:hidden property="additionalInfoEn" />
				<html:hidden property="linksEn" />				
				</logic:equal>
				</logic:present>
		</logic:present>
	</table>
	
	</br></br>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>                    		         	
	</html:submit>       
	<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>  
	</br></br>
</html:form> 