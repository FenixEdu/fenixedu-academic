<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %> 
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %> 
<logic:present name="equivalency" >
	<logic:present name="selectedCases" >
		<logic:present name="unselectedCases" >
			<logic:present name="hiddenSelectedCases" >
				<bean:define id="equivalency" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoEquivalency" scope="request" name="equivalency"/>
				<bean:define id="selectedCases" type="java.util.List" scope="request" name="selectedCases"/>
				<bean:define id="unselectedCases" type="java.util.List" scope="request" name="unselectedCases"/>
				<bean:define id="hiddenSelectedCases" type="java.util.List" scope="request" name="hiddenSelectedCases"/>
				<bean:size id="selectedCasesSize" scope="request" name="selectedCases"/>
				<h2><bean:message key="label.candidacyFormTitle"/></h2>
				<h2 class="redtxt">Informa��o:</h2><p><bean:message key="message.seminaries.selectCaseStudy"/></p>
				<html:form action="manageCaseStudyChoices.do" method="post">
				<table width="100%" align="center" border="0">
					<tr>
						<td width="45%" align="left">
							<b><bean:message key="message.seminaries.avaliableCases"/></b><br/>
							<html:select bundle="HTMLALT_RESOURCES" altKey="select.selectedCases" style="width:100%" size = "7" multiple="true" property="selectedCases">
						
								<html:options collection="unselectedCases" property="idInternal" labelProperty="name"/>
							
							 </html:select>
						</td>
					</tr>
					<tr>
						<td width="10%" align="center">
							<logic:lessThan name="selectedCasesSize" value="5">
								<tr>
									<td>
										<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submition" styleClass="button" style="width:100%" value="Adicionar" property="submition"/>
									</td>
								</tr>
							</logic:lessThan>
						</td>
					</tr>
					<tr>
						<td width="45%" align="left">
							<br/><br/>
							<b><bean:message key="message.seminaries.selectedCases"/></b><br/>
							<html:select bundle="HTMLALT_RESOURCES" altKey="select.unselectedCases" style="width:100%" size = "5" multiple="true" property="unselectedCases">
				
								<html:options collection="selectedCases" property="idInternal" labelProperty="name"/>
					
							</html:select>
						</td>
					</tr>
					<tr>
						<td>
							<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submition" styleClass="button" style="width:100%" value="Remover" property="submition"/>
						</td>
					</tr>
					<logic:equal name="selectedCasesSize" value="5">
						<tr>
							<td>
								<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submition" styleClass="button" value="Submeter" property="submition"/>			
							</td>
						</tr>
					</logic:equal>
					<tr>
						<td>
							<input alt="input.equivalencyID" type="hidden" name="equivalencyID" value="<%= equivalency.getIdInternal()%>">			
						</td>
					</tr>
					<tr>
						<td>
							<input alt="input.themeID" type="hidden" name="themeID" value="<%=request.getParameter("themeID")%>">			
						</td>
					</tr>
					<tr>
						<td>
							<input alt="input.motivation" type="hidden" name="motivation" value='<%=request.getParameter("motivation")%>'>			
						</td>
					</tr>
					<tr>
						<td>
							<logic:iterate id="selectedCase" type="net.sourceforge.fenixedu.dataTransferObject.Seminaries.InfoCaseStudy" name="hiddenSelectedCases">
								<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.hiddenSelectedCases" property="hiddenSelectedCases" value="<%=selectedCase.getIdInternal().toString()%>"/>
							</logic:iterate>
						</td>
					</tr>
				</table>
				</html:form>


					</logic:present>
			</logic:present>
	</logic:present>
</logic:present>
<br/>
<br/>
<html:errors/>