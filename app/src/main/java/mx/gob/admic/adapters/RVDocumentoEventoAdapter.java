package mx.gob.admic.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.gob.admic.R;
import mx.gob.admic.model.DocumentoEvento;

/**
 * Created by codigus on 24/11/2017.
 */

public class RVDocumentoEventoAdapter extends RecyclerView.Adapter<RVDocumentoEventoAdapter.ViewHolderDocumentoEvento> {
    private Context context;
    private List<DocumentoEvento> documentosEvento;

    public RVDocumentoEventoAdapter(Context context, List<DocumentoEvento> documentosEvento) {
        this.context = context;
        this.documentosEvento = documentosEvento;
    }


    @Override
    public ViewHolderDocumentoEvento onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_documento, parent, false);
        return new ViewHolderDocumentoEvento(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderDocumentoEvento holder, int position) {
        int tipoDocumento = documentosEvento.get(position).getFormato().getIdFormato();

        switch(tipoDocumento) {
            case 1:
                holder.imagenFormato.setImageResource(R.mipmap.ic_pdf);
                break;
            case 2:
                holder.imagenFormato.setImageResource(R.mipmap.ic_doc);
                break;
            case 3:
                holder.imagenFormato.setImageResource(R.mipmap.ic_xls);
                break;
            default:
                holder.imagenFormato.setImageResource(R.mipmap.ic_unknow);
                break;
        }

        holder.tituloDocumento.setText(documentosEvento.get(position).getTitulo());
        holder.url = documentosEvento.get(position).getRutaDocumento();
        holder.context = context;
    }

    @Override
    public int getItemCount() {
        return documentosEvento.size();
    }

    class ViewHolderDocumentoEvento extends RecyclerView.ViewHolder {
        ImageView imagenFormato;
        TextView tituloDocumento;
        String url;
        Context context;

        ViewHolderDocumentoEvento(View itemview) {
            super(itemview);

            imagenFormato = (ImageView) itemView.findViewById(R.id.img_formato_documento);
            tituloDocumento = (TextView) itemView.findViewById(R.id.tv_titulo_documento);

            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }
    }
}
