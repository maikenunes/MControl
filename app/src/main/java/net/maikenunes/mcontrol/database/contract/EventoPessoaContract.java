package net.maikenunes.mcontrol.database.contract;

import android.provider.BaseColumns;

public final class EventoPessoaContract {

    //Criando um construtor vazio para garantir que ninguém vai instanciar a classe PessoaContract
    public EventoPessoaContract(){}

    //Classe interna que define uma tabela e seu conteudo
    public static abstract class EventoPessoa implements BaseColumns {
        public final static String TABLE_NAME    = "evento_pessoa";
        public final static String EVENTO        = "evento_id";
        public final static String PESSOA        = "pessoa_id";
        public final static String COD_EXTERNO   = "cod_externo";
        public final static String DATA_CADASTRO = "data_cadastro";


    }
}
