package net.maikenunes.mcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.maikenunes.mcontrol.adapter.EventoEncontroArrayAdapter;
import net.maikenunes.mcontrol.adapter.EventoPessoaArrayAdapter;
import net.maikenunes.mcontrol.database.EventoEncontroDAO;
import net.maikenunes.mcontrol.database.EventoPessoaDAO;
import net.maikenunes.mcontrol.entity.Encontro;
import net.maikenunes.mcontrol.entity.Evento;
import net.maikenunes.mcontrol.entity.Pessoa;

import java.util.List;


public class ListaEventoEncontroActivity extends ActionBarActivity {

    private int evenId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_evento_encontro);

        if(this.evenId == 0) {
            Intent intent = getIntent();
            this.evenId = intent.getIntExtra("ID", 0);
        }

        setTitle("Encontros p/ Evento");

        this.buscarEventoEncontro();

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.buscarEventoEncontro();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_evento_encontro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_cadastrar:
                Intent intent = new Intent(getBaseContext(),EditEventoEncontroActivity.class);
                intent.putExtra("EVENTO_ID", this.evenId);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buscarEventoEncontro(){

            EventoEncontroDAO evenEncDao = new EventoEncontroDAO(this);

            List<Encontro> encontros = evenEncDao.getByEvent(this.evenId);
            EventoEncontroArrayAdapter<Encontro> adapter = new EventoEncontroArrayAdapter(this, encontros);

            final ListView lista = (ListView) findViewById(R.id.listViewEventoEncontro);

            if(encontros.size() == 0){
                Toast.makeText(this, "encontro não encontrado! :(", Toast.LENGTH_SHORT).show();
            }else{
                lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                        Encontro enc = (Encontro) lista.getItemAtPosition(position);
                        Intent intent = new Intent(getBaseContext(),EditEventoEncontroActivity.class);
                        intent.putExtra("ID", enc.getId());
                        intent.putExtra("EVENTO_ID", evenId);

                        startActivity(intent);

                    }
                });
            }
            lista.setAdapter(adapter);
    }

}
