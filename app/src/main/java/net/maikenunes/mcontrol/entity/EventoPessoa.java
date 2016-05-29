package net.maikenunes.mcontrol.entity;

import java.util.Date;

/**
 * Created by Maike Nunes on 18/05/2015.
 */
public class EventoPessoa {
    private int evento;
    private int pessoa;
    private Date data;
    private String cod_origem;

    public EventoPessoa(){
        this.setData(new Date());
    }

    public int getEvento() {
        return evento;
    }

    public void setEvento(int evento) {
        this.evento = evento;
    }

    public int getPessoa() {
        return pessoa;
    }

    public void setPessoa(int pessoa) {
        this.pessoa = pessoa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCodOrigem() {
        return cod_origem;
    }

    public void setCodOrigem(String cod_origem) {
        this.cod_origem = cod_origem;
    }
}
