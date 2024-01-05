package pack.job2day.Usuarios.Candidatos

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pack.job2day.R

class activity_candidatos_modificarcv : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var cvRef: DatabaseReference
    private lateinit var btnMod_modificar : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidatos_modificarcv)
        FirebaseApp.initializeApp(this)
        btnMod_modificar = findViewById(R.id.btnMod_modificar)

        val editTextNombreModCv = findViewById<EditText>(R.id.editMod_nombre)
        val editTextApellidosModCv = findViewById<EditText>(R.id.editMod_apellidos)
        val editTextFechaModCv = findViewById<EditText>(R.id.editMod_fecha)
        val editTextEmailModCv = findViewById<EditText>(R.id.editMod_email)
        val editTextTelefonoModCv = findViewById<EditText>(R.id.editMod_telefono)
        val editTextPaisModCv = findViewById<EditText>(R.id.editMod_pais)
        val editTextPoblacionModCv = findViewById<EditText>(R.id.editMod_poblacion)
        val editTextCodigoModCv = findViewById<EditText>(R.id.editMod_codigo)
        val multiTextFormacionModCv = findViewById<EditText>(R.id.multiMod_formacion)
        val multiTextExperienciaModCv = findViewById<EditText>(R.id.multiMod_experiencia)
        val multiTextConocimientosModCv = findViewById<EditText>(R.id.multiMod_conocimientos)
        val multiTextOtrosModCv = findViewById<EditText>(R.id.multiMod_otros)
        val btnMod_modificar = findViewById<Button>(R.id.btnMod_modificar)

        // Obtener la referencia al nodo del CV del usuario actual
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val database =
            FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
        val nodoCandidatos = database.getReference("Candidatos")

        // Obtener los valores actuales del CV y mostrarlos en los campos del formulario
        Toast.makeText(this, "Modifica los campos que desees cambiar.", Toast.LENGTH_LONG).show()
        userId?.let {
            val nodoUsuario = nodoCandidatos.child(userId)
            val cvRef = nodoUsuario.child("cv")

            cvRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val cv = snapshot.getValue(activity_candidatos_crearcv.CV::class.java)
                    cv?.let {
                        editTextNombreModCv.setText(cv.nombre)
                        editTextApellidosModCv.setText(cv.apellidos)
                        editTextFechaModCv.setText(cv.fechaNacimiento)
                        editTextEmailModCv.setText(cv.email)
                        editTextTelefonoModCv.setText(cv.telefono)
                        editTextPaisModCv.setText(cv.pais)
                        editTextPoblacionModCv.setText(cv.poblacion)
                        editTextCodigoModCv.setText(cv.codigoPostal)
                        multiTextFormacionModCv.setText(cv.formacion)
                        multiTextExperienciaModCv.setText(cv.experiencia)
                        multiTextConocimientosModCv.setText(cv.conocimientos)
                        multiTextOtrosModCv.setText(cv.otros)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

            btnMod_modificar.setOnClickListener {

                // Obtener los valores de los campos del formulario
                // Hashmap para modificar los los campos rellenados
                val camposModificados = mutableMapOf<String, Any>()
                val nombre = editTextNombreModCv.text.toString()
                if (nombre.isNotEmpty()) {
                    camposModificados["nombre"] = nombre
                }
                val apellidos = editTextApellidosModCv.text.toString()
                if (apellidos.isNotEmpty()) {
                    camposModificados["apellidos"] = apellidos
                }
                val fechaNacimiento = editTextFechaModCv.text.toString()
                if (fechaNacimiento.isNotEmpty()) {
                    camposModificados["fechaNacimiento"] = fechaNacimiento
                }
                val email = editTextEmailModCv.text.toString()
                if (email.isNotEmpty()) {
                    camposModificados["email"] = email
                }
                val telefono = editTextTelefonoModCv.text.toString()
                if (telefono.isNotEmpty()) {
                    camposModificados["telefono"] = telefono
                }
                val pais = editTextPaisModCv.text.toString()
                if (pais.isNotEmpty()) {
                    camposModificados["pais"] = pais
                }

                val poblacion = editTextPoblacionModCv.text.toString()
                if (poblacion.isNotEmpty()) {
                    camposModificados["poblacion"] = poblacion
                }
                val codigoPostal = editTextCodigoModCv.text.toString()
                if (codigoPostal.isNotEmpty()) {
                    camposModificados["codigoPostal"] = codigoPostal
                }
                val formacion = multiTextFormacionModCv.text.toString()
                if (formacion.isNotEmpty()) {
                    camposModificados["formacion"] = formacion
                }
                val experiencia = multiTextExperienciaModCv.text.toString()
                if (experiencia.isNotEmpty()) {
                    camposModificados["experiencia"] = experiencia
                }
                val conocimientos = multiTextConocimientosModCv.text.toString()
                if (conocimientos.isNotEmpty()) {
                    camposModificados["conocimientos"] = conocimientos
                }
                val otros = multiTextOtrosModCv.text.toString()
                if (otros.isNotEmpty()) {
                    camposModificados["otros"] = otros
                }

                val cv = activity_candidatos_crearcv.CV(
                    nombre, apellidos, fechaNacimiento, email, telefono, pais, poblacion,
                    codigoPostal, formacion, experiencia, conocimientos, otros
                )

                // Actualizar el objeto CV en Firebase
                cvRef.setValue(cv)

                // Mostrar un mensaje indicando que el CV se ha actualizado correctamente
                Toast.makeText(this, "CV actualizado con éxito", Toast.LENGTH_SHORT).show()
            }
        }
    }
}