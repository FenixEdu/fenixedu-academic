package middleware.middlewareDomain;

public class MWGrupoIleec
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
        boolean result = false;
        if (obj instanceof MWGrupoIleec)
        {
            MWGrupoIleec mwgi = (MWGrupoIleec) obj;
            result = this.getIdAreaEspecializacao().equals(mwgi.getIdAreaEspecializacao()) &&
            		this.getIdAreaSecundaria().equals(mwgi.getIdAreaSecundaria()) &&
            		this.getMaxCreditos().equals(mwgi.getMaxCreditos()) &&
            		this.getMinCreditos().equals(mwgi.getMinCreditos());
        }
        return result;
    }
}