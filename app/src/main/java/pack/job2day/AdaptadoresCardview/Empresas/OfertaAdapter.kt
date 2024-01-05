package pack.job2day.AdaptadoresCardview.Empresas

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pack.job2day.R
import pack.job2day.Usuarios.Empresas.activity_empresa_crearoferta

class OfertaAdapter(
    var ofertas: MutableList<activity_empresa_crearoferta.Oferta>,
    private val onDeleteClickListener: (activity_empresa_crearoferta.Oferta) -> Unit
) : RecyclerView.Adapter<OfertaAdapter.OfertaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ofertas, parent, false)
        return OfertaViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfertaViewHolder, position: Int) {
        val oferta = ofertas[position]
        Log.d("OfertaAdapter", "Oferta en posición $position: $oferta")
        holder.bind(oferta, onDeleteClickListener)
    }

    override fun getItemCount() = ofertas.size

    class OfertaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvNombrePuesto: TextView = itemView.findViewById(R.id.tvNombrePuesto)
        private val tvDescripcionOferta: TextView = itemView.findViewById(R.id.tvDescripcionOferta)
        private val btnEliminarOferta: Button = itemView.findViewById(R.id.btnEliminarOferta)

        fun bind(oferta: activity_empresa_crearoferta.Oferta, onDeleteClickListener: (activity_empresa_crearoferta.Oferta) -> Unit) {
            tvNombrePuesto.text = oferta.nombrePuesto
            tvDescripcionOferta.text = oferta.descripcionOferta
            btnEliminarOferta.setOnClickListener { onDeleteClickListener(oferta) }
        }
    }
}
