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
<html:form action="/bibliographicReferenceManager">
<html:hidden property="page" value="0"/>
<p><div class="gen-button"><img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="" /> <html:link page="/bibliographicReferenceManager.do?method=prepareEditBibliographicReference"><bean:message key="label.insertBibliographicReference"/>                   		     
</html:link></div>
</p>
<br />            
<h2><bean:message key="message.recommendedBibliography"/></h2>
<table cellspacing="0" cellpadding="0">
  		<tbody>
            <tr>
            	<% int index = 0; %>             
           		<logic:iterate id="bibliographicReference" name="BibliographicReferences">
                <logic:notEqual name="bibliographicReference" property="optional" value="true">
            </tr>
            <tr>
            </tr>
              	<td><!--  <bean:message key="message.bibliographicReferenceTitle"/> --><bean:write name="bibliographicReference" property="title"/>
               	</td>
          	</tr>
           	<tr>
             	<td><!--   <bean:message key="message.bibliographicReferenceAuthors"/> --><bean:write name="bibliographicReference" property="authors"/>
             	</td>
          	</tr>
          	<tr>
              	<td><!--   <bean:message key="message.bibliographicReferenceReference"/> --><bean:write name="bibliographicReference" property="reference"/>
              	</td>
            </tr>
            <tr>
               	<td><!--    <bean:message key="message.bibliographicReferenceYear"/> --><bean:write name="bibliographicReference" property="year"/>                            
              	<br />
              	<br />
              	</td>
          	</tr>
          	<tr>
            	<td><div class="gen-button"><html:link page="/bibliographicReferenceManager.do?method=prepareEditBibliographicReference" indexId="index" indexed="true">			
                   	<bean:message key="button.edit"/>                    		     
                    </html:link></div>                               	       
                 	<div class="gen-button"><html:link page="/bibliographicReferenceManager.do?method=deleteBibliographicReference" indexId="index" indexed="true">			
                    <bean:message key="button.delete"/>                  		     
                    </html:link></div>
    	            <br />
               	</td>
           	</tr>
           	</logic:notEqual>
           	<% index++; %>	
            </logic:iterate>
		</tbody>
</table>
<br />
<h2><bean:message key="message.optionalBibliography"/></h2>
<table cellspacing="0" cellpadding="0" border="0">
		<tbody>
             	<logic:iterate id="bibliographicReference" name="BibliographicReferences">
                <logic:notEqual name="bibliographicReference" property="optional" value="false">
          	<tr>
              	<td><!--   <bean:message key="message.bibliographicReferenceTitle"/> --><bean:write name="bibliographicReference" property="title"/>
               	</td>
          	</tr>
           	<tr>
               	<td><!--    <bean:message key="message.bibliographicReferenceAuthors"/> --><bean:write name="bibliographicReference" property="authors"/>
               	</td>
         	</tr>
           	<tr>
             	<td><!--     <bean:message key="message.bibliographicReferenceReference"/> --><bean:write name="bibliographicReference" property="reference"/>
               	</td>
          	</tr>
            <tr>
               	<td><!--     <bean:message key="message.bibliographicReferenceYear"/> -->
                            <bean:write name="bibliographicReference" property="year"/>                            
               	</td>
                    </tr>
                    <tr>
                        <td>     
                  			<div class="gen-button"><html:link page="/bibliographicReferenceManager.do?method=prepareEditBibliographicReference" indexId="index" indexed="true">			
                            <bean:message key="button.edit"/>                   		     
                            </html:link></div>                               	       
                  			<div class="gen-button"><html:link page="/bibliographicReferenceManager.do?method=deleteBibliographicReference" indexId="index" indexed="true">			
                           	<bean:message key="button.delete"/>             		     
                            </html:link></div>                                	       
                        </td>
                    </tr>
                </logic:notEqual>
                <% index++; %>	
            </logic:iterate>
        </tbody>
    </html:form>
</table>





