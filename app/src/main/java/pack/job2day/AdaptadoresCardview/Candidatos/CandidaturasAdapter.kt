package pack.job2day.AdaptadoresCardview.Candidatos

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pack.job2day.Clases.Candidatos.Candidatura
import pack.job2day.R


class CandidaturasAdapter(var list: ArrayList<Candidatura>) :
    RecyclerView.Adapter<CandidaturasAdapter.CandidaturasViewHolder>() {

    // FIREBASE
    val database = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
    val nodoEmpresas = database.getReference("Empresas")
    val userEmail = FirebaseAuth.getInstance().currentUser?.email

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidaturasViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_candidatos_card,
            parent, false
        )
        itemView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        return CandidaturasViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CandidaturasViewHolder, position: Int) {
        val currentItem = list[position]
        holder.tvNombreCandidatura.text = currentItem.nombrePuesto
        holder.tvFechaOferta.text = currentItem.fechaOferta

        // DETALLES CANDIDATURA
        holder.btnVerCandidatura.setOnClickListener {
            dialogDetalles(holder.itemView.context, currentItem)

        }

        // CANCELAR CANDIDATURA
        holder.btnCancelarCandidatura.setOnClickListener {
            if (userEmail != null) {
                val encodedEmail = userEmail.replace(".", ",")
                nodoEmpresas.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (empresaSnapshot in snapshot.children) {
                                val nodoEmpresa = empresaSnapshot.ref
                                val nodoOfertas = nodoEmpresa.child("Ofertas")
                                nodoOfertas.child(currentItem.nombrePuesto).child("Candidatos").child(encodedEmail).removeValue()
                                list.removeAt(holder.adapterPosition)
                                notifyItemRemoved(holder.adapterPosition)
                                break
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e(ContentValues.TAG, "Error al leer el nodo de la empresa: ${error.message}")
                    }
                })
            }
        }
    }

    private fun dialogDetalles(context: Context, candidatura: Candidatura) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_oferta_detalles, null)

        view.findViewById<TextView>(R.id.tvNombrePuesto).text = candidatura.nombrePuesto
        view.findViewById<TextView>(R.id.tvFechaPuesto).text = candidatura.fechaOferta
        view.findViewById<TextView>(R.id.tvDescripcionPuesto).text = candidatura.descripcionPuesto
        view.findViewById<TextView>(R.id.tvExpPuesto).text = candidatura.experienciaPuesto
        view.findViewById<TextView>(R.id.tvFormacionPuesto).text = candidatura.formacionPuesto

        AlertDialog.Builder(context)
            .setTitle("Detalles de la oferta")
            .setView(view)
            .setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CandidaturasViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnVerCandidatura: Button = itemView.findViewById(R.id.btnVerCandidatura)
        val btnCancelarCandidatura: Button = itemView.findViewById(R.id.btnCancelarCandidatura)
        val tvNombreCandidatura: TextView = itemView.findViewById(R.id.tvNombreCandidatura)
        val tvFechaOferta: TextView = itemView.findViewById(R.id.tvFechaOferta)
    }
}
