/*
 * Created on Feb 26, 2004
 */
package middleware.middlewareDomain.grant.utils;

import Util.Sexo;

/**
 * @author pica
 * @author barbosa
 */
public class MWOldSexType2NewSexType
{
	public static final int OLD_MASCULINO = 1;
	public static final int OLD_FEMININO = 2;

	public MWOldSexType2NewSexType()
	{
	}

	public Sexo ConvertOldSexType2NewSexType(Integer oldSexType)
	{
		return ConvertOldSexType2NewSexType(oldSexType.intValue());
	}

	public Sexo ConvertOldSexType2NewSexType(int oldSexType)
	{
		if (oldSexType == MWOldSexType2NewSexType.OLD_MASCULINO)
			return new Sexo(Sexo.MASCULINO);
		if (oldSexType == MWOldSexType2NewSexType.OLD_FEMININO)
			return new Sexo(Sexo.FEMININO);
		return null;
	}

}
