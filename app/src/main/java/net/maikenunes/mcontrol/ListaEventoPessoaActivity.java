package net.maikenunes.mcontrol;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.maikenunes.mcontrol.adapter.EventoArrayAdapter;
import net.maikenunes.mcontrol.adapter.EventoPessoaArrayAdapter;
import net.maikenunes.mcontrol.database.EventoDAO;
import net.maikenunes.mcontrol.database.EventoPessoaDAO;
import net.maikenunes.mcontrol.database.PessoaDAO;
import net.maikenunes.mcontrol.entity.Evento;
import net.maikenunes.mcontrol.entity.EventoPessoa;
import net.maikenunes.mcontrol.entity.Pessoa;
import net.maikenunes.mcontrol.utils.FileDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ListaEventoPessoaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_evento_pessoa);

        setTitle("Pessoas no Evento");

        this.buscarEventoPessoa();

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.buscarEventoPessoa();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_evento_pessoa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cadastrar:
                Intent intentTemp = getIntent();
                Intent intent = new Intent(getBaseContext(),EditEventoPessoaActivity.class);
                intent.putExtra("ID", intentTemp.getIntExtra("ID", 0));
                startActivity(intent);
                break;
            case R.id.action_import:
                FileDialog fileDialog = new FileDialog(this, new File(Environment.getExternalStorageDirectory()+"/Download"), ".csv", false);
                fileDialog.addFileListener(new FileDialog.FileSelectedListener() {
                    public void fileSelected(File file) {
                        importPessoas(file);
                        Log.d("WBSA", "selected file " + file.toString());
                    }
                });
                fileDialog.showDialog();
                break;
            case R.id.home:
                this.finish();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void buscarEventoPessoa(){
        EventoPessoaDAO evenPesDao = new EventoPessoaDAO(this);
        Intent intEventPes = getIntent();
        int id = intEventPes.getIntExtra("ID", 0);

        List<Pessoa> pessoas = evenPesDao.getByEvent(id);
        EventoPessoaArrayAdapter<Pessoa> adapter = new EventoPessoaArrayAdapter(this, pessoas);

        final ListView lista = (ListView) findViewById(R.id.listViewEventoPessoa);

        if(pessoas.size() == 0){
            Toast.makeText(this, "pessoa não encontrada! :(", Toast.LENGTH_SHORT).show();
        }
        lista.setAdapter(adapter);
    }

    public void importPessoas(File csvPessoas){
        Toast.makeText(this, csvPessoas.getPath().toString(), Toast.LENGTH_LONG).show();
        ProgressDialog progressDialog = ProgressDialog.show(ListaEventoPessoaActivity.this, "", "processando...");

        Scanner inputStream = null;
        int countPessoas = 0;
        try{
            // le o arquivo
            inputStream = new Scanner(csvPessoas);
            String dados;
            List<Pessoa> pessoas = new ArrayList<>();
            while(inputStream.hasNext()){
                Pessoa pessoa = new Pessoa();
                // le linha a linha
                // nome;email;telefone;matricula;cod_origem
                dados = inputStream.nextLine();
                Log.v("CSVIMP", dados);
                String parseDados[] = dados.split(";");

                pessoa.setNome(parseDados[0].toString());
                pessoa.setEmail(parseDados[1].toString());
                pessoa.setTelefone(parseDados[2].toString());
                pessoa.setMatricula(Integer.parseInt(parseDados[3].toString()));
                pessoa.setCodOrigem(parseDados[4].toString());
                pessoa.setCategoria(1); // importado daqui sempre será aluno
                pessoas.add(pessoa);

                countPessoas++;

            }

            PessoaDAO pesDAO = new PessoaDAO(this);
            EventoPessoaDAO ePesqDAO = new EventoPessoaDAO(this);
            for (int x=0;x<pessoas.size();x++){
                Pessoa pessoa = pesDAO.getByMatricula(pessoas.get(x).getMatricula());
                if(pessoa.getId()!=0){
                    // ja existe pessoa
                    pessoas.remove(x);
                    pessoas.add(x, pessoa);
                }else{
                    // cadastra pessoa
                    long logId = pesDAO.insert(pessoas.get(x));
                    pessoa = pessoas.get(x);
                    pessoa.setId(Integer.parseInt(String.valueOf(logId)));
                    pessoas.remove(x);
                    pessoas.add(x, pessoa);
                }

                // checa se ela esta no evento,
                EventoPessoa evenPes = new EventoPessoa();
                evenPes.setEvento(getIntent().getIntExtra("ID", 0));
                evenPes.setPessoa(pessoa.getId());
                Pessoa pesEvenTest = ePesqDAO.getByIdInNotEvent(evenPes);
                if(!(pesEvenTest.getId() > 0)){ // nao esta no evento, então cadastra
                    evenPes.setCodOrigem(pessoa.getCodOrigem());
                    long evenPesId = ePesqDAO.insert(evenPes);
                    if(evenPesId == 0){
                        Toast.makeText(this, "Falha ao inserir pessoa ao evento :) ", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            inputStream.close();
            progressDialog.cancel();


        }catch (Exception exep){
            inputStream.close();
            progressDialog.cancel();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Erro na linha "+countPessoas+" do respectivo arquivo..")
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Falha na importação das pessoas");
            alert.show();
            exep.printStackTrace();
            return;
        }

        Toast.makeText(ListaEventoPessoaActivity.this, "Importado com sucesso!", Toast.LENGTH_LONG).show();
        this.buscarEventoPessoa();

    }

}
