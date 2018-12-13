package isec.xicos.reversisec2.Reversi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.lang.Math.abs;

public class ReversicoXi implements Serializable {

    private int getDeslocamentoAbsoluto(int deslocX, int deslocY) {

        if (abs(deslocX) < abs(deslocY))
            return abs(deslocY);
        else
            return abs(deslocX);
    }

    private void limpaMarcadoresJogaveis() {
        for (int x = 0; x < 8; x++)
            for (int y =0; y < 8; y++) {
                if (campo.get(y).get(x).getCelula() == "Jogavel")
                    campo.get(y).get(x).setVazio();
            }
    }

    private boolean verificaDirecao(int x, int y, int deslocX, int deslocY, String player, String enemy) throws Exception {

        int desloc = getDeslocamentoAbsoluto(deslocX, deslocY);

        if (x + deslocX > 7 || x + deslocX < 0
                || y + deslocY > 7 || y + deslocY < 0)
            return false;
        else {
            String res = campo.get(y + deslocY).get(x + deslocX).getCelula();

            if (res == "Vazio" || res == "Jogavel" || (res == player && desloc == 1))
                return false; // não vai procurar mais nesta direção
            else
            if (res == player && desloc > 1) {
                posJogaveis.add(new Coord(x, y));
                return false; //para não procurar mais nesta direção
            }
            if (res == enemy)
                return true;
        }

        throw new Exception();
    }
    private void verificaDirecoesCardeais(int x, int y, String player, String enemy) {

        //  NW  N  NE         (-1,-1) (-1, 0) (-1, 1)
        //   W      E    ->   ( 0,-1) ( 0, 0) ( 0, 1)
        //  SW  S  SE         ( 1,-1) ( 1, 0) ( 1, 1)

        boolean N = true, NE = true, E = true, SE = true,
                S = true, SW = true, W = true, NW = true;

        try {
            for (int c = 1; c < 8; c++) {
                if (N)
                    N = verificaDirecao(x, y, 0, -c, player, enemy);
                if (NE)
                    NE = verificaDirecao(x, y, c, -c, player, enemy);
                if (E)
                    E = verificaDirecao(x, y, c, 0, player, enemy);
                if (SE)
                    SE = verificaDirecao(x, y, c, c, player, enemy);
                if (S)
                    S = verificaDirecao(x, y, 0, c, player, enemy);
                if (SW)
                    SW = verificaDirecao(x, y, -c, c, player, enemy);
                if (W)
                    W = verificaDirecao(x, y, -c, 0, player, enemy);
                if (NW)
                    NW = verificaDirecao(x, y, -c, -c, player, enemy);
            }
        } catch (Exception e)
        { e.printStackTrace(); }
    }
    private void marcaPosicoesLivres(int paraPlayer) { // um ou dois

        String player = (paraPlayer == 1) ? "Branco" : "Preto";
        String enemy = (paraPlayer == 1) ? "Preto" : "Branco";

        posJogaveis = new ArrayList<Coord>();

        int i=0, j=0;
        for(List<Celula> l: campo) {
            for (Celula s : l) {
                if (s.getCelula() == "Vazio")
                    verificaDirecoesCardeais(j, i, player, enemy);
                j++;
            }
            j = 0;
            i++;
        }

        for(Coord c : posJogaveis) {
            campo.get(c.getY()).get(c.getX()).setJogavel();
        }
    }

    private boolean marcaPeçaComestível(int x, int y, int deslocX, int deslocY, String player, String enemy, int direcao) {

        int desloc = getDeslocamentoAbsoluto(deslocX, deslocY);

        if (x + deslocX > 7 || x + deslocX < 0          // cada deslocamento tem que chegar a esta função exatamente
                || y + deslocY > 7 || y + deslocY < 0) // porque esta validação tem que ser feita.
        {
            posAComer.get(direcao).clear(); // the list will be empty after the call returns.
            return false;
        } else {
            String res = campo.get(y + deslocY).get(x + deslocX).getCelula(); // [Vazio, Branco, Preto, Jogavel]
            if (desloc == 1) {
                if (res == enemy) {
                    posAComer.get(direcao).add(new Coord(x + deslocX, y + deslocY));
                    return true;
                } else
                    return false;
            } else {
                if (res == enemy) {
                    posAComer.get(direcao).add(new Coord(x + deslocX, y + deslocY));
                    return true;
                } else if (res == player)
                    return false; // marcou as posições a comer, e já não vai procurar mais nesta direção.
                else if (res == "Vazio" || res == "Jogavel") {
                    // se ele chegou aqui, significa que depois de ter passado por peças inimigas,
                    // encontrou uma posição vazia e então temos que eliminar as posições marcadas
                    // para comer nesta direção
                    posAComer.get(direcao).clear(); // the list will be empty after the call returns.
                    return false;
                } else
                    return false;
            }
        }
    }
    private void encontraPosicoesAComer(int x, int y, String player, String enemy) {

        posAComer = new ArrayList<List<Coord>>();
        for (int i = 0; i < 8; i++)
            posAComer.add( new ArrayList<Coord>());
        //_comerAté_ é uma lista que contem listas para cada ponto cardeal.
        // -> o objetivo aqui é para cada direção, guardar as posições que irão ser comidas.
        // -> essas posições serão descartadas caso nessa direção, antes de encontrar
        //  um espaço vazio, não se encontre uma peça do jogador.

        boolean N = true, NE = true, E = true, SE = true,
                S = true, SW = true, W = true, NW = true;
        try {
            for (int c = 1; c < 8; c++) {
                if (N)
                    N = marcaPeçaComestível(x, y, 0, -c, player, enemy, 0);
                if (NE)
                    NE = marcaPeçaComestível(x, y, +c, -c, player, enemy, 1);
                if (E)
                    E = marcaPeçaComestível(x, y, +c, 0, player, enemy, 2);
                if (SE)
                    SE = marcaPeçaComestível(x, y, +c, +c, player, enemy, 3);
                if (S)
                    S = marcaPeçaComestível(x, y, 0, +c, player, enemy, 4);
                if (SW)
                    SW = marcaPeçaComestível(x, y, -c, +c, player, enemy, 5);
                if (W)
                    W = marcaPeçaComestível(x, y, -c, 0, player, enemy, 6);
                if (NW)
                    NW = marcaPeçaComestível(x, y, -c, -c, player, enemy, 7);
            }
        } catch (Exception e)
        { e.printStackTrace(); }

    }
    private void realizaJogada(int paraPlayer, int x, int y) {

        String player = (paraPlayer == 1) ? "Branco" : "Preto";
        String enemy = (paraPlayer == 1) ? "Preto" : "Branco";

        encontraPosicoesAComer(x, y, player, enemy);

        if (player == "Branco")
            campo.get(y).get(x).setBranco();
        else
            campo.get(y).get(x).setPreto();

        System.out.println("vai comer as posições...");
        for (List<Coord> l : posAComer)
            for (Coord c : l) {
                System.out.println("x:" + c.getX() + " y:" + c.getY());
                if (player == "Branco")
                    campo.get(c.getY()).get(c.getX()).setBranco();
                else
                    campo.get(c.getY()).get(c.getX()).setPreto();
            }

    }
    private Integer calculaPontosAGanhar(int paraPlayer, int x, int y) {
        String player = (paraPlayer == 1) ? "Branco" : "Preto";
        String enemy = (paraPlayer == 1) ? "Preto" : "Branco";

        encontraPosicoesAComer(x, y, player, enemy);

        int contador = 1; // ponto ganho à partida
        for (List<Coord> l : posAComer)
            for (Coord c : l)
                contador++;

        return contador;
    }

    List<List<Celula>> campo;
    public List<List<Celula>> getCampo() { return campo; }
    List<Coord> posJogaveis;
    List<List<Coord>> posAComer;
    int jogadorAtual = 1;
    public int getJogadorAtual() { return jogadorAtual; }

    public ReversicoXi() {
        this.campo = new ArrayList<List<Celula>>();
        for(int i = 0; i < 8; i++)
            campo.add( new ArrayList<Celula>());
        for(int i = 0 ; i < 8 ; i++)
            for (int j = 0; j < 8; j++)
                campo.get(i).add( new Celula());
        campo.get(3).get(3).setBranco();
        campo.get(4).get(4).setBranco();
        campo.get(3).get(4).setPreto();
        campo.get(4).get(3).setPreto();
    }

    public void jogoAIvsAI() {

        HashMap<Coord, Integer> nrPontosObtidosPorJogada;

        Scanner sc = new Scanner(System.in);
        int jogador = 1;
        while(true) {
            System.out.println("Jogador " + jogador);
            marcaPosicoesLivres(jogador);
//            imprimeCampo(campo);


            // bot aleatório
            Collections.shuffle(posJogaveis);
            Coord jogada = posJogaveis.get(0);
            System.out.println("vai meter a peça em: " + jogada.getX() + " " + jogada.getY());
            realizaJogada(jogador, jogada.getX(), jogada.getY());

            limpaMarcadoresJogaveis();

            if(jogador == 1) jogador = 2;
            else jogador = 1;

            sc.nextLine();
        }
      }

    public void AIvsAI2() { }
              /*// bot a jogar a jogada que dá mais pontos
            nrPontosObtidosPorJogada = new HashMap<>(posJogaveis.size());
            for (Coord c : posJogaveis) {
                nrPontosObtidosPorJogada.put(c, calculaPontosGanhos(jogador, c.getX(), c.getY()));

                System.out.println("Pontos obtidos ao jogar em " + "x:" + c.getX() + " y:" + c.getY()
                 + " -> " + nrPontosObtidosPorJogada.get(c));
            }*/


    public void PvsAI() { }

    public int jogadaAIvsAI() {

        marcaPosicoesLivres(jogadorAtual);
        // if posicoesLivres == 0
        //      testar se o inimigo pode jogar
        // else
        //      end game
        Collections.shuffle(posJogaveis);
        Coord jogada = posJogaveis.get(0);
        realizaJogada(jogadorAtual, jogada.getX(), jogada.getY());
        limpaMarcadoresJogaveis();

        if(jogadorAtual == 1) jogadorAtual = 2;
        else jogadorAtual = 1;

        return jogadorAtual;
    }
}
