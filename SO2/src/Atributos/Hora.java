
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Atributos;

import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import so2.Central;

/**
 *Classe que vai controlar as horas de toda a simulação
 * @author Miguel
 */
public class Hora extends Thread {

    private LocalTime time;
    private Central central;

    public Hora(LocalTime time, Central central) {
        this.time = time;
        this.central = central;
    }

    public LocalTime getTime() {
        return time;
    }

    public LocalTime horaInicial() {
        return time;
    }

    public Central getCentral() {
        return central;
    }
    /**
     * Método que vai diz qual o funcionamente da thread horas
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Hora.class.getName()).log(Level.SEVERE, null, ex);
            }

            time = time.plusMinutes(10);
            System.out.println(time);

            if (time.getHour() == 0 && time.getMinute() == 0){
               System.out.println(central.toString());
                System.exit(0);
            }

        }

    }

    @Override
    public String toString() {
        return getTime().toString();
    }
}
