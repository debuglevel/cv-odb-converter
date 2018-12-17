<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="ArrayList/item">

        <div class="resume-item d-flex flex-column flex-md-row mb-5">
            <div class="resume-content mr-auto">
                <h3 class="mb-0">
                    <xsl:value-of select="title"/>
                </h3>
                <div class="subheading mb-3">
                    <a>
                        <xsl:attribute name="href">
                            <xsl:value-of select="placeUrl"/>
                        </xsl:attribute>
                        <xsl:value-of select="place"/>
                    </a>
                </div>
                <p>
                    <xsl:value-of select="description"/>
                </p>
            </div>
            <div class="resume-date text-md-right">
                <span class="text-primary">somewhen bis März 2018</span>
            </div>
        </div>
    </xsl:template>
</xsl:stylesheet>