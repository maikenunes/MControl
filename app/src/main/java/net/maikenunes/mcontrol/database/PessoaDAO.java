package net.maikenunes.mcontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import net.maikenunes.mcontrol.database.contract.PessoaContract;
import net.maikenunes.mcontrol.entity.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class PessoaDAO extends DAO {

    public PessoaDAO(Context context) {
        super(context); // chama o construtor da classe DAO
    }

    public Pessoa getById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                PessoaContract.Pessoa._ID,
                PessoaContract.Pessoa.NOME,
                PessoaContract.Pessoa.EMAIL,
                PessoaContract.Pessoa.TELEFONE,
                PessoaContract.Pessoa.MATRICULA,
                PessoaContract.Pessoa.FOTO,
                PessoaContract.Pessoa.FOTO_FACIAL,
                PessoaContract.Pessoa.CATEGORIA
        };

        //Colunas para filtrar no WHERE da query
        String selection = PessoaContract.Pessoa._ID + " = ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(id)
        };

        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder =
                PessoaContract.Pessoa._ID + " DESC";

        Cursor cursorSearch = db.query(
                PessoaContract.Pessoa.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        Pessoa pessoa = new Pessoa();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            pessoa.setId(Integer.parseInt(cursorSearch.getString(0)));
            pessoa.setNome(cursorSearch.getString(1));
            pessoa.setEmail(cursorSearch.getString(2));
            pessoa.setTelefone(cursorSearch.getString(3));
            String matricula = cursorSearch.getString(4);
            if(!matricula.equals("")){
                pessoa.setMatricula(Integer.parseInt(matricula));
            }
            pessoa.setFoto(cursorSearch.getString(5));
            pessoa.setFotoFacial(cursorSearch.getString(6));
            pessoa.setCategoria(Integer.parseInt(cursorSearch.getString(7)));
        }
        return pessoa;
    }

    public Pessoa getByMatricula(int matriculaParam){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                PessoaContract.Pessoa._ID,
                PessoaContract.Pessoa.NOME,
                PessoaContract.Pessoa.EMAIL,
                PessoaContract.Pessoa.TELEFONE,
                PessoaContract.Pessoa.MATRICULA,
                PessoaContract.Pessoa.FOTO,
                PessoaContract.Pessoa.FOTO_FACIAL,
                PessoaContract.Pessoa.CATEGORIA
        };

        //Colunas para filtrar no WHERE da query
        String selection = PessoaContract.Pessoa.MATRICULA + " = ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(matriculaParam)
        };

        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder =
                PessoaContract.Pessoa._ID + " DESC";

        Cursor cursorSearch = db.query(
                PessoaContract.Pessoa.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        Pessoa pessoa = new Pessoa();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            pessoa.setId(Integer.parseInt(cursorSearch.getString(0)));
            pessoa.setNome(cursorSearch.getString(1));
            pessoa.setEmail(cursorSearch.getString(2));
            pessoa.setTelefone(cursorSearch.getString(3));
            String matricula = cursorSearch.getString(4);
            if(!matricula.equals("")){
                pessoa.setMatricula(Integer.parseInt(matricula));
            }
            pessoa.setFoto(cursorSearch.getString(5));
            pessoa.setFoto(cursorSearch.getString(6));
            pessoa.setCategoria(Integer.parseInt(cursorSearch.getString(7)));
        }
        return pessoa;
    }

    public Pessoa getNextById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                PessoaContract.Pessoa._ID,
                PessoaContract.Pessoa.NOME,
                PessoaContract.Pessoa.EMAIL,
                PessoaContract.Pessoa.TELEFONE,
                PessoaContract.Pessoa.MATRICULA,
                PessoaContract.Pessoa.FOTO,
                PessoaContract.Pessoa.FOTO_FACIAL,
                PessoaContract.Pessoa.CATEGORIA
        };

        //Colunas para filtrar no WHERE da query
        String selection = PessoaContract.Pessoa._ID + " > ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(id)
        };

        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder = PessoaContract.Pessoa._ID + " ASC ";

        Cursor cursorSearch = db.query(
                PessoaContract.Pessoa.TABLE_NAME,      // The table to query
                projection,                            // The columns to return
                selection,                             // The columns for the WHERE clause
                selectionArgs,                         // The values for the WHERE clause
                null,                                  // don't group the rows
                null,                                  // don't filter by row groups
                sortOrder,                             // The sort order
                "1"                                    // limit 1
        );

        Pessoa pessoa = new Pessoa();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            pessoa.setId(cursorSearch.getInt(0));
            pessoa.setNome(cursorSearch.getString(1));
            pessoa.setEmail(cursorSearch.getString(2));
        }

        return pessoa;
    }

    public Pessoa getLastById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                PessoaContract.Pessoa._ID,
                PessoaContract.Pessoa.NOME,
                PessoaContract.Pessoa.EMAIL,
                PessoaContract.Pessoa.TELEFONE,
                PessoaContract.Pessoa.MATRICULA,
                PessoaContract.Pessoa.FOTO,
                PessoaContract.Pessoa.CATEGORIA
        };

        //Colunas para filtrar no WHERE da query
        String selection = PessoaContract.Pessoa._ID + " < ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(id)
        };

        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder = PessoaContract.Pessoa._ID + " DESC ";

        Cursor cursorSearch = db.query(
                PessoaContract.Pessoa.TABLE_NAME,      // The table to query
                projection,                            // The columns to return
                selection,                             // The columns for the WHERE clause
                selectionArgs,                         // The values for the WHERE clause
                null,                                  // don't group the rows
                null,                                  // don't filter by row groups
                sortOrder,                             // The sort order
                "1"                                    // limit 1
        );

        Pessoa pessoa = new Pessoa();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            pessoa.setId(cursorSearch.getInt(0));
            pessoa.setNome(cursorSearch.getString(1));
            pessoa.setEmail(cursorSearch.getString(2));
        }

        return pessoa;
    }

    public List<Pessoa> getByName(String name){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                PessoaContract.Pessoa._ID,
                PessoaContract.Pessoa.NOME,
                PessoaContract.Pessoa.EMAIL,
                PessoaContract.Pessoa.TELEFONE,
                PessoaContract.Pessoa.MATRICULA,
                PessoaContract.Pessoa.FOTO,
                PessoaContract.Pessoa.FOTO_FACIAL,
                PessoaContract.Pessoa.CATEGORIA
        };

        //Colunas para filtrar no WHERE da query
        String selection = PessoaContract.Pessoa.NOME + " like ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                name + "%"
        };
        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder = PessoaContract.Pessoa._ID + " DESC";

        Cursor cursorSearch = db.query(
                PessoaContract.Pessoa.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        List<Pessoa> pessoas = new ArrayList<Pessoa>();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            Pessoa pes_temp;
            do {
                pes_temp = new Pessoa();
                pes_temp.setId(Integer.parseInt(cursorSearch.getString(0)));
                pes_temp.setNome(cursorSearch.getString(1));
                pes_temp.setEmail(cursorSearch.getString(2));
                pes_temp.setTelefone(cursorSearch.getString(3));
                String matricula = cursorSearch.getString(4);
                if(!matricula.equals("")){
                    pes_temp.setMatricula(Integer.parseInt(matricula));
                }
                pes_temp.setFoto(cursorSearch.getString(5));
                pes_temp.setFotoFacial(cursorSearch.getString(6));
                pes_temp.setCategoria(Integer.parseInt(cursorSearch.getString(7)));

                pessoas.add(pes_temp);
            } while (cursorSearch.moveToNext());

        }
        return pessoas;
    }

    public long insert(Pessoa pessoa){

        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();
        //Criando um mapa de informações para serem gravadas no bnaco - conjunto de chave-valor
        ContentValues pessoaContent = new ContentValues();
        pessoaContent.put(PessoaContract.Pessoa.NOME,        pessoa.getNome());
        pessoaContent.put(PessoaContract.Pessoa.EMAIL,       pessoa.getEmail());
        pessoaContent.put(PessoaContract.Pessoa.TELEFONE,    pessoa.getTelefone());
        pessoaContent.put(PessoaContract.Pessoa.MATRICULA,   pessoa.getMatricula());
        pessoaContent.put(PessoaContract.Pessoa.FOTO,        pessoa.getFoto());
        pessoaContent.put(PessoaContract.Pessoa.FOTO_FACIAL, pessoa.getFotoFacial());
        pessoaContent.put(PessoaContract.Pessoa.CATEGORIA,   pessoa.getCategoria());

        try{
            return db.insert(
                    PessoaContract.Pessoa.TABLE_NAME,
                    null,   //The second argument provides the name of a column in which the framework can insert NULL in the event that the ContentValues is empty (if you instead set this to "null", then the framework will not insert a row when there are no values).
                    pessoaContent);
        }catch (SQLiteException e){
            return 0;
        }

    }

    public long update(Pessoa pessoa) {
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues pessoaContent = new ContentValues();
        pessoaContent.put(PessoaContract.Pessoa.NOME,        pessoa.getNome());
        pessoaContent.put(PessoaContract.Pessoa.EMAIL,       pessoa.getEmail());
        pessoaContent.put(PessoaContract.Pessoa.TELEFONE,    pessoa.getTelefone());
        pessoaContent.put(PessoaContract.Pessoa.MATRICULA,   pessoa.getMatricula());
        pessoaContent.put(PessoaContract.Pessoa.FOTO,        pessoa.getFoto());
        pessoaContent.put(PessoaContract.Pessoa.FOTO_FACIAL, pessoa.getFotoFacial());
        pessoaContent.put(PessoaContract.Pessoa.CATEGORIA,   pessoa.getCategoria());

        try {
            return db.update(
                    PessoaContract.Pessoa.TABLE_NAME,
                    pessoaContent,
                    PessoaContract.Pessoa._ID + " = ? ",
                    new String[]{ String.valueOf(pessoa.getId()) });
        }catch (SQLiteException ex){
            return 0;
        }
    }

    public boolean delete(int id){
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            int return_del = db.delete(
                    PessoaContract.Pessoa.TABLE_NAME,
                    PessoaContract.Pessoa._ID+"= ? ",
                    new String[]{ String.valueOf(id) });

            return (id != 0);
        }catch (SQLiteException ex){
            return false;
        }
    }

}
