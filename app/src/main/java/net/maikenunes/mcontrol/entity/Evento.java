package net.maikenunes.mcontrol.entity;

import java.util.Date;

/**
 * Created by Maike Nunes on 11/05/2015.
 */
public class Evento {
    private int id;
    private String nome;
    private Date dataHoraInicial;
    private Date dataHoraFinal;
    private int qtdEncontros;
    private String descricao;
    private String deletado;

    public Evento(){
        this.setDeletado("N");
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataHoraInicial() {
        return dataHoraInicial;
    }

    public void setDataHoraInicial(Date dataHoraInicial) {
        this.dataHoraInicial = dataHoraInicial;
    }

    public Date getDataHoraFinal() {
        return dataHoraFinal;
    }

    public void setDataHoraFinal(Date dataHoraFinal) {
        this.dataHoraFinal = dataHoraFinal;
    }

    public int getQtdEncontros() {
        return qtdEncontros;
    }

    public void setQtdEncontros(int qtdEncontros) {
        this.qtdEncontros = qtdEncontros;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getDeletado() {
        return deletado;
    }

    public void setDeletado(String deletado) {
        this.deletado = deletado;
    }

    public String toString(){
        return ( this.getId() + " | " + this.getNome() );
    }

}
