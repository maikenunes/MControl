package net.maikenunes.mcontrol;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import net.maikenunes.mcontrol.database.EventoDAO;
import net.maikenunes.mcontrol.database.PresencaDAO;
import net.maikenunes.mcontrol.entity.Evento;
import net.maikenunes.mcontrol.entity.Relatorio;
import net.maikenunes.mcontrol.utils.CsvFileWriter;

import java.io.File;
import java.util.List;


public class EditRelatorioActivity extends ActionBarActivity {

    private Evento evento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_relatorio);

        setTitle("Relatórios");

        int id;
        if(this.evento == null){
            this.evento = new Evento();
            Intent intent = getIntent();
            id = intent.getIntExtra("ID", 0);
        }else{
            id = this.evento.getId();
        }

        if (id > 0) {
            EventoDAO evenDAO = new EventoDAO(this);
            this.evento = evenDAO.getById(id);
        }else{
            this.finish();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_relatorio, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void gerarRelatorioFaltas(View v){
        PresencaDAO presDao = new PresencaDAO(this);
        List<Relatorio> rel = presDao.getByEventNotPresen(this.evento.getId());
        if(rel.size() > 0) {
            CsvFileWriter.writeCsvFile("relatorio_faltas_even_" + this.evento.getNome() + ".csv", rel, "faltas");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Deseja enviar por email?");
            //builder.setMessage("Qualifique este software");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Uri attachedUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/relatorio_faltas_even_" + evento.getNome() + ".csv"));

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"maikenuness@gmail.com"}); // recipients
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Trabalho 2015/01 Maike Nunes");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Segue anexo relatório de faltas exportado diretamento do MControl!! :) ");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, attachedUri);

                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(emailIntent);
                    } else {
                        Toast.makeText(getBaseContext(), "Nenhum software de e-mail instalado", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(getBaseContext(), "Relatório salvo com sucesso!", Toast.LENGTH_LONG).show();
                }
            });
            builder.create().show();


        }else{
            Toast.makeText(this, "Nenhuma presença registrada no evento!", Toast.LENGTH_LONG).show();
        }
    }

    public void gerarRelatorioPresencas(View v){
        PresencaDAO presDao = new PresencaDAO(this);
        List<Relatorio> rel = presDao.getByEvent(this.evento.getId());
        if(rel.size() > 0) {
            CsvFileWriter.writeCsvFile("relatorio_presencas_even_" + this.evento.getNome() + ".csv", rel, "presencas");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Deseja enviar por email?");
            //builder.setMessage("Qualifique este software");
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Uri attachedUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/relatorio_presencas_even_" + evento.getNome() + ".csv"));

                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    emailIntent.setType("text/html");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"maikenuness@gmail.com"}); // recipients
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Trabalho 2015/01 Maike Nunes");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "Segue anexo relatório de presenças exportado diretamento do MControl!! :) ");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, attachedUri);

                    if (emailIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(emailIntent);
                    } else {
                        Toast.makeText(getBaseContext(), "Nenhum software de e-mail instalado", Toast.LENGTH_LONG).show();
                    }
                }
            });
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Toast.makeText(getBaseContext(), "Relatório salvo com sucesso!", Toast.LENGTH_LONG).show();
                }
            });
            builder.create().show();


        }else{
            Toast.makeText(this, "Nenhuma presença registrada no evento!", Toast.LENGTH_LONG).show();
        }

    }

}
