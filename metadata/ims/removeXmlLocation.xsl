<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:output method = "xml"  version="1.0" encoding="" doctype-system="build/app/WEB-INF/ims/imsmd2_rootv1p2.dtd"/>
   <xsl:param name="xmlDocument"/>

   <xsl:template match="lom/ist/members/location[.=$xmlDocument]">
   </xsl:template>
   
    <xsl:template match="/|*">	
	<xsl:copy>
	  <xsl:for-each select="@*">
	    <xsl:copy><xsl:value-of select="."/></xsl:copy>
	  </xsl:for-each>
	  <xsl:apply-templates select="node()"/>
	</xsl:copy>
	</xsl:template>

</xsl:stylesheet>