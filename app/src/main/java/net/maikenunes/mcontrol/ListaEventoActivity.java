package net.maikenunes.mcontrol;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.maikenunes.mcontrol.adapter.EventoArrayAdapter;
import net.maikenunes.mcontrol.adapter.PessoaArrayAdapter;
import net.maikenunes.mcontrol.database.EventoDAO;
import net.maikenunes.mcontrol.database.PessoaDAO;
import net.maikenunes.mcontrol.entity.Evento;
import net.maikenunes.mcontrol.entity.Pessoa;

import java.util.ArrayList;
import java.util.List;


public class ListaEventoActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_evento);

        setTitle("Lista de Eventos");

        buscarEventoDB("");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_evento, menu);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
            SearchView search     = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    buscarEventoDB(query);
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String query) {
                    buscarEventoDB(query);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cadastrar:
                Intent intent = new Intent(getBaseContext(),EditEventoActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buscarEventoDB(String busca) {


        if(busca.matches("")){
            busca = "%";
        }

        EventoDAO evenDao = new EventoDAO(this);
        List<Evento> eventos = evenDao.getByName(busca);
        EventoArrayAdapter<Evento> adapter = new EventoArrayAdapter(this, eventos);

        final ListView lista = (ListView) findViewById(R.id.listViewEventos);

        if(eventos.size() == 0){
            Toast.makeText(this, "Evento não encontrado! :(", Toast.LENGTH_SHORT).show();
        }else{
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Evento even = (Evento) lista.getItemAtPosition(position);
                    Intent intent = new Intent(getBaseContext(),EditEventoActivity.class);
                    intent.putExtra("ID", even.getId());

                    startActivity(intent);

                }
            });
        }
        lista.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        buscarEventoDB("");
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            buscarEventoDB(query);
        }
        super.onResume();
    }

}
