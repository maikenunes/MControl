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
public class EventoEncontroArrayAdapter<Pessoa> extends ArrayAdapter {
    private final Context context;
    private final List<net.maikenunes.mcontrol.entity.Encontro> itemsArrayList;

    public EventoEncontroArrayAdapter(Context context, List<net.maikenunes.mcontrol.entity.Encontro> itemsArrayList) {

        super(context, R.layout.row_list_busca_encontro, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_list_busca_encontro, parent, false);

        // 3. Get the two text view from the rowView
        TextView eId      = (TextView) rowView.findViewById(R.id.eId);
        TextView eDataIni = (TextView) rowView.findViewById(R.id.editDataIni);
        TextView eDataFim = (TextView) rowView.findViewById(R.id.editDataFim);

        // 4. Set the text for textView
        net.maikenunes.mcontrol.entity.Encontro encontro = this.itemsArrayList.get(position);
        eId.setText(String.valueOf(encontro.getId()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        eDataIni.setText(formatter.format(encontro.getDataHoraIni()));
        eDataFim.setText(formatter.format(encontro.getDataHoraFim()));

        // 5. retrn rowView
        return rowView;
    }
}
