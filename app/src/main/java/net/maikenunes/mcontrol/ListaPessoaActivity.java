package net.maikenunes.mcontrol;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.maikenunes.mcontrol.adapter.PessoaArrayAdapter;
import net.maikenunes.mcontrol.database.PessoaDAO;
import net.maikenunes.mcontrol.entity.Pessoa;

import java.util.List;


public class ListaPessoaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_pessoa);
        setTitle("Lista de Pessoas");

        buscarPessoaDB("");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_lista_pessoa, menu);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager manager = (SearchManager) getSystemService(this.SEARCH_SERVICE);
            SearchView search     = (SearchView) menu.findItem(R.id.action_search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    buscarPessoaDB(query);
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String query) {
                    buscarPessoaDB(query);
                    return true;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cadastrar:
                Intent intent = new Intent(getBaseContext(),EditPessoaActivity.class);
                startActivity(intent);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void buscarPessoaDB(String busca) {

        if(busca.matches("")){
            busca = "%";
        }

        PessoaDAO pesDao = new PessoaDAO(this);
        List<Pessoa> pessoas = pesDao.getByName(busca);

        PessoaArrayAdapter<Pessoa> adapter = new PessoaArrayAdapter(this, pessoas);
        final ListView lista = (ListView) findViewById(R.id.listView);

        if(pessoas.size() == 0){
            Toast.makeText(this, "Pessoa não encontrada! :(", Toast.LENGTH_SHORT).show();
        }else{
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Pessoa p = (Pessoa) lista.getItemAtPosition(position);

                    Intent intent = new Intent(getBaseContext(),EditPessoaActivity.class);
                    intent.putExtra("ID",p.getId());

                    startActivity(intent);

                }
            });
        }
        lista.setAdapter(adapter);

    }

    @Override
    protected void onResume() {

        buscarPessoaDB("");

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            buscarPessoaDB(query);
        }
        super.onResume();
    }

}
