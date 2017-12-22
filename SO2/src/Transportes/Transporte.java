/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Transportes;

import so2.Central;
import java.time.LocalTime;

/**
 *
 * @author Miguel
 */
public class Transporte {

    private int numero;
    private String destino;
    private int terminal;
    private LocalTime chegadaPrevista;
    private LocalTime partidaPrevista;
    private LocalTime chegada;
    private LocalTime partida;
    private int capacidade;
    private int lugaresOcupados;
    private Central central;
    private int chegadaDiaria;
    private long atrasoChegadaMinutos;
    private long atrasoPartidaMinutos;
    private LocalTime partidaSemTolerancia;
    private int passageirosDentroDoHorario;
    private int passageirosForaDoHorario;

    public Transporte(Central central) {
        this.central = central;
        this.chegadaDiaria = 0;
        this.atrasoChegadaMinutos = 0;
        this.atrasoPartidaMinutos = 0;
        this.partidaSemTolerancia = null;
        this.passageirosDentroDoHorario = 0;
        this.passageirosForaDoHorario = 0;

    }

    public Transporte(int numero, String destino, int terminal,
            LocalTime chegadaPrevista, LocalTime partidaPrevista, LocalTime horaActual, int capacidade,
            int lugaresOcupados, Central central) {
        this.numero = numero;
        this.destino = destino;
        this.terminal = terminal;
        this.chegadaPrevista = chegadaPrevista;
        this.partidaPrevista = partidaPrevista;
        this.capacidade = capacidade;
        this.lugaresOcupados = lugaresOcupados;
        this.central = central;
        this.chegadaDiaria = 0;
        this.atrasoChegadaMinutos = 0;
        this.atrasoPartidaMinutos = 0;
        this.partidaSemTolerancia = null;
        this.passageirosDentroDoHorario = 0;
        this.passageirosForaDoHorario = 0;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public int getTerminal() {
        return terminal;
    }

    public void setTerminal(int terminal) {
        this.terminal = terminal;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public int getLugaresOcupados() {
        return lugaresOcupados;
    }

    public synchronized void setLugaresOcupados(int lugaresOcupados) {
        this.lugaresOcupados = lugaresOcupados;
    }

    public Central getCentral() {
        return central;
    }

    public void setCentral(Central central) {
        this.central = central;
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

    public synchronized void setChegada(LocalTime chegada) {
        this.chegada = chegada;
    }

    public LocalTime getPartida() {
        return partida;
    }

    public synchronized void setPartida(LocalTime partida) {
        this.partida = partida;
    }

    public int getChegadaDiaria() {
        return chegadaDiaria;
    }

    public synchronized void setChegadaDiaria(int chegadaDiaria) {
        this.chegadaDiaria = chegadaDiaria;
    }

    public long getAtrasoChegadaMinutos() {
        return atrasoChegadaMinutos;
    }

    public synchronized void setAtrasoChegadaMinutos(long atrasoChegadaMinutos) {
        this.atrasoChegadaMinutos = atrasoChegadaMinutos;
    }

    public long getAtrasoPartidaMinutos() {
        return atrasoPartidaMinutos;
    }

    public synchronized void setAtrasoPartidaMinutos(long atrasoPartidaMinutos) {
        this.atrasoPartidaMinutos = atrasoPartidaMinutos;
    }

    public LocalTime getPartidaSemTolerancia() {
        return partidaSemTolerancia;
    }

    public synchronized void setPartidaSemTolerancia(LocalTime partidaSemTolerancia) {
        this.partidaSemTolerancia = partidaSemTolerancia;
    }

    public int getPassageirosDentroDoHorario() {
        return passageirosDentroDoHorario;
    }

    public synchronized void setPassageirosDentroDoHorario(int passageirosDentroDoHorario) {
        this.passageirosDentroDoHorario = passageirosDentroDoHorario;
    }

    public int getPassageirosForaDoHorario() {
        return passageirosForaDoHorario;
    }

    public synchronized void setPassageirosForaDoHorario(int passageirosForaDoHorario) {
        this.passageirosForaDoHorario = passageirosForaDoHorario;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("Numero: ").append(getNumero()).append("|");
        str.append("Destino: ").append(getDestino()).append("|");
        str.append("Chegada: ").append(getChegadaPrevista()).append("|");
        str.append("Partida: ").append(getPartidaPrevista()).append("|");
        str.append("Porta: ").append(getTerminal()).append("|");
        str.append("Capacidade: ").append(getCapacidade());

        return str.toString();

    }
}












