package net.maikenunes.mcontrol;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import net.maikenunes.mcontrol.adapter.EventoPessoaArrayAdapter;
import net.maikenunes.mcontrol.adapter.PresencaPessoaArrayAdapter;
import net.maikenunes.mcontrol.database.EventoEncontroDAO;
import net.maikenunes.mcontrol.database.EventoPessoaDAO;
import net.maikenunes.mcontrol.database.PresencaDAO;
import net.maikenunes.mcontrol.entity.Encontro;
import net.maikenunes.mcontrol.entity.Pessoa;
import net.maikenunes.mcontrol.entity.Presenca;

import java.util.List;


public class EditPresencaConfirmActivity extends ActionBarActivity {
    private Encontro encontro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_presenca_confirm);

        setTitle("Presenças Confirmadas");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(this.encontro == null){
            EventoEncontroDAO encDAO = new EventoEncontroDAO(this);
            this.encontro = encDAO.getById(getIntent().getIntExtra("ID", 0));
        }

        this.buscarEventoPessoaConfirm();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_presenca_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.homeAsUp:
                this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void buscarEventoPessoaConfirm(){

        PresencaDAO preDAO = new PresencaDAO(this);

        List<Presenca> presencas = preDAO.getByEncontro(this.encontro.getId());
        PresencaPessoaArrayAdapter<Pessoa> adapter = new PresencaPessoaArrayAdapter(this, presencas);

        final ListView lista = (ListView) findViewById(R.id.listViewEventoEncontroPessoaConfirm);

        if(presencas.size() == 0){
            Toast.makeText(this, "pessoa não encontrado! :(", Toast.LENGTH_SHORT).show();
        }
        lista.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
