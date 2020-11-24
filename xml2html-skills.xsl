<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns:xs="http://www.w3.org/2001/XMLSchema" exclude-result-prefixes="xs xsi xsl">

    <xsl:template match="/">

        <xsl:for-each-group select="/ArrayList/item" group-by="category">
            <div class="col-lg-4 ml-auto">
                <h4><xsl:value-of select="category"/></h4>
                <xsl:for-each-group select="current-group()" group-by="subcategory">
                    <xsl:comment><h5><xsl:value-of select="subcategory"/></h5></xsl:comment>
                    <p class="lead skills">
                        <xsl:for-each select="current-group()">
                          <span data-toggle="tooltip">
                            <xsl:attribute name="class">badge badge-<xsl:value-of select="./level"/></xsl:attribute>
                            <xsl:attribute name="title">
                                <xsl:value-of select="./description"/>
                            </xsl:attribute>
                            <xsl:choose>
                                <xsl:when test="./hexColor != ''">
                                    <xsl:attribute name="style">background-color: <xsl:value-of select="./hexColor"/>;</xsl:attribute>
                                </xsl:when>
                                <xsl:otherwise>
                                </xsl:otherwise>
                            </xsl:choose>
                            <xsl:value-of select="./label"/>
                          </span>
                      </xsl:for-each>
                    </p>
                </xsl:for-each-group>
            </div>
        </xsl:for-each-group>

    </xsl:template>
</xsl:stylesheet>