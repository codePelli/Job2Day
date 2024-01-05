package pack.job2day.AdaptadoresCardview.Candidatos

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pack.job2day.R
import pack.job2day.Usuarios.Empresas.activity_empresa_crearoferta

class activity_card_lista : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    private lateinit var dbref: DatabaseReference
    private lateinit var ofertasAdapter: AplicarOfertaAdapter
    private lateinit var ofertasRecyclerView: RecyclerView
    private lateinit var ofertasArrayList: ArrayList<activity_empresa_crearoferta.Oferta>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidatos_busquedaempleo)

        ofertasArrayList = arrayListOf<activity_empresa_crearoferta.Oferta>()
        ofertasAdapter = AplicarOfertaAdapter(ofertasArrayList)

        ofertasRecyclerView = findViewById(R.id.ofertasList)
        ofertasRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@activity_card_lista)
            adapter = ofertasAdapter
        }

        getUserData()
    }

    private fun getUserData() {
        val dbref = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Empresas")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (userSnapshot in snapshot.children) {
                        val ofertasSnapshot = userSnapshot.child("Ofertas")
                        for (ofertaSnapshot in ofertasSnapshot.children) {
                            val oferta = ofertaSnapshot.getValue(activity_empresa_crearoferta.Oferta::class.java)
                            if (oferta != null) {
                                ofertasArrayList.add(oferta)
                            }
                        }
                    }

                    runOnUiThread {
                        ofertasAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}
