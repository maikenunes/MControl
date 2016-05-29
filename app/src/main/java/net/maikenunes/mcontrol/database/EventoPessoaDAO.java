package net.maikenunes.mcontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import net.maikenunes.mcontrol.database.contract.EventoContract;
import net.maikenunes.mcontrol.database.contract.EventoPessoaContract;
import net.maikenunes.mcontrol.database.contract.PessoaContract;
import net.maikenunes.mcontrol.entity.Encontro;
import net.maikenunes.mcontrol.entity.EventoPessoa;
import net.maikenunes.mcontrol.entity.Pessoa;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EventoPessoaDAO extends DAO {

    public EventoPessoaDAO(Context context) {
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

    public Pessoa getByIdInNotEvent(EventoPessoa evenPes){
        SQLiteDatabase db = this.getReadableDatabase();

        //Colunas para filtrar no WHERE da query
        String[] selectionArgs = {
                String.valueOf(evenPes.getEvento()),
                String.valueOf(evenPes.getPessoa())
        };

        Cursor cursorSearch = db.rawQuery(
                " SELECT pes._id, pes.nome " +
                        " FROM pessoas pes " +
                        " INNER JOIN evento_pessoa evenPes ON evenPes.pessoa_id = pes._id " +
                        " WHERE evenPes.evento_id = ? AND evenPes.pessoa_id = ? ; ",
                selectionArgs
        );

        Pessoa pessoa = new Pessoa();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            pessoa.setId(Integer.parseInt(cursorSearch.getString(0)));
            pessoa.setNome(cursorSearch.getString(1));
        }
        return pessoa;
    }

    public Pessoa getByCodPesEvent(EventoPessoa evenPes){
        SQLiteDatabase db = this.getReadableDatabase();

        //Colunas para filtrar no WHERE da query
        String[] selectionArgs = {
                String.valueOf(evenPes.getEvento()),
                String.valueOf(evenPes.getCodOrigem())
        };

        Cursor cursorSearch = db.rawQuery(
                " SELECT evenPes.pessoa_id, evenPes.cod_externo " +
                        " FROM evento_pessoa evenPes " +
                        " WHERE evenPes.evento_id = ? AND evenPes.cod_externo = ? ; ",
                selectionArgs
        );

        Pessoa pessoa = new Pessoa();
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            pessoa.setId(Integer.parseInt(cursorSearch.getString(0)));
            pessoa.setCodOrigem(cursorSearch.getString(1));
        }
        return pessoa;
    }

    public List<Pessoa> getByEvent(int even){
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
            String.valueOf(even)
        };
        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder = PessoaContract.Pessoa._ID + " DESC";

        Cursor cursorSearch = db.rawQuery(
                " SELECT pes._id, pes.nome, pes.email, pes.telefone, pes.matricula, pes.foto_path, pes.foto_facial_path, pes.categoria_id, pes.deletado " +
                        " FROM pessoas pes " +
                        " INNER JOIN evento_pessoa evenPes ON evenPes.pessoa_id = pes._id " +
                        " WHERE evenPes.evento_id = ? ",
                selectionArgs
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

    public List<Pessoa> getByEventNotPresenc(Encontro enc){
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
        String[] selectionArgs = {
                String.valueOf(enc.getEvenId()),
                String.valueOf(enc.getId())
        };

        Cursor cursorSearch = db.rawQuery(
                " SELECT pes._id, pes.nome, pes.email, pes.telefone, pes.matricula, pes.foto_path, pes.foto_facial_path, pes.categoria_id, pes.deletado " +
                        " FROM pessoas pes " +
                        " INNER JOIN evento_pessoa evenPes ON evenPes.pessoa_id = pes._id " +
                        " WHERE evenPes.evento_id = ? AND evenPes.pessoa_id NOT IN ( " +
                        "   SELECT pre.pessoa_id FROM encontros enc" +
                        "   INNER JOIN presencas pre ON pre.encontro_id = enc._id " +
                        "   WHERE evenPes.evento_id = enc.evento_id AND pre.encontro_id = ?  )  ",
                selectionArgs
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

    public Pessoa getByEventCodExterno(int encontro_id, int evento_id, String cod_origem ){
        SQLiteDatabase db = this.getReadableDatabase();

        //Colunas para filtrar no WHERE da query
        String[] selectionArgs = {
                String.valueOf(cod_origem),
                String.valueOf(evento_id),
                String.valueOf(encontro_id)
        };

        Cursor cursorSearch = db.rawQuery(
                " SELECT pes._id, pes.nome, pes.email, pes.telefone, pes.matricula, pes.foto_path, pes.foto_facial_path, pes.categoria_id, pes.deletado " +
                        " FROM pessoas pes " +
                        " INNER JOIN evento_pessoa evenPes ON evenPes.pessoa_id = pes._id " +
                        " WHERE evenPes.cod_externo = ? AND evenPes.evento_id = ? AND evenPes.pessoa_id NOT IN ( " +
                        "   SELECT pre.pessoa_id FROM encontros enc" +
                        "   INNER JOIN presencas pre ON pre.encontro_id = enc._id " +
                        "   WHERE evenPes.evento_id = enc.evento_id AND pre.encontro_id = ?  )  ",
                selectionArgs
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

    public Pessoa getPesByCodExternoPresenca(int encontro_id, int evento_id, String cod_origem ){
        SQLiteDatabase db = this.getReadableDatabase();

        //Colunas para filtrar no WHERE da query
        String[] selectionArgs = {
                String.valueOf(cod_origem),
                String.valueOf(evento_id),
                String.valueOf(encontro_id)
        };

        Cursor cursorSearch = db.rawQuery(
                " SELECT pes._id, pes.nome, pes.email, pes.telefone, pes.matricula, pes.foto_path, pes.foto_facial_path, pes.categoria_id, pes.deletado " +
                        " FROM pessoas pes " +
                        " INNER JOIN evento_pessoa evenPes ON evenPes.pessoa_id = pes._id " +
                        " WHERE evenPes.cod_externo = ? AND evenPes.evento_id = ? AND evenPes.pessoa_id IN ( " +
                        "   SELECT pre.pessoa_id FROM encontros enc" +
                        "   INNER JOIN presencas pre ON pre.encontro_id = enc._id " +
                        "   WHERE evenPes.evento_id = enc.evento_id AND pre.encontro_id = ?  )  ",
                selectionArgs
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

    public List<Pessoa> getByNotInEvent(int even){
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
                String.valueOf(even)
        };
        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder = PessoaContract.Pessoa._ID + " DESC";

        Cursor cursorSearch = db.rawQuery(
                " SELECT pes._id, pes.nome, pes.email, pes.telefone, pes.matricula, pes.foto_path, pes.foto_facial_path, pes.categoria_id, pes.deletado " +
                        " FROM pessoas pes " +
                        " WHERE pes._id NOT IN ( SELECT pessoa_id FROM evento_pessoa" +
                        "   WHERE pessoa_id = pes._id AND evento_id = ? )",
                selectionArgs
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

    public long insert(EventoPessoa epessoa){

        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        //Criando um mapa de informações para serem gravadas no bnaco - conjunto de chave-valor
        ContentValues epessoaContent = new ContentValues();
        epessoaContent.put(EventoPessoaContract.EventoPessoa.EVENTO, epessoa.getEvento());
        epessoaContent.put(EventoPessoaContract.EventoPessoa.PESSOA,  epessoa.getPessoa());
        epessoaContent.put(EventoPessoaContract.EventoPessoa.COD_EXTERNO,  epessoa.getCodOrigem());
        try {
            epessoaContent.put(EventoPessoaContract.EventoPessoa.DATA_CADASTRO,  formatter.format(epessoa.getData()));
        }catch (Exception e){
            Log.i("TAG", "erro :( : " + e.getMessage());
        }

        try{
            return db.insert(
                    EventoPessoaContract.EventoPessoa.TABLE_NAME,
                    null,   //The second argument provides the name of a column in which the framework can insert NULL in the event that the ContentValues is empty (if you instead set this to "null", then the framework will not insert a row when there are no values).
                    epessoaContent);
        }catch (SQLiteException e){
            return 0;
        }

    }

    public boolean delete(int evento, int pessoa){
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            int return_del = db.delete(
                    EventoPessoaContract.EventoPessoa.TABLE_NAME,
                    EventoPessoaContract.EventoPessoa.EVENTO+"= ? AND "+EventoPessoaContract.EventoPessoa.PESSOA+"= ? ",
                    new String[]{ String.valueOf(evento), String.valueOf(pessoa) });

            return (return_del != 0);
        }catch (SQLiteException ex){
            return false;
        }
    }

}
