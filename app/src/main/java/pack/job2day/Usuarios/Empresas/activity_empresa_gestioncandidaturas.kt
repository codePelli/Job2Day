package pack.job2day.Usuarios.Empresas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pack.job2day.AdaptadoresCardview.Empresas.ListaCandidatosAdapter
import pack.job2day.Clases.Empresas.Candidato
import pack.job2day.Clases.Candidatos.Candidatura
import pack.job2day.R

class activity_empresa_gestioncandidaturas : AppCompatActivity() {

    private var listaOfertas: ArrayList<activity_empresa_crearoferta.Oferta> = ArrayList()

    private val database = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")

    private val nodoEmpresas = database.getReference("Empresas")

    private val nodoCandidatos = database.getReference("Candidatos")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_gestioncandidaturas)

        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Agregar un listener para leer las ofertas de la empresa actual
        nodoEmpresas.orderByKey().equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val candidaturasSnapshot = snapshot.child(userId!!).child("Ofertas").children.first().child("Candidaturas")
                    val candidatos: ArrayList<Candidato> = ArrayList()

                    fun cargarCandidatos() {
                        // Crear el adaptador con la lista de candidatos
                        val adapter = ListaCandidatosAdapter(candidatos)

                        // Configurar el RecyclerView con el adaptador
                        val recyclerView = findViewById<RecyclerView>(R.id.rvCandidaturas)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this@activity_empresa_gestioncandidaturas)
                    }

                    var candidatosCargados = 0

                    for (candidaturaSnapshot in candidaturasSnapshot.children) {
                        val candidatoId = candidaturaSnapshot.key
                        nodoCandidatos.child(candidatoId!!).addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(candidatoSnapshot: DataSnapshot) {
                                // Obtener el nombre del puesto de trabajo
                                val nombrePuesto = candidatoSnapshot.child("OfertasAplicadas").children.first().child("nombrePuesto").getValue(String::class.java)

                                val cvSnapshot = candidatoSnapshot.child("cv")
                                val candidato = Candidato(
                                    nombrePuesto = nombrePuesto,
                                    apellidos = cvSnapshot.child("apellidos").getValue(String::class.java),
                                    codigoPostal = cvSnapshot.child("codigoPostal").getValue(String::class.java),
                                    conocimientos = cvSnapshot.child("conocimientos").getValue(String::class.java),
                                    email = cvSnapshot.child("email").getValue(String::class.java),
                                    experiencia = cvSnapshot.child("experiencia").getValue(String::class.java),
                                    fechaNacimiento = cvSnapshot.child("fechaNacimiento").getValue(String::class.java),
                                    formacion = cvSnapshot.child("formacion").getValue(String::class.java),
                                    nombre = cvSnapshot.child("nombre").getValue(String::class.java),
                                    otros = cvSnapshot.child("otros").getValue(String::class.java),
                                    pais = cvSnapshot.child("pais").getValue(String::class.java),
                                    poblacion = cvSnapshot.child("poblacion").getValue(String::class.java),
                                    telefono = cvSnapshot.child("telefono").getValue(String::class.java)
                                )
                                if (candidato != null) {
                                    candidatos.add(candidato)
                                }

                                // Incrementa el contador
                                candidatosCargados++
                                if (candidatosCargados == candidaturasSnapshot.childrenCount.toInt()) {
                                    cargarCandidatos()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
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
