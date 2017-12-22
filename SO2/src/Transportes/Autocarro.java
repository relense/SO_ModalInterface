/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transportes;

import Atributos.Grupo;
import so2.Central;
import java.time.LocalTime;
import java.util.ArrayList;
import so2.TerminalRodoviario;

/**
 * Classe que representa um autocarro
 *
 * @author Miguel
 */
public class Autocarro extends Transporte implements Runnable {

    private TerminalRodoviario terminalRodoviario;
    private ArrayList<Grupo> gruposAutocarro;

    public Autocarro(Central central, TerminalRodoviario terminal) {
        super(central);
        this.terminalRodoviario = terminal;
        this.gruposAutocarro = new ArrayList<>();
    }

    public Autocarro(int numero, String destino, int terminal, LocalTime chegadaPrevista,
            LocalTime partidaPrevista, LocalTime horaActual, int capacidade, int lugaresOcupados, Central central, TerminalRodoviario terminalRodoviario) {

        super(numero, destino, terminal, chegadaPrevista, partidaPrevista, horaActual, capacidade,
                lugaresOcupados, central);
        this.terminalRodoviario = terminalRodoviario;
    }

    public TerminalRodoviario getTerminalRodoviario() {
        return terminalRodoviario;
    }

    public void setTerminalRodoviario(TerminalRodoviario terminalRodoviario) {
        this.terminalRodoviario = terminalRodoviario;
    }

    public ArrayList<Grupo> getGruposAutocarro() {
        return gruposAutocarro;
    }

    public void setGruposAutocarro(ArrayList<Grupo> gruposAutocarro) {
        this.gruposAutocarro = gruposAutocarro;
    }
    /**
     * Método que vai controlar o funcionamente das várias thread autocarros
     */
    @Override
    public void run() {
        while (true) {
            switch (getTerminal()) {
                case 1:
                    if (getChegadaDiaria() == 0 && terminalRodoviario.terminalLivre(0)) {//Ainda não estacionou hoje e o terminal está livre
                        if (!getChegadaPrevista().isAfter(getCentral().getHoras())) {
                            setChegadaDiaria(1);//já chegou uma vez hoje

                            terminalRodoviario.chegadaTerminal1(this);//chega ao terminal

                            while (getPartidaPrevista().isAfter(getCentral().getHoras())) {//enquanto não puder partir dorme
                                terminalRodoviario.esperarAtePartir();
                            }

                            if (getCentral().devoEsperar(this.getDestino())) {//se existir um aviao a vir dorme
                                terminalRodoviario.tolerancia(this);
                            }

                            terminalRodoviario.partida(0, this);//parte do terminal
                            setPartida(getCentral().getHoras());

                        } else {
                            terminalRodoviario.esperarAtePartir();
                        }
                    } else {//Não está livre o terminal ou já estacionou uma vez então espera
                        terminalRodoviario.esperarAteLivre();
                    }

                    break;
                case 2:
                    if (getChegadaDiaria() == 0 && terminalRodoviario.terminalLivre(1)) {
                        if (!getChegadaPrevista().isAfter(getCentral().getHoras())) {
                            setChegadaDiaria(1);

                            terminalRodoviario.chegadaTerminal2(this);

                            while (getPartidaPrevista().isAfter(getCentral().getHoras())) {
                                terminalRodoviario.esperarAtePartir();
                            }

                            if (getCentral().devoEsperar(this.getDestino())) {
                                terminalRodoviario.tolerancia(this);
                            }

                            terminalRodoviario.partida(1, this);

                        } else {
                            terminalRodoviario.esperaParaEstacionar();
                        }
                    } else {
                        terminalRodoviario.esperarAteLivre();
                    }

                    break;
                case 3:
                    if (getChegadaDiaria() == 0 && terminalRodoviario.terminalLivre(2)) {
                        if (!getChegadaPrevista().isAfter(getCentral().getHoras())) {
                            setChegadaDiaria(1);

                            terminalRodoviario.chegadaTerminal3(this);

                            while (getPartidaPrevista().isAfter(getCentral().getHoras())) {
                                terminalRodoviario.esperarAtePartir();
                            }

                            if (getCentral().devoEsperar(this.getDestino())) {
                                terminalRodoviario.tolerancia(this);
                            }

                            terminalRodoviario.partida(2, this);

                        } else {
                            terminalRodoviario.esperaParaEstacionar();
                        }
                    } else {
                        terminalRodoviario.esperarAteLivre();
                    }
                    break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append(super.toString()).append("|");

        return str.toString();

    }

}
