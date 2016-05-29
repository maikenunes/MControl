package net.maikenunes.mcontrol.entity;

/**
 * Created by Maike Nunes on 20/05/2015.
 */
public class Relatorio {
    private Categoria categoria;
    private Encontro encontro;
    private Evento evento;
    private EventoPessoa ePessoa;
    private Pessoa pessoa;
    private Presenca presenca;


    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Encontro getEncontro() {
        return encontro;
    }

    public void setEncontro(Encontro encontro) {
        this.encontro = encontro;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public EventoPessoa getePessoa() {
        return ePessoa;
    }

    public void setePessoa(EventoPessoa ePessoa) {
        this.ePessoa = ePessoa;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Presenca getPresenca() {
        return presenca;
    }

    public void setPresenca(Presenca presenca) {
        this.presenca = presenca;
    }
}
