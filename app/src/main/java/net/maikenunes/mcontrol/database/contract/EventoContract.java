package net.maikenunes.mcontrol.database.contract;

import android.provider.BaseColumns;

public final class EventoContract {

    //Criando um construtor vazio para garantir que ninguém vai instanciar a classe PessoaContract
    public EventoContract(){}

    //Classe interna que define uma tabela e seu conteudo
    public static abstract class Evento implements BaseColumns {
        public final static String TABLE_NAME = "eventos";
        public final static String NOME       = "nome";
        public final static String DATA_INI   = "data_ini";
        public final static String DATA_FIM   = "data_fim";
        public final static String DESCRICAO  = "descricao";
        public final static String DELETADO   = "deletado";


    }
}
