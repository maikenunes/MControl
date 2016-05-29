package net.maikenunes.mcontrol.entity;

/**
 * Created by Maike Nunes on 11/05/2015.
 */
public class Pessoa {
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private int matricula;
    private String foto;
    private String foto_facial;
    private int categoria;
    private String cod_origem;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getFotoFacial() {
        return foto_facial;
    }

    public void setFotoFacial(String foto_facial) {
        this.foto_facial = foto_facial;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCodOrigem() {
        return cod_origem;
    }

    public void setCodOrigem(String cod_origem) {
        this.cod_origem = cod_origem;
    }

    public String toString(){
        return ( this.getId() + " | " + this.getNome() + " - " + this.getEmail() );
    }

}
