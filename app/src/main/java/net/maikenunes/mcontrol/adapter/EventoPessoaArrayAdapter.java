package net.maikenunes.mcontrol.adapter;

import android.content.Context;
import android.util.Log;
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
import java.util.List;

/**
 * Created by Maike Nunes on 18/05/2015.
 */
public class EventoPessoaArrayAdapter<Pessoa> extends ArrayAdapter {
    private final Context context;
    private final List<net.maikenunes.mcontrol.entity.Pessoa> itemsArrayList;

    public EventoPessoaArrayAdapter(Context context, List<net.maikenunes.mcontrol.entity.Pessoa> itemsArrayList) {

        super(context, R.layout.row_list_multiple_select_pessoa, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_list_multiple_select_pessoa, parent, false);

        // 3. Get the two text view from the rowView
        TextView eId    = (TextView) rowView.findViewById(R.id.eId);
        TextView eNome  = (TextView) rowView.findViewById(R.id.eNome);
        TextView eEmail = (TextView) rowView.findViewById(R.id.eEmail);
        CheckedTextView eSelect = (CheckedTextView) rowView.findViewById(R.id.editSelect);

        // 4. Set the text for textView
        net.maikenunes.mcontrol.entity.Pessoa pessoa = this.itemsArrayList.get(position);
        eId.setText(String.valueOf(pessoa.getId()));
        eNome.setText(pessoa.getNome());
        eEmail.setText(pessoa.getEmail());

        String srcFoto = pessoa.getFoto();
        if(srcFoto != null && !srcFoto.equals("")) {
            ImageView imgPes = (ImageView) rowView.findViewById(R.id.icon);
            imgPes.setImageBitmap(DecodeBitmap.decodeFile(new File(pessoa.getFotoFacial())));
            //imgPes.setRotation(-90);
        }

        // 5. retrn rowView
        return rowView;
    }
}
