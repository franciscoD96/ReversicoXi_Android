package isec.xicos.reversisec2.Reversi;

import java.io.Serializable;

public class Celula implements Serializable {
    boolean branco, preto, jogavel;
    Celula () { branco = false; preto = false; jogavel = false; }
    public void setBranco() { branco = true; preto = false; jogavel = false; }
    public void setPreto() { preto = true; branco = false; jogavel = false; }
    public void setJogavel() { preto = false; branco = false; jogavel = true; }
    public void setVazio() { preto = false; branco = false; jogavel = false; }
    public String getCelula() {
        if (branco == preto == jogavel == false)
            return "Vazio";
        else
        if (branco == true && preto == false && jogavel == false)
            return "Branco";
        else
        if (branco == false && preto == true && jogavel == false)
            return "Preto";
        else
        if (branco == false && preto == false && jogavel == true)
            return "Jogavel";
        else
            return "-1";
    }

}
