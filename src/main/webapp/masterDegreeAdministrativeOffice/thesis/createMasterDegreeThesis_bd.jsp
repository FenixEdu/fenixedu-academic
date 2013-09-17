<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.student.Registration" %>
<%@ page import="net.sourceforge.fenixedu.domain.Teacher" %>
<%@ page import="net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract" %>

<bean:define id="student" name="<%= PresentationConstants.STUDENT %>" scope="request"/>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.create"/></h2>
<center>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<br/>

<html:form action="/createMasterDegreeThesisLookup.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="scpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" value="MASTER_DEGREE"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<table border="0" width="100%" cellspacing="3" cellpadding="10">
		<tr>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentName"/></th>
		</tr>
	
		<tr>
			<td align="left">
				<bean:write name="student" property="number"/>
			</td>
			<td align="left">
				<bean:write name="student" property="person.name"/>
			</td>			
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
	
		<!-- Dissertation Title -->
		<tr>
			<th align="left" colspan="2">
				<bean:message key="label.masterDegree.administrativeOffice.dissertationTitle"/>:&nbsp;
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.dissertationTitle" property="dissertationTitle" size="45"/>
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
		
	</table>
	
	
	<table border="0" width="100%" cellspacing="3" cellpadding="10">
		<!-- Guiders -->
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.guiders"/></th>				
		</tr>
			<logic:present name="<%= PresentationConstants.GUIDERS_LIST %>" scope="request">
				<bean:define id="guidersList" name="<%= PresentationConstants.GUIDERS_LIST %>" type="java.util.List"/>
				<tr>
					<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherId"/></th>
					<th align="left" width="40%"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>
					<td width="30%">&nbsp;</td>
					<td>&nbsp;</td>						
				</tr>					
				<logic:iterate id="guider" name="guidersList">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.guidersNumbers" property="guidersNumbers" value="<%= ((Teacher)guider).getTeacherId().toString() %>"/>
					<tr>
						<td align="left"><bean:write name="guider" property="teacherId"/></td>
						<td align="left"><bean:write name="guider" property="person.name"/></td>
						<td>&nbsp;</td>
						<td align="center">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.removedGuidersNumbers" property="removedGuidersNumbers">
								<bean:write name="guider" property="teacherId"/>
							</html:multibox>	
						</td>						
					</tr>				
				</logic:iterate>
				<tr>
					<td colspan="4" align="right">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
							<bean:message key="button.submit.masterDegree.thesis.removeGuiders"/>
						</html:submit>
					</td>
				</tr>
			</logic:present >				
		<tr>
			<th align="left" colspan="4">
				<bean:message key="label.masterDegree.administrativeOffice.teacherId"/>:
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.guidersNumbers" property="guidersNumbers" size="5" value="" />
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.submit.masterDegree.thesis.addGuider"/>
				</html:submit>
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
		
		
		<!-- External Guiders -->
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.externalGuiders"/></th>				
		</tr>
				
		<logic:present name="<%= PresentationConstants.EXTERNAL_GUIDERS_LIST %>" scope="request">
			<bean:define id="externalGuidersList" name="<%= PresentationConstants.EXTERNAL_GUIDERS_LIST %>" type="java.util.List"/>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>
				<td>&nbsp;</td>									
			</tr>			
			<logic:iterate id="externalGuider" name="externalGuidersList">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalGuidersIDs" property="externalGuidersIDs" value="<%= ((ExternalContract)externalGuider).getExternalId().toString() %>"/>
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalGuider" property="person.name"/></td>
					<td align="left"><bean:write name="externalGuider" property="institutionUnit.name"/></td>
					<td align="center">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.removedExternalGuidersIDs" property="removedExternalGuidersIDs">
							<bean:write name="externalGuider" property="externalId"/>
						</html:multibox>	
					</td>						
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.removeExternalGuiders"/>
					</html:submit>
				</td>
			</tr>			
		</logic:present>		

		<logic:notPresent name="<%= PresentationConstants.SEARCH_EXTERNAL_GUIDERS %>" scope="request">
			<logic:notPresent name="<%= PresentationConstants.EXTERNAL_GUIDERS_SEARCH_RESULTS %>" scope="request">
				<tr>
					<td align="left" colspan="4">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
							<bean:message key="button.submit.masterDegree.thesis.externalGuider"/>
						</html:submit>
					</td>
				</tr>
			</logic:notPresent>
		</logic:notPresent>
		
		<logic:present name="<%= PresentationConstants.SEARCH_EXTERNAL_GUIDERS %>" scope="request">
			<tr><td colspan="4" >
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="infoop" >
							<span class="emphasis-box">info</span>
						</td>
						<td class="infoop">
							<strong>Nota:</strong> Na indicaçãodo nome pode ser fornecido apenas parte do nome do orientador externo.<br/>
							Exemplo 1: Para selecionar todos os orientadores externos que começam com a letra "A" escreva <strong>A%</strong><br/>
							Exemplo 2: Para selecionar todos os orientadores externos que começam com a letra "A" e que tenham um segundo nome que começam com a letra "M" escreva <strong>A% M%</strong>
						</td>
					</tr>
				</table></td>	
			</tr>
			<tr>
				<td align="left" colspan="4">
					<bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/>:
					<input alt="input.externalGuiderName" type="text" name="externalGuiderName" size="25" value=""/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.searchExternalGuider"/>
					</html:submit>
				</td>
			</tr>
		</logic:present>
		<!-- External Person search results -->
		<logic:present name="<%= PresentationConstants.EXTERNAL_GUIDERS_SEARCH_RESULTS %>" scope="request">
			<bean:define id="externalGuidersSearchResultsList" name="<%= PresentationConstants.EXTERNAL_GUIDERS_SEARCH_RESULTS %>" type="java.util.List"/>
			<tr>
				<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.searchResults"/></th>				
			</tr>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>
				<td>&nbsp;</td>									
			</tr>				
			<logic:iterate id="externalGuider" name="externalGuidersSearchResultsList">
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalGuider" property="person.name"/></td>
					<td align="left"><bean:write name="externalGuider" property="institutionUnit.name"/></td>						
					<td>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.externalGuidersIDs" idName="externalGuider" property="externalGuidersIDs" value="externalId"/>	
					</td>
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.addExternalGuider"/>
					</html:submit>
				</td>
			</tr>
			<tr> 
				<td>&nbsp;</td>
			</tr>	
		</logic:present>
		
		
		
		<!-- Assistent Guiders -->
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.assistentGuiders"/></th>				
		</tr>
		
		<logic:present name="<%= PresentationConstants.ASSISTENT_GUIDERS_LIST %>" scope="request">
			<bean:define id="assistentsGuidersList" name="<%= PresentationConstants.ASSISTENT_GUIDERS_LIST %>" type="java.util.List"/>
			<tr>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherId"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>
				<td>&nbsp;</td>
				<td>&nbsp;</td>						
			</tr>
			<logic:iterate id="assistentGuider" name="assistentsGuidersList">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.assistentGuidersNumbers" property="assistentGuidersNumbers" value="<%= ((Teacher)assistentGuider).getTeacherId().toString() %>"/>
				<tr>
					<td align="left"><bean:write name="assistentGuider" property="teacherId"/></td>
					<td align="left"><bean:write name="assistentGuider" property="person.name"/></td>
					<td>&nbsp;</td>
					<td align="center">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.removedAssistentGuidersNumbers" property="removedAssistentGuidersNumbers">
							<bean:write name="assistentGuider" property="teacherId"/>
						</html:multibox>	
					</td>						
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.removeAssistentGuiders"/>
					</html:submit>
				</td>
			</tr>			
		</logic:present>
		<tr>
			<th align="left" colspan="4">
				<bean:message key="label.masterDegree.administrativeOffice.teacherId"/>:
				<input alt="input.assistentGuidersNumbers" type="text" name="assistentGuidersNumbers" size="5" value=""/>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.submit.masterDegree.thesis.addAssistentGuider"/>
				</html:submit>
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>		

		<!-- External Assistent Guiders -->
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.externalAssistentGuiders"/></th>				
		</tr>
				
		<logic:present name="<%= PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST %>" scope="request">
			<bean:define id="externalAssistentsGuidersList" name="<%= PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST %>" type="java.util.List"/>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>
				<td>&nbsp;</td>									
			</tr>			
			<logic:iterate id="externalAssistentGuider" name="externalAssistentsGuidersList">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalAssistentGuidersIDs" property="externalAssistentGuidersIDs" value="<%= ((ExternalContract)externalAssistentGuider).getExternalId().toString() %>"/>
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalAssistentGuider" property="person.name"/></td>
					<td align="left"><bean:write name="externalAssistentGuider" property="institutionUnit.name"/></td>
					<td align="center">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.removedExternalAssistentGuidersIDs" property="removedExternalAssistentGuidersIDs">
							<bean:write name="externalAssistentGuider" property="externalId"/>
						</html:multibox>	
					</td>						
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.removeExternalAssistentGuiders"/>
					</html:submit>
				</td>
			</tr>			
		</logic:present>		

		<logic:notPresent name="<%= PresentationConstants.SEARCH_EXTERNAL_ASSISTENT_GUIDERS %>" scope="request">
			<logic:notPresent name="<%= PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_SEARCH_RESULTS %>" scope="request">
				<tr>
					<td align="left" colspan="4">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
							<bean:message key="button.submit.masterDegree.thesis.externalAssitentGuider"/>
						</html:submit>
					</td>
				</tr>
			</logic:notPresent>
		</logic:notPresent>
		
		<logic:present name="<%= PresentationConstants.SEARCH_EXTERNAL_ASSISTENT_GUIDERS %>" scope="request">
			<tr><td colspan="4" >
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="infoop" >
							<span class="emphasis-box">info</span>
						</td>
						<td class="infoop">
							<strong>Nota:</strong> Na indicaçãodo nome pode ser fornecido apenas parte do nome do co-orientador externo.<br/>
							Exemplo 1: Para selecionar todos os co-orientadores externos que começam com a letra "A" escreva <strong>A%</strong><br/>
							Exemplo 2: Para selecionar todos os co-orientadores externos que começam com a letra "A" e que tenham um segundo nome que começam com a letra "M" escreva <strong>A% M%</strong>
						</td>
					</tr>
				</table></td>	
			</tr>
			<tr>
				<td align="left" colspan="4">
					<bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/>:
					<input alt="input.externalAssistentGuiderName" type="text" name="externalAssistentGuiderName" size="25" value=""/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.searchExternalAssitentGuider"/>
					</html:submit>
				</td>
			</tr>
		</logic:present>
		<!-- External Person search results -->
		<logic:present name="<%= PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_SEARCH_RESULTS %>" scope="request">
			<bean:define id="externalAssistentsGuidersSearchResultsList" name="<%= PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_SEARCH_RESULTS %>" type="java.util.List"/>
			<tr>
				<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.searchResults"/></th>				
			</tr>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>
				<td>&nbsp;</td>									
			</tr>				
			<logic:iterate id="externalAssistentGuider" name="externalAssistentsGuidersSearchResultsList">
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalAssistentGuider" property="person.name"/></td>
					<td align="left"><bean:write name="externalAssistentGuider" property="institutionUnit.name"/></td>						
					<td>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.externalAssistentGuidersIDs" idName="externalAssistentGuider" property="externalAssistentGuidersIDs" value="externalId"/>	
					</td>
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.addExternalAssistentGuider"/>
					</html:submit>
				</td>
			</tr>

		</logic:present>
		
		<tr>
			<tr> 
				<td>&nbsp;</td>
			</tr>			
			<td colspan="4" align="center">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.submit.masterDegree.thesis.createThesis"/>
				</html:submit>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.cancel"/>
				</html:submit>
			</td>

		</tr>
		
		
	</table>
</html:form>


</center>