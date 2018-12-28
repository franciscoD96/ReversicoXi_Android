package isec.xicos.reversisec2.Reversi;

import java.io.Serializable;
import java.util.List;

public class Campo implements Serializable {
    private List<List<Celula>> campo;
    public List<List<Celula>> getCampo() { return campo; }
    public Campo(List<List<Celula>> c) {this.campo = c;}
}