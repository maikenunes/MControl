package net.maikenunes.mcontrol.entity;

import java.util.Date;

/**
 * Created by Maike Nunes on 11/05/2015.
 */
public class Encontro {
    private int id;
    private int evenId;
    private Date dataHoraIni;
    private Date dataHoraFim;
    private Evento evento;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEvenId() {
        return evenId;
    }

    public void setEvenId(int evenId) {
        this.evenId = evenId;
    }

    public Date getDataHoraIni() {
        return dataHoraIni;
    }

    public void setDataHoraIni(Date dataHoraIni) {
        this.dataHoraIni = dataHoraIni;
    }

    public Date getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(Date dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }
}
