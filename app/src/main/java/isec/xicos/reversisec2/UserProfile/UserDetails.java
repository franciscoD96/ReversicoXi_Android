package isec.xicos.reversisec2.UserProfile;

import java.io.Serializable;

public class UserDetails implements Serializable {
    private String username;
    private int jogosGanhos = 0;
    private int jogosPerdidos = 0;

    public UserDetails(String u) { username = u; }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public int getJogosGanhos() {
        return jogosGanhos;
    }
    public void setJogosGanhos(int jogosGanhos) {
        this.jogosGanhos = jogosGanhos;
    }

    public int getJogosPerdidos() {
        return jogosPerdidos;
    }
    public void setJogosPerdidos(int jogosPerdidos) {
        this.jogosPerdidos = jogosPerdidos;
    }
}
