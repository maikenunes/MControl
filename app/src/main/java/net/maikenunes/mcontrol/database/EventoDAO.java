package net.maikenunes.mcontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import net.maikenunes.mcontrol.database.contract.EventoContract;
import net.maikenunes.mcontrol.database.contract.PessoaContract;
import net.maikenunes.mcontrol.entity.Evento;
import net.maikenunes.mcontrol.entity.Pessoa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maike Nunes on 17/05/2015.
 */
public class EventoDAO extends DAO {

    public EventoDAO(Context context) {
        super(context); // chama o construtor da classe DAO
    }

    public Evento getById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                EventoContract.Evento._ID,
                EventoContract.Evento.NOME,
                EventoContract.Evento.DATA_INI,
                EventoContract.Evento.DATA_FIM,
                EventoContract.Evento.DESCRICAO,
                EventoContract.Evento.DELETADO
        };

        //Colunas para filtrar no WHERE da query
        String selection = EventoContract.Evento._ID + " = ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(id)
        };

        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder =
                EventoContract.Evento._ID + " DESC";

        Cursor cursorSearch = db.query(
                EventoContract.Evento.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        Evento evento = new Evento();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            evento.setId(Integer.parseInt(cursorSearch.getString(0)));
            evento.setNome(cursorSearch.getString(1));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                evento.setDataHoraInicial(formatter.parse(cursorSearch.getString(2)));
                evento.setDataHoraFinal(formatter.parse(cursorSearch.getString(3)));
            }catch (Exception e){
                Log.i("TAG","erro :( : "+e.getMessage());
            }
            evento.setDescricao(cursorSearch.getString(4));
            evento.setDeletado(cursorSearch.getString(5));
        }
        return evento;
    }

    public List<Evento> getByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                EventoContract.Evento._ID,
                EventoContract.Evento.NOME,
                EventoContract.Evento.DATA_INI,
                EventoContract.Evento.DATA_FIM,
                EventoContract.Evento.DESCRICAO,
                EventoContract.Evento.DELETADO
        };

        //Colunas para filtrar no WHERE da query
        String selection = EventoContract.Evento.NOME + " like ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                name + "%"
        };
        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder = EventoContract.Evento._ID + " DESC";

        Cursor cursorSearch = db.query(
                EventoContract.Evento.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        List<Evento> eventos = new ArrayList<Evento>();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            Evento even_temp;
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            do {
                even_temp = new Evento();
                even_temp.setId(Integer.parseInt(cursorSearch.getString(0)));
                even_temp.setNome(cursorSearch.getString(1));

                try {
                    even_temp.setDataHoraInicial(formatter.parse(cursorSearch.getString(2)));
                    even_temp.setDataHoraFinal(formatter.parse(cursorSearch.getString(3)));
                }catch (Exception e){
                    Log.i("TAG","erro :( : "+e.getMessage());
                }
                even_temp.setDescricao(cursorSearch.getString(4));
                even_temp.setDeletado(cursorSearch.getString(5));

                eventos.add(even_temp);
            } while (cursorSearch.moveToNext());

        }
        return eventos;
    }

    public long insert(Evento evento){

        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();
        //Criando um mapa de informações para serem gravadas no bnaco - conjunto de chave-valor
        ContentValues eventoContent = new ContentValues();
        eventoContent.put(EventoContract.Evento.NOME,      evento.getNome());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataInicial = formatter.format(evento.getDataHoraInicial());
        String dataFinal   = formatter.format(evento.getDataHoraFinal());

        eventoContent.put(EventoContract.Evento.DATA_INI,  dataInicial);
        eventoContent.put(EventoContract.Evento.DATA_FIM,  dataFinal);
        eventoContent.put(EventoContract.Evento.DESCRICAO, evento.getDescricao());
        eventoContent.put(EventoContract.Evento.DELETADO,  evento.getDeletado());

        try{
            return db.insert(
                    EventoContract.Evento.TABLE_NAME,
                    null,   //The second argument provides the name of a column in which the framework can insert NULL in the event that the ContentValues is empty (if you instead set this to "null", then the framework will not insert a row when there are no values).
                    eventoContent);
        }catch (SQLiteException e){
            return 0;
        }

    }

    public long update(Evento evento) {
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues eventoContent = new ContentValues();
        eventoContent.put(EventoContract.Evento.NOME,      evento.getNome());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataInicial = formatter.format(evento.getDataHoraInicial());
        String dataFinal   = formatter.format(evento.getDataHoraFinal());

        eventoContent.put(EventoContract.Evento.DATA_INI,  dataInicial);
        eventoContent.put(EventoContract.Evento.DATA_FIM,  dataFinal);
        eventoContent.put(EventoContract.Evento.DESCRICAO, evento.getDescricao());
        eventoContent.put(EventoContract.Evento.DELETADO,  evento.getDeletado());

        try {
            return db.update(
                    EventoContract.Evento.TABLE_NAME,
                    eventoContent,
                    EventoContract.Evento._ID + " = ? ",
                    new String[]{ String.valueOf(evento.getId()) });
        }catch (SQLiteException ex){
            return 0;
        }
    }


    public int getCountEncontros(int even){
        SQLiteDatabase db = this.getReadableDatabase();

        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(even)
        };

        Cursor cursorSearch = db.rawQuery(
                " SELECT COUNT(1) as qtd " +
                        " FROM encontros " +
                        " WHERE evento_id = ? ",
                selectionArgs
        );
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            return cursorSearch.getInt(0);
        }
        return 0;
    }

}
