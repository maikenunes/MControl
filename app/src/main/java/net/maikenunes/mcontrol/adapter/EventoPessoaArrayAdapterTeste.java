package net.maikenunes.mcontrol.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import net.maikenunes.mcontrol.R;
import net.maikenunes.mcontrol.entity.Pessoa;
import net.maikenunes.mcontrol.utils.DecodeBitmap;

import java.io.File;
import java.util.List;

/**
 * Created by Maike Nunes on 20/05/2015.
 */
public class EventoPessoaArrayAdapterTeste extends ArrayAdapter<Pessoa> {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    List<net.maikenunes.mcontrol.entity.Pessoa> pessoalist;
    private SparseBooleanArray mSelectedItemsIds;

    public EventoPessoaArrayAdapterTeste(Context context, int resourceId, List<net.maikenunes.mcontrol.entity.Pessoa> pessoalist) {
        super(context, resourceId, pessoalist);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.pessoalist = pessoalist;
        inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View view, ViewGroup parent) {
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
        net.maikenunes.mcontrol.entity.Pessoa pessoa = this.pessoalist.get(position);
        eId.setText(String.valueOf(pessoa.getId()));
        eNome.setText(pessoa.getNome());
        eEmail.setText(pessoa.getEmail());

        String srcFoto = pessoa.getFoto();
        if(srcFoto != null && !srcFoto.equals("")) {
            ImageView imgPes = (ImageView) rowView.findViewById(R.id.icon);
            imgPes.setImageBitmap(DecodeBitmap.decodeFile(new File(pessoa.getFotoFacial())));
            //imgPes.setRotation(-90);
        }
        return rowView;
    }

    public void remove(net.maikenunes.mcontrol.entity.Pessoa object) {
        pessoalist.remove(object);
        notifyDataSetChanged();
    }

    public List<net.maikenunes.mcontrol.entity.Pessoa> getPessoa() {
        return pessoalist;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value) {
            mSelectedItemsIds.put(position, value);
        }else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}
