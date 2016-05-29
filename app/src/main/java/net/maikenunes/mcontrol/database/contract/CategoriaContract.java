package net.maikenunes.mcontrol.database.contract;

import android.provider.BaseColumns;

public final class CategoriaContract {

    //Criando um construtor vazio para garantir que ninguém vai instanciar a classe PessoaContract
    public CategoriaContract(){}

    //Classe interna que define uma tabela e seu conteudo
    public static abstract class Categoria implements BaseColumns {
        public final static String TABLE_NAME = "categorias";
        public final static String NOME       = "nome";
        public final static String DELETADO   = "deletado";


    }
}
