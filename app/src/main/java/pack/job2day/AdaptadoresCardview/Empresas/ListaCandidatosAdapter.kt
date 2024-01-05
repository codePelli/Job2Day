package pack.job2day.AdaptadoresCardview.Empresas

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import pack.job2day.Clases.Empresas.Candidato
import pack.job2day.R

class ListaCandidatosAdapter(private val candidatosList: List<Candidato>) :
    RecyclerView.Adapter<ListaCandidatosAdapter.CandidatosViewHolder>() {

    var database =
        FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")

    class CandidatosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCandidatoNombre: TextView = itemView.findViewById(R.id.tvCandidatoNombre)
        val tvCandidatoEmail: TextView = itemView.findViewById(R.id.tvCandidatoEmail)
        val tvCandidatoOfertaAplicada: TextView =
            itemView.findViewById(R.id.tvCandidatoOfertaAplicada)
        val btnCandidatoVerCV: Button = itemView.findViewById(R.id.btnCandidatoVerCV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidatosViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_empresa_candidatoscard, parent, false)
        return CandidatosViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: CandidatosViewHolder, position: Int) {

        val currentItem = candidatosList[position]
        holder.tvCandidatoNombre.text = currentItem.nombre
        holder.tvCandidatoEmail.text = currentItem.email
        holder.tvCandidatoOfertaAplicada.text = currentItem.nombrePuesto
        holder.btnCandidatoVerCV.setOnClickListener {
            dialogCv(holder.itemView.context, currentItem)
        }

    }
    override fun getItemCount() = candidatosList.size

    private fun dialogCv(context: Context, Candidato: Candidato) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_cv_gestioncandidaturas, null)

        view.findViewById<TextView>(R.id.tvNombre).text = Candidato.nombre
        view.findViewById<TextView>(R.id.tvApellidos).text = Candidato.apellidos
        view.findViewById<TextView>(R.id.tvEmail).text = Candidato.email
        view.findViewById<TextView>(R.id.tvTelefono).text = Candidato.telefono
        view.findViewById<TextView>(R.id.tvPais).text = Candidato.pais
        view.findViewById<TextView>(R.id.tvPoblacion).text = Candidato.poblacion
        view.findViewById<TextView>(R.id.tvCodigoPostal).text = Candidato.codigoPostal
        view.findViewById<TextView>(R.id.tvFechaNacimiento).text = Candidato.fechaNacimiento
        view.findViewById<TextView>(R.id.tvFormacion).text = Candidato.formacion
        view.findViewById<TextView>(R.id.tvExperiencia).text = Candidato.experiencia
        view.findViewById<TextView>(R.id.tvConocimientos).text = Candidato.conocimientos
        view.findViewById<TextView>(R.id.tvOtros).text = Candidato.otros

        AlertDialog.Builder(context)
            .setTitle("CV del Candidato")
            .setView(view)
            .setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}

