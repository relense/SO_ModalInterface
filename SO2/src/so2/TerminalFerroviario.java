/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so2;

import Transportes.Comboio;
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
public class TerminalFerroviario {

    private static final int PLATAFORMAS = 2;

    private ArrayList<Comboio> comboios;
    private int[] plataformaOcupada;
    private Comboio[] comboioParado;
    private Central central;

    public TerminalFerroviario(Central central) {
        this.comboios = new ArrayList<>();
        this.plataformaOcupada = new int[PLATAFORMAS];
        this.central = central;
        this.comboioParado = new Comboio[PLATAFORMAS];

    }

    public ArrayList<Comboio> getComboios() {
        return comboios;
    }

    public void setComboios(ArrayList<Comboio> comboios) {
        this.comboios = comboios;
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

    public Comboio[] getComboioParado() {
        return comboioParado;
    }

    public void setComboioParado(Comboio[] comboioParado) {
        this.comboioParado = comboioParado;
    }

    /**
     * Método para criar comboios a partir de um ficheiro txt
     *
     * @throws IOException exceção
     */
    public void criarComboios() throws IOException {

        this.comboios.add(new Comboio(this.central, this));

        File readingFile = new File("comboios.txt");
        try {
            FileReader fileReader = new FileReader(readingFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int i = 0;
            int j = 0;

            String line = "";
            while (line != null) {
                switch (j) {
                    case 1:
                        this.comboios.get(i).setNumero(Integer.parseInt(line));
                        break;
                    case 2:
                        this.comboios.get(i).setDestino(line);
                        break;
                    case 3:
                        String str[] = line.split(":");
                        this.comboios.get(i).setChegadaPrevista(LocalTime.of(Integer.parseInt(str[0]), Integer.parseInt(str[1])));
                        break;
                    case 4:
                        String str1[] = line.split(":");
                        this.comboios.get(i).setPartidaPrevista(LocalTime.of(Integer.parseInt(str1[0]), Integer.parseInt(str1[1])));
                        break;
                    case 5:
                        this.comboios.get(i).setTerminal(Integer.parseInt(line));
                        break;
                    case 6:
                        this.comboios.get(i).setCapacidade(Integer.parseInt(line));
                        break;

                }

                line = bufferedReader.readLine();
                j++;

                if ("".equals(line)) {

                    this.comboios.add(new Comboio(this.central, this));
                    i++;
                    j = 0;

                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Método para que caso uma thread queira estacionar se a plataforma estiver
     * ocupada fica à espera
     */
    public synchronized void esperaAteLivre() {
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(TerminalFerroviario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método para adormecer a thread caso ainda exista uma aviao com pasageiros
     * a chegar
     *
     * @param comboio comboio que dorme
     */
    public void tolerancia(Comboio comboio) {
        System.out.println("Comboio " + comboio.getNumero() + "->Existe um avião a chegar com passageiros"
                + " tempo de tolerancia: 10 minutos");
        try {
            Thread.sleep(101);
        } catch (InterruptedException ex) {
            Logger.getLogger(TerminalFerroviario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método para adormecer o comboio caso ainda não seja a hora de partida
     */
    public void esperarAtePartir() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(TerminalFerroviario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método para adormecer o comboio caso este ainda não posso estacionar
     */
    public void esperarParaEstacionar() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(TerminalFerroviario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método para imprimir o horário dos comboios
     */
    public void horarioComboios() {
        System.out.println("\n***Comboios***");
        this.comboios.stream().forEach((comboio) -> {
            System.out.println(comboio.toString());
        });
    }

    /**
     * Método para registar que um comboio chegou à 1 plataforma
     *
     * @param comboio comboio que chegou
     */
    public synchronized void chegadaTerminal1(Comboio comboio) {
        System.out.println(Thread.currentThread().getName() + " parou às " + central.getHoras());
        comboio.setChegada(getCentral().getHoras());
        this.plataformaOcupada[0]++;
        this.comboioParado[0] = comboio;
        notifyAll();
    }

    /**
     * Método para registar que um comboio chegou à 2 plataforma
     *
     * @param comboio comboio que chegou
     */
    public synchronized void chegadaTerminal2(Comboio comboio) {
        System.out.println(Thread.currentThread().getName() + " parou às " + central.getHoras());
        comboio.setChegada(getCentral().getHoras());
        this.plataformaOcupada[1]++;
        this.comboioParado[1] = comboio;
        notifyAll();
    }

    /**
     * Método que ser para registar a partida de um comboio
     *
     * @param i numero da plataforma
     * @param comboio que vai partir
     */
    public synchronized void partida(int i, Comboio comboio) {
        System.out.println(Thread.currentThread().getName() + " partiu às " + central.getHoras());
        comboio.setPartida(getCentral().getHoras());
        this.plataformaOcupada[i]--;
        this.comboioParado[i] = null;
        comboio.setAtrasoChegadaMinutos(ChronoUnit.MINUTES.between(comboio.getChegadaPrevista(), comboio.getChegada()));
        comboio.setAtrasoPartidaMinutos(ChronoUnit.MINUTES.between(comboio.getPartidaPrevista(), comboio.getPartida()));
        notifyAll();
    }

    /**
     * Método que devolve true caso a plataforma esteja livre
     *
     * @param i numero da plataforma
     * @return true se estiver livre ou seja igual a 0 ou false se estiver
     * ocupada
     */
    public boolean terminalLivre(int i) {
        return this.plataformaOcupada[i] == 0;

    }

}
