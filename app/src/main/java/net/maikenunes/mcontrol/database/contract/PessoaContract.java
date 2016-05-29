package net.maikenunes.mcontrol.database.contract;

import android.provider.BaseColumns;

public final class PessoaContract {

    //Criando um construtor vazio para garantir que ninguém vai instanciar a classe PessoaContract
    public PessoaContract(){}

    //Classe interna que define uma tabela e seu conteudo
    public static abstract class Pessoa implements BaseColumns {
        public final static String TABLE_NAME  = "pessoas";
        public final static String NOME        = "nome";
        public final static String EMAIL       = "email";
        public final static String TELEFONE    = "telefone";
        public final static String MATRICULA   = "matricula";
        public final static String FOTO        = "foto_path";
        public final static String FOTO_FACIAL = "foto_facial_path";
        public final static String CATEGORIA   = "categoria_id";
        public final static String DELETADO    = "deletado";


    }
}
