package net.maikenunes.mcontrol.entity;

/**
 * Created by Maike Nunes on 11/05/2015.
 */
public class Categoria {
    private int id;
    private String nome;
    private String deletado;

    public String toString(){
        return ( this.getId() + " | " + this.getNome() );
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

    public String getDeletado() {
        return deletado;
    }

    public void setDeletado(String deletado) {
        this.deletado = deletado;
    }
}
