package net.maikenunes.mcontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.maikenunes.mcontrol.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Maike Nunes on 18/05/2015.
 */
public class EventoEncontroPresencaArrayAdapter<Pessoa> extends ArrayAdapter {
    private final Context context;
    private final List<net.maikenunes.mcontrol.entity.Encontro> itemsArrayList;

    public EventoEncontroPresencaArrayAdapter(Context context, List<net.maikenunes.mcontrol.entity.Encontro> itemsArrayList) {

        super(context, R.layout.row_list_busca_encontro_presenca, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_list_busca_encontro_presenca, parent, false);

        // 3. Get the two text view from the rowView
        TextView eId      = (TextView) rowView.findViewById(R.id.eId);
        TextView eEvento  = (TextView) rowView.findViewById(R.id.editEvento);
        TextView eData    = (TextView) rowView.findViewById(R.id.editData);

        // 4. Set the text for textView
        net.maikenunes.mcontrol.entity.Encontro encontro = this.itemsArrayList.get(position);
        eId.setText(String.valueOf(encontro.getId()));
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        eEvento.setText(encontro.getEvento().getNome());
        eData.setText(formatter.format(encontro.getDataHoraIni()) + " | " + formatter.format(encontro.getDataHoraFim()));

        // 5. retrn rowView
        return rowView;
    }
}
