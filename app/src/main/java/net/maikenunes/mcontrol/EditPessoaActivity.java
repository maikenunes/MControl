package net.maikenunes.mcontrol;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import net.maikenunes.mcontrol.database.CategoriaDAO;
import net.maikenunes.mcontrol.database.PessoaDAO;
import net.maikenunes.mcontrol.entity.Categoria;
import net.maikenunes.mcontrol.entity.Pessoa;
import net.maikenunes.mcontrol.utils.DecodeBitmap;
import net.maikenunes.mcontrol.utils.Mask;

import org.apache.http.protocol.HTTP;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class EditPessoaActivity extends ActionBarActivity {

    Pessoa pessoa;
    List<Categoria> categs;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static String mCurrentPhotoPath;       //string com caminho da imagem salva no filesystem do device
    static String rPhotoPath;
    static String rPhotoFacePath;
    static Bitmap bmpImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pessoa);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.pessoa = new Pessoa();

        carregaSpinner();

        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);

        EditText edit_phone = (EditText) findViewById(R.id.editTelefone);
        edit_phone.addTextChangedListener(Mask.insert("(##)####-#####", edit_phone));

        if( id > 0 ){
            setTitle("Edição de Pessoa");

            PessoaDAO pesDAO = new PessoaDAO(this);
            this.pessoa = pesDAO.getById(id);

            EditText edit_nome = (EditText) findViewById(R.id.editNome);
            edit_nome.setText(this.pessoa.getNome());
            EditText edit_email = (EditText) findViewById(R.id.editEmail);
            edit_email.setText(this.pessoa.getEmail());


            edit_phone.setText(this.pessoa.getTelefone());

            EditText edit_matricula = (EditText) findViewById(R.id.editMatricula);
            edit_matricula.setText(String.valueOf(this.pessoa.getMatricula()));

            EditText edit_id = (EditText) findViewById(R.id.editID);
            edit_id.setText(String.valueOf(this.pessoa.getId()));

            Spinner edit_categoria = (Spinner) findViewById(R.id.spnCategoria);
            edit_categoria.setSelection(this.encontraPosicaoCategoria(this.pessoa.getCategoria()));

            String srcFoto = pessoa.getFoto();
            if(srcFoto != null && !srcFoto.equals("")) {
                ImageView imgPes = (ImageView) findViewById(R.id.imgFoto);
                imgPes.setImageBitmap(BitmapFactory.decodeFile(pessoa.getFoto()));

                ImageView imgPesFace = (ImageView) findViewById(R.id.imgFotoFacial);
                imgPesFace.setImageBitmap(BitmapFactory.decodeFile(pessoa.getFotoFacial()));
            }
        }else{
            setTitle("Cadastro de Pessoa");
        }
    }

    public void carregaSpinner(){
        CategoriaDAO categorias = new CategoriaDAO(this);
        this.categs  = categorias.getAll();

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spnCategoria);
        // Spinner click listener
        //spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List categories = new ArrayList<String>();
        for (int x = 0; x<this.categs.size();x++){
            categories.add(this.categs.get(x).getNome());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_pessoa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:

                break;

            case R.id.action_save:
                try {
                    insertPessoa();
                } catch (Exception e) {
                    Log.w("TAG", "Error ao salvar pessoa " + e.getMessage());
                    return false;
                }

                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private File createImageFile(String suffixImg) throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator + "mControl/temp");
        storageDir.mkdirs();
        File image = File.createTempFile(
                "temp"+suffixImg,  //new SimpleDateFormat("HHmmss").format(new Date()),  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        if(suffixImg.equals("")){
            rPhotoPath = image.getAbsolutePath();
        }else{
            rPhotoFacePath = image.getAbsolutePath();
        }

        return image;
    }

    public void tirarFoto(View view){
        // inicio foto reconhecimento
        Intent insertIntent = new Intent(this, CameraActivity.class);
        File photoFile = null;
        File photoFileFace = null;
        try {
            photoFile     = createImageFile("");
            photoFileFace = createImageFile("Face");
        } catch (IOException ex) {
            Log.e("IMG", "Erro ao gravar arquivo da foto", ex);
        }
        insertIntent.putExtra("TITLE", "Foto pessoa");
        startActivityForResult(insertIntent, 0671);
        // fim foto reconhecimento

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // inicio utilizado para foto reconhecimento
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0671) {
            Bundle extras = data.getExtras();
            if (data.hasExtra("picture")) {
                Bitmap bmp = BitmapFactory.decodeFile(extras.getString("picture"));
                ImageView image = (ImageView) findViewById(R.id.imgFoto);
                image.setImageBitmap(bmp);

                try {
                    File fileFoto = new File(rPhotoPath);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(fileFoto);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                }catch (Exception exc){
                    Log.v("ERROR", "erro ao salvar foto: "+exc.getMessage());
                }
            }
            if (data.hasExtra("pictureFacial")) {
                Bitmap bmp = BitmapFactory.decodeFile(extras.getString("pictureFacial"));
                ImageView image = (ImageView) findViewById(R.id.imgFotoFacial);
                image.setImageBitmap(bmp);

                try {
                    File fileFoto = new File(rPhotoFacePath);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    FileOutputStream fos = new FileOutputStream(fileFoto);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                }catch (Exception exc){
                    Log.v("ERROR", "erro ao salvar foto face: "+exc.getMessage());
                }

            }
        }
        // fim utilizado para foto reconhecimento

    }

    public void insertPessoa(){

        //Buscando nome da pessoa digitado na pelo usuário
        EditText edit_id_pessoa    = (EditText) findViewById(R.id.editID);
        EditText edit_nome_pessoa  = (EditText) findViewById(R.id.editNome);
        EditText edit_email_pessoa = (EditText) findViewById(R.id.editEmail);
        EditText edit_matri_pessoa = (EditText) findViewById(R.id.editMatricula);
        EditText edit_telef_pessoa = (EditText) findViewById(R.id.editTelefone);
        Spinner edit_categ_pessoa  = (Spinner) findViewById(R.id.spnCategoria);

        if(edit_nome_pessoa.getText().toString().equals("")){
            Toast.makeText(this, "Nome é obrigatório!", Toast.LENGTH_SHORT).show();
            return;
        }else if(edit_email_pessoa.getText().toString().equals("")){
            Toast.makeText(this, "Email é obrigatório!", Toast.LENGTH_SHORT).show();
            return;
        }
        //Criando um mapa de informações para serem gravadas no bnaco - conjunto de chave-valor
        PessoaDAO pesDao = new PessoaDAO(this);

        this.pessoa.setNome( edit_nome_pessoa.getText().toString() );
        this.pessoa.setEmail(edit_email_pessoa.getText().toString());
        this.pessoa.setTelefone(edit_telef_pessoa.getText().toString());
        if(!edit_matri_pessoa.getText().toString().equals("")){
            this.pessoa.setMatricula(Integer.parseInt(edit_matri_pessoa.getText().toString()));
        }

        Categoria cat = this.categs.get(edit_categ_pessoa.getSelectedItemPosition());
        this.pessoa.setCategoria(cat.getId());

        long pesId;
        if(edit_id_pessoa.getText().toString().equals("")){
            pesId = pesDao.insert(this.pessoa);
            if(pesId > 0){
                this.pessoa.setId((int)pesId);
                Toast.makeText(this, "Pessoa inserida com sucesso: " + pesId, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Falha ao inserir Pessoa: " + this.pessoa.getNome(), Toast.LENGTH_SHORT).show();
            }
        }else{
            this.pessoa.setId(Integer.parseInt(edit_id_pessoa.getText().toString()));
            pesId = pesDao.update(this.pessoa);
            if(pesId > 0){
                pesId = this.pessoa.getId();
                Toast.makeText(this, "Pessoa alterada com sucesso: " + pesId, Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Falha ao alterar Pessoa: " + this.pessoa.getNome(), Toast.LENGTH_SHORT).show();
            }
        }

        if(rPhotoPath != null && !rPhotoPath.toString().equals("") && pesId > 0){
            File from = new File(rPhotoPath);
            File to = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/mControl/imgs");
            to.mkdirs(); // cria diretório caso necessário
            to = new File(to.getPath()+"/"+pesId+".png");
            from.renameTo(to); // move arquivo
            this.pessoa.setFoto(to.getAbsolutePath());

            from = new File(rPhotoFacePath);
            to = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/mControl/imgsFaces");
            to.mkdirs(); // cria diretório caso necessário
            to = new File(to.getPath()+"/"+pesId+".png");
            from.renameTo(to); // move arquivo
            this.pessoa.setFotoFacial(to.getAbsolutePath());

            pesId = pesDao.update(this.pessoa);
        }
        this.finish();
    }

    public int encontraPosicaoCategoria(int id){
        int x = 0;
        for (x = 0; x < this.categs.size(); x++){
            if(this.categs.get(x).getId() == id){
                break;
            }
        }
        return x;
    }

}
