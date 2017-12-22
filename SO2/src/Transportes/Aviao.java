/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transportes;

import so2.Aeroporto;
import so2.Central;
import Atributos.Grupo;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 *
 * @author Miguel
 */
public class Aviao implements Runnable {

    private int numero;
    private String origem;
    private int terminal;
    private LocalTime chegadaPrevista;
    private LocalTime partidaPrevista;
    private LocalTime chegada;
    private LocalTime partida;
    private Central central;
    private ArrayList<Grupo> grupos;
    private Aeroporto aeroporto;
    private int aterragemDiaria;
    private boolean todosEmbarcaram;
    private boolean descolou;

    public Aviao(Aeroporto aeroporto, Central central) {
        this.aeroporto = aeroporto;
        this.grupos = new ArrayList<>();
        this.central = central;
        this.chegada = null;
        this.partida = null;
        this.chegadaPrevista = null;
        this.aterragemDiaria = 0;
        this.todosEmbarcaram = false;
        this.descolou = false;

    }

    public Aviao(int numero, String origem, int terminal,
            LocalTime chegadaPrevista, LocalTime partidaPrevista, LocalTime chegada,
            LocalTime partida, LocalTime horaActual, Central central, Grupo[] grupos, Aeroporto aeroporto) {
        this.numero = numero;
        this.origem = origem;
        this.terminal = terminal;
        this.chegadaPrevista = null;
        this.partidaPrevista = null;
        this.chegada = null;
        this.partida = null;
        this.central = central;
        this.aeroporto = aeroporto;
        this.grupos = new ArrayList<>();
        this.aterragemDiaria = 0;
        this.todosEmbarcaram = false;
        this.descolou = false;

    }

    public ArrayList<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(ArrayList<Grupo> grupos) {
        this.grupos = grupos;
    }

    public Aeroporto getAeroporto() {
        return aeroporto;
    }

    public void setAeroporto(Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public int getTerminal() {
        return terminal;
    }

    public void setTerminal(int terminal) {
        this.terminal = terminal;
    }

    public Central getCentral() {
        return central;
    }

    public void setCentral(Central central) {
        this.central = central;
    }

    public int getAterragemDiaria() {
        return aterragemDiaria;
    }

    public synchronized void setAterragemDiaria(int aterragemDiaria) {
        this.aterragemDiaria = aterragemDiaria;
    }

    public LocalTime getChegadaPrevista() {
        return chegadaPrevista;
    }

    public void setChegadaPrevista(LocalTime chegadaPrevista) {
        this.chegadaPrevista = chegadaPrevista;
    }

    public LocalTime getPartidaPrevista() {
        return partidaPrevista;
    }

    public void setPartidaPrevista(LocalTime partidaPrevista) {
        this.partidaPrevista = partidaPrevista;
    }

    public LocalTime getChegada() {
        return chegada;
    }

    public void setChegada(LocalTime chegada) {
        this.chegada = chegada;
    }

    public LocalTime getPartida() {
        return partida;
    }

    public void setPartida(LocalTime partida) {
        this.partida = partida;
    }

    public boolean isTodosEmbarcaram() {
        return todosEmbarcaram;
    }

    public void setTodosEmbarcaram(boolean todosEmbarcaram) {
        this.todosEmbarcaram = todosEmbarcaram;
    }

    public boolean isDescolou() {
        return descolou;
    }

    public void setDescolou(boolean descolou) {
        this.descolou = descolou;
    }

    /**
     * Método run que irá determinar o funcionamento dos avioes
     */
    @Override
    public void run() {
        while (true) {
            switch (this.terminal) {
                case 1:
                    if (!aeroporto.jaAterrou(this) && aeroporto.pistaLivre(0))//se ainda não aterrou 1 vez e a pista está livre aterra
                    {
                        if ((!getChegadaPrevista().isAfter(central.getHoras()))) {
                            setAterragemDiaria(1);//definir que já aterrou uma vez
                            setChegada(central.getHoras());//definir a hora de chegada verdadeira

                            aeroporto.aterra1(this);//aterrar

                            while (this.todosEmbarcaram == false && aeroporto.gruposFaltam(grupos) != 0) {//enquanto houver grupos por embarcar continua a tentar embarca-los
                                for (Grupo g : grupos) {
                                    if (g.getTransporte().equals("autocarro") && central.getTerminalRodoviario().getAutocarroParado() != null) {// se o grupo tiver como transporte autocarro e houver autocarros parados
                                        central.embarcarAutocarro(g, this);//embarcar no autocarro

                                    } else if (g.getTransporte().equals("comboio") && central.getTerminalFerroviario().getComboioParado() != null) {//se o grupos tiver como transporte comboio e houver comboios parados
                                        central.embarcarComboio(g, this);//embarcar no comboio

                                    } else {
                                        aeroporto.esperaPorComboiosOuAutocarros();//metodo para dizer ao aviao caso haja grupos para esperar por comboios ou autucarros
                                    }
                                }
                                aeroporto.gruposEmbarcaram(grupos, this);// metodo que vai confirmar se todos os grupos já embarcaram
                            }

                            this.aeroporto.LimparAviao();//metodo para limpar o aviao apos todos os grupos terem desembarcado   
                            this.aeroporto.partida(0, this);//metodo para o aviao puder partir
                            this.partida = this.central.getHoras();//define a hora de partida
                        } else {
                            aeroporto.esperaPorHoraDeAterrar(); // caso não seja a hora dele de aterrar espera
                        }
                    } else {
                        aeroporto.esperaPorPistaLivre();//caso a pista não esteja livre espera
                    }
                    break;
                case 2:
                    if (!aeroporto.jaAterrou(this) && aeroporto.pistaLivre(1)) {
                        if (!getChegadaPrevista().isAfter(central.getHoras())) {
                            setAterragemDiaria(1);
                            setChegada(central.getHoras());
                            aeroporto.aterra2(this);

                            while (this.todosEmbarcaram == false && aeroporto.gruposFaltam(grupos) != 0) {
                                for (Grupo g : grupos) {
                                    if (g.getTransporte().equals("autocarro") && central.getTerminalRodoviario().getAutocarroParado() != null) {
                                        central.embarcarAutocarro(g, this);

                                    } else if (g.getTransporte().equals("comboio") && central.getTerminalFerroviario().getComboioParado() != null) {
                                        central.embarcarComboio(g, this);

                                    } else {
                                        aeroporto.esperaPorComboiosOuAutocarros();
                                    }
                                }

                                aeroporto.gruposEmbarcaram(grupos, this);
                            }

                            this.aeroporto.LimparAviao();
                            aeroporto.partida(1, this);
                            this.partida = central.getHoras();
                        } else {
                            aeroporto.esperaPorHoraDeAterrar();
                        }
                    } else {
                        aeroporto.esperaPorPistaLivre();
                    }
                    break;
                case 3:
                    if (!aeroporto.jaAterrou(this) && aeroporto.pistaLivre(2)) {
                        if (!getChegadaPrevista().isAfter(central.getHoras())) {
                            setAterragemDiaria(1);
                            setChegada(central.getHoras());
                            aeroporto.aterra3(this);

                            while (this.todosEmbarcaram == false && aeroporto.gruposFaltam(grupos) != 0) {
                                for (Grupo g : grupos) {
                                    if (g.getTransporte().equals("autocarro") && central.getTerminalRodoviario().getAutocarroParado() != null) {
                                        central.embarcarAutocarro(g, this);

                                    } else if (g.getTransporte().equals("comboio") && central.getTerminalFerroviario().getComboioParado() != null) {
                                        central.embarcarComboio(g, this);

                                    } else {
                                        aeroporto.esperaPorComboiosOuAutocarros();
                                    }
                                }

                                aeroporto.gruposEmbarcaram(grupos, this);
                            }

                            this.aeroporto.LimparAviao();
                            aeroporto.partida(2, this);
                            this.partida = central.getHoras();
                        } else {
                            aeroporto.esperaPorHoraDeAterrar();
                        }
                    } else {
                        aeroporto.esperaPorPistaLivre();
                    }
                    break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("Numero: ").append(getNumero()).append("|");
        str.append("Origem: ").append(getOrigem()).append("|");
        str.append("Chegada: ").append(getChegadaPrevista()).append("|");
        str.append("Porta: ").append(getTerminal());

        for (int i = 0; i < grupos.size(); i++) {

            str.append("\n");
            str.append("Numero Pessoas: ").append(getGrupos().get(i).getNumeroPessoas()).append("|");
            str.append("Transporte: ").append(getGrupos().get(i).getTransporte()).append("|");
            str.append("Destino: ").append(getGrupos().get(i).getDestino());

        }
        str.append("\n");

        return str.toString();

    }

}
