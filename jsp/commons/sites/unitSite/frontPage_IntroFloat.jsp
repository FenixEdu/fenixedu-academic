<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:xhtml/>

<logic:equal name="site" property="showBanner" value="true">

<bean:define id="style" value="width: 100%; float: left; background-color: #019AD7; background-image: url(http://www.ist.utl.pt/img/spot/bolonha/bolonha_bck.gif); background-repeat: repeat-x;" toScope="request"/>

<logic:notEmpty name="site" property="banners">
	 <bean:define id="banner" name="site" property="currentBanner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner" toScope="request"/>
	 <bean:define id="style" type="java.lang.String" value="<%= "width: 100%; float: left; " +  ((banner.getColor()!=null) ? "background-color: " + banner.getColor() + ";" : "") + (banner.hasBackgroundImage() ? " background-image: url('" + banner.getBackgroundImage().getDownloadUrl() +"'); background-repeat: repeat-x;" : "") %>" toScope="request"/>
 </logic:notEmpty>

<div class="usitebanner" style="<fr:view name="style"/>">
	<logic:equal name="site" property="showFlags" value="true">
		<div id="version" style="width: 100px; position: absolute; top: 0; right: 0;">
			<html:form action="/changeLocaleTo.do">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.windowLocation" property="windowLocation" value=""/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newLanguage" property="newLanguage" value=""/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newCountry" property="newCountry" value=""/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.newVariant" property="newVariant" value=""/>
				 
	            <logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="pt">
					<input type="image" src="<%= request.getContextPath() %>/images/flags/pt.gif" alt="<bean:message key="pt" bundle="IMAGE_RESOURCES" />" title="Português" value="PT"
					 onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();" />
					<input class="activeflag" type="image" src="<%= request.getContextPath() %>/images/flags/en.gif" alt="English" title="English" value="EN" 
					onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();"/>
				</logic:notEqual>
					
	            <logic:notEqual name="<%= org.apache.struts.Globals.LOCALE_KEY %>" property="language" value="en">			
					<input class="activeflag" type="image" src="<%= request.getContextPath() %>/images/flags/pt.gif" alt="Português" title="Português" value="PT"
					 onclick="this.form.newLanguage.value='pt';this.form.newCountry.value='PT';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();" />
					<input type="image" src="<%= request.getContextPath() %>/images/flags/en.gif" alt="English" title="English" value="EN" 
					onclick="this.form.newLanguage.value='en';this.form.newCountry.value='EN';this.form.newVariant.value='<%= net.sourceforge.fenixedu._development.PropertiesManager.getProperty("variant") %>';this.form.windowLocation.value=window.location;this.form.submit();"/>
				</logic:notEqual>
			
			</html:form>
		</div>
	</logic:equal>

	<logic:equal name="site" property="showIntroduction" value="true">
		<logic:present name="site" property="description">
			<div class="usiteintrofloated">
				<fr:view name="site"  property="description" layout="html"/>
			</div>
		</logic:present>
		<logic:notPresent name="site" property="description">
			<div class="usiteintrofloated" style="background-color: #eeeeee; height: 150px;">

			</div>
		</logic:notPresent>
	</logic:equal>
	
	<logic:present name="banner" >
		<bean:define id="banner" name="banner" type="net.sourceforge.fenixedu.domain.UnitSiteBanner"/>
		<img src="<%= banner.getMainImage().getDownloadUrl() %>"/>
	</logic:present>
	<logic:notPresent name="banner">
		<a href="http://www.bolonha.ist.eu"><img src="http://www.ist.utl.pt/img/spot/bolonha/bolonha.gif" alt="Com o IST, entra no melhor ensino superior europeu - www.bolonha.ist.eu" width="420" height="150" />
	</logic:notPresent>
</div>
<div style="clear: both;"></div>

</logic:equal>

<logic:notEqual name="site" property="showBanner" value="true">
	<logic:equal name="site" property="showIntroduction" value="true">
	<logic:present name="site" property="description">
		<div class="usiteintro">
			<fr:view name="site"  property="description" layout="html"/>
		</div>
	</logic:present>
	<logic:notPresent name="site" property="description">
		<div style="background-color: #eeeeee; height: 150px;">

		</div>
	</logic:notPresent>
	</logic:equal>
</logic:notEqual>

<jsp:include flush="true" page="mainBody.jsp"/>
