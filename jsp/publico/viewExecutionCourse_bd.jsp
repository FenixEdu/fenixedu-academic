<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:notPresent name="siteView">
<table align="center" cellpadding='0' cellspacing='0'>
    <tr align="center">
        <td>
            <span class="error">
                <bean:message key="errors.invalidSiteExecutionCourse"/>
            </span>
        </td>
    </tr>
</table>
</logic:notPresent>

<logic:present name="siteView">
    <logic:notPresent name="siteView" property="component">
        <table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<span class="error">
					     <bean:message key="message.public.notfound.executionCourse"/> 
					 </span>
				</td>
			</tr>
		</table>
    </logic:notPresent>


    <logic:present name="siteView" property="component">
        <bean:define id="component" name="siteView" property="component" />
	
        <logic:notEmpty name="component" property="initialStatement">
            <table align="center" cellspacing="0" width="90%">
             <tr>
               <td class="citation">
                 <p><bean:write name="component" property="initialStatement" filter="false"/></p>
               </td>
            </tr>
            </table>		
         <br/>
         <br/>
        </logic:notEmpty>
		
          <logic:notEmpty name="component" property="lastAnnouncement" >		
            <table id="anuncios" cellspacing="0" width="90%">
            	<tr>
                    <td  class="ultAnuncioAviso"> 
            		<img alt="" border="0"  src="<%= request.getContextPath() %>/images/icon_warning.gif"  />
            		<bean:message key="message.lastAnnouncement"/> 
                    </td>      
                </tr>
                
                <bean:define id="firstAnnouncement" name="component" property="lastAnnouncement"/>
                <tr>
                  <td class="ultAnuncio">
                   <img alt="" border="0"  src="<%= request.getContextPath() %>/images/icon_anuncio.gif"  />
	               <html:link  page="<%="/viewSite.do"+"?method=announcements&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
	               <bean:write name="firstAnnouncement" property="title"/>:
	               </html:link>	
	               <br/>
	               <br/>
	               <bean:write name="firstAnnouncement" property="information" filter="false"/>
	               <hr>
	              </td>
	            </tr>
	            	            
	            <!--<tr>		
	           	 <td class="ultAnuncio-date">	
	           	   <bean:message key="message.modifiedOn"/>
	           	   <dt:format pattern="dd-MM-yyyy HH:mm">
	           		 <bean:write name="firstAnnouncement" property="lastModifiedDate.time"/>
	           	   </dt:format>
	           	 </td>
	            </tr>-->   
	            
               <logic:notEmpty name="component" property="lastFiveAnnouncements" >		               
	             <tr>
	               <td class="ultAnuncio">
	                 <logic:iterate id="announcement" name="component" property="lastFiveAnnouncements" type="DataBeans.InfoAnnouncement">
	                 	<img alt="" border="0"  src="<%= request.getContextPath() %>/images/icon_anuncio.gif"  />
	                   	<html:link  page="<%="/viewSite.do"+"?method=announcements&amp;objectCode=" + pageContext.findAttribute("objectCode") %>">
	                   	<bean:write name="announcement" property="title"/>
	                   	</html:link>
	                   	<br/>	
	                 </logic:iterate>     	
	               </td>
	             </tr>
	           </logic:notEmpty>
	                      
             </table>
        </logic:notEmpty>
<br/>
<br/>
<br/>
<br/>
<br/>

    
     <logic:notEmpty 	name="component" property="alternativeSite" >	
            <h2><bean:message key="message.siteAddress" /></h2>
            <bean:define id="alternativeSite" name="component" property="alternativeSite"/>
            <html:link href="<%=(String)pageContext.findAttribute("alternativeSite") %>" target="_blank">
			<bean:write name="alternativeSite" />
            </html:link>
			<br/>
			<br/>
		
	</logic:notEmpty>			

   
     <logic:notEmpty name="component" property="introduction">
     	
        <h2><bean:message key="message.introduction" /></h2>
          <p><bean:write name="component" property="introduction" filter="false" /></p>
         <br/>
        <br/>
      </logic:notEmpty>
	
	
        <table>
            
        <logic:notEmpty name="component" property="responsibleTeachers">	
	
           
                <tr>
            	<td>
            		<h2><bean:message key="label.lecturingTeachers"/></h2>	
            	</td>
                </tr>	

            <logic:iterate id="infoResponsableTeacher" name="component" property="responsibleTeachers">
            	<tr>
            	<td>
				<bean:write name="infoResponsableTeacher" property="infoPerson.nome" /> <bean:message key="label.responsible"/>
                </td>
                </tr>
            </logic:iterate>	
        </logic:notEmpty>
        <logic:notEmpty name="component" property="lecturingTeachers" >	
             
            <logic:empty name="component" property="responsibleTeachers">	
	            
               
                <tr>
                <td>
                <h2><bean:message key="label.lecturingTeachers"/></h2>	
                </td>
                </tr>	
             </logic:empty>
            <logic:iterate id="infoTeacher" name="component" property="lecturingTeachers">
                <tr>
                <td>
				<bean:write name="infoTeacher" property="infoPerson.nome" /> 
                </td>
                </tr>
            </logic:iterate>	

        </logic:notEmpty>
     
         </table>
       
    </logic:notEmpty> 
   </logic:present>
</logic:present>