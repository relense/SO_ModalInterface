/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package so2;

import Atributos.Grupo;
import Atributos.Hora;
import Transportes.Autocarro;
import Transportes.Aviao;
import Transportes.Comboio;
import java.io.IOException;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Miguel
 */
public class Central {

    private Aeroporto aeroporto;
    private TerminalFerroviario terminalFerroviario;
    private TerminalRodoviario terminalRodoviario;
    private Hora horas;

    public Central() {
        this.aeroporto = new Aeroporto(this);
        this.terminalFerroviario = new TerminalFerroviario(this);
        this.terminalRodoviario = new TerminalRodoviario(this);

    }

    public Aeroporto getAeroporto() {
        return aeroporto;
    }

    public void setAeroporto(Aeroporto aeroporto) {
        this.aeroporto = aeroporto;
    }

    public TerminalFerroviario getTerminalFerroviario() {
        return terminalFerroviario;
    }

    public void setTerminalFerroviario(TerminalFerroviario terminalFerroviario) {
        this.terminalFerroviario = terminalFerroviario;
    }

    public TerminalRodoviario getTerminalRodoviario() {
        return terminalRodoviario;
    }

    public void setTerminalRodoviario(TerminalRodoviario terminalRodoviario) {
        this.terminalRodoviario = terminalRodoviario;
    }

    public LocalTime getHoras() {
        return horas.getTime();
    }

    public boolean hasGrupos() {
        return false;
    }

    /**
     * Método para começar a thread de horas
     *
     * @param tempo horas
     */
    public void startHoras(LocalTime tempo) {
        this.horas = new Hora(tempo, this);
        horas.start();
    }

    /**
     * Método boleano que return true caso haja um grupo num aviao por aterrar
     * que precise de apnhar o transporte
     *
     * @param destino para onde vai o grupo
     * @return true se houver passageiros false se não
     */
    public synchronized boolean devoEsperar(String destino) {
        for (Aviao a : aeroporto.getAvioes()) {
            if (a.getAterragemDiaria() != 1)
                for (Grupo g : a.getGrupos()) {
                    if (g.getDestino().equals(destino))
                        return true;

                }
        }
        return false;
    }

    /**
     * Método para criar os transportes
     */
    public void Criar() {

        try {
            this.aeroporto.criarAvioes();
            this.terminalFerroviario.criarComboios();
            this.terminalRodoviario.criarAutocarros();
        } catch (IOException ex) {
            Logger.getLogger(Central.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método para imprimir os horarios de todos os transportes criados
     */
    public void Horarios() {
        System.out.println("HORAS:" + horas.getTime());
        this.aeroporto.horarioAvioes();
        this.terminalFerroviario.horarioComboios();
        this.terminalRodoviario.horarioAutocarros();
    }

    /**
     * Método para saber quantos avioes descolaram
     *
     * @return numero de avioes que descolaram
     */
    public int descolaram() {
        int descolaram = 0;
        for (Aviao a : aeroporto.getAvioes()) {
            if (a.isDescolou())
                descolaram++;
        }

        return descolaram;
    }

    /**
     * Método para saber quantos avioes descolaram
     *
     * @return numero de avioes que não descolaram
     */
    public int naoDescolaram() {
        int naoDescolaram = 0;
        for (Aviao a : aeroporto.getAvioes()) {
            if (!a.isDescolou())
                naoDescolaram++;

        }
        return naoDescolaram;
    }

    /**
     * Método para embarcar os passageiros nos comboios
     *
     * @param grupo a embarcar
     * @param aviao a desembarcar
     */
    public synchronized void embarcarComboio(Grupo grupo, Aviao aviao) {
        for (Comboio c : terminalFerroviario.getComboioParado()) {
            if (c != null)
                if (grupo.isEmbarcou() != true && c.getCapacidade() >= c.getLugaresOcupados()
                        && (c.getLugaresOcupados() + grupo.getNumeroPessoas() <= c.getCapacidade()))
                    if (grupo.getDestino().equals(c.getDestino())) {
                        c.setLugaresOcupados(c.getLugaresOcupados() + grupo.getNumeroPessoas());
                        c.getGruposComboio().add(grupo);
                        grupo.setEmbarcou(true);
                        grupo.setHoraEntrada(getHoras());
                        grupo.setTempoEspera(ChronoUnit.MINUTES.between(grupo.getHoraChegada(), grupo.getHoraEntrada()));

                        System.out.println("O grupo de " + grupo.getDestino()
                                + " que veio no aviao " + aviao.getNumero()
                                + " entrou no comboio " + c.getNumero()
                                + " às " + grupo.getHoraEntrada());
                        System.out.println("Comboio " + c.getNumero()
                                + " com destino a " + c.getDestino() + " tem "
                                + c.getLugaresOcupados() + " pessoas");

                    }
        }
    }

    /**
     * Método para embarcar os passageiros nos autocarros
     *
     * @param grupo a embarcar
     * @param aviao que está a desembarcar
     */
    public synchronized void embarcarAutocarro(Grupo grupo, Aviao aviao) {
        for (Autocarro a : terminalRodoviario.getAutocarroParado()) {
            if (a != null)
                if (grupo.isEmbarcou() != true && a.getCapacidade() >= a.getLugaresOcupados()
                        && (a.getLugaresOcupados() + grupo.getNumeroPessoas() <= a.getCapacidade()))
                    if (grupo.getDestino().equals(a.getDestino())) {
                        a.setLugaresOcupados(a.getLugaresOcupados() + grupo.getNumeroPessoas());
                        a.getGruposAutocarro().add(grupo);
                        grupo.setEmbarcou(true);
                        grupo.setHoraEntrada(getHoras());
                        grupo.setTempoEspera(ChronoUnit.MINUTES.between(grupo.getHoraChegada(), grupo.getHoraEntrada()));

                        System.out.println("O grupo de " + grupo.getDestino()
                                + " que veio no aviao " + aviao.getNumero()
                                + " entrou no autocarro " + a.getNumero()
                                + " às " + grupo.getHoraEntrada());
                        System.out.println("Autocarro " + a.getNumero()
                                + " tem " + a.getLugaresOcupados() + " pessoas");

                    }
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("****AVIOES QUE NÃO DESEMBARCARAM TODOS OS PASSAGEIROS = ")
                .append(naoDescolaram()).append("****\n");
        for (Aviao a : aeroporto.getAvioes()) {

            if (!a.isDescolou() && a.getChegada() != null) {

                str.append("\nAviao ").append(a.getNumero()).append(" aterrou às ")
                        .append(a.getChegada()).append("\n");
                str.append("**********Informação dos grupos********** \n");
                for (Grupo g : a.getGrupos()) {
                    if (g.isEmbarcou())
                        str.append("Grupo de ").append(g.getDestino())
                                .append(" embarcou às ").append(g.getHoraEntrada())
                                .append("\n");
                    else
                        str.append("Grupo de ").append(g.getDestino())
                                .append(" não embarcou \n");
                }
                str.append("***************************************** \n");

            } else if (!a.isDescolou() && a.getChegada() == null) {

                str.append("\nAviao ").append(a.getNumero()).append(" não aterrou \n");
                str.append("**********Informação dos grupos********** \n");
                for (Grupo g : a.getGrupos()) {
                    str.append("Grupo de ").append(g.getDestino())
                            .append(" não embarcou \n");
                }
                str.append("*****************************************\n");
            }

        }
        str.append("\n****AVIOES QUE DSEMBARCARAM TODOS OS PASSAGEIROS = ")
                .append(descolaram()).append("**** \n");
        for (Aviao a : aeroporto.getAvioes()) {
            if (a.isDescolou()) {

                str.append("\nAviao ").append(a.getNumero()).append(" aterrou às ")
                        .append(a.getChegada()).append(" e partiu às ")
                        .append(a.getPartida()).append("\n");
                str.append("**********Informação dos grupos**********").append("\n");
                for (Grupo g : a.getGrupos()) {

                    if (g.isEmbarcou())
                        str.append("Grupo de ").append(g.getDestino())
                                .append(" embarcou às ").append(g.getHoraEntrada()).append("\n");

                }
                str.append("***************************************** \n");
            }
        }
        str.append("\n");
        str.append("******INFORMAÇÃO DOS COMBOIOS****** \n");
        for (Comboio comboio : terminalFerroviario.getComboios()) {
            if (comboio.getAtrasoChegadaMinutos() > 0) {

                str.append("\nComboio ").append(comboio.getNumero())
                        .append(" com destino a ").append(comboio.getDestino()).append(" chegou às ")
                        .append(comboio.getChegada()).append(" com um atraso de ")
                        .append(comboio.getAtrasoChegadaMinutos())
                        .append(" minutos partiu às ").append(comboio.getPartida())
                        .append(" com um atraso de ").append(comboio.getAtrasoPartidaMinutos())
                        .append(" minutos e com ").append(comboio.getLugaresOcupados())
                        .append(" passageiros \n");

                if (comboio.getLugaresOcupados() != 0) {
                    str.append("Grupos que embarcaram: \n");
                    for (Grupo grupo : comboio.getGruposComboio()) {
                        str.append("Grupo de ").append(grupo.getNumeroPessoas())
                                .append(" pessoas que chegou no avião ").append(grupo.getNumeroAviao())
                                .append(" entrou no comboio às ").append(grupo.getHoraEntrada())
                                .append(" e esperou ").append(grupo.getTempoEspera())
                                .append(" minutos \n");
                        if (grupo.getHoraEntrada() == grupo.getHoraChegada())
                            comboio.setPassageirosDentroDoHorario(comboio.getPassageirosDentroDoHorario() + grupo.getNumeroPessoas());
                        else
                            comboio.setPassageirosForaDoHorario(comboio.getPassageirosForaDoHorario() + grupo.getNumeroPessoas());

                    }
                    str.append("Numero de passageiros dentro do horario: ").append(comboio.getPassageirosDentroDoHorario()).append("\n");
                    str.append("Numero de passageiros fora do horario: ").append(comboio.getPassageirosForaDoHorario()).append("\n");

                } else
                    str.append("Não teve embarcações \n");

            } else {
                str.append("\nComboio ").append(comboio.getNumero())
                        .append(" com destino a ").append(comboio.getDestino())
                        .append(" chegou às ").append(comboio.getChegada())
                        .append(" a horas partiu às ").append(comboio.getPartida())
                        .append(" com um atraso de ").append(comboio.getAtrasoPartidaMinutos())
                        .append(" minutos e com ").append(comboio.getLugaresOcupados())
                        .append(" passageiros \n");
                if (comboio.getLugaresOcupados() != 0) {
                    str.append("Grupos que embarcaram:\n");
                    for (Grupo grupo : comboio.getGruposComboio()) {
                        str.append("Grupo de ").append(grupo.getNumeroPessoas())
                                .append(" pessoas que chegou no avião ").append(grupo.getNumeroAviao())
                                .append(" entrou no comboio às ").append(grupo.getHoraEntrada())
                                .append(" e esperou ").append(grupo.getTempoEspera())
                                .append(" minutos \n");
                        if (grupo.getHoraEntrada() == grupo.getHoraChegada())
                            comboio.setPassageirosDentroDoHorario(comboio.getPassageirosDentroDoHorario() + grupo.getNumeroPessoas());
                        else
                            comboio.setPassageirosForaDoHorario(comboio.getPassageirosForaDoHorario() + grupo.getNumeroPessoas());

                    }
                    str.append("Numero de passageiros dentro do horario: ").append(comboio.getPassageirosDentroDoHorario()).append("\n");
                    str.append("Numero de passageiros fora do horario: ").append(comboio.getPassageirosForaDoHorario()).append("\n");
                } else
                    str.append("Não teve embarcações \n");

            }

        }
        str.append("***************************************** \n");
        str.append("\n******INFORMAÇÃO DOS Autocarros****** \n");
        for (Autocarro autocarro : terminalRodoviario.getAutocarros()) {
            if (autocarro.getAtrasoChegadaMinutos() > 0) {

                str.append("\nAutocarro ").append(autocarro.getNumero())
                        .append(" com destino a ").append(autocarro.getDestino()).append(" chegou às ")
                        .append(autocarro.getChegada()).append(" com um atraso de ")
                        .append(autocarro.getAtrasoChegadaMinutos())
                        .append(" minutos partiu às ").append(autocarro.getPartida())
                        .append(" com um atraso de ").append(autocarro.getAtrasoPartidaMinutos())
                        .append(" minutos e com ").append(autocarro.getLugaresOcupados())
                        .append(" passageiros \n");

                if (autocarro.getLugaresOcupados() != 0) {
                    str.append("Grupos que embarcaram: \n");
                    for (Grupo grupo : autocarro.getGruposAutocarro()) {
                        str.append("Grupo de ").append(grupo.getNumeroPessoas())
                                .append(" pessoas que chegou no avião ").append(grupo.getNumeroAviao())
                                .append(" entrou no comboio às ").append(grupo.getHoraEntrada())
                                .append(" e esperou ").append(grupo.getTempoEspera())
                                .append(" minutos \n");
                        if (grupo.getHoraEntrada() == grupo.getHoraChegada())
                            autocarro.setPassageirosDentroDoHorario(autocarro.getPassageirosDentroDoHorario() + grupo.getNumeroPessoas());
                        else
                            autocarro.setPassageirosForaDoHorario(autocarro.getPassageirosForaDoHorario() + grupo.getNumeroPessoas());

                    }
                    str.append("Numero de passageiros dentro do horario: ").append(autocarro.getPassageirosDentroDoHorario()).append("\n");
                    str.append("Numero de passageiros fora do horario: ").append(autocarro.getPassageirosForaDoHorario()).append("\n");

                } else
                    str.append("Não teve embarcações \n");

            } else {
                str.append("\nAutocarro ").append(autocarro.getNumero())
                        .append(" com destino a ").append(autocarro.getDestino())
                        .append(" chegou às ").append(autocarro.getChegada())
                        .append(" a horas partiu às ").append(autocarro.getPartida())
                        .append(" com um atraso de ").append(autocarro.getAtrasoPartidaMinutos())
                        .append(" minutos e com ").append(autocarro.getLugaresOcupados())
                        .append(" passageiros \n");
                if (autocarro.getLugaresOcupados() != 0) {
                    str.append("Grupos que embarcaram:\n");
                    for (Grupo grupo : autocarro.getGruposAutocarro()) {
                        str.append("Grupo de ").append(grupo.getNumeroPessoas())
                                .append(" pessoas que chegou no avião ").append(grupo.getNumeroAviao())
                                .append(" entrou no comboio às ").append(grupo.getHoraEntrada())
                                .append(" e esperou ").append(grupo.getTempoEspera())
                                .append(" minutos \n");

                        if (grupo.getHoraEntrada() == grupo.getHoraChegada())
                            autocarro.setPassageirosDentroDoHorario(autocarro.getPassageirosDentroDoHorario() + grupo.getNumeroPessoas());
                        else
                            autocarro.setPassageirosForaDoHorario(autocarro.getPassageirosForaDoHorario() + grupo.getNumeroPessoas());

                    }
                    str.append("Numero de passageiros dentro do horario: ").append(autocarro.getPassageirosDentroDoHorario()).append("\n");
                    str.append("Numero de passageiros fora do horario: ").append(autocarro.getPassageirosForaDoHorario()).append("\n");

                } else
                    str.append("Não teve embarcações \n");

            }

        }
        str.append("***************************************** \n");
        return str.toString();
    }
}
