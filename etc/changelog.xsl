<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet
    xmlns:xsl='http://www.w3.org/1999/XSL/Transform'
    version='1.0'>

  <xsl:param name="title"/>
  <xsl:param name="module"/>
  <xsl:param name="cvsweb"/>

  <xsl:output method="html" indent="yes" encoding="US-ASCII"/>

  <!-- Copy standard document elements.  Elements that
       should be ignored must be filtered by apply-templates
       tags. -->
  <xsl:template match="*">
    <xsl:copy>
      <xsl:copy-of select="attribute::*[. != '']"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="changelog">
== <xsl:value-of select="$title"/> ==
<xsl:text>

</xsl:text>
<xsl:apply-templates select=".//entry">
	<xsl:sort select="date" data-type="text" order="descending"/>
	<xsl:sort select="time" data-type="text" order="descending"/>
</xsl:apply-templates>
</xsl:template>
  
  <xsl:template match="entry">
-------
||<xsl:value-of select="date"/>||<xsl:value-of select="time"/>||<xsl:value-of select="author"/>||
{{{
<xsl:apply-templates select="msg"/>
}}}
<xsl:apply-templates select="file"/>

</xsl:template>

  <xsl:template match="date"><xsl:value-of select="."/></xsl:template>

  <xsl:template match="time"><xsl:value-of select="."/></xsl:template>

  <xsl:template match="author"><xsl:attribute name="href">mailto:<xsl:value-of select="."/></xsl:attribute><xsl:value-of select="."/></xsl:template>

  <xsl:template match="file"><xsl:value-of select="$cvsweb"/>/<xsl:value-of select="name" /> (<xsl:value-of select="revision"/>)</xsl:template>

  <!-- Any elements within a msg are processed,
       so that we can preserve HTML tags. -->
  <xsl:template match="msg">
    <xsl:apply-templates/>
  </xsl:template>
  
</xsl:stylesheet>
