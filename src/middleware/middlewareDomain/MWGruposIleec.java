/*
 * Created on 10/Dez/2003
 *
 */
 
package middleware.middlewareDomain;

public class MWGruposIleec
{

    private Integer idAreaEspecializacao;
    private Integer idAreaSecundaria;
    private Integer idGrupo;
    private Integer maxCreditos;
    private Integer minCreditos;
    

    public Integer getIdAreaEspecializacao()
    {
        return this.idAreaEspecializacao;
    }
    public void setIdAreaEspecializacao(Integer param)
    {
        this.idAreaEspecializacao = param;
    }

    public Integer getIdAreaSecundaria()
    {
        return this.idAreaSecundaria;
    }
    public void setIdAreaSecundaria(Integer param)
    {
        this.idAreaSecundaria = param;
    }

    public Integer getIdGrupo()
    {
        return this.idGrupo;
    }
    public void setIdGrupo(Integer param)
    {
        this.idGrupo = param;
    }

    public Integer getMaxCreditos()
    {
        return this.maxCreditos;
    }
    public void setMaxCreditos(Integer param)
    {
        this.maxCreditos = param;
    }

    public Integer getMinCreditos()
    {
        return this.minCreditos;
    }
    public void setMinCreditos(Integer param)
    {
        this.minCreditos = param;
    }

    public String toString()
    {
        return " [idAreaEspecializacao] "
            + idAreaEspecializacao
            + " [idAreaSecundaria] "
            + idAreaSecundaria
            + " [idGrupo] "
            + idGrupo
            + " [maxCreditos] "
            + maxCreditos
            + " [minCreditos] "
            + minCreditos;
    }

    public boolean equals(Object obj)
    {
        boolean resultado = false;
        if (obj instanceof MWGruposIleec)
        {
            MWGruposIleec mwgi = (MWGruposIleec) obj;
            resultado =
                getIdGrupo().equals(mwgi.getIdGrupo())
                    && (((getIdAreaEspecializacao() != null)
                        && (mwgi.getIdAreaEspecializacao() != null)
                        && getIdAreaEspecializacao().equals(mwgi.getIdAreaEspecializacao()))
                        || ((getIdAreaSecundaria() != null)
                            && (mwgi.getIdAreaSecundaria() != null)
                            && getIdAreaSecundaria().equals(mwgi.getIdAreaSecundaria())));
        }
        return resultado;
    }
}
