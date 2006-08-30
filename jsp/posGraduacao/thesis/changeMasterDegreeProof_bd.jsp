<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudent" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoExternalPerson" %>


<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>
<bean:define id="dissertationTitleFromRequest" name="<%= SessionConstants.DISSERTATION_TITLE %>" />
		
<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.changeProof"/></h2>
<center>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<br/>

<html:form action="/changeMasterDegreeProofLookup.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentNumber" property="studentNumber" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />


	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dissertationTitle" property="dissertationTitle" />

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
				<bean:write name="dissertationTitleFromRequest"/>
				
				
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
		
	</table>
	
	<table cellspacing="3" cellpadding="10"  >					
		<tr >
			<td align="left">&nbsp;</td>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.day"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.month"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.year"/></th>
		</tr>	
		
		<!-- Proof Date -->
		<tr >
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.proofDate"/>:&nbsp;</th>
			<th>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.proofDateDay" property="proofDateDay">
			    	<html:options collection="<%= SessionConstants.DAYS_LIST %>" property="value" labelProperty="label" />
			   </html:select> 
			</th>
			<th>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.proofDateMonth" property="proofDateMonth">
			    	<html:options collection="<%= SessionConstants.MONTHS_LIST %>" property="value" labelProperty="label" />
			   </html:select> 
			</th>
			<th>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.proofDateYear" property="proofDateYear">
			    	<html:options collection="<%= SessionConstants.YEARS_LIST %>" property="value" labelProperty="label" />
			   </html:select> 
			</th>						

		</tr>
		
		<!-- Thesis Delivery Date -->
		<tr >
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.thesisDeliveryDate"/>:&nbsp;</th>
			<th>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.thesisDeliveryDateDay" property="thesisDeliveryDateDay">
			    	<html:options collection="<%= SessionConstants.DAYS_LIST %>" property="value" labelProperty="label" />
			   </html:select> 
			</th>
			<th>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.thesisDeliveryDateMonth" property="thesisDeliveryDateMonth">
			    	<html:options collection="<%= SessionConstants.MONTHS_LIST %>" property="value" labelProperty="label" />
			   </html:select> 
			</th>
			<th>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.thesisDeliveryDateYear" property="thesisDeliveryDateYear">
			    	<html:options collection="<%= SessionConstants.YEARS_LIST %>" property="value" labelProperty="label" />
			   </html:select> 
			</th>	
		</tr>
			
	</table>				
	
	<br/>
		
	<table border="0" width="100%" cellspacing="3" cellpadding="10">
	
	<!-- Final Result -->
		<tr>	
			<th align="left" colspan="3">
				<bean:message key="label.masterDegree.administrativeOffice.finalResult"/>:&nbsp;
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.masterDegree.MasterDegreeClassification" bundle="ENUMERATION_RESOURCES"/>	            
			    <html:select bundle="HTMLALT_RESOURCES" altKey="select.finalResult" property="finalResult">
			    	<html:option key="dropDown.Default" value=""/>
			    	<html:options collection="values" property="value" labelProperty="label" />
			   </html:select> 
			</th>
		</tr>
					
		

		
		<!-- Attached Copies Number -->
		<tr>
			<th align="left" colspan="4">
				<bean:message key="label.masterDegree.administrativeOffice.attachedCopiesNumber"/>:&nbsp;
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.attachedCopiesNumber" property="attachedCopiesNumber" size="5"/>
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
				
		<!-- Juries -->
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.juries"/></th>				
		</tr>
			<logic:present name="<%= SessionConstants.JURIES_LIST %>" scope="request">
				<bean:define id="juriesList" name="<%= SessionConstants.JURIES_LIST %>" type="java.util.List"/>
				<tr>
					<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherNumber"/></th>
					<th align="left" width="40%"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>
					<td width="30%">&nbsp;</td>
					<td>&nbsp;</td>						
				</tr>					
				<logic:iterate id="jury" name="juriesList">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.juriesNumbers" property="juriesNumbers" value="<%= ((InfoTeacher)jury).getTeacherNumber().toString() %>"/>
					<tr>
						<td align="left"><bean:write name="jury" property="teacherNumber"/></td>
						<td align="left"><bean:write name="jury" property="infoPerson.nome"/></td>
						<td>&nbsp;</td>
						<td align="center">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.removedJuriesNumbers" property="removedJuriesNumbers">
								<bean:write name="jury" property="teacherNumber"/>
							</html:multibox>	
						</td>						
					</tr>				
				</logic:iterate>
				<tr>
					<td colspan="4" align="right">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
							<bean:message key="button.submit.masterDegree.thesis.removeJuries"/>
						</html:submit>
					</td>
				</tr>
			</logic:present >				
		<tr>
			<th align="left" colspan="4">
				<bean:message key="label.masterDegree.administrativeOffice.teacherNumber"/>:
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.juriesNumbers" property="juriesNumbers" size="5" value="" />
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.submit.masterDegree.thesis.addJury"/>
				</html:submit>
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
		
		
		
		<!-- External Juries -->
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.externalJuries"/></th>				
		</tr>
				
		<logic:present name="<%= SessionConstants.EXTERNAL_JURIES_LIST %>" scope="request">
			<bean:define id="externalJuriesList" name="<%= SessionConstants.EXTERNAL_JURIES_LIST %>" type="java.util.List"/>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>
				<td>&nbsp;</td>									
			</tr>			
			<logic:iterate id="externalJury" name="externalJuriesList">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.externalJuriesIDs" property="externalJuriesIDs" value="<%= ((InfoExternalPerson)externalJury).getIdInternal().toString() %>"/>
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalJury" property="infoPerson.nome"/></td>
					<td align="left">
						<logic:notEmpty name="externalJury" property="infoInstitution" >
							<bean:write name="externalJury" property="infoInstitution.name"/>
						</logic:notEmpty>&nbsp;
					</td>
					<td align="center">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.removedExternalJuriesIDs" property="removedExternalJuriesIDs">
							<bean:write name="externalJury" property="idInternal"/>
						</html:multibox>	
					</td>						
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.removeExternalJuries"/>
					</html:submit>
				</td>
			</tr>			
		</logic:present>		

		<logic:notPresent name="<%= SessionConstants.SEARCH_EXTERNAL_JURIES %>" scope="request">
			<logic:notPresent name="<%= SessionConstants.EXTERNAL_JURIES_SEARCH_RESULTS %>" scope="request">
				<tr>
					<td align="left" colspan="4">
						<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
							<bean:message key="button.submit.masterDegree.thesis.externalJury"/>
						</html:submit>
					</td>
				</tr>
			</logic:notPresent>
		</logic:notPresent>
		
		<logic:present name="<%= SessionConstants.SEARCH_EXTERNAL_JURIES %>" scope="request">
			<tr><td colspan="4" >
				<table border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td class="infoop" >
							<span class="emphasis-box">info</span>
						</td>
						<td class="infoop">
							<strong>Nota:</strong> Na indicaçãodo nome pode ser fornecido apenas parte do nome do júri externo.<br/>
							Exemplo 1: Para selecionar todos os júris externos que começam com a letra "A" escreva <strong>A%</strong><br/>
							Exemplo 2: Para selecionar todos os júris externos que começam com a letra "A" e que tenham um segundo nome que começam com a letra "M" escreva <strong>A% M%</strong>
						</td>
					</tr>
				</table></td>	
			</tr>
			<tr>
				<td align="left" colspan="4">
					<bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/>:
					<input alt="input.externalJuryName" type="text" name="externalJuryName" size="25" value=""/>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.searchExternalJury"/>
					</html:submit>
				</td>
			</tr>
		</logic:present>
		<!-- External Person search results -->
		<logic:present name="<%= SessionConstants.EXTERNAL_JURIES_SEARCH_RESULTS %>" scope="request">
			<bean:define id="externalJuriesSearchResultsList" name="<%= SessionConstants.EXTERNAL_JURIES_SEARCH_RESULTS %>" type="java.util.List"/>
			<tr>
				<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.searchResults"/></th>				
			</tr>
			<tr>
				<td>&nbsp;</td>	
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>
				<td>&nbsp;</td>									
			</tr>				
			<logic:iterate id="externalJury" name="externalJuriesSearchResultsList">
				<tr>
					<td>&nbsp;</td>
					<td align="left"><bean:write name="externalJury" property="infoPerson.nome"/></td>
					<td align="left">
						<logic:notEmpty name="externalJury" property="infoInstitution" >
							<bean:write name="externalJury" property="infoInstitution.name"/>
						</logic:notEmpty>&nbsp;
					</td>						
					<td>
						<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.externalJuriesIDs" idName="externalJury" property="externalJuriesIDs" value="idInternal"/>	
					</td>
				</tr>				
			</logic:iterate>
			<tr>
				<td colspan="4" align="right">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
						<bean:message key="button.submit.masterDegree.thesis.addExternalJury"/>
					</html:submit>
				</td>
			</tr>

		</logic:present>
		
		
		
		
		<!-- confirmation -->
		<tr>		
			<td colspan="4" align="center">
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.submit.masterDegree.thesis.changeProof"/>
				</html:submit>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" styleClass="inputbuttonSmall" property="method">
					<bean:message key="button.cancel"/>
				</html:submit>
			</td>

		</tr>
		
		
	</table>
</html:form>


</center>