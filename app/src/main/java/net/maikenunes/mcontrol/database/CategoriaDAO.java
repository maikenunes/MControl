package net.maikenunes.mcontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import net.maikenunes.mcontrol.database.contract.CategoriaContract;
import net.maikenunes.mcontrol.database.contract.PessoaContract;
import net.maikenunes.mcontrol.entity.Categoria;
import net.maikenunes.mcontrol.entity.Pessoa;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO extends DAO {

    public CategoriaDAO(Context context) {
        super(context); // chama o construtor da classe DAO
    }

    public Categoria getById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                CategoriaContract.Categoria._ID,
                CategoriaContract.Categoria.NOME,
                CategoriaContract.Categoria.DELETADO
        };

        //Colunas para filtrar no WHERE da query
        String selection = CategoriaContract.Categoria._ID + " = ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(id)
        };

        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder =
                CategoriaContract.Categoria._ID + " DESC";

        Cursor cursorSearch = db.query(
                CategoriaContract.Categoria.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        Categoria categoria = new Categoria();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            categoria.setId(Integer.parseInt(cursorSearch.getString(0)));
            categoria.setNome(cursorSearch.getString(1));
            categoria.setDeletado(cursorSearch.getString(2));
        }
        return categoria;
    }

    public List<Categoria> getAll(){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados você vai utilizar na query a seguir
        String[] projection = {
                CategoriaContract.Categoria._ID,
                CategoriaContract.Categoria.NOME,
                CategoriaContract.Categoria.DELETADO
        };
        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder = CategoriaContract.Categoria.NOME + " ASC";

        Cursor cursorSearch = db.query(
                CategoriaContract.Categoria.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );
        List<Categoria> categorias = new ArrayList<Categoria>();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            Categoria cat_temp;
            do {
                cat_temp = new Categoria();
                cat_temp.setId(Integer.parseInt(cursorSearch.getString(0)));
                cat_temp.setNome(cursorSearch.getString(1));
                cat_temp.setDeletado(cursorSearch.getString(2));

                categorias.add(cat_temp);
            } while (cursorSearch.moveToNext());

        }
        return categorias;
    }

    public long insert(Categoria categoria){

        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();
        //Criando um mapa de informações para serem gravadas no bnaco - conjunto de chave-valor
        ContentValues categoriaContent = new ContentValues();
        categoriaContent.put(CategoriaContract.Categoria.NOME,     categoria.getNome());
        categoriaContent.put(CategoriaContract.Categoria.DELETADO, categoria.getDeletado());
        try{
            return db.insert(
                    CategoriaContract.Categoria.TABLE_NAME,
                    null,   //The second argument provides the name of a column in which the framework can insert NULL in the event that the ContentValues is empty (if you instead set this to "null", then the framework will not insert a row when there are no values).
                    categoriaContent);
        }catch (SQLiteException e){
            return 0;
        }

    }

    public long update(Categoria c) {
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues categoriaContent = new ContentValues();
        categoriaContent.put(CategoriaContract.Categoria.NOME,     c.getNome());
        categoriaContent.put(CategoriaContract.Categoria.DELETADO, c.getDeletado());

        try {
            return db.update(
                    CategoriaContract.Categoria.TABLE_NAME,
                    categoriaContent,
                    CategoriaContract.Categoria._ID + " = ? ",
                    new String[]{ String.valueOf(c.getId()) });
        }catch (SQLiteException ex){
            return 0;
        }
    }

    public boolean delete(int id){
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            int return_del = db.delete(
                    CategoriaContract.Categoria.TABLE_NAME,
                    CategoriaContract.Categoria._ID+"= ? ",
                    new String[]{ String.valueOf(id) });

            return (id != 0);
        }catch (SQLiteException ex){
            return false;
        }
    }

}
