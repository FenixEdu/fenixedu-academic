<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<table border="0" style="text-align: left;">

    <html:form action="/bibliographicReferenceManager">
    	<html:hidden property="page" value="0"/>
        <tbody>
            <tr>
                <td>
                  	<html:link page="/bibliographicReferenceManager.do?method=prepareEditBibliographicReference">			
                         <bean:message key="label.insertBibliographicReference"/>                    		     
                    </html:link>                               	       
                    &nbsp;&nbsp;
                    <br><br><hr>
                </td>
            </tr>
            <tr>
                <td>
                    <br><h2>
                    	<bean:message key="message.recommendedBibliography"/>
                    </h2>
                </td>
            </tr>
            	<% int index = 0; %>             
            <logic:iterate id="bibliographicReference" name="BibliographicReferences">
                <logic:notEqual name="bibliographicReference" property="optional" value="true">
                    <tr>
                        <td>                                               
                          <!--  <bean:message key="message.bibliographicReferenceTitle"/> -->
                            <bean:write name="bibliographicReference" property="title"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                         <!--   <bean:message key="message.bibliographicReferenceAuthors"/> -->
                            <bean:write name="bibliographicReference" property="authors"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                         <!--   <bean:message key="message.bibliographicReferenceReference"/> -->
                            <bean:write name="bibliographicReference" property="reference"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <!--    <bean:message key="message.bibliographicReferenceYear"/> -->
                            <bean:write name="bibliographicReference" property="year"/>                            
                        </td>
                    </tr>
                    <tr>
                        <td>                                        	 	                        
       		  	  			<html:link page="/bibliographicReferenceManager.do?method=prepareEditBibliographicReference" indexId="index" indexed="true">			
                                  <bean:message key="button.edit"/>                    		     
                            </html:link>                               	       
                  			<html:link page="/bibliographicReferenceManager.do?method=deleteBibliographicReference" indexId="index" indexed="true">			
                                  <bean:message key="button.delete"/>                    		     
                            </html:link>                               	                       
	                    &nbsp;&nbsp;
    	                <br><hr>
                        </td>
                    </tr>
                </logic:notEqual>
               <% index++; %>	
            </logic:iterate>
            
            
            <tr>
                <td>
                    <h2>
                     	<bean:message key="message.optionalBibliography"/>
                    </h2>
                </td>
            </tr>           

             <logic:iterate id="bibliographicReference" name="BibliographicReferences">
                <logic:notEqual name="bibliographicReference" property="optional" value="false">
                    <tr>
                        <td>                            
                         <!--   <bean:message key="message.bibliographicReferenceTitle"/> -->
                            <bean:write name="bibliographicReference" property="title"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                        <!--    <bean:message key="message.bibliographicReferenceAuthors"/> -->
                            <bean:write name="bibliographicReference" property="authors"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                       <!--     <bean:message key="message.bibliographicReferenceReference"/> -->
                            <bean:write name="bibliographicReference" property="reference"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                       <!--     <bean:message key="message.bibliographicReferenceYear"/> -->
                            <bean:write name="bibliographicReference" property="year"/>                            
                        </td>
                    </tr>
                    <tr>
                        <td>     
                  			<html:link page="/bibliographicReferenceManager.do?method=prepareEditBibliographicReference" indexId="index" indexed="true">			
                                  <bean:message key="button.edit"/>                    		     
                            </html:link>                               	       
                  			<html:link page="/bibliographicReferenceManager.do?method=deleteBibliographicReference" indexId="index" indexed="true">			
                                  <bean:message key="button.delete"/>                    		     
                            </html:link>                               	       
                        </td>
                    </tr>
                </logic:notEqual>
                <% index++; %>	
            </logic:iterate>
        </tbody>
    </html:form>
</table>





