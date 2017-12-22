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
import so2.TerminalFerroviario;

/**
 *
 * @author Miguel
 */
public class Comboio extends Transporte implements Runnable {

    private TerminalFerroviario terminalFerroviario;
    private ArrayList<Grupo> gruposComboio;

    public Comboio(Central central, TerminalFerroviario terminalFerroviario) {
        super(central);
        this.terminalFerroviario = terminalFerroviario;
        this.gruposComboio = new ArrayList<>();
    }

    public Comboio(int numero, String destino, int terminal, LocalTime chegadaPrevista,
            LocalTime partidaPrevista, LocalTime horaActual, int capacidade, int lugaresOcupados, Central central,
            TerminalFerroviario terminalFerroviario) {

        super(numero, destino, terminal, chegadaPrevista, partidaPrevista, horaActual,
                capacidade, lugaresOcupados, central);

        this.terminalFerroviario = terminalFerroviario;

    }

    public TerminalFerroviario getTerminalFerroviario() {
        return terminalFerroviario;
    }

    public void setTerminalFerroviario(TerminalFerroviario terminalFerroviario) {
        this.terminalFerroviario = terminalFerroviario;
    }

    public ArrayList<Grupo> getGruposComboio() {
        return gruposComboio;
    }

    public void setGruposComboio(ArrayList<Grupo> gruposComboio) {
        this.gruposComboio = gruposComboio;
    }
    /**
     * Método  que vai determinar o funcionamento das threads comboio
     */
    @Override
    public void run() {
        while (true) {
            switch (getTerminal()) {
                case 1:
                    if (getChegadaDiaria() == 0 && terminalFerroviario.terminalLivre(0))//se ainda nãoe estacioun uma vez hoje e se o terminal está livre
                        if (!getChegadaPrevista().isAfter(getCentral().getHoras())) {
                            setChegadaDiaria(1);//define que já estacionou uma vez hoje

                            terminalFerroviario.chegadaTerminal1(this); //metodo que vai definir que o comboio estacioun

                            while (getPartidaPrevista().isAfter(getCentral().getHoras())) {
                                terminalFerroviario.esperarAtePartir();//enquanto não for a hora de partir espera
                            }

                            if (getCentral().devoEsperar(this.getDestino()))//se vem um aviao com passageiros que vão apanhar este autocarro espera
                                terminalFerroviario.tolerancia(this);//espera tempo de tolerancia

                            terminalFerroviario.partida(0, this);//metodo de partida do comboio da estação

                        } else
                            terminalFerroviario.esperarParaEstacionar();//espera pela hora para estacionar
                    else
                        terminalFerroviario.esperaAteLivre();//espera até a plataforma estar livre
                    break;

                case 2:
                    if (getChegadaDiaria() == 0 && terminalFerroviario.terminalLivre(1))
                        if ((!getChegadaPrevista().isAfter(getCentral().getHoras()))) {
                            setChegadaDiaria(1);

                            this.terminalFerroviario.chegadaTerminal2(this);

                            while (getPartidaPrevista().isAfter(getCentral().getHoras())) {
                                terminalFerroviario.esperarAtePartir();
                            }

                            if (getCentral().devoEsperar(this.getDestino()))
                                terminalFerroviario.tolerancia(this);
                            
                            this.terminalFerroviario.partida(1, this);

                        } else
                            terminalFerroviario.esperarParaEstacionar();
                    else
                        terminalFerroviario.esperaAteLivre();
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
