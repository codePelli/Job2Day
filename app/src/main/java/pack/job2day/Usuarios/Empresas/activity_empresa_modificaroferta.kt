package pack.job2day.Usuarios.Empresas

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pack.job2day.AdaptadoresCardview.Empresas.OfertaAdapter
import pack.job2day.R

class activity_empresa_modificaroferta : AppCompatActivity() {

    private lateinit var rvOfertas: RecyclerView
    private lateinit var ofertaAdapter: OfertaAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_listado_ofertas)

        rvOfertas = findViewById(R.id.rvOfertas)
        rvOfertas.layoutManager = LinearLayoutManager(this)

        ofertaAdapter = OfertaAdapter(mutableListOf()) { oferta ->
            eliminarOferta(oferta)
        }
        rvOfertas.adapter = ofertaAdapter


        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail != null) {
            val database =
                FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
            val nodoEmpresas = database.getReference("Empresas")

            nodoEmpresas.orderByChild("email").equalTo(userEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (usuarioSnapshot in snapshot.children) {
                                val nodoUsuario = usuarioSnapshot.ref
                                val nodoOfertas = nodoUsuario.child("Ofertas")

                                nodoOfertas.addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val ofertas =
                                            mutableListOf<activity_empresa_crearoferta.Oferta>()
                                        for (ofertaSnapshot in snapshot.children) {
                                            val oferta =
                                                ofertaSnapshot.getValue(activity_empresa_crearoferta.Oferta::class.java)
                                            if (oferta != null) {
                                                ofertas.add(oferta)
                                            }
                                        }
                                        ofertaAdapter.ofertas = ofertas
                                        ofertaAdapter.notifyDataSetChanged()
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    private fun eliminarOferta(oferta: activity_empresa_crearoferta.Oferta) {
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        if (userEmail != null) {
            val database =
                FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
            val nodoEmpresas = database.getReference("Empresas")

            nodoEmpresas.orderByChild("email").equalTo(userEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (usuarioSnapshot in snapshot.children) {
                                val nodoUsuario = usuarioSnapshot.ref
                                val nodoOfertas = nodoUsuario.child("Ofertas")

                                nodoOfertas.child(oferta.idOferta.toString()).removeValue()
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
}
