package net.maikenunes.mcontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import net.maikenunes.mcontrol.R;

import java.util.List;

/**
 * Created by Maike Nunes on 25/04/2015.
 */
public class EventoArrayAdapter<Evento> extends ArrayAdapter {
    private final Context context;
    private final List<net.maikenunes.mcontrol.entity.Evento> itemsArrayList;

    public EventoArrayAdapter(Context context, List<net.maikenunes.mcontrol.entity.Evento> itemsArrayList) {

        super(context, R.layout.row_list_busca_evento, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_list_busca_evento, parent, false);

        // 3. Get the two text view from the rowView
        TextView eId    = (TextView) rowView.findViewById(R.id.eId);
        TextView eNome  = (TextView) rowView.findViewById(R.id.eNome);

        // 4. Set the text for textView
        net.maikenunes.mcontrol.entity.Evento evento = this.itemsArrayList.get(position);
        eId.setText(String.valueOf(evento.getId()));
        eNome.setText(evento.getNome());

        // 5. retrn rowView
        return rowView;
    }
}
