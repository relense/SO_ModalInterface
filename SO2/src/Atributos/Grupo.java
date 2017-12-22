/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Atributos;

import java.time.LocalTime;

/**
 *Classe que representa cada grupo de pessoas que irá chegar no aviao
 * @author Miguel
 */
public class Grupo {

    private int numeroPessoas;
    private String transporte;
    private String destino;
    private LocalTime horaEntrada;
    private boolean embarcou;
    private LocalTime horaChegada;
    private int numeroAviao;
    private long tempoEspera;

    public Grupo(int numeroPessoas, String transporte, String destino, int numeroAviao) {
        this.numeroPessoas = numeroPessoas;
        this.transporte = transporte;
        this.destino = destino;
        this.horaEntrada = null;
        this.embarcou = false;
        this.horaChegada = null;
        this.numeroAviao = numeroAviao;
        this.tempoEspera = 0;
    }

    public int getNumeroPessoas() {
        return numeroPessoas;
    }

    public void setNumeroPessoas(int numeroPessoas) {
        this.numeroPessoas = numeroPessoas;
    }

    public String getTransporte() {
        return transporte;
    }

    public void setTransporte(String transporte) {
        this.transporte = transporte;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalTime getHoraEntrada() {
        return horaEntrada;
    }

    public synchronized void setHoraEntrada(LocalTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public boolean isEmbarcou() {
        return embarcou;
    }

    public synchronized void setEmbarcou(boolean embarcou) {
        this.embarcou = embarcou;
    }

    public LocalTime getHoraChegada() {
        return horaChegada;
    }

    public synchronized void setHoraChegada(LocalTime horaChegada) {
        this.horaChegada = horaChegada;
    }

    public int getNumeroAviao() {
        return numeroAviao;
    }

    public void setNumeroAviao(int numeroAviao) {
        this.numeroAviao = numeroAviao;
    }

    public long getTempoEspera() {
        return tempoEspera;
    }

    public synchronized void setTempoEspera(long tempoEspera) {
        this.tempoEspera = tempoEspera;
    }

    
    
}
