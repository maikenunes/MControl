package net.maikenunes.mcontrol.database.contract;

import android.provider.BaseColumns;

public final class EncontroContract {

    //Criando um construtor vazio para garantir que ninguém vai instanciar a classe PessoaContract
    public EncontroContract(){}

    //Classe interna que define uma tabela e seu conteudo
    public static abstract class Encontro implements BaseColumns {
        public final static String TABLE_NAME = "encontros";
        public final static String DATA_INI   = "data_ini";
        public final static String DATA_FIM   = "data_fim";
        public final static String EVENTO     = "evento_id";

    }
}
