<%@ page language="java" %> 
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<logic:notPresent name="BibliographicReferences">
	<h2><bean:message key="message.bibliography.not.available"/></h2>
</logic:notPresent>
<logic:present name="BibliographicReferences" >
<logic:empty name="BibliographicReferences">
	<h2><bean:message key="message.bibliography.not.available"/></h2>
</logic:empty>	
<logic:notEmpty name="BibliographicReferences" >
<table >
	<tbody>
		<tr>
        	<td>                               
            	<h2>
                	<bean:message key="message.recommendedBibliography"/>
				</h2>
			</td>
		</tr>
            <logic:iterate id="bibliographicReference" name="BibliographicReferences">
                <logic:notEqual name="bibliographicReference" property="optional" value="true">
                    <tr>
                    	<td>
                    		<bean:message key="label.bibliography.title" />
                    	</td>
                        <td>                                               
                            <bean:write name="bibliographicReference" property="title"/>
                        </td>
                    </tr>
                    <tr>
                    	<td>
                    		<bean:message key="label.bibliography.authors" />
                    	</td>
                        <td>
                            <bean:write name="bibliographicReference" property="authors"/>
                        </td>
                    </tr>
                    <tr>
                    	<td>
                    		<bean:message key="label.bibliography.reference" />
                    	</td>
                        <td>
                            <bean:write name="bibliographicReference" property="reference"/>
                        </td>
                    </tr>
                    <tr>
                    	<td>
                    		<bean:message key="label.bibliography.year" />
                    	</td>
                        <td>
                            <bean:write name="bibliographicReference" property="year"/>                            
                        </td>
                    </tr>
                    <tr>
                    </tr>
                     <tr>
                    </tr>
                </logic:notEqual>
               
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
                    		<bean:message key="label.bibliography.title" />
                    	</td>
                        <td>                            
                            <bean:write name="bibliographicReference" property="title"/>
                        </td>
                    </tr>
                    <tr>
                    	<td>
                    		<bean:message key="label.bibliography.authors" />
                    	</td>
                        <td>
                            <bean:write name="bibliographicReference" property="authors"/>
                        </td>
                    </tr>
                    <tr>
                    	<td>
                    		<bean:message key="label.bibliography.reference" />
                    	</td>
                        <td>
                            <bean:write name="bibliographicReference" property="reference"/>
                        </td>
                    </tr>
                    <tr>
                    	<td>
                    		<bean:message key="label.bibliography.year" />
                    	</td>
                        <td>
                            <bean:write name="bibliographicReference" property="year"/>                            
                        </td>
                    </tr>
                      <tr>
                    </tr>
                     <tr>
                    </tr>
                </logic:notEqual>
            </logic:iterate>
        </tbody>
</table>
</logic:notEmpty>
</logic:present>