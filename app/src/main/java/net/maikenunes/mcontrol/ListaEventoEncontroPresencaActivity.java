package net.maikenunes.mcontrol;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.maikenunes.mcontrol.adapter.EventoArrayAdapter;
import net.maikenunes.mcontrol.adapter.EventoEncontroPresencaArrayAdapter;
import net.maikenunes.mcontrol.database.EventoDAO;
import net.maikenunes.mcontrol.database.EventoEncontroDAO;
import net.maikenunes.mcontrol.entity.Encontro;
import net.maikenunes.mcontrol.entity.Evento;

import java.util.List;


public class ListaEventoEncontroPresencaActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_evento_encontro_presenca);

        setTitle("Lista Encontros");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        buscarEventoEncontroDB();

    }

    private void buscarEventoEncontroDB() {
        EventoEncontroDAO evenEncDao = new EventoEncontroDAO(this);
        List<Encontro> eEncontros = evenEncDao.getByEncontrosEvento();
        EventoEncontroPresencaArrayAdapter<Encontro> adapter = new EventoEncontroPresencaArrayAdapter(this, eEncontros);

        final ListView lista = (ListView) findViewById(R.id.listViewEventoEncontros);

        if(eEncontros.size() == 0){
            Toast.makeText(this, "nada encontrado! :(", Toast.LENGTH_SHORT).show();
        }else{
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Encontro enc = (Encontro) lista.getItemAtPosition(position);
                    Intent intent = new Intent(getBaseContext(),EditPresencaActivity.class);
                    intent.putExtra("ID", enc.getId());

                    startActivity(intent);

                }
            });
        }
        lista.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_evento_encontro_presenca, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        this.finish();
    }

}
