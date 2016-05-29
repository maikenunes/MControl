package net.maikenunes.mcontrol.database.contract;

import android.provider.BaseColumns;

public final class PresencaContract {

    //Criando um construtor vazio para garantir que ninguém vai instanciar a classe PessoaContract
    public PresencaContract(){}

    //Classe interna que define uma tabela e seu conteudo
    public static abstract class Presenca implements BaseColumns {
        public final static String TABLE_NAME   = "presencas";
        public final static String DATA_ENTRADA = "data_entrada";
        public final static String DATA_SAIDA   = "data_saida";
        public final static String ENCONTRO     = "encontro_id";
        public final static String PESSOA       = "pessoa_id";
    }
}
