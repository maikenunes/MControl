package net.maikenunes.mcontrol;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                break;

            case R.id.action_person:
                Intent insertIntent      = new Intent(this, EditPessoaActivity.class); // "intenção de abrir outra activity"
                startActivity(insertIntent); // start a segunda tela
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void listaPessoas(View view){
        Intent listPessoa = new Intent(this, ListaPessoaActivity.class); // "intenção de abrir outra activity"
        startActivity(listPessoa); // start a segunda tela
    }

    public void listaEventos(View view){
        Intent listEvento = new Intent(this, ListaEventoActivity.class); // "intenção de abrir outra activity"
        startActivity(listEvento); // start a segunda tela
    }

    public void registraPresenca(View view){
        Intent listEventoPresenca = new Intent(this, ListaEventoEncontroPresencaActivity.class); // "intenção de abrir outra activity"
        startActivity(listEventoPresenca); // start a segunda tela
    }



}
