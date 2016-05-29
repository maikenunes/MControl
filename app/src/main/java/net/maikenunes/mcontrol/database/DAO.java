package net.maikenunes.mcontrol.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.maikenunes.mcontrol.database.contract.CategoriaContract;
import net.maikenunes.mcontrol.database.contract.EncontroContract;
import net.maikenunes.mcontrol.database.contract.EventoContract;
import net.maikenunes.mcontrol.database.contract.EventoPessoaContract;
import net.maikenunes.mcontrol.database.contract.PessoaContract;
import net.maikenunes.mcontrol.database.contract.PresencaContract;

public class DAO extends SQLiteOpenHelper {

    private static final String SQL_CREATE_TABLE_CATEGORIA =
            "CREATE TABLE " + CategoriaContract.Categoria.TABLE_NAME + " ( " +
                    CategoriaContract.Categoria._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CategoriaContract.Categoria.NOME + " TEXT NOT NULL, " +
                    CategoriaContract.Categoria.DELETADO + " CHAR NOT NULL DEFAULT 'N' " +
                    " )";
    private static final String SQL_DELETE_TABLE_CATEGORIA = "DROP TABLE IF EXISTS " + CategoriaContract.Categoria.TABLE_NAME;


    private static final String SQL_CREATE_TABLE_PESSOA =
            "CREATE TABLE " + PessoaContract.Pessoa.TABLE_NAME + " ( " +
                    PessoaContract.Pessoa._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PessoaContract.Pessoa.NOME + " TEXT NOT NULL, " +
                    PessoaContract.Pessoa.EMAIL + " TEXT NOT NULL, " +
                    PessoaContract.Pessoa.TELEFONE + " TEXT, " +
                    PessoaContract.Pessoa.MATRICULA + " INT, " +
                    PessoaContract.Pessoa.FOTO + " TEXT, " +
                    PessoaContract.Pessoa.FOTO_FACIAL + " TEXT, " +
                    PessoaContract.Pessoa.CATEGORIA + " INT NOT NULL, " +
                    PessoaContract.Pessoa.DELETADO + " CHAR NOT NULL DEFAULT 'N' " +
                    //", FOREIGN KEY (categoria_id) REFERENCES categorias(id) " +
                    " )";
    private static final String SQL_DELETE_TABLE_PESSOA = "DROP TABLE IF EXISTS " + PessoaContract.Pessoa.TABLE_NAME;


    private static final String SQL_CREATE_TABLE_EVENTO =
            "CREATE TABLE " + EventoContract.Evento.TABLE_NAME + " ( " +
                    EventoContract.Evento._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EventoContract.Evento.NOME + " TEXT NOT NULL, " +
                    EventoContract.Evento.DESCRICAO + " TEXT, " +
                    EventoContract.Evento.DATA_INI + " DATETIME NOT NULL, " +
                    EventoContract.Evento.DATA_FIM + " DATETIME NOT NULL, " +
                    EventoContract.Evento.DELETADO + " CHAR NOT NULL DEFAULT 'N' " +
                    " )";
    private static final String SQL_DELETE_TABLE_EVENTO = "DROP TABLE IF EXISTS " + EventoContract.Evento.TABLE_NAME;


    private static final String SQL_CREATE_TABLE_ENCONTRO =
            "CREATE TABLE " + EncontroContract.Encontro.TABLE_NAME + " ( " +
                    EncontroContract.Encontro._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EncontroContract.Encontro.DATA_INI + " DATETIME NOT NULL, " +
                    EncontroContract.Encontro.DATA_FIM + " DATETIME NOT NULL, " +
                    EncontroContract.Encontro.EVENTO + " INT NOT NULL " +
                   // ", FOREIGN KEY (evento_id) REFERENCES eventos(id) " +
                    " )";
    private static final String SQL_DELETE_TABLE_ENCONTRO = "DROP TABLE IF EXISTS " + EncontroContract.Encontro.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_PRESENCA =
            "CREATE TABLE " + PresencaContract.Presenca.TABLE_NAME + " ( " +
                    PresencaContract.Presenca._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PresencaContract.Presenca.DATA_ENTRADA + " DATETIME NOT NULL, " +
                    PresencaContract.Presenca.DATA_SAIDA + " DATETIME NOT NULL, " +
                    PresencaContract.Presenca.PESSOA + " INT NOT NULL, " +
                    PresencaContract.Presenca.ENCONTRO + " INT NOT NULL " +
                    //", FOREIGN KEY (encontro_id) REFERENCES encontros(id), " +
                    //", FOREIGN KEY (pessoa_id) REFERENCES pessoas(id) " +
                    " )";
    private static final String SQL_DELETE_TABLE_PRESENCA = "DROP TABLE IF EXISTS " + PresencaContract.Presenca.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_EVENTO_PESSOA =
            "CREATE TABLE " + EventoPessoaContract.EventoPessoa.TABLE_NAME + " ( " +
                    EventoPessoaContract.EventoPessoa.PESSOA + " INT NOT NULL, " +
                    EventoPessoaContract.EventoPessoa.EVENTO + " INT NOT NULL, " +
                    EventoPessoaContract.EventoPessoa.COD_EXTERNO + " TEXT, " +
                    EventoPessoaContract.EventoPessoa.DATA_CADASTRO + " DATETIME NOT NULL " +
                    " )";
    private static final String SQL_DELETE_TABLE_EVENTO_PESSOA = "DROP TABLE IF EXISTS " + EventoPessoaContract.EventoPessoa.TABLE_NAME;

    // Se você modificar o schema do banco, você deve incrementar a versão do software.
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "MCONTROL.db";

    public DAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_CATEGORIA);
        // não sabia exatamente onde colocar isso, já que nao
        db.execSQL("INSERT INTO categorias (nome, deletado) VALUES ( 'Aluno', 'N' )");
        db.execSQL("INSERT INTO categorias (nome, deletado) VALUES ( 'Professor', 'N' )");
        db.execSQL("INSERT INTO categorias (nome, deletado) VALUES ( 'Visitante', 'N' )");
        db.execSQL(SQL_CREATE_TABLE_PESSOA);
        db.execSQL(SQL_CREATE_TABLE_EVENTO);
        db.execSQL(SQL_CREATE_TABLE_ENCONTRO);
        db.execSQL(SQL_CREATE_TABLE_PRESENCA);
        db.execSQL(SQL_CREATE_TABLE_EVENTO_PESSOA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_TABLE_CATEGORIA);
        db.execSQL(SQL_DELETE_TABLE_PESSOA);
        db.execSQL(SQL_DELETE_TABLE_EVENTO);
        db.execSQL(SQL_DELETE_TABLE_ENCONTRO);
        db.execSQL(SQL_DELETE_TABLE_PRESENCA);
        db.execSQL(SQL_DELETE_TABLE_EVENTO_PESSOA);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
