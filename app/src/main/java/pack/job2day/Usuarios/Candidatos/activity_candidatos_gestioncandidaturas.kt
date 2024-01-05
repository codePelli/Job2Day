package pack.job2day.Usuarios.Candidatos

import pack.job2day.AdaptadoresCardview.Candidatos.CandidaturasAdapter
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import pack.job2day.Clases.Candidatos.Candidatura
import pack.job2day.R

class activity_candidatos_gestioncandidaturas : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: CandidaturasAdapter
    val candidaturasList = ArrayList<Candidatura>()
    private lateinit var candidaturasAdapter: CandidaturasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidatos_gestioncandidaturas)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth

        // Inicializa el adaptador y RecyclerView

        candidaturasAdapter = CandidaturasAdapter(candidaturasList)
        val recyclerViewCandidaturas = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerViewCandidaturas.adapter = candidaturasAdapter
        recyclerViewCandidaturas.layoutManager = LinearLayoutManager(this)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Llama a la función para obtener las candidaturas

        if (userId != null) {
            obtenerCandidaturas(userId)
        }

    }

    private fun obtenerCandidaturas(userId: String) {
        val candidatosRef = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app").getReference("Candidatos")

        // Accede al nodo del candidato actual y luego busca las ofertas a las que ha aplicado
        candidatosRef.child(userId).child("OfertasAplicadas").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (appliedOfferSnapshot in snapshot.children) {
                        val candidatura = appliedOfferSnapshot.getValue(Candidatura::class.java)
                        if (candidatura != null) {
                            candidaturasList.add(candidatura)
                        }
                    }
                    // Notifica al adaptador que los datos han cambiado
                    candidaturasAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@activity_candidatos_gestioncandidaturas, "No hay candidaturas.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }



}
