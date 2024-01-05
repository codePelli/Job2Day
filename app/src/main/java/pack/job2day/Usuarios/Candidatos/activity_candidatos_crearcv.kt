package pack.job2day.Usuarios.Candidatos

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

class activity_candidatos_crearcv : AppCompatActivity() {


    // Recopila la información del usuario, se pone a parte para usarla en otro activity

    class CV {
        var nombre: String = ""
        var apellidos: String = ""
        var fechaNacimiento: String = ""
        var email: String = ""
        var telefono: String = ""
        var pais: String = ""
        var poblacion: String = ""
        var codigoPostal: String = ""
        var formacion: String = ""
        var experiencia: String = ""
        var conocimientos: String = ""
        var otros: String = ""

        constructor()

        constructor(
            nombre: String, apellidos: String, fechaNacimiento: String, email: String,
            telefono: String, pais: String, poblacion: String,
            codigoPostal: String, formacion: String, experiencia: String, conocimientos:
            String, otros: String
        ) {
            this.nombre = nombre
            this.apellidos = apellidos
            this.fechaNacimiento = fechaNacimiento
            this.email = email
            this.telefono = telefono
            this.pais = pais
            this.poblacion = poblacion
            this.codigoPostal = codigoPostal
            this.formacion = formacion
            this.experiencia = experiencia
            this.conocimientos = conocimientos
            this.otros = otros
        }
    }

    lateinit var auth: FirebaseAuth

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidatos_crearcv)
        FirebaseApp.initializeApp(this)

        val editTextNombreCreacv = findViewById<EditText>(R.id.editTextNombreCreacv)
        val editTextApellidosCreacv = findViewById<EditText>(R.id.editTextApellidosCreacv)
        val editTextFechaCreacv = findViewById<EditText>(R.id.editTextFechaCreacv)
        val editTextEmailCreacv = findViewById<EditText>(R.id.editTextEmailCreacv)
        val editTextTelefonoCreacv = findViewById<EditText>(R.id.editTextTelefonoCreacv)
        val editTextPaisCreacv = findViewById<EditText>(R.id.editTextPais)
        val editTextPoblacionCreacv = findViewById<EditText>(R.id.editTextEmail)
        val editTextCodigoCrearcv = findViewById<EditText>(R.id.editTextCodigo)
        val multiTextFormacionCreacv = findViewById<EditText>(R.id.multiTextFormacionCreacv)
        val multiTextExperienciaCreacv = findViewById<EditText>(R.id.multiTextExperienciaCreacv)
        val multiTextConocimientosCreacv = findViewById<EditText>(R.id.multiTextConocimientosCreacv)
        val multiTextOtrosCreacv = findViewById<EditText>(R.id.multiTextOtrosCreacv)
        val buttonCrearcv = findViewById<Button>(R.id.buttonCrearcv)

        //Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()

            buttonCrearcv.setOnClickListener {

                // Obtener los valores de los campos del formulario
                val nombre = editTextNombreCreacv.text.toString()
                val apellidos = editTextApellidosCreacv.text.toString()
                val fechaNacimiento = editTextFechaCreacv.text.toString()
                val email = editTextEmailCreacv.text.toString()
                val telefono = editTextTelefonoCreacv.text.toString()
                val pais = editTextPaisCreacv.text.toString()
                val poblacion = editTextPoblacionCreacv.text.toString()
                val codigoPostal = editTextCodigoCrearcv.text.toString()
                val formacion = multiTextFormacionCreacv.text.toString()
                val experiencia = multiTextExperienciaCreacv.text.toString()
                val conocimientos = multiTextConocimientosCreacv.text.toString()
                val otros = multiTextOtrosCreacv.text.toString()

                val cv = CV(
                    nombre, apellidos, fechaNacimiento, email, telefono, pais, poblacion,
                    codigoPostal, formacion, experiencia, conocimientos, otros
                )

                // https://firebase.google.com/docs/auth/android/start
                // Guardar el objeto 'cv' en el nodo 'cv' del usuario actual

                val userEmail = FirebaseAuth.getInstance().currentUser?.email
                if (userEmail != null) {
                    val database = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
                    val nodoCandidatos = database.getReference("Candidatos")

                    nodoCandidatos.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (usuarioSnapshot in snapshot.children) {
                                    val nodoUsuario = usuarioSnapshot.ref
                                    if (usuarioSnapshot.hasChild("cv")) {
                                        Toast.makeText(this@activity_candidatos_crearcv, "Ya has creado un CV anteriormente", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val nodoCv = nodoUsuario.child("cv")
                                        nodoCv.setValue(cv)
                                        Toast.makeText(this@activity_candidatos_crearcv, "CV guardado con éxito", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Toast.makeText(this@activity_candidatos_crearcv, "No se pudo encontrar el nodo del usuario.", Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }

                val intent = Intent(this, activity_menu_candidatos::class.java)
                startActivity(intent)


            }
    }
}


