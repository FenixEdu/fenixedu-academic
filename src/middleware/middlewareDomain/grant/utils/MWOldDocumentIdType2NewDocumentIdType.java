/*
 * Created on Feb 19, 2004
 */
package middleware.middlewareDomain.grant.utils;

import Util.TipoDocumentoIdentificacao;

/**
 * @author pica
 * @author barbosa
 */
public class MWOldDocumentIdType2NewDocumentIdType
{
    public static final int OLD_BILHETE_DE_IDENTIDADE = 1;
    public static final int OLD_PASSAPORTE = 2;
    public static final int OLD_BILHETE_IDENTIDADE_MARINHA = 3;
    public static final int OLD_BILHETE_IDENTIDADE_ESTRANGEIRO = 4;
    public static final int OLD_BILHETE_IDENTIDADE_PAIS_ORIGEM = 5;
    public static final int OLD_BILHETE_IDENTIDADE_FORCA_AREA = 6;
    public static final int OLD_OUTRO = 7;
    
	public MWOldDocumentIdType2NewDocumentIdType()
	{
	}

    public TipoDocumentoIdentificacao ConvertOldDocumentType2NewDocumentType(Integer oldDocumentType)
    {
    	return ConvertOldDocumentType2NewDocumentType(oldDocumentType.intValue());
    }
    
    public TipoDocumentoIdentificacao ConvertOldDocumentType2NewDocumentType(int oldDocumentType)
    {
        if(oldDocumentType == MWOldDocumentIdType2NewDocumentIdType.OLD_BILHETE_DE_IDENTIDADE)
            return new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE);
        if(oldDocumentType == MWOldDocumentIdType2NewDocumentIdType.OLD_PASSAPORTE)
            return new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.PASSAPORTE);
        if(oldDocumentType == MWOldDocumentIdType2NewDocumentIdType.OLD_BILHETE_IDENTIDADE_MARINHA)
            return new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_MARINHA);
        if(oldDocumentType == MWOldDocumentIdType2NewDocumentIdType.OLD_BILHETE_IDENTIDADE_ESTRANGEIRO)
            return new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DE_CIDADAO_ESTRANGEIRO);
        if(oldDocumentType == MWOldDocumentIdType2NewDocumentIdType.OLD_BILHETE_IDENTIDADE_PAIS_ORIGEM)
            return new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DO_PAIS_DE_ORIGEM);
        if(oldDocumentType == MWOldDocumentIdType2NewDocumentIdType.OLD_BILHETE_IDENTIDADE_FORCA_AREA)
            return new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.BILHETE_DE_IDENTIDADE_DA_FORCA_AEREA);
        if(oldDocumentType == MWOldDocumentIdType2NewDocumentIdType.OLD_OUTRO)
            return new TipoDocumentoIdentificacao(TipoDocumentoIdentificacao.OUTRO);
    	return null;
    }

}
