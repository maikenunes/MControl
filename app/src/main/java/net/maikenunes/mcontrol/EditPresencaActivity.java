package net.maikenunes.mcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import net.maikenunes.mcontrol.adapter.EventoPessoaArrayAdapter;
import net.maikenunes.mcontrol.database.EventoEncontroDAO;
import net.maikenunes.mcontrol.database.EventoPessoaDAO;
import net.maikenunes.mcontrol.database.PessoaDAO;
import net.maikenunes.mcontrol.database.PresencaDAO;
import net.maikenunes.mcontrol.entity.Encontro;
import net.maikenunes.mcontrol.entity.EventoPessoa;
import net.maikenunes.mcontrol.entity.Pessoa;
import net.maikenunes.mcontrol.entity.Presenca;
import net.maikenunes.mcontrol.utils.PersonRecognizer;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EditPresencaActivity extends ActionBarActivity {

    private Encontro encontro;
    private Pessoa pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_presenca);

        setTitle("Presença");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(this.encontro == null){
            EventoEncontroDAO encDAO = new EventoEncontroDAO(this);
            this.encontro = encDAO.getById(getIntent().getIntExtra("ID", 0));
            this.pessoa = new Pessoa();
        }

        this.buscarEventoPessoa();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_presenca, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_list:
                Intent intent = new Intent(getBaseContext(),EditPresencaConfirmActivity.class);
                intent.putExtra("ID", this.encontro.getId());
                startActivity(intent);
                break;
            case R.id.action_reader:
                IntentIntegrator integrator = new IntentIntegrator(this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("ESCANEAR CODIGO");
                integrator.setCameraId(0);
                integrator.initiateScan();
                break;
            case R.id.action_photo:
                // inicio foto reconhecimento
                Intent insertIntent = new Intent(this, CameraActivity.class);
                insertIntent.putExtra("TITLE", "Foto Rec pessoa");
                insertIntent.putExtra("evento_id", this.encontro.getEvenId());
                insertIntent.putExtra("encontro_id", this.encontro.getId());
                startActivityForResult(insertIntent, 0671);
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void buscarEventoPessoa(){

        EventoPessoaDAO evenPesDao = new EventoPessoaDAO(this);

        List<Pessoa> pessoas = evenPesDao.getByEventNotPresenc(this.encontro);
        EventoPessoaArrayAdapter<Pessoa> adapter = new EventoPessoaArrayAdapter(this, pessoas);

        final ListView lista = (ListView) findViewById(R.id.listViewEventoEncontroPessoa);

        if(pessoas.size() == 0){
            Toast.makeText(this, "pessoa não encontrado! :(", Toast.LENGTH_SHORT).show();
        }else{
            lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                //Pessoa pes = (Pessoa) lista.getItemAtPosition(position);
                //insertPresenca(pes, encontro);

                }
            });
        }
        lista.setAdapter(adapter);
    }

    public void insertPresenca(Pessoa pes, Encontro enc){

        try{
            Presenca ePresenca = new Presenca();
            ePresenca.setPesId(pes.getId());
            ePresenca.setEncId(enc.getId());
            ePresenca.setEntrada(new Date());
            ePresenca.setSaida(enc.getDataHoraFim());
            PresencaDAO preDAO = new PresencaDAO(this);
            long id = preDAO.insert(ePresenca);
            if(id > 0){
                Toast.makeText(this, "Presenca cadastrada com sucesso!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Falha ao inserir Presenca!", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(this, "Falha ao cadastrar Presenca!"+e.getMessage(), Toast.LENGTH_LONG).show();
        }

        this.buscarEventoPessoa();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if(resultCode == RESULT_OK && requestCode == 0671){

            Bundle extras = intent.getExtras();
            if (intent.hasExtra("pictureFacial")) {
                Bitmap bmpImage = BitmapFactory.decodeFile(extras.getString("pictureFacial"));

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        insertPresenca(pessoa, encontro);
                    }
                }).setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), "LEITURA CANCELADA!", Toast.LENGTH_LONG).show();
                    }
                }).setNeutralButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent insertIntent = new Intent(getBaseContext(), CameraActivity.class);
                        insertIntent.putExtra("TITLE", "Foto Rec pessoa");
                        insertIntent.putExtra("evento_id", encontro.getEvenId());
                        insertIntent.putExtra("encontro_id", encontro.getId());
                        startActivityForResult(insertIntent, 0671);
                    }
                });

                EventoPessoaDAO evenPesDao = new EventoPessoaDAO(this);
                List<Pessoa> pessoas = evenPesDao.getByEventNotPresenc(this.encontro);

                List<File> imgsReco = new ArrayList<>();
                File tempPes = null;
                int[] posicoesImgsList = new int[(pessoas.size() > 0 ? pessoas.size() : 1)];
                int countImgsFace = 0;
                for (int x = 0; x<pessoas.size();x++){
                     if(pessoas.get(x).getFotoFacial() != null) {
                        tempPes = new File(pessoas.get(x).getFotoFacial());
                        if (tempPes.isFile()) {
                            imgsReco.add(tempPes);
                            posicoesImgsList[countImgsFace] = x;
                            countImgsFace++;
                        }
                    }
                }

                PersonRecognizer personRecon = new PersonRecognizer(imgsReco);
                personRecon.load();

                Mat matImg = new Mat();
                Utils.bitmapToMat(bmpImage, matImg);

                String textItem = "";
                int rankingReco = 999;
                try {
                    textItem = personRecon.predict(matImg);
                    rankingReco = personRecon.getProb();
                }catch (Exception ec){

                }

                AlertDialog dialog = builder.create();
                LayoutInflater inflater = getLayoutInflater();
                View dialogLayout = inflater.inflate(R.layout.alert_face_image, null);
                dialog.setView(dialogLayout);
                ImageView image = (ImageView) dialogLayout.findViewById(R.id.imageView);
                image.setImageBitmap(bmpImage);

                dialog.setTitle("Confirmação de presença: " + rankingReco);
                dialog.show();
                TextView txtMesg = (TextView) dialogLayout.findViewById(R.id.textView);

                try {
                    int posItem = Integer.parseInt(textItem);
                    posItem = posicoesImgsList[posItem];
                    if (posItem != -1 && rankingReco < 80) {
                        txtMesg.setText("Confirmar '"+pessoas.get(posItem).getNome()+"' ?");
                        this.pessoa = pessoas.get(posItem);
                    }else{
                        txtMesg.setText("Nenhuma pessoa encontrada");
                        dialog.getButton(Dialog.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);
                        dialog.getButton(Dialog.BUTTON_NEGATIVE).setText("Cancelar");
                    }
                }catch (Exception falPes){
                    txtMesg.setText("Nenhuma pessoa encontrada");
                    dialog.getButton(Dialog.BUTTON_POSITIVE).setVisibility(View.INVISIBLE);
                    dialog.getButton(Dialog.BUTTON_NEGATIVE).setText("Cancelar");
                }
            }
        }else{
            try {
                IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
                if (scanningResult.getFormatName() != null) {
                    //Toast.makeText(this, scanningResult.getFormatName() + " : " + scanningResult.getContents(), Toast.LENGTH_LONG).show();

                    EventoPessoaDAO evenPesDao = new EventoPessoaDAO(this);
                    Pessoa pesCont = evenPesDao.getByEventCodExterno(this.encontro.getId(), this.encontro.getEvenId(), scanningResult.getContents().toString());

                    if(pesCont.getId() > 0) {
                        insertPresenca(pesCont, encontro);
                    }else{
                        //Toast.makeText(this, scanningResult.getFormatName() + " : " + scanningResult.getContents(), Toast.LENGTH_LONG).show();
                        pesCont = evenPesDao.getPesByCodExternoPresenca(this.encontro.getId(), this.encontro.getEvenId(), scanningResult.getContents().toString());
                        if(pesCont.getId() > 0){
                            Toast.makeText(this, "'"+pesCont.getNome()+"' já acessou! ", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(this, scanningResult.getContents() + " não foi encontrada! ", Toast.LENGTH_LONG).show();
                        }


                    }

                } else {
                    Toast.makeText(this, "SCAN CANCELADO", Toast.LENGTH_LONG).show();
                }
            }catch (Exception ex){

            }
        }

    }


}
