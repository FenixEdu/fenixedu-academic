/*
 * Created on Feb 26, 2004
 */
package middleware.middlewareDomain.grant.utils;

import Util.EstadoCivil;

/**
 * @author pica
 * @author barbosa
 */
public class MWOldMaritalStatusType2NewMaritalStatusType
{
	public static final int OLD_SOLTEIRO = 1;
	public static final int OLD_CASADO = 2;
	public static final int OLD_DIVORCIADO = 3;
	public static final int OLD_VIUVO = 4;
	public static final int OLD_OUTRO = 0;

	public MWOldMaritalStatusType2NewMaritalStatusType()
	{
	}

	public EstadoCivil ConvertOldMaritalStatusType2NewMaritalStatusType(Integer oldMaritalStatusType)
	{
		return ConvertOldMaritalStatusType2NewMaritalStatusType(oldMaritalStatusType.intValue());
	}

	public EstadoCivil ConvertOldMaritalStatusType2NewMaritalStatusType(int oldMaritalStatusType)
	{
		if (oldMaritalStatusType == MWOldMaritalStatusType2NewMaritalStatusType.OLD_SOLTEIRO)
			return new EstadoCivil(EstadoCivil.SOLTEIRO);
		if (oldMaritalStatusType == MWOldMaritalStatusType2NewMaritalStatusType.OLD_CASADO)
			return new EstadoCivil(EstadoCivil.CASADO);
		if (oldMaritalStatusType == MWOldMaritalStatusType2NewMaritalStatusType.OLD_DIVORCIADO)
			return new EstadoCivil(EstadoCivil.DIVORCIADO);
		if (oldMaritalStatusType == MWOldMaritalStatusType2NewMaritalStatusType.OLD_VIUVO)
			return new EstadoCivil(EstadoCivil.VIUVO);
		if (oldMaritalStatusType == MWOldMaritalStatusType2NewMaritalStatusType.OLD_OUTRO)
			return new EstadoCivil(EstadoCivil.DESCONHECIDO);
		return null;
	}

}
