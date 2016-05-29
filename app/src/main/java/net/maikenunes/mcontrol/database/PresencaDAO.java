package net.maikenunes.mcontrol.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import net.maikenunes.mcontrol.database.contract.PessoaContract;
import net.maikenunes.mcontrol.database.contract.PresencaContract;
import net.maikenunes.mcontrol.entity.Encontro;
import net.maikenunes.mcontrol.entity.Evento;
import net.maikenunes.mcontrol.entity.Pessoa;
import net.maikenunes.mcontrol.entity.Presenca;
import net.maikenunes.mcontrol.entity.Relatorio;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PresencaDAO extends DAO {

    public PresencaDAO(Context context) {
        super(context); // chama o construtor da classe DAO
    }

    public Presenca getById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        //Defina uma projeção que especifica quais colunas do banco de dados
        //você vai utilizar na query a seguir
        String[] projection = {
                PresencaContract.Presenca._ID,
                PresencaContract.Presenca.ENCONTRO,
                PresencaContract.Presenca.PESSOA,
                PresencaContract.Presenca.DATA_ENTRADA,
                PresencaContract.Presenca.DATA_SAIDA,
        };

        //Colunas para filtrar no WHERE da query
        String selection = PresencaContract.Presenca._ID + " = ?";
        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(id)
        };

        //Como você quer que os resultados sejam ordenados no Cursor
        String sortOrder =
                PresencaContract.Presenca._ID + " DESC";

        Cursor cursorSearch = db.query(
                PresencaContract.Presenca.TABLE_NAME,      // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        Presenca presenca = new Presenca();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            presenca.setId(Integer.parseInt(cursorSearch.getString(0)));
            presenca.setEncId(Integer.parseInt(cursorSearch.getString(1)));
            presenca.setPesId(Integer.parseInt(cursorSearch.getString(2)));
            try {
                presenca.setEntrada(formatter.parse(cursorSearch.getString(3)));
                presenca.setSaida(formatter.parse(cursorSearch.getString(4)));
            }catch (Exception e){
                Log.i("TAG", "erro :( : " + e.getMessage());
            }
        }
        return presenca;
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
                pes_temp.setCategoria(Integer.parseInt(cursorSearch.getString(6)));

                pessoas.add(pes_temp);
            } while (cursorSearch.moveToNext());

        }
        return pessoas;
    }

    public long insert(Presenca presenca){

        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();
        //Criando um mapa de informações para serem gravadas no bnaco - conjunto de chave-valor
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        ContentValues presencaContent = new ContentValues();

        try{
            presencaContent.put(PresencaContract.Presenca.ENCONTRO,     presenca.getEncId());
            presencaContent.put(PresencaContract.Presenca.PESSOA,       presenca.getPesId());
            presencaContent.put(PresencaContract.Presenca.DATA_ENTRADA, formatter.format(presenca.getEntrada()));
            presencaContent.put(PresencaContract.Presenca.DATA_SAIDA,   formatter.format(presenca.getSaida()));
            return db.insert(
                    PresencaContract.Presenca.TABLE_NAME,
                    null,   //The second argument provides the name of a column in which the framework can insert NULL in the event that the ContentValues is empty (if you instead set this to "null", then the framework will not insert a row when there are no values).
                    presencaContent);
        }catch (SQLiteException e){
            return 0;
        }

    }

    public long update(Presenca presenca) {
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        ContentValues presencaContent = new ContentValues();
        presencaContent.put(PresencaContract.Presenca.ENCONTRO,     presenca.getEncId());
        presencaContent.put(PresencaContract.Presenca.PESSOA,       presenca.getPesId());
        presencaContent.put(PresencaContract.Presenca.DATA_ENTRADA, formatter.format(presenca.getEntrada()));
        presencaContent.put(PresencaContract.Presenca.DATA_SAIDA,   formatter.format(presenca.getSaida()));

        try {
            return db.update(
                    PresencaContract.Presenca.TABLE_NAME,
                    presencaContent,
                    PresencaContract.Presenca._ID + " = ? ",
                    new String[]{String.valueOf(presenca.getId())});
        }catch (SQLiteException ex){
            return 0;
        }
    }

    public boolean delete(int id){
        // Instanciando um banco no modo de gravação
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            int return_del = db.delete(
                    PresencaContract.Presenca.TABLE_NAME,
                    PresencaContract.Presenca._ID+"= ? ",
                    new String[]{ String.valueOf(id) });

            return (id != 0);
        }catch (SQLiteException ex){
            return false;
        }
    }

    public List<Presenca> getByEncontro(int enc){
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

        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(enc)
        };

        Cursor cursorSearch = db.rawQuery(
                " SELECT pes._id, pes.nome, pes.email, pes.telefone, pes.matricula, pes.foto_path, pes.foto_facial_path, pes.categoria_id, pes.deletado, pre.encontro_id, pre.pessoa_id, pre.data_entrada, pre.data_saida " +
                        " FROM pessoas pes " +
                        " INNER JOIN presencas pre ON pre.pessoa_id = pes._id " +
                        " WHERE pre.encontro_id = ? ",
                selectionArgs
        );
        List<Presenca> presencas = new ArrayList<Presenca>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            Pessoa pes_temp;
            Presenca pre_temp;
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

                pre_temp = new Presenca();
                pre_temp.setEncId(Integer.parseInt(cursorSearch.getString(9)));
                pre_temp.setPesId(Integer.parseInt(cursorSearch.getString(10)));
                try {
                    pre_temp.setEntrada(formatter.parse(cursorSearch.getString(11)));
                    pre_temp.setSaida(formatter.parse(cursorSearch.getString(12)));
                }catch (Exception e){
                    Log.i("TAGER", e.getMessage());
                }

                pre_temp.setPessoa(pes_temp);

                presencas.add(pre_temp);
            } while (cursorSearch.moveToNext());

        }
        return presencas;
    }

    public List<Relatorio> getByEvent(int enc){
        SQLiteDatabase db = this.getReadableDatabase();

        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(enc)
        };

        Cursor cursorSearch = db.rawQuery(
                " SELECT pes._id, pes.nome, pes.email, pes.telefone, pes.matricula, pes.foto_path, pes.categoria_id, pes.deletado, pre.encontro_id, pre.pessoa_id, pre.data_entrada, pre.data_saida , enc.evento_id, even.nome " +
                        " FROM pessoas pes " +
                        " INNER JOIN presencas pre ON pre.pessoa_id = pes._id " +
                        " INNER JOIN encontros enc ON enc._id = pre.encontro_id " +
                        " INNER JOIN eventos even  ON even._id = enc.evento_id" +
                        " WHERE enc.evento_id = ? "+
                        " ORDER BY enc.evento_id DESC, enc._id DESC, pes.nome ASC ",
                selectionArgs
        );
        List<Relatorio> relatorios = new ArrayList<Relatorio>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            Pessoa pes_temp;
            Presenca pre_temp;
            Relatorio rel_temp;
            Evento even_temp;
            do {
                rel_temp = new Relatorio();

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
                pes_temp.setCategoria(Integer.parseInt(cursorSearch.getString(6)));

                pre_temp = new Presenca();
                pre_temp.setEncId(Integer.parseInt(cursorSearch.getString(8)));
                pre_temp.setPesId(Integer.parseInt(cursorSearch.getString(9)));
                try {
                    pre_temp.setEntrada(formatter.parse(cursorSearch.getString(10)));
                    pre_temp.setSaida(formatter.parse(cursorSearch.getString(11)));
                }catch (Exception e){
                    Log.i("TAGER", e.getMessage());
                }

                even_temp = new Evento();
                even_temp.setId(Integer.parseInt(cursorSearch.getString(12)));
                even_temp.setNome(cursorSearch.getString(13));

                rel_temp.setPessoa(pes_temp);
                rel_temp.setPresenca(pre_temp);
                rel_temp.setEvento(even_temp);

                pre_temp.setPessoa(pes_temp);

                relatorios.add(rel_temp);
            } while (cursorSearch.moveToNext());

        }
        return relatorios;
    }

    public List<Relatorio> getByEventNotPresen(int enc){
        SQLiteDatabase db = this.getReadableDatabase();

        //Valores para filtrar no WHERE da query, na mesma ordem das colunas
        String[] selectionArgs = {
                String.valueOf(enc)
        };

        Cursor cursorSearch = db.rawQuery(
                " SELECT pes._id, pes.nome, pes.email, pes.telefone, pes.matricula, pes.foto_path, pes.categoria_id, pes.deletado, enc.data_ini, enc.evento_id, enc._id " +
                        " FROM pessoas pes " +
                        " INNER JOIN evento_pessoa evenPes ON evenPes.pessoa_id = pes._id " +
                        " INNER JOIN encontros enc ON enc.evento_id = evenPes.evento_id " +
                        " WHERE evenPes.evento_id = ? AND evenPes.pessoa_id NOT IN ( " +
                        "   SELECT pre.pessoa_id FROM presencas pre " +
                        "   WHERE pre.encontro_id = enc._id AND pre.pessoa_id = evenPes.pessoa_id  )" +
                        " ORDER BY evenPes.evento_id DESC, enc._id DESC, pes.nome ASC ",
                selectionArgs
        );
        List<Relatorio> relatorios = new ArrayList<Relatorio>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if(cursorSearch.getCount() > 0){
            cursorSearch.moveToFirst();
            Pessoa pes_temp;
            Relatorio rel_temp;
            Encontro enc_temp;
            Presenca pre_temp;
            Evento even_temp;
            do {
                rel_temp = new Relatorio();

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
                pes_temp.setCategoria(Integer.parseInt(cursorSearch.getString(6)));

                enc_temp = new Encontro();
                try {
                    enc_temp.setDataHoraIni(formatter.parse(cursorSearch.getString(8)));
                }catch (Exception e){
                    Log.i("TAGER", e.getMessage());
                }

                pre_temp = new Presenca();
                pre_temp.setEncId(Integer.parseInt(cursorSearch.getString(10)));
                pre_temp.setPesId(Integer.parseInt(cursorSearch.getString(0)));

                even_temp = new Evento();
                even_temp.setId(Integer.parseInt(cursorSearch.getString(9)));

                rel_temp.setPessoa(pes_temp);
                rel_temp.setPresenca(pre_temp);
                rel_temp.setEvento(even_temp);

                rel_temp.setPessoa(pes_temp);
                rel_temp.setEncontro(enc_temp);

                relatorios.add(rel_temp);

            } while (cursorSearch.moveToNext());

        }
        return relatorios;
    }

}
