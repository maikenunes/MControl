package net.maikenunes.mcontrol;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.maikenunes.mcontrol.database.EventoDAO;
import net.maikenunes.mcontrol.database.PresencaDAO;
import net.maikenunes.mcontrol.entity.Evento;
import net.maikenunes.mcontrol.entity.Relatorio;
import net.maikenunes.mcontrol.utils.CsvFileWriter;
import net.maikenunes.mcontrol.utils.Valid;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class EditEventoActivity extends ActionBarActivity implements View.OnClickListener {

    private Evento evento;
    private EditText edit_data_ini;
    private ImageButton btn_data_ini;
    private EditText edit_data_fim;
    private ImageButton btn_data_fim;
    private EditText edit_hora_ini;
    private ImageButton btn_hora_ini;
    private EditText edit_hora_fim;
    private ImageButton btn_hora_fim;

    private DatePickerDialog iniDatePickerDialog;
    private DatePickerDialog fimDatePickerDialog;

    private TimePickerDialog iniTimePickerDialog;
    private TimePickerDialog fimTimePickerDialog;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_evento);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("Evento");

        int id;
        if(this.evento == null){
            this.evento = new Evento();
            Intent intent = getIntent();
            id = intent.getIntExtra("ID", 0);
        }else{
            id = this.evento.getId();
        }

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        edit_data_ini = (EditText) findViewById(R.id.editDataIni);
        btn_data_ini  = (ImageButton) findViewById(R.id.btnDataIni);
        edit_data_ini.setInputType(InputType.TYPE_NULL);

        edit_data_fim = (EditText) findViewById(R.id.editDataFim);
        btn_data_fim  = (ImageButton) findViewById(R.id.btnDataFim);
        edit_data_fim.setInputType(InputType.TYPE_NULL);

        edit_hora_ini = (EditText) findViewById(R.id.editHoraIni);
        btn_hora_ini  = (ImageButton) findViewById(R.id.btnHoraIni);
        edit_hora_ini.setInputType(InputType.TYPE_NULL);

        edit_hora_fim = (EditText) findViewById(R.id.editHoraFim);
        btn_hora_fim  = (ImageButton) findViewById(R.id.btnHoraFim);
        edit_hora_fim.setInputType(InputType.TYPE_NULL);

        EditText edit_qtd = (EditText) findViewById(R.id.editQtd);

        if (id > 0) {

            EventoDAO evenDAO = new EventoDAO(this);
            this.evento = evenDAO.getById(id);

            EditText edit_nome = (EditText) findViewById(R.id.editNome);
            edit_nome.setText(this.evento.getNome());

            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
            edit_data_ini.setText(formatterDate.format(this.evento.getDataHoraInicial()));
            edit_data_fim.setText(formatterDate.format(this.evento.getDataHoraFinal()));

            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
            edit_hora_ini.setText(formatterTime.format(this.evento.getDataHoraInicial()));
            edit_hora_fim.setText(formatterTime.format(this.evento.getDataHoraFinal()));

            EditText edit_descricao = (EditText) findViewById(R.id.editDescricao);
            edit_descricao.setText(String.valueOf(this.evento.getDescricao()));

            EditText edit_id = (EditText) findViewById(R.id.editID);
            edit_id.setText(String.valueOf(this.evento.getId()));

            edit_qtd.setText(String.valueOf(evenDAO.getCountEncontros(id)));
            edit_qtd.setEnabled(false);

        }else{
            TextView txt_qtd = (TextView) findViewById(R.id.txtQtd);
            txt_qtd.setVisibility(View.INVISIBLE);
            edit_qtd.setVisibility(View.INVISIBLE);
        }

        btn_data_ini.setOnClickListener(this);
        edit_data_ini.setOnClickListener(this);
        btn_data_fim.setOnClickListener(this);
        edit_data_fim.setOnClickListener(this);
        btn_hora_ini.setOnClickListener(this);
        edit_hora_ini.setOnClickListener(this);
        btn_hora_fim.setOnClickListener(this);
        edit_hora_fim.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        if(!edit_data_ini.getText().toString().equals("")) {
            newCalendar.setTime(this.evento.getDataHoraInicial());
        }

        iniDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edit_data_ini.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        iniTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                view.setIs24HourView(false);
                edit_hora_ini.setText(hourOfDay + ":" + minute);
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);


        if(!edit_data_fim.getText().toString().equals("")) {
            newCalendar.setTime(this.evento.getDataHoraFinal());
        }
        fimDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                edit_data_fim.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        fimTimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                view.setIs24HourView(false);
                edit_hora_fim.setText(hourOfDay + ":" + minute);
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_evento, menu);

        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);

        if(id>0){
            menu.findItem(R.id.action_person).setVisible(true);
            menu.findItem(R.id.action_relat).setVisible(true);
            menu.findItem(R.id.action_encontro).setVisible(true);
        }else{
            menu.findItem(R.id.action_person).setVisible(false);
            menu.findItem(R.id.action_relat).setVisible(false);
            menu.findItem(R.id.action_encontro).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertEvento();
                break;
            case R.id.action_person:
                Intent intentPessoa = new Intent(getBaseContext(),ListaEventoPessoaActivity.class);
                intentPessoa.putExtra("ID", this.evento.getId());
                startActivity(intentPessoa);
                break;
            case R.id.action_encontro:
                Intent intentEncontro = new Intent(getBaseContext(),ListaEventoEncontroActivity.class);
                intentEncontro.putExtra("ID", this.evento.getId());
                startActivity(intentEncontro);
                break;
            case R.id.action_relat:
                Intent intentRelatorio = new Intent(getBaseContext(),EditRelatorioActivity.class);
                intentRelatorio.putExtra("ID", this.evento.getId());
                startActivity(intentRelatorio);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void insertEvento(){
        // insere evento ;)
        EditText edit_id_evento      = (EditText) findViewById(R.id.editID);
        EditText edit_nome_evento    = (EditText) findViewById(R.id.editNome);
        EditText edit_dataIni_evento = (EditText) findViewById(R.id.editDataIni);
        EditText edit_horaIni_evento = (EditText) findViewById(R.id.editHoraIni);
        EditText edit_dataFim_evento = (EditText) findViewById(R.id.editDataFim);
        EditText edit_horaFim_evento = (EditText) findViewById(R.id.editHoraFim);
        EditText edit_descr_evento   = (EditText) findViewById(R.id.editDescricao);

        if(edit_nome_evento.getText().toString().equals("")){
            Toast.makeText(this, "Nome é obrigatório!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!Valid.isDateValid(edit_dataIni_evento.getText().toString()) || !Valid.isTimeValid(edit_horaIni_evento.getText().toString())){
            Toast.makeText(this, "Data/Hora inicial inválida!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!Valid.isDateValid(edit_dataFim_evento.getText().toString()) || !Valid.isTimeValid(edit_horaFim_evento.getText().toString())){
            Toast.makeText(this, "Data/Hora final inválida!", Toast.LENGTH_SHORT).show();
            return;
        }
        //Criando um mapa de informações para serem gravadas no bnaco - conjunto de chave-valor
        EventoDAO evenDao = new EventoDAO(this);

        this.evento.setNome(edit_nome_evento.getText().toString());
        this.evento.setDescricao(edit_descr_evento.getText().toString());

        SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        try {
            this.evento.setDataHoraInicial(formatterDateTime.parse(edit_dataIni_evento.getText().toString() + " " + edit_horaIni_evento.getText().toString()));
            this.evento.setDataHoraFinal(formatterDateTime.parse(edit_dataFim_evento.getText().toString() + " " + edit_horaFim_evento.getText().toString()));
        }catch (Exception e){
            Toast.makeText(this, "Data/Hora inválida!", Toast.LENGTH_SHORT).show();
            return;
        }

        long evenId;
        if(edit_id_evento.getText().toString().equals("")){
            evenId = evenDao.insert(this.evento);
            if(evenId > 0){
                this.evento.setId((int)evenId);
                Toast.makeText(this, "Evento inserido com sucesso: " + evenId, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Falha ao inserir evento: " + this.evento.getNome(), Toast.LENGTH_SHORT).show();
            }
        }else{
            this.evento.setId(Integer.parseInt(edit_id_evento.getText().toString()));
            evenId = evenDao.update(this.evento);
            if(evenId > 0){
                evenId = this.evento.getId();
                Toast.makeText(this, "Evento alterado com sucesso: " + evenId, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Falha ao alterar evento: " + this.evento.getNome(), Toast.LENGTH_SHORT).show();
            }
        }
        this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v == btn_data_ini || edit_data_ini == v) {
            iniDatePickerDialog.show();
        } else if(v == btn_data_fim || edit_data_fim == v) {
            fimDatePickerDialog.show();
        } else if(v == btn_hora_ini || edit_hora_ini == v) {
            iniTimePickerDialog.show();
        } else if(v == btn_hora_fim || edit_hora_fim == v) {
            fimTimePickerDialog.show();
        }
    }
}
