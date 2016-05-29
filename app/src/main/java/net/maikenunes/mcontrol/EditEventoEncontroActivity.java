package net.maikenunes.mcontrol;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import net.maikenunes.mcontrol.database.EventoDAO;
import net.maikenunes.mcontrol.database.EventoEncontroDAO;
import net.maikenunes.mcontrol.entity.Encontro;
import net.maikenunes.mcontrol.entity.Evento;
import net.maikenunes.mcontrol.utils.Mask;
import net.maikenunes.mcontrol.utils.Valid;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class EditEventoEncontroActivity extends ActionBarActivity implements View.OnClickListener {

    private Encontro encontro;
    private int evenId;
    private DatePickerDialog iniDatePickerDialog;
    private DatePickerDialog fimDatePickerDialog;
    private TimePickerDialog iniTimePickerDialog;
    private TimePickerDialog fimTimePickerDialog;
    private ImageButton btn_data_ini;
    private EditText edit_data_ini;
    private ImageButton btn_data_fim;
    private EditText edit_data_fim;
    private ImageButton btn_hora_ini;
    private EditText edit_hora_ini;
    private ImageButton btn_hora_fim;
    private EditText edit_hora_fim;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_evento_encontro);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        int id;
        if(this.encontro == null){
            this.encontro = new Encontro();
            Intent intent = getIntent();
            id = intent.getIntExtra("ID", 0);
            this.evenId = intent.getIntExtra("EVENTO_ID", 0);
        }else{
            id = this.encontro.getId();
        }

        edit_data_ini = (EditText) findViewById(R.id.editDataIni);
        edit_data_ini.setInputType(InputType.TYPE_NULL);
        btn_data_ini  = (ImageButton) findViewById(R.id.btnDataIni);
        edit_hora_ini = (EditText) findViewById(R.id.editHoraIni);
        edit_hora_ini.setInputType(InputType.TYPE_NULL);
        btn_hora_ini  = (ImageButton) findViewById(R.id.btnHoraIni);
        edit_data_fim = (EditText) findViewById(R.id.editDataFim);
        edit_data_fim.setInputType(InputType.TYPE_NULL);
        btn_data_fim  = (ImageButton) findViewById(R.id.btnDataFim);
        edit_hora_fim = (EditText) findViewById(R.id.editHoraFim);
        edit_hora_fim.setInputType(InputType.TYPE_NULL);
        btn_hora_fim  = (ImageButton) findViewById(R.id.btnHoraFim);

        if (id > 0) {
            setTitle("Edit Encontro");

            EventoEncontroDAO evenEncDAO = new EventoEncontroDAO(this);
            this.encontro = evenEncDAO.getById(id);

            SimpleDateFormat formatterDate = new SimpleDateFormat("dd/MM/yyyy");
            edit_data_ini.setText(formatterDate.format(this.encontro.getDataHoraIni()));
            edit_data_fim.setText(formatterDate.format(this.encontro.getDataHoraFim()));

            SimpleDateFormat formatterTime = new SimpleDateFormat("HH:mm");
            edit_hora_ini.setText(formatterTime.format(this.encontro.getDataHoraIni()));
            edit_hora_fim.setText(formatterTime.format(this.encontro.getDataHoraFim()));

            EditText edit_id = (EditText) findViewById(R.id.editID);
            edit_id.setText(String.valueOf(this.encontro.getId()));

        }else{
            setTitle("Cadastro de Encontro");
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
            newCalendar.setTime(this.encontro.getDataHoraIni());
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
            newCalendar.setTime(this.encontro.getDataHoraFim());
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
        getMenuInflater().inflate(R.menu.menu_edit_evento_encontro, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                insertEventoEncontro();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void insertEventoEncontro(){
        // insere encontro ;)
        EditText edit_id_encontro      = (EditText) findViewById(R.id.editID);
        EditText edit_dataIni_encontro = (EditText) findViewById(R.id.editDataIni);
        EditText edit_horaIni_encontro = (EditText) findViewById(R.id.editHoraIni);
        EditText edit_dataFim_encontro = (EditText) findViewById(R.id.editDataFim);
        EditText edit_horaFim_encontro = (EditText) findViewById(R.id.editHoraFim);

        if(!Valid.isDateValid(edit_dataIni_encontro.getText().toString()) || !Valid.isTimeValid(edit_horaIni_encontro.getText().toString())){
            Toast.makeText(this, "Data/Hora inicial inválida!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!Valid.isDateValid(edit_dataFim_encontro.getText().toString()) || !Valid.isTimeValid(edit_horaFim_encontro.getText().toString())){
            Toast.makeText(this, "Data/Hora final inválida!", Toast.LENGTH_SHORT).show();
            return;
        }
        //Criando um mapa de informações para serem gravadas no bnaco - conjunto de chave-valor
        EventoEncontroDAO evenEncDao = new EventoEncontroDAO(this);

        SimpleDateFormat formatterDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.encontro.setEvenId(this.evenId);
        try {
            this.encontro.setDataHoraIni(formatterDateTime.parse(edit_dataIni_encontro.getText().toString() + " " + edit_horaIni_encontro.getText().toString()));
            this.encontro.setDataHoraFim(formatterDateTime.parse(edit_dataFim_encontro.getText().toString() + " " + edit_horaFim_encontro.getText().toString()));
        }catch (Exception e){
            Toast.makeText(this, "Data/Hora inválida!", Toast.LENGTH_SHORT).show();
            return;
        }

        long evenId;
        if(edit_id_encontro.getText().toString().equals("")){
            evenId = evenEncDao.insert(this.encontro);
            if(evenId > 0){
                this.encontro.setId((int)evenId);
                Toast.makeText(this, "Encontro inserido com sucesso: " + evenId, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Falha ao inserir encontro ", Toast.LENGTH_SHORT).show();
            }
        }else{
            this.encontro.setId(Integer.parseInt(edit_id_encontro.getText().toString()));
            evenId = evenEncDao.update(this.encontro);
            if(evenId > 0){
                evenId = this.encontro.getId();
                Toast.makeText(this, "Encontro alterado com sucesso: " + evenId, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Falha ao alterar encontro ", Toast.LENGTH_SHORT).show();
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
