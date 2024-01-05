package pack.job2day.Usuarios.Empresas

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pack.job2day.R
import java.util.*
import kotlin.random.Random

class activity_empresa_crearoferta : AppCompatActivity() {

    lateinit var btnGuardarOferta: Button

    lateinit var auth: FirebaseAuth

    class Oferta {
        var nombrePuesto: String = ""
        var idOferta: Int = 0
        var fechaOferta: String = ""
        var descripcionOferta: String = ""
        var experienciaOferta: String = ""
        var formacionOferta: String = ""

        constructor()

        constructor(
            nombreOferta: String, idOferta: Int, fechaOferta: String, descripcionOferta: String,
            experienciaOferta: String, formacionOferta: String
        ) {
            this.nombrePuesto = nombreOferta
            this.idOferta = idOferta
            this.fechaOferta = fechaOferta
            this.descripcionOferta = descripcionOferta
            this.experienciaOferta = experienciaOferta
            this.formacionOferta = formacionOferta
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_crearoferta)

        FirebaseApp.initializeApp(this)

        val etNombrePuesto = findViewById<EditText>(R.id.etNombrePuesto)
        val etFechaPuesto = findViewById<EditText>(R.id.etFechaPuesto)
        val multiDescripcionPuesto = findViewById<EditText>(R.id.multiDescripcionPuesto)
        val multiExperienciaPuesto = findViewById<EditText>(R.id.tvExpPuesto)
        val multiFormacionPuesto = findViewById<EditText>(R.id.tvFormacionPuesto)

        // FECHA

        val calendario = Calendar.getInstance()
        val año = calendario.get(Calendar.YEAR)
        val mes = calendario.get(Calendar.MONTH) + 1
        val dia = calendario.get(Calendar.DAY_OF_MONTH)
        val formatoFecha = String.format("%02d/%02d/%04d", dia, mes, año)

        val fechaOferta = findViewById<EditText>(R.id.etFechaPuesto)

        fechaOferta.setText(formatoFecha)

        // BOTON GUARDAR

        btnGuardarOferta = findViewById(R.id.btnGuardarOferta)

        btnGuardarOferta.setOnClickListener {

            val nombrePuesto = etNombrePuesto.text.toString()
            val idOferta = Random.nextInt(1, Int.MAX_VALUE)
            val fechaOferta = etFechaPuesto.text.toString()
            val descripcionOferta = multiDescripcionPuesto.text.toString()
            val experienciaOferta = multiExperienciaPuesto.text.toString()
            val formacionOferta = multiFormacionPuesto.text.toString()

            val oferta = Oferta(
                nombrePuesto, idOferta, fechaOferta, descripcionOferta, experienciaOferta, formacionOferta
            )
            val userEmail = FirebaseAuth.getInstance().currentUser?.email
            if (userEmail != null) {
                val database = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
                val nodoEmpresas = database.getReference("Empresas")

                nodoEmpresas.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (usuarioSnapshot in snapshot.children) {
                                val nodoUsuario = usuarioSnapshot.ref

                                // Guarda las ofertas de trabajo en un nodo hijo de Ofertas

                                val nodoOferta = nodoUsuario.child("Ofertas").child(idOferta.toString())
                                nodoOferta.setValue(oferta)
                                Toast.makeText(this@activity_empresa_crearoferta, "Se ha guardado la oferta.", Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@activity_empresa_crearoferta, activity_empresa_gestionofertas::class.java)
                                startActivity(intent)
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
        }
    }
}
