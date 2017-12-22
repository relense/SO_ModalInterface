/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so2;

import Atributos.Grupo;
import Transportes.Aviao;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class Aeroporto {

    private static final int MAX_PISTAS = 3;

    private ArrayList<Aviao> avioes;
    private int[] avioesAterrados;
    private Central central;

    public Aeroporto(Central central) {
        this.avioes = new ArrayList<>();
        this.avioesAterrados = new int[MAX_PISTAS];
        this.central = central;

    }

    public ArrayList<Aviao> getAvioes() {
        return avioes;
    }

    public void setAvioes(ArrayList<Aviao> avioes) {
        this.avioes = avioes;
    }

    public Central getCentral() {
        return central;
    }

    public void setCentral(Central central) {
        this.central = central;
    }

    public int[] getAvioesAterrados() {
        return avioesAterrados;
    }

    public void setAvioesAterrados(int[] avioesAterrados) {
        this.avioesAterrados = avioesAterrados;
    }

    /**
     * Método para definir a hora de chegada dos grupos
     *
     * @param grupos grupos que chegaram
     */
    public synchronized void horaDeChegadaGrupos(ArrayList<Grupo> grupos) {
        for (Grupo g : grupos) {
            g.setHoraChegada(getCentral().getHoras());
        }

    }

    /**
     * Método boolean se indica se na plataforma i existe um aviao aterrado
     *
     * @param i numero da plataforma
     * @return true se a pista estiver livre = 0 false se = 1
     */
    public synchronized boolean pistaLivre(int i) {
        return this.avioesAterrados[i] == 0;
    }

    /**
     * Método para aterrar um aviao na plataforma 1
     *
     * @param aviao aviao que vai aterrar
     */
    public synchronized void aterra1(Aviao aviao) {
        System.out.println(Thread.currentThread().getName() + " aterrou às " + central.getHoras() + " na pista " + aviao.getTerminal());
        this.avioesAterrados[0]++;
        horaDeChegadaGrupos(aviao.getGrupos());

    }

    /**
     * Método para aterrar um aviao na plataforma 2
     *
     * @param aviao aviao que vai aterrar
     */
    public synchronized void aterra2(Aviao aviao) {
        System.out.println(Thread.currentThread().getName() + " aterrou às " + central.getHoras() + " na pista " + aviao.getTerminal());
        this.avioesAterrados[1]++;
        horaDeChegadaGrupos(aviao.getGrupos());

    }

    /**
     * Método para aterrar um aviao na plataforma 3
     *
     * @param aviao aviao que vai aterrar
     */
    public synchronized void aterra3(Aviao aviao) {
        System.out.println(Thread.currentThread().getName() + " aterrou às " + central.getHoras() + " na pista " + aviao.getTerminal());
        this.avioesAterrados[2]++;
        horaDeChegadaGrupos(aviao.getGrupos());

    }

    /**
     * Método para que o avião possa partir do aeroporto
     *
     * @param i plataforma de onde o aviao vai partir
     * @param aviao aviao a partir
     */
    public synchronized void partida(int i, Aviao aviao) {
        System.out.println(Thread.currentThread().getName() + " partiu às " + central.getHoras() + " da pista " + (i + 1));
        this.avioesAterrados[i]--;
        aviao.setDescolou(true);
        notifyAll();
    }

    /**
     * Método para limpar o aviao durante 30min
     */
    public synchronized void LimparAviao() {
        System.out.println("Todos os passageiros já sairam do " + Thread.currentThread().getName());
        System.out.println("Em limpeza");

        try {
            wait(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeroporto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que vai confirmar se todos os grupos existentes num avião já
     * embarcaram ou nos comboios ou nos autocarros
     *
     * @param grupo grupo
     * @param aviao aviao a desembarcar
     */
    public synchronized void gruposEmbarcaram(ArrayList<Grupo> grupo, Aviao aviao) {
        for (Grupo g : grupo) {
            if (g.isEmbarcou() == true) {
                aviao.setTodosEmbarcaram(true);
            } else {
                aviao.setTodosEmbarcaram(false);
                break;
            }
        }

    }

    /**
     * Método que devolve quantos grupos ainda precisam de embarcar
     *
     * @param grupos grupos que precisam de embarcar
     * @return quantos faltam
     */
    public synchronized int gruposFaltam(ArrayList<Grupo> grupos) {
        int faltam = 0;
        for (Grupo g : grupos) {
            if (g.isEmbarcou() == false) {
                faltam++;
            }

        }
        return faltam;
    }

    /**
     * Método boolean que retorna true caso o aviao já tenha aterrado
     *
     * @param aviao que está a tentar aterrar
     * @return true se já aterrou = 1 false se não aterrou = 0
     */
    public synchronized boolean jaAterrou(Aviao aviao) {
        return aviao.getAterragemDiaria() == 1;

    }

    /**
     * Método caso a pista de aterragem não esteja livre mete a thread a dormir
     */
    public synchronized void esperaPorPistaLivre() {

        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeroporto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método caso haja grupos no aviao e não haja autocarros ou comboios
     * disponiveis o aviao dorme
     */
    public void esperaPorComboiosOuAutocarros() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeroporto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que diz ao aviao para dormir se ainda não pode aterrar
     */
    public void esperaPorHoraDeAterrar() {

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Aeroporto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que cria os aviões a partir de um ficheiro
     *
     * @throws IOException
     */
    public void criarAvioes() throws IOException {
        this.avioes.add(new Aviao(this, central));
        File readingFile = new File("voos.txt");
        try {
            FileReader fileReader = new FileReader(readingFile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int i = 0;//váriavel de criação 
            int j = 0;//numero de linha

            String line = "";
            while (line != null) {

                switch (j) {
                    case 0:
                        break;
                    case 1://define o numero do aviao
                        this.avioes.get(i).setNumero(Integer.parseInt(line));
                        break;
                    case 2://define o destino
                        this.avioes.get(i).setOrigem(line);
                        break;
                    case 3://define a hora de chegada
                        String str5[] = line.split(":");
                        this.avioes.get(i).setChegadaPrevista(LocalTime.of(Integer.parseInt(str5[0]), Integer.parseInt(str5[1])));
                        break;
                    case 4://defini a porta de desenbarque
                        this.avioes.get(i).setTerminal(Integer.parseInt(line));
                        break;
                    default://define um grupo de passageiros
                        String str[] = line.split(";");
                        this.avioes.get(i).getGrupos().add(new Grupo(Integer.parseInt(str[0]), str[1], str[2], this.avioes.get(i).getNumero()));
                        break;
                }

                line = bufferedReader.readLine();//lê uma linha
                j++;//cada vez que lê uma linha incrementa

                //se for igual quer dizer que não existe mais informação deste aviao
                if ("".equals(line)) {
                    this.avioes.add(new Aviao(this, this.central)); //cria-se um novo avião
                    i++;
                    j = 0;//volta a zera porque vamos começar a ler outro avião
                }

            }
        } catch (IOException e) {
            System.out.println(e.getMessage());

        }

    }

    /**
     * Método para imprimir os horarios dos avioes
     */
    public void horarioAvioes() {
        System.out.println("***Avioes***");
        for (Aviao avioe : this.avioes) {
            System.out.println(avioe.toString());
        }
    }

}
