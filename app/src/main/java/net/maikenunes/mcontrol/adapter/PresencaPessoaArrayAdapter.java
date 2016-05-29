package net.maikenunes.mcontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import net.maikenunes.mcontrol.R;
import net.maikenunes.mcontrol.utils.DecodeBitmap;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Maike Nunes on 18/05/2015.
 */
public class PresencaPessoaArrayAdapter<Pessoa> extends ArrayAdapter {
    private final Context context;
    private final List<net.maikenunes.mcontrol.entity.Presenca> itemsArrayList;

    public PresencaPessoaArrayAdapter(Context context, List<net.maikenunes.mcontrol.entity.Presenca> itemsArrayList) {

        super(context, R.layout.row_list_busca_presenca, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_list_busca_presenca, parent, false);

        // 3. Get the two text view from the rowView
        TextView eId         = (TextView) rowView.findViewById(R.id.eId);
        TextView eNome       = (TextView) rowView.findViewById(R.id.eNome);
        TextView eEmail      = (TextView) rowView.findViewById(R.id.eEmail);
        TextView eDataAcesso = (TextView) rowView.findViewById(R.id.eDataAcesso);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

        // 4. Set the text for textView
        net.maikenunes.mcontrol.entity.Presenca presenca = this.itemsArrayList.get(position);
        eId.setText(String.valueOf(presenca.getId()));
        eNome.setText(presenca.getPessoa().getNome());
        eEmail.setText(presenca.getPessoa().getEmail());
        eDataAcesso.setText(formatter.format(presenca.getEntrada()));

        String srcFoto = presenca.getPessoa().getFoto();
        if(srcFoto != null && !srcFoto.equals("")) {
            ImageView imgPes = (ImageView) rowView.findViewById(R.id.icon);
            imgPes.setImageBitmap(DecodeBitmap.decodeFile(new File(presenca.getPessoa().getFotoFacial())));
            //imgPes.setRotation(-90);
        }

        // 5. retrn rowView
        return rowView;
    }
}
