package net.maikenunes.mcontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import net.maikenunes.mcontrol.database.contract.EncontroContract;
import net.maikenunes.mcontrol.database.contract.EventoContract;
import net.maikenunes.mcontrol.database.contract.EventoPessoaContract;
import net.maikenunes.mcontrol.database.contract.PessoaContract;
import net.maikenunes.mcontrol.entity.Encontro;
import net.maikenunes.mcontrol.entity.Evento;
import net.maikenunes.mcontrol.entity.EventoPessoa;
import net.maikenunes.mcontrol.entity.Pessoa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventoEncontroDAO extends DAO {
    private Context contexto;
    public EventoEncontroDAO(Context context) {
        // chama o construtor da classe DAO
        super(context);
        this.contexto = context;
    }

    public Encontro getById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                EncontroContract.Encontro._ID,
                EncontroContract.Encontro.EVENTO,
                EncontroContract.Encontro.DATA_INI,
                EncontroContract.Encontro.DATA_FIM
        };

        //Colunas para filtrar no WHERE da query
        String selection = EncontroContract.Encontro._ID + " = ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(id)
        };

        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder =
                EncontroContract.Encontro._ID + " DESC";

        Cursor cursorSearch = db.query(
                EncontroContract.Encontro.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Encontro encontro = new Encontro();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            encontro.setId(Integer.parseInt(cursorSearch.getString(0)));
            encontro.setEvenId(Integer.parseInt(cursorSearch.getString(1)));
            try {
                encontro.setDataHoraIni(formatter.parse(cursorSearch.getString(2)));
                encontro.setDataHoraFim(formatter.parse(cursorSearch.getString(3)));
            }catch (Exception e){

            }
        }
        return encontro;
    }

    public List<Encontro> getByEvent(int even){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                EncontroContract.Encontro._ID,
                EncontroContract.Encontro.EVENTO,
                EncontroContract.Encontro.DATA_INI,
                EncontroContract.Encontro.DATA_FIM
        };

        //Colunas para filtrar no WHERE da query
        String selection = EncontroContract.Encontro.EVENTO + " = ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
            String.valueOf(even)
        };
        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder = EncontroContract.Encontro._ID + " DESC";

        Cursor cursorSearch = db.query(
                EncontroContract.Encontro.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder
        );
        List<Encontro> encontros = new ArrayList<Encontro>();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            Encontro enc_temp;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            do {
                enc_temp = new Encontro();
                enc_temp.setId(Integer.parseInt(cursorSearch.getString(0)));
                enc_temp.setEvenId(Integer.parseInt(cursorSearch.getString(1)));
                try {
                    enc_temp.setDataHoraIni(formatter.parse(cursorSearch.getString(2)));
                    enc_temp.setDataHoraFim(formatter.parse(cursorSearch.getString(3)));
                }catch (Exception e){

                }

                encontros.add(enc_temp);
            } while (cursorSearch.moveToNext());

        }
        return encontros;
    }

    public long insert(Encontro encontro){

        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        //Criando um mapa de informações para serem gravadas no bnaco - conjunto de chave-valor
        ContentValues encontroContent = new ContentValues();
        encontroContent.put(EncontroContract.Encontro.EVENTO, encontro.getEvenId());
        try {
            encontroContent.put(EncontroContract.Encontro.DATA_INI,  formatter.format(encontro.getDataHoraIni()));
            encontroContent.put(EncontroContract.Encontro.DATA_FIM,  formatter.format(encontro.getDataHoraFim()));
        }catch (Exception e){
            Log.i("TAG", "erro :( : " + e.getMessage());
        }

        try{
            return db.insert(
                    EncontroContract.Encontro.TABLE_NAME,
                    null,   //The second argument provides the name of a column in which the framework can insert NULL in the event that the ContentValues is empty (if you instead set this to "null", then the framework will not insert a row when there are no values).
                    encontroContent);
        }catch (SQLiteException e){
            return 0;
        }

    }

    public long update(Encontro encontro) {
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues encontroContent = new ContentValues();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataInicial = formatter.format(encontro.getDataHoraIni());
        String dataFinal   = formatter.format(encontro.getDataHoraFim());

        encontroContent.put(EventoContract.Evento.DATA_INI,  dataInicial);
        encontroContent.put(EventoContract.Evento.DATA_FIM,  dataFinal);

        try {
            return db.update(
                    EncontroContract.Encontro.TABLE_NAME,
                    encontroContent,
                    EncontroContract.Encontro._ID + " = ? ",
                    new String[]{ String.valueOf(encontro.getId()) });
        }catch (SQLiteException ex){
            return 0;
        }
    }

    public boolean delete(int id){
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            int return_del = db.delete(
                    EncontroContract.Encontro.TABLE_NAME,
                    EncontroContract.Encontro.EVENTO+"= ? AND "+EncontroContract.Encontro._ID+" = ? ",
                    new String[]{ String.valueOf(id) });

            return (return_del != 0);
        }catch (SQLiteException ex){
            return false;
        }
    }

    public List<Encontro> getByEncontrosEvento(){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                EncontroContract.Encontro._ID,
                EncontroContract.Encontro.EVENTO,
                EncontroContract.Encontro.DATA_INI,
                EncontroContract.Encontro.DATA_FIM
        };

        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder = EncontroContract.Encontro.DATA_INI + " DESC";

        Cursor cursorSearch = db.query(
                EncontroContract.Encontro.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder
        );
        List<Encontro> encontros = new ArrayList<Encontro>();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            Encontro enc_temp;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

            EventoDAO evenDAO = new EventoDAO(this.contexto);

            do {
                enc_temp = new Encontro();
                enc_temp.setId(Integer.parseInt(cursorSearch.getString(0)));
                enc_temp.setEvenId(Integer.parseInt(cursorSearch.getString(1)));
                try {
                    enc_temp.setDataHoraIni(formatter.parse(cursorSearch.getString(2)));
                    enc_temp.setDataHoraFim(formatter.parse(cursorSearch.getString(3)));
                }catch (Exception e){

                }

                enc_temp.setEvento(evenDAO.getById(enc_temp.getEvenId()));

                encontros.add(enc_temp);
            } while (cursorSearch.moveToNext());

        }
        return encontros;
    }

}
