package pack.job2day.Usuarios.Candidatos
import pack.job2day.AdaptadoresCardview.Candidatos.AplicarOfertaAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.annotation.SuppressLint
import pack.job2day.R
import pack.job2day.Usuarios.Empresas.activity_empresa_crearoferta

class activity_candidatos_busquedaempleo : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidatos_busquedaempleo)

        // Almacenará los candidatos que apliquen

        var candidatos: HashMap<String, String> = HashMap()

        // Agrega la referencia a la base de datos de "Empresas"

        val database =
            FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
        val nodoEmpresas = database.getReference("Empresas")


        // Inicializar RecyclerView y LayoutManager

        val recyclerView = findViewById<RecyclerView>(R.id.ofertasList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Crear lista vacía de ofertas y adaptador

        val ofertasList = ArrayList<activity_empresa_crearoferta.Oferta>()
        val aplicarOfertaAdapter = AplicarOfertaAdapter(ofertasList)

        // Asignar el adaptador al RecyclerView

        recyclerView.adapter = aplicarOfertaAdapter

        // Obtener ofertas de Firebase

        getOfertasFromFirebase { ofertas ->
            ofertasList.clear()
            ofertasList.addAll(ofertas)
            aplicarOfertaAdapter.notifyDataSetChanged()
        }


    }

    private fun getOfertasFromFirebase(callback: (ArrayList<activity_empresa_crearoferta.Oferta>) -> Unit) {
        val database = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
        val databaseReference = database.getReference("Empresas")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ofertas = ArrayList<activity_empresa_crearoferta.Oferta>()
                for (empresaSnapshot in snapshot.children) {
                    val ofertasSnapshot = empresaSnapshot.child("Ofertas")
                    for (ofertaSnapshot in ofertasSnapshot.children) {
                        val oferta = ofertaSnapshot.getValue(activity_empresa_crearoferta.Oferta::class.java)
                        if (oferta != null) {
                            ofertas.add(oferta)
                        }
                    }
                }
                callback(ofertas)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}
