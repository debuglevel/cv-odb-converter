<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="ArrayList/item">
        <div class="resume-item d-flex flex-column flex-md-row mb-5">
            <div class="resume-content mr-auto">
                <h3 class="mb-0">
                    <xsl:value-of select="activity"/>
                </h3>
                <div class="subheading mb-3">
                    <a>
                        <xsl:attribute name="href">
                            <xsl:value-of select="placeUrl"/>
                        </xsl:attribute>
                        <xsl:value-of select="place"/>
                    </a><!--<xsl:choose>
                    <xsl:when test="department">, <xsl:value-of select="department"/></xsl:when>
                    </xsl:choose>-->
                    <xsl:choose>
                        <xsl:when test="industrialSector/text()"> (<xsl:value-of select="industrialSector"/>)</xsl:when>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="placeSize/text()">, <xsl:value-of select="placeSize"/> Mitarbeiter</xsl:when>
                    </xsl:choose>
                    <xsl:choose>
                        <xsl:when test="title/text()">, <xsl:value-of select="title"/></xsl:when>
                    </xsl:choose>
                </div>
                <!-- <p> -->
                <xsl:value-of select="description" disable-output-escaping="yes"/>
                <!-- </p> -->
            </div>
            <div class="resume-date text-md-right">
                <span class="text-primary">
                    <xsl:value-of select="format-number(begin/monthValue, '00')"/>/<xsl:value-of select="begin/year"/>&#160;bis&#160;<xsl:choose>
                        <xsl:when test="end/year"><xsl:value-of select="format-number(end/monthValue, '00')"/>/<xsl:value-of select="end/year"/></xsl:when>
                        <xsl:otherwise>heute</xsl:otherwise>
                    </xsl:choose>
                </span>
            </div>
        </div>
    </xsl:template>
</xsl:stylesheet>