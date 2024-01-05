
package pack.job2day.RegistroUsuarios.Candidatos

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import pack.job2day.MainActivity
import pack.job2day.R
import java.util.regex.Pattern

class activity_reg_candidato : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_candidato)
        FirebaseApp.initializeApp(this)
        auth = Firebase.auth


        // Recopila la información del usuario
        val user = Firebase.auth.currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val name = it.displayName
            val email = it.email
            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            val uid = it.uid
        }

        Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()

        // Obtiene las referencias a los objetos de la interfaz de usuario
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val editTextApellido = findViewById<EditText>(R.id.editTextApellido)
        val editTextTelefono = findViewById<EditText>(R.id.editTextTelefono)
        val editTextPoblacion = findViewById<EditText>(R.id.editTextPoblacion)
        val editTextPais = findViewById<EditText>(R.id.editTextPais)

        val buttonGuardar = findViewById<Button>(R.id.buttonRegistro)


        class Candidato {
            var email: String = ""
            var password: String = ""
            var nombre: String = ""
            var apellido: String = ""
            var telefono: String = ""
            var poblacion: String = ""
            var pais: String = ""

            constructor(
                email: String,
                password: String,
                nombre: String,
                apellido: String,
                telefono: String,
                poblacion: String,
                pais: String,
            ) {
                this.email = email
                this.password = password
                this.nombre = nombre
                this.apellido = apellido
                this.telefono = telefono
                this.poblacion = poblacion
                this.pais = pais
            }

        }

        // Acciones que realiza el botón REGISTRAR
        buttonGuardar.setOnClickListener {

            // Crea un objeto Candidato con los datos del formulario
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val nombre = editTextNombre.text.toString()
            val apellido = editTextApellido.text.toString()
            val telefono = editTextTelefono.text.toString()
            val poblacion = editTextPoblacion.text.toString()
            val pais = editTextPais.text.toString()


            // Validar los campos rellenados
            if (!camposRellenados(email, password, nombre, apellido, telefono,poblacion, pais)) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!validacionPassword(password)) {
                editTextPassword.error =
                    "La contraseña debe tener al menos 8 caracteres y contener, al menos, una letra y un número"
                return@setOnClickListener
            }

            if (!validacionEmail(email)) {
                Toast.makeText(this, "Email incorrecto o ya en uso", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!validacionTelefono(telefono)) {
                Toast.makeText(
                    this,
                    "El número de teléfono debe contener sólo 9 números",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val candidato = Candidato(email, password, nombre, apellido,telefono,poblacion, pais)

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val uid = userId ?: ""
            val database = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
            val ref = database.getReference("Candidatos")
            ref.child(uid).setValue(candidato)
            Toast.makeText(this, "Se ha registrado correctamente", Toast.LENGTH_SHORT).show()

            // Vuelve al Main_activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    // Funciones de comprobación de los datos insertados
    private fun validacionTelefono(telefono: String): Boolean {
        return !TextUtils.isEmpty(telefono) && TextUtils.isDigitsOnly(telefono) && telefono.length == 9
    }

    private fun camposRellenados(
        email: String,
        password: String,
        nombre: String,
        apellido: String,
        telefono: String,
        poblacion: String,
        pais: String,

    ): Boolean {
            return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
            nombre
        ) &&
                !TextUtils.isEmpty(apellido) && !TextUtils.isEmpty(telefono) && !TextUtils.isEmpty(
            pais
        ) &&
                !TextUtils.isEmpty(poblacion)
    }

    private fun validacionEmail(email: String): Boolean {
        val emailPattern = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
        val matcher = emailPattern.matcher(email)
        return matcher.matches()
    }

    private fun validacionPassword(password: String): Boolean {
        val passwordPattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{8,}$")
        val matcher = passwordPattern.matcher(password)
        return matcher.matches()
    }

}



