package org.jax.mgi.dbs.mgd.dao;

import java.sql.Timestamp;

/**
 * @is An object with attributes needed to create a new probe record in
 *     the PRB_Probe table.
 * @has
 *   <UL>
 *   <LI> Probe attributes
 *   </UL>
 * @does
 *   <UL>
 *   <LI> Provides get/set methods for its attribute(s).
 *   <LI> Provides a method to clear its attribute(s).
 *   </UL>
 * @company The Jackson Laboratory
 * @author dbm
 * @version 1.0
 */

public class SequenceState
{

    // Sequence attributes.
    //
    private Integer sequenceKey = null;
    private Integer  sequenceTypeKey  = null;
    private Integer sequenceQualityKey = null;
    private Integer sequenceStatusKey = null;
    private Integer segmentTypeKey = null;
    private Integer  sequenceProviderKey  = null;
    private Integer  length  = null;
    private String description = null;
    private String version = null;
    private String  division  = null;
    private Boolean virtual = null;
    private String  rawType  = null;
    private String rawLibrary = null;
    private String rawOrganism = null;
    private String  rawStrain  = null;
    private String  rawTissue  = null;
    private String  rawAge  = null;
    private String  rawSex  = null;
    private String  rawCellLine  = null;
    private Integer  numberOfOrganisms  = null;
    private Timestamp seqrecordDate  = null;
    private Timestamp sequenceDate  = null;

    public Integer getSequenceTypeKey ()
    {
      return sequenceTypeKey;
    }

    public Integer getSequenceQualityKey ()
    {
      return sequenceQualityKey;
    }

    public Integer getSequenceStatusKey ()
    {
      return sequenceStatusKey;
    }

    public Integer getSegmentTypeKey ()
    {
      return segmentTypeKey;
    }

    public Integer getSequenceProviderKey ()
    {
      return sequenceProviderKey;
    }

    public Integer getLength ()
    {
      return length;
    }

    public String getDescription ()
    {
      return description;
    }

    public String getVersion ()
    {
      return version;
    }

    public String getDivision ()
    {
      return division;
    }

    public Boolean getVirtual ()
    {
      return virtual;
    }

    public String getRawType ()
    {
      return rawType;
    }

    public String getRawLibrary ()
    {
      return rawLibrary;
    }

    public String getRawOrganism ()
    {
      return rawOrganism;
    }

    public String getRawStrain ()
    {
      return rawStrain;
    }

    public String getRawTissue ()
    {
      return rawTissue;
    }

    public String getRawAge ()
    {
      return rawAge;
    }

    public String getRawSex ()
    {
      return rawSex;
    }

    public String getRawCellLine ()
    {
      return rawCellLine;
    }

    public Integer getNumberOfOrganisms ()
    {
      return numberOfOrganisms;
    }

    public Timestamp getSeqrecordDate ()
    {
      return seqrecordDate;
    }

    public Timestamp getSequenceDate ()
    {
      return sequenceDate;
    }

    public void setSequenceTypeKey(Integer in)
    {
      sequenceTypeKey = in;
    }

    public void setSequenceQualityKey(Integer in)
    {
      sequenceQualityKey = in;
    }

    public void setSequenceStatusKey(Integer in)
    {
      sequenceStatusKey = in;
    }

    public void setSegmentTypeKey(Integer in)
    {
      segmentTypeKey = in;
    }

    public void setSequenceProviderKey(Integer in)
    {
      sequenceProviderKey = in;
    }

    public void setLength(Integer in)
    {
      length = in;
    }

    public void setLength(String in)
    {
      description = in;
    }

    public void setVersion(String in)
    {
      version = in;
    }

    public void setDivision(String in)
    {
      division = in;
    }

    public void setVirtual(Boolean in)
    {
      virtual = in;
    }

    public void setRawType(String in)
    {
      rawType = in;
    }

    public void setRawLibrary(String in)
    {
      rawLibrary = in;
    }

    public void setRawOrganism(String in)
    {
      rawOrganism = in;
    }

    public void setRawStrain(String in)
    {
      rawStrain = in;
    }

    public void setRawTissue(String in)
    {
      rawTissue = in;
    }

    public void setRawAge(String in)
    {
      rawAge = in;
    }

    public void setRawSex(String in)
    {
      rawSex = in;
    }

    public void setRawCellLine(String in)
    {
      rawCellLine = in;
    }

    public void setNumberOfOrganisms(Integer in)
    {
      numberOfOrganisms = in;
    }

    public void setSeqrecordDate(Timestamp in)
    {
      seqrecordDate = in;
    }

    public void setSequenceDate(Timestamp in)
    {
      sequenceDate = in;
    }

    public void clear ()
    {
        sequenceTypeKey = null;
        sequenceQualityKey = null;
        sequenceStatusKey = null;
        segmentTypeKey = null;
        sequenceProviderKey = null;
        length = null;
        description = null;
        version = null;
        division = null;
        virtual = null;
        rawType = null;
        rawLibrary = null;
        rawOrganism = null;
        rawStrain = null;
        rawTissue = null;
        rawAge = null;
        rawSex = null;
        rawCellLine = null;
        numberOfOrganisms = null;
        seqrecordDate = null;
        sequenceDate = null;
    }

}
