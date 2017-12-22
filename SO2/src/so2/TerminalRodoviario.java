/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so2;

import Transportes.Autocarro;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class TerminalRodoviario {

    private static final int PLATAFORMAS = 3;

    private ArrayList<Autocarro> autocarros;
    private int[] plataformaOcupada;
    private Central central;
    private Autocarro[] autocarroParado;

    public TerminalRodoviario(Central central) {
        this.autocarros = new ArrayList<>();
        this.plataformaOcupada = new int[PLATAFORMAS];
        this.central = central;
        this.autocarroParado = new Autocarro[PLATAFORMAS];
    }

    public ArrayList<Autocarro> getAutocarros() {
        return autocarros;
    }

    public void setAutocarros(ArrayList<Autocarro> autocarros) {
        this.autocarros = autocarros;
    }

    public int[] getPlataformaOcupada() {
        return plataformaOcupada;
    }

    public void setPlataformaOcupada(int[] plataformaOcupada) {
        this.plataformaOcupada = plataformaOcupada;
    }

    public Central getCentral() {
        return central;
    }

    public void setCentral(Central central) {
        this.central = central;
    }

    public Autocarro[] getAutocarroParado() {
        return autocarroParado;
    }

    public void setAutocarroParado(Autocarro[] autocarroParados) {
        this.autocarroParado = autocarroParados;
    }

    /**
     * Método para a criação de autocarros a partir de um ficheiro txt
     *
     * @throws IOException exceçao
     */
    public void criarAutocarros() throws IOException {

        this.autocarros.add(new Autocarro(this.central, this));

        File readingFile = new File("autocarros.txt");
        try {
            FileReader fileReader = new FileReader(readingFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int i = 0;
            int j = 0;

            String line = "";
            while (line != null) {

                switch (j) {
                    case 1:
                        this.autocarros.get(i).setNumero(Integer.parseInt(line));
                        break;
                    case 2:
                        this.autocarros.get(i).setDestino(line);
                        break;
                    case 3:
                        String str[] = line.split(":");
                        this.autocarros.get(i).setChegadaPrevista(LocalTime.of(Integer.parseInt(str[0]), Integer.parseInt(str[1])));
                        break;
                    case 4:
                        String str1[] = line.split(":");
                        this.autocarros.get(i).setPartidaPrevista(LocalTime.of(Integer.parseInt(str1[0]), Integer.parseInt(str1[1])));
                        break;
                    case 5:
                        this.autocarros.get(i).setTerminal(Integer.parseInt(line));
                        break;
                    case 6:
                        this.autocarros.get(i).setCapacidade(Integer.parseInt(line));
                        break;

                }

                line = bufferedReader.readLine();
                j++;

                if ("".equals(line)) {
                    this.autocarros.add(new Autocarro(this.central, this));
                    i++;
                    j = 0;

                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Método para imprirmi os horários dos autocarros
     */
    public void horarioAutocarros() {
        System.out.println("\n***Autocarros***");
        for (Autocarro auto : this.autocarros) {
            System.out.println(auto.toString());
        }
    }

    /**
     * Método para fazer o autocarro esperar até a pista estar livre
     */
    public synchronized void esperarAteLivre() {
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(TerminalRodoviario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método em poe o autocarro a dormir o tempo de tolerancia caso haja um
     * aviao a vir com passageiros para este autocarro
     *
     * @param autocarro autocarro que vai dormir
     */
    public void tolerancia(Autocarro autocarro) {
        System.out.println("Autocarro " + autocarro.getNumero() + "->Existe um avião a chegar com passageiros"
                + " tempo de tolerancia: 20 minutos");
        try {
            Thread.sleep(201);
        } catch (InterruptedException ex) {
            Logger.getLogger(TerminalRodoviario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método para adormecer a thread enquanto esta não puder partir
     */
    public void esperarAtePartir() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(TerminalRodoviario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que adormece a thread caso está ainda não possa estacionar
     */
    public void esperaParaEstacionar() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(TerminalRodoviario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que defini quando o autocarro chega ao terminal 1
     *
     * @param autocarro autocarro que estaciouna
     */
    public synchronized void chegadaTerminal1(Autocarro autocarro) {
        System.out.println(Thread.currentThread().getName() + " parou às " + central.getHoras());
        autocarro.setChegada(getCentral().getHoras());
        this.plataformaOcupada[0]++;
        this.autocarroParado[0] = autocarro;
        notifyAll();

    }

    /**
     * Método que defini quando o autocarro chega ao terminal 2
     *
     * @param autocarro autocarro que estaciouna
     */
    public synchronized void chegadaTerminal2(Autocarro autocarro) {
        System.out.println(Thread.currentThread().getName() + " parou às " + central.getHoras());
        autocarro.setChegada(getCentral().getHoras());
        this.plataformaOcupada[1]++;
        this.autocarroParado[1] = autocarro;
        notifyAll();

    }

    /**
     * Método que defini quando o autocarro chega ao terminal 3
     *
     * @param autocarro autocarro que estaciouna
     */
    public synchronized void chegadaTerminal3(Autocarro autocarro) {
        System.out.println(Thread.currentThread().getName() + " parou às " + central.getHoras());
        autocarro.setChegada(getCentral().getHoras());
        this.plataformaOcupada[2]++;
        this.autocarroParado[2] = autocarro;
        notifyAll();
    }

    /**
     * Método que define a partida do autocarro da plataforma i
     *
     * @param i plataforma
     * @param autocarro autocarro que vai partir
     */
    public synchronized void partida(int i, Autocarro autocarro) {
        System.out.println(Thread.currentThread().getName() + " partiu às " + central.getHoras());
        this.plataformaOcupada[i]--;
        this.autocarroParado[i] = null;
        autocarro.setPartida(getCentral().getHoras());
        autocarro.setAtrasoChegadaMinutos(ChronoUnit.MINUTES.between(autocarro.getChegadaPrevista(), autocarro.getChegada()));
        autocarro.setAtrasoPartidaMinutos(ChronoUnit.MINUTES.between(autocarro.getPartidaPrevista(), autocarro.getPartida()));
        notifyAll();
    }
    /**
     * Método boolean que devolve true caso a pltaforma não esteja ocupada
     * @param i plataforma
     * @return true se estiver livre = 0, false se estiver ocupada = 1 
     */
    public boolean terminalLivre(int i) {
        return this.plataformaOcupada[i] == 0;

    }

}
