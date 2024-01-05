package pack.job2day.AdaptadoresCardview.Candidatos

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pack.job2day.Clases.Candidatos.Candidatura
import pack.job2day.R
import pack.job2day.Usuarios.Empresas.activity_empresa_crearoferta

class AplicarOfertaAdapter(private val list: ArrayList<activity_empresa_crearoferta.Oferta>) :
    RecyclerView.Adapter<AplicarOfertaAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.activity_card_layout,
            parent, false
        )
        itemView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        return MyViewHolder(itemView)

    }

    // Almacenará los candidatos que apliquen

    var candidatos: HashMap<String, String> = HashMap()

    // Agrega la referencia a la base de datos de "Empresas"

    val database =
        FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
    val nodoEmpresas = database.getReference("Empresas")

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = list[position]
        holder.tvNombreCarta.text = currentItem.nombrePuesto
        holder.tvFechaOfertaCarta.text = currentItem.fechaOferta


        holder.btnDetallesCarta.setOnClickListener {
            val oferta = list[position]
            val inflater = LayoutInflater.from(holder.itemView.context)
            val view = inflater.inflate(R.layout.dialog_oferta_detalles, null)

            // Datos de la oferta del dialog

            view.findViewById<TextView>(R.id.tvNombrePuesto).text = oferta.nombrePuesto
            view.findViewById<TextView>(R.id.tvFechaPuesto).text = oferta.fechaOferta
            view.findViewById<TextView>(R.id.tvDescripcionPuesto).text = oferta.descripcionOferta
            view.findViewById<TextView>(R.id.tvExpPuesto).text = oferta.experienciaOferta
            view.findViewById<TextView>(R.id.tvFormacionPuesto).text = oferta.formacionOferta


            AlertDialog.Builder(holder.itemView.context)
                .setTitle("Detalles de la oferta")
                .setView(view)
                .setPositiveButton("Cerrar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }


        holder.btnAplicarCarta.setOnClickListener {
            val userEmail = FirebaseAuth.getInstance().currentUser?.email
            val idOferta = currentItem.idOferta
            val database =
                FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
            val nodoCandidatos = database.getReference("Candidatos")
            val nodoEmpresas = database.getReference("Empresas")
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                nodoCandidatos.orderByKey().equalTo(userId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (usuarioSnapshot in snapshot.children) {
                                    val nodoUsuario = usuarioSnapshot.ref

                                    // Agrega userId del usuario a la lista de candidatos
                                    candidatos[usuarioSnapshot.key!!] = userId

                                    // Actualiza los datos en la base de datos
                                    nodoUsuario.child("OfertasAplicadas")
                                        .child(currentItem.nombrePuesto).setValue(
                                            Candidatura(
                                                currentItem.nombrePuesto,
                                                currentItem.fechaOferta,
                                                currentItem.descripcionOferta,
                                                currentItem.experienciaOferta,
                                                currentItem.formacionOferta,
                                                currentItem.idOferta.toString()
                                            )
                                        )

                                    Toast.makeText(
                                        holder.itemView.context,
                                        "Has aplicado a la oferta.",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    // Obtiene el nodo de la empresa que ha creado la oferta
                                    nodoEmpresas.addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                for (empresaSnapshot in snapshot.children) {
                                                    val nodoEmpresa = empresaSnapshot.ref
                                                    val nodoOfertas = nodoEmpresa.child("Ofertas")

                                                    // Busca la oferta por el idOferta
                                                    nodoOfertas.child(idOferta.toString())
                                                        .addListenerForSingleValueEvent(object : ValueEventListener {
                                                            override fun onDataChange(ofertaSnapshot: DataSnapshot) {
                                                                if (ofertaSnapshot.exists()) {
                                                                    // Obtiene el nodo de la oferta
                                                                    val nodoOferta = ofertaSnapshot.ref

                                                                    // Agrega el candidato a la oferta en el nodo de Candidaturas dentro de la oferta creada
                                                                    nodoOferta.child("Candidaturas")
                                                                        .child(userId)
                                                                        .setValue(true)
                                                                }
                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                // Log the error
                                                            }
                                                        })
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            // Log the error
                                        }
                                    })
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }
    }


    override fun getItemCount(): Int {
        Log.d("CardViewDebug", "getItemCount: ${list.size}")
        return list.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val btnAplicarCarta: Button = itemView.findViewById(R.id.btnAplicarCarta)
        val btnDetallesCarta: Button = itemView.findViewById(R.id.btDetallesCarta)
        val tvNombreCarta: TextView = itemView.findViewById(R.id.tvNombreCarta)
        val tvFechaOfertaCarta: TextView = itemView.findViewById(R.id.tvFechaOfertaCarta)

    }

}


