package net.maikenunes.mcontrol;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import net.maikenunes.mcontrol.adapter.EventoPessoaArrayAdapterTeste;
import net.maikenunes.mcontrol.database.EventoPessoaDAO;
import net.maikenunes.mcontrol.entity.EventoPessoa;
import net.maikenunes.mcontrol.entity.Pessoa;
import net.maikenunes.mcontrol.utils.FileDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class EditEventoPessoaActivity extends ActionBarActivity {

    public ArrayList<Pessoa> selPessoas = null;
    private int idEven = 0;
    private Menu mInterno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_evento_pessoa);

        setTitle("Pessoas para Evento");

        if(this.idEven == 0) {
            Intent intEventPes = getIntent();
            this.idEven = intEventPes.getIntExtra("ID", 0);
        }

        this.selPessoas = new ArrayList<Pessoa>();

        this.buscarEventoPessoa();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_evento_pessoa, menu);
        menu.findItem(R.id.action_save).setVisible(false);
        this.mInterno = menu;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertEventoPessoa();
                break;
            case R.id.home:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void buscarEventoPessoa(){

        EventoPessoaDAO evenPesDao = new EventoPessoaDAO(this);
        Intent intEventPes = getIntent();
        int id = intEventPes.getIntExtra("ID", 0);

        final List<Pessoa> pessoas = evenPesDao.getByNotInEvent(id);
        final EventoPessoaArrayAdapterTeste adapter = new EventoPessoaArrayAdapterTeste(this, R.layout.row_list_multiple_select_pessoa, pessoas);

        final ListView lista = (ListView) findViewById(R.id.listView);

        if(pessoas.size() == 0){
            Toast.makeText(this, "pessoa não encontrado! :(", Toast.LENGTH_SHORT).show();
        }
        lista.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lista.setAdapter(adapter);
        lista.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // Capture total checked items
                final int checkedCount = lista.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selecionados");
                selPessoas = new ArrayList<Pessoa>();
                if(checkedCount > 0){
                    SparseBooleanArray selected = lista.getCheckedItemPositions();
                    Log.v("TAGSELINI", " "+selected.size() );
                    for (int i = 0; i < selected.size(); i++) {
                        if (selected.get(i)) {
                            Pessoa selecteditem = adapter.getItem(selected.keyAt(i));
                            Log.v("TAGSELLLL", selecteditem.toString());
                            selPessoas.add(selecteditem);
                        }else{
                            Log.v("TAGSEL", i+" -|- falhou :( ");
                        }
                    }
                    mInterno.findItem(R.id.action_save).setVisible(true);
                }else{
                    mInterno.findItem(R.id.action_save).setVisible(false);
                }

                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_save:
                        insertEventoPessoa();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                mode.getMenuInflater().inflate(R.menu.menu_edit_evento_pessoa, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                adapter.removeSelection();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }
        });

    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    public void insertEventoPessoa(){
        EventoPessoa ePessoa;
        EventoPessoaDAO ePesDAO = new EventoPessoaDAO(this);
        boolean validCadastro = false;
        //Toast.makeText(this, this.selPessoas.size() +" -|- "+this.selPessoas.size(),Toast.LENGTH_LONG).show();
        for (int x = 0; x < this.selPessoas.size(); x++ ){
            validCadastro = true;
            ePessoa = new EventoPessoa();
            ePessoa.setPessoa(this.selPessoas.get(x).getId());
            ePessoa.setEvento(this.idEven);

            long evenPesId = ePesDAO.insert(ePessoa);
            if(evenPesId == 0){
                Toast.makeText(this, "Falha ao inserir pessoa ao evento :) ", Toast.LENGTH_SHORT).show();
                validCadastro = true;
                break;
            }
        }
        if(!validCadastro) {
            Toast.makeText(this, "Pessoa adicionadas ao evento com sucesso!", Toast.LENGTH_SHORT).show();
        }
        this.finish();
    }


}
