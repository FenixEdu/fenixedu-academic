<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoStudent" %>
<%@ page import="DataBeans.InfoTeacher" %>
<%@ page import="DataBeans.InfoExternalPerson" %>

<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.change"/></h2>
<center>
<span class="error"><html:errors/></span>

<br/>

<html:form action="/changeMasterDegreeThesisLookup.do">
	<html:hidden property="studentNumber" value="<%= ((InfoStudent)student).getNumber().toString()%>" />
	<html:hidden property="degreeType" value="2"/>
	<html:hidden property="page" value="1"/>

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
				<bean:write name="student" property="infoPerson.nome"/>
			</td>			
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
	
		<!-- Dissertation Title -->
		<tr>
			<th align="left" colspan="2">
				<bean:message key="label.masterDegree.administrativeOffice.dissertationTitle"/>:&nbsp;
				<html:text property="dissertationTitle" size="45"/>
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
			<logic:present name="<%= SessionConstants.GUIDERS_LIST %>" scope="request">
				<bean:define id="guidersList" name="<%= SessionConstants.GUIDERS_LIST %>" type="java.util.List"/>
				<tr>
					<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherNumber"/></th>
					<th align="left" width="40%"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>
					<td width="30%">&nbsp;</td>
					<td>&nbsp;</td>						
				</tr>					
				<logic:iterate id="guider" name="guidersList">
					<html:hidden property="guidersNumbers" value="<%= ((InfoTeacher)guider).getTeacherNumber().toString() %>"/>
					<tr>
						<td align="left"><bean:write name="guider" property="teacherNumber"/></td>
						<td align="left"><bean:write name="guider" property="infoPerson.nome"/></td>
						<td>&nbsp;</td>
						<td align="center">
							<html:multibox property="removedGuidersNumbers">
								<bean:write name="guider" property="teacherNumber"/>
							</html:multibox>	
						</td>						
					</tr>				
				</logic:iterate>
				<tr>
					<td colspan="4" align="right">
						<html:submit styleClass="inputbuttonSmall" property="method">
							<bean:message key="button.submit.masterDegree.thesis.removeGuiders"/>
						</html:submit>
					</td>
				</tr>
			</logic:present >				
		<tr>
			<th align="left" colspan="4">
				<bean:message key="label.masterDegree.administrativeOffice.teacherNumber"/>:
				<html:text property="guidersNumbers" size="5" value="" />
				<html:submit styleClass="inputbuttonSmall" property="method">
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
				
		<logic:present name="<%= SessionConstants.EXTERNAL_GUIDERS_LIST %>" scope="request">
			<bean:define id="externalGuidersList" name="<%= SessionConstants.EXTERNAL_GUIDERS_LIST %>" type="java.util.List"/>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonWorkLocation"/></th>
				<td>&nbsp;</td>									
			</tr>			
			<logic:iterate id="externalGuider" name="externalGuidersList">
				<html:hidden property="externalGuidersIDs" value="<%= ((InfoExternalPerson)externalGuider).getIdInternal().toString() %>"/>
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalGuider" property="infoPerson.nome"/></td>
					<td align="left"><bean:write name="externalGuider" property="infoWorkLocation.name"/></td>
					<td align="center">
						<html:multibox property="removedExternalGuidersIDs">
							<bean:write name="externalGuider" property="idInternal"/>
						</html:multibox>	
					</td>						
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.removeExternalGuiders"/>
					</html:submit>
				</td>
			</tr>			
		</logic:present>		

		<logic:notPresent name="<%= SessionConstants.SEARCH_EXTERNAL_GUIDERS %>" scope="request">
			<logic:notPresent name="<%= SessionConstants.EXTERNAL_GUIDERS_SEARCH_RESULTS %>" scope="request">
				<tr>
					<td align="left" colspan="4">
						<html:submit styleClass="inputbuttonSmall" property="method">
							<bean:message key="button.submit.masterDegree.thesis.externalGuider"/>
						</html:submit>
					</td>
				</tr>
			</logic:notPresent>
		</logic:notPresent>
		
		<logic:present name="<%= SessionConstants.SEARCH_EXTERNAL_GUIDERS %>" scope="request">
			<tr><td colspan="4" >
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="infoop" >
							<span class="emphasis-box">info</span>
						</td>
						<td class="infoop">
							<strong>Nota:</strong> Na indicação do nome pode ser fornecido apenas parte do nome do orientador externo.<br/>
							Exemplo 1: Para selecionar todos os orientadores externos que começam com a letra "A" escreva <strong>A%</strong><br/>
							Exemplo 2: Para selecionar todos os orientadores externos que começam com a letra "A" e que tenham um segundo nome que começam com a letra "M" escreva <strong>A% M%</strong>
						</td>
					</tr>
				</table></td>	
			</tr>
			<tr>
				<td align="left" colspan="4">
					<bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/>:
					<input type="text" name="externalGuiderName" size="25" value=""/>
					<html:submit styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.searchExternalGuider"/>
					</html:submit>
				</td>
			</tr>
		</logic:present>
		<!-- External Person search results -->
		<logic:present name="<%= SessionConstants.EXTERNAL_GUIDERS_SEARCH_RESULTS %>" scope="request">
			<bean:define id="externalGuidersSearchResultsList" name="<%= SessionConstants.EXTERNAL_GUIDERS_SEARCH_RESULTS %>" type="java.util.List"/>
			<tr>
				<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.searchResults"/></th>				
			</tr>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonWorkLocation"/></th>
				<td>&nbsp;</td>									
			</tr>				
			<logic:iterate id="externalGuider" name="externalGuidersSearchResultsList">
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalGuider" property="infoPerson.nome"/></td>
					<td align="left"><bean:write name="externalGuider" property="infoWorkLocation.name"/></td>						
					<td>
						<html:radio idName="externalGuider" property="externalGuidersIDs" value="idInternal"/>	
					</td>
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit styleClass="inputbuttonSmall" property="method">
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
		
		<logic:present name="<%= SessionConstants.ASSISTENT_GUIDERS_LIST %>" scope="request">
			<bean:define id="assistentsGuidersList" name="<%= SessionConstants.ASSISTENT_GUIDERS_LIST %>" type="java.util.List"/>
			<tr>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherNumber"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>
				<td>&nbsp;</td>
				<td>&nbsp;</td>						
			</tr>
			<logic:iterate id="assistentGuider" name="assistentsGuidersList">
				<html:hidden property="assistentGuidersNumbers" value="<%= ((InfoTeacher)assistentGuider).getTeacherNumber().toString() %>"/>
				<tr>
					<td align="left"><bean:write name="assistentGuider" property="teacherNumber"/></td>
					<td align="left"><bean:write name="assistentGuider" property="infoPerson.nome"/></td>
					<td>&nbsp;</td>
					<td align="center">
						<html:multibox property="removedAssistentGuidersNumbers">
							<bean:write name="assistentGuider" property="teacherNumber"/>
						</html:multibox>	
					</td>						
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.removeAssistentGuiders"/>
					</html:submit>
				</td>
			</tr>			
		</logic:present>
		<tr>
			<th align="left" colspan="4">
				<bean:message key="label.masterDegree.administrativeOffice.teacherNumber"/>:
				<input type="text" name="assistentGuidersNumbers" size="5" value=""/>
				<html:submit styleClass="inputbuttonSmall" property="method">
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
				
		<logic:present name="<%= SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST %>" scope="request">
			<bean:define id="externalAssistentsGuidersList" name="<%= SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST %>" type="java.util.List"/>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonWorkLocation"/></th>
				<td>&nbsp;</td>									
			</tr>			
			<logic:iterate id="externalAssistentGuider" name="externalAssistentsGuidersList">
				<html:hidden property="externalAssistentGuidersIDs" value="<%= ((InfoExternalPerson)externalAssistentGuider).getIdInternal().toString() %>"/>
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalAssistentGuider" property="infoPerson.nome"/></td>
					<td align="left"><bean:write name="externalAssistentGuider" property="infoWorkLocation.name"/></td>
					<td align="center">
						<html:multibox property="removedExternalAssistentGuidersIDs">
							<bean:write name="externalAssistentGuider" property="idInternal"/>
						</html:multibox>	
					</td>						
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.removeExternalAssistentGuiders"/>
					</html:submit>
				</td>
			</tr>			
		</logic:present>		

		<logic:notPresent name="<%= SessionConstants.SEARCH_EXTERNAL_ASSISTENT_GUIDERS %>" scope="request">
			<logic:notPresent name="<%= SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_SEARCH_RESULTS %>" scope="request">
				<tr>
					<td align="left" colspan="4">
						<html:submit styleClass="inputbuttonSmall" property="method">
							<bean:message key="button.submit.masterDegree.thesis.externalAssitentGuider"/>
						</html:submit>
					</td>
				</tr>
			</logic:notPresent>
		</logic:notPresent>
		
		<logic:present name="<%= SessionConstants.SEARCH_EXTERNAL_ASSISTENT_GUIDERS %>" scope="request">
			<tr><td colspan="4" >
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="infoop" >
							<span class="emphasis-box">info</span>
						</td>
						<td class="infoop">
							<strong>Nota:</strong> Na indicação do nome pode ser fornecido apenas parte do nome do co-orientador externo.<br/>
							Exemplo 1: Para selecionar todos os co-orientadores externos que começam com a letra "A" escreva <strong>A%</strong><br/>
							Exemplo 2: Para selecionar todos os co-orientadores externos que começam com a letra "A" e que tenham um segundo nome que começa com a letra "M" escreva <strong>A% M%</strong>
						</td>
					</tr>
				</table></td>	
			</tr>
			<tr>
				<td align="left" colspan="4">
					<bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/>:
					<input type="text" name="externalAssistentGuiderName" size="25" value=""/>
					<html:submit styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.searchExternalAssitentGuider"/>
					</html:submit>
				</td>
			</tr>
		</logic:present>
		<!-- External Person search results -->
		<logic:present name="<%= SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_SEARCH_RESULTS %>" scope="request">
			<bean:define id="externalAssistentsGuidersSearchResultsList" name="<%= SessionConstants.EXTERNAL_ASSISTENT_GUIDERS_SEARCH_RESULTS %>" type="java.util.List"/>
			<tr>
				<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.searchResults"/></th>				
			</tr>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonWorkLocation"/></th>
				<td>&nbsp;</td>									
			</tr>				
			<logic:iterate id="externalAssistentGuider" name="externalAssistentsGuidersSearchResultsList">
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalAssistentGuider" property="infoPerson.nome"/></td>
					<td align="left"><bean:write name="externalAssistentGuider" property="infoWorkLocation.name"/></td>						
					<td>
						<html:radio idName="externalAssistentGuider" property="externalAssistentGuidersIDs" value="idInternal"/>	
					</td>
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit styleClass="inputbuttonSmall" property="method">
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
				<html:submit styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.submit.masterDegree.thesis.changeThesis"/>
				</html:submit>
				<html:submit styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.cancel"/>
				</html:submit>
			</td>

		</tr>
		
		
	</table>
</html:form>


</center>