<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<br />
<table width="100%">
	<tr>
		<td class="infoop">
			<bean:message key="label.bibliography.explanation" />
		</td>
	</tr>
</table>

<p>
<div class="gen-button">
	<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="" /> 
	<html:link page="<%= "/createBibliographicReference.do?method=prepareCreateBibliographicReference&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
		<bean:message key="label.insertBibliographicReference"/>                   		     
	</html:link>
</div>
</p>


<logic:present name="siteView">
<bean:define id="bodyComponent" name="siteView" property="component"/>
<bean:define id="bibliographicReferencesList" name="bodyComponent" property="bibliographicReferences"/>

<logic:empty name="bibliographicReferencesList">
	<bean:message key="message.bibliography.not.exists" />
</logic:empty>

<logic:notEmpty name="bibliographicReferencesList">
	<html:form action="/bibliographicReferenceManager">
	<html:hidden property="page" value="0"/>
	<br />            
	<h2><bean:message key="message.recommendedBibliography"/></h2>
	<table cellspacing="0" cellpadding="0">
	  	<tbody>
			<logic:iterate id="bibliographicReference" name="bibliographicReferencesList">
			<logic:notEqual name="bibliographicReference" property="optional" value="true">
				<tr>
					<td>
						<!--  <bean:message key="message.bibliographicReferenceTitle"/> -->
						<bean:write name="bibliographicReference" property="title" filter="false"/>
					</td>
				</tr>
				<tr>
					<td>
						<!--   <bean:message key="message.bibliographicReferenceAuthors"/> -->
						<bean:write name="bibliographicReference" property="authors" filter="false" />
					</td>
				</tr>
				<tr>
					   <td>
						   <!--   <bean:message key="message.bibliographicReferenceReference"/> -->
						   <bean:write name="bibliographicReference" property="reference"  filter="false" />
					   </td>
				</tr>
				<tr>
					<td>
						<!--    <bean:message key="message.bibliographicReferenceYear"/> -->
						<bean:write name="bibliographicReference" property="year" filter="false" />                            
						  <br />
						  <br />
					</td>
				</tr>
				<tr>
					<td>
						<div class="gen-button">
							<bean:define id="bibliographicReferenceCode" name="bibliographicReference" property="idInternal"/>
							<html:link page="<%= "/bibliographicReferenceManager.do?method=prepareEditBibliographicReference&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;bibliographicReferenceCode=" + bibliographicReferenceCode %>">
								<bean:message key="button.edit"/>                    		     
							</html:link>
						</div>                               	       
						<div class="gen-button">
							<html:link page="<%= "/bibliographicReferenceManager.do?method=deleteBibliographicReference&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;bibliographicReferenceCode=" + bibliographicReferenceCode %>" onclick="return confirm('Tem a certeza que deseja apagar esta referência bibliográfica ?')">
								<bean:message key="button.delete"/>                  		     
							</html:link>
						</div>
						<br />
					</td>
				</tr>
			</logic:notEqual>
	        </logic:iterate>
		</tbody>
	</table>
	<br />
	<h2><bean:message key="message.optionalBibliography"/></h2>
	<table cellspacing="0" cellpadding="0" border="0">
		<tbody>
	       	<logic:iterate id="bibliographicReference" name="bibliographicReferencesList">
	        <logic:notEqual name="bibliographicReference" property="optional" value="false">
	          	<tr>
	              	<td>
	              		<!--   <bean:message key="message.bibliographicReferenceTitle"/> -->
	              		<bean:write name="bibliographicReference" property="title" filter="false" />
	               	</td>
	          	</tr>
	           	<tr>
	               	<td>
	               		<!--    <bean:message key="message.bibliographicReferenceAuthors"/> -->
	               		<bean:write name="bibliographicReference" property="authors" filter="false" />
	               	</td>
	         	</tr>
	           	<tr>
	             	<td>
	             		<!--     <bean:message key="message.bibliographicReferenceReference"/> -->
	             		<bean:write name="bibliographicReference" property="reference" filter="false" />
	               	</td>
	          	</tr>
	            <tr>
	               	<td>
	               		<!--     <bean:message key="message.bibliographicReferenceYear" /> -->
	                    <bean:write name="bibliographicReference" property="year" filter="false" />                            
	               	</td>
	            </tr>
	            <tr>
	            	<td>     
						<div class="gen-button">
							<bean:define id="bibliographicReferenceCode" name="bibliographicReference" property="idInternal"/>
							<html:link page="<%= "/bibliographicReferenceManager.do?method=prepareEditBibliographicReference&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;bibliographicReferenceCode=" + bibliographicReferenceCode %>">
								<bean:message key="button.edit"/>                    		     
							</html:link>
						</div>                               	       
						<div class="gen-button">
							<html:link page="<%= "/bibliographicReferenceManager.do?method=deleteBibliographicReference&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;bibliographicReferenceCode=" + bibliographicReferenceCode %>" onclick="return confirm('Tem a certeza que deseja apagar esta referência bibliográfica ?')">
								<bean:message key="button.delete"/>                  		     
							</html:link>
						</div>
	                </td>
	            </tr>
	        </logic:notEqual>
	        </logic:iterate>
	    </tbody>
	</table>
	</html:form>
</logic:notEmpty>
</logic:present>