package net.maikenunes.mcontrol.utils;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import net.maikenunes.mcontrol.entity.Relatorio;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Maike Nunes on 20/05/2015.
 */
public class CsvFileWriter {
    //Delimiter used in CSV file
    private static final String COMMA_DELIMITER = ";";
    private static final String NEW_LINE_SEPARATOR = "\n";

    //CSV file header
    private static final String FILE_HEADER     = "idEvendo;idEncontro;matricula;idPessoa;nome;email;chegada;saida;foto";
    private static final String FILE_HEADER_FAL = "idEvendo;idEncontro;matricula;idPessoa;nome;email;encontro";

    public static void writeCsvFile(String fileName, List<Relatorio> relatorio, String formatCsv) {

        FileWriter fileWriter = null;

        try {
            String fileNameCsv = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/"+fileName;

            File testFile = new File(fileNameCsv);
            if(testFile.isFile()){
                testFile.delete();
            }

            fileWriter = new FileWriter(fileNameCsv);

            //Write the CSV file header
            if(formatCsv.toString().equals("faltas")){
                fileWriter.append(FILE_HEADER_FAL.toString());
            }else{
                fileWriter.append(FILE_HEADER.toString());
            }

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            if(relatorio.size() == 0){
                return;
            }
            //Write a new student object list to the CSV file
            for (int x = 0; x < relatorio.size(); x++) {
                fileWriter.append(String.valueOf(relatorio.get(x).getEvento().getId()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(relatorio.get(x).getPresenca().getEncId()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(relatorio.get(x).getPessoa().getMatricula()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(relatorio.get(x).getPessoa().getId()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(relatorio.get(x).getPessoa().getNome());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(relatorio.get(x).getPessoa().getEmail());
                fileWriter.append(COMMA_DELIMITER);
                if(formatCsv.toString().equals("faltas")){
                    fileWriter.append(formatter.format(relatorio.get(x).getEncontro().getDataHoraIni()));
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }else{
                    fileWriter.append(formatter.format(relatorio.get(x).getPresenca().getEntrada()));
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(formatter.format(relatorio.get(x).getPresenca().getSaida()));
                    fileWriter.append(COMMA_DELIMITER);
                    fileWriter.append(relatorio.get(x).getPessoa().getFoto().toString());
                    fileWriter.append(NEW_LINE_SEPARATOR);
                }

            }

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!"+e.getMessage());
            e.printStackTrace();
        } finally {

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }
    }
}
