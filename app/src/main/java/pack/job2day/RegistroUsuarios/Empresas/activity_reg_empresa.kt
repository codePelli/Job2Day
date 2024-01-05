package pack.job2day.RegistroUsuarios.Empresas

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
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


class activity_reg_empresa : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg_empresa)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()


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

        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val editTextNombre = findViewById<EditText>(R.id.editTextNombre)
        val editTextCif = findViewById<EditText>(R.id.editTextCif)
        val editTextPais = findViewById<EditText>(R.id.editTextPais)
        val editTextPoblacion = findViewById<EditText>(R.id.editTextPoblacion)
        val editTextCodigoPostal = findViewById<EditText>(R.id.editTextCodigo)
        val editTextTelefono = findViewById<EditText>(R.id.editTextTelefono)
        val editTextDireccion = findViewById<EditText>(R.id.editTextDireccion)

        // Obtiene una referencia al botón y agrega un escuchador de eventos

        val buttonGuardar = findViewById<Button>(R.id.btn_confirmarCambios)

        //Usuario empresa para Firebase

        class Empresa {
            var email: String = ""
            var password: String = ""
            var nombre: String = ""
            var cif: String = ""
            var pais: String = ""
            var poblacion: String = ""
            var codigoPostal: String = ""
            var telefono: String = ""
            var direccion: String = ""

            constructor()

            constructor(
                email: String, password: String, nombre: String, cif: String, pais: String,
                poblacion: String, codigoPostal: String, telefono: String, direccion: String
            ) {
                this.email = email
                this.password = password
                this.nombre = nombre
                this.cif = cif
                this.pais = pais
                this.poblacion = poblacion
                this.codigoPostal = codigoPostal
                this.telefono = telefono
                this.direccion = direccion
            }

        }

        buttonGuardar.setOnClickListener {

            // Crea un objeto Empresa con los datos del formulario

            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            val nombre = editTextNombre.text.toString()
            val cif = editTextCif.text.toString()
            val pais = editTextPais.text.toString()
            val poblacion = editTextPoblacion.text.toString()
            val codigoPostal = editTextCodigoPostal.text.toString()
            val telefono = editTextTelefono.text.toString()
            val direccion = editTextDireccion.text.toString()

            // Comprueba que todos los campos obligatorios están rellenados

            if (!camposRellenados(
                    email, password, nombre, cif,
                    pais, poblacion, codigoPostal, telefono, direccion
                )
            ) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            if (!validacionCodigoPostal(codigoPostal)) {
                editTextCodigoPostal.error = "El código postal debe tener 5 dígitos"
                return@setOnClickListener
            }

            if (!validacionEmail(email)) {
                Toast.makeText(this, "Introduce un email válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!validacionPassword(password)) {
                editTextPassword.error =
                    "La contraseña debe tener al menos 8 caracteres y contener, al menos, una letra, un número y un carácter especial"
                return@setOnClickListener
            }

            val empresa = Empresa(
                email,
                password,
                nombre,
                cif,
                pais,
                poblacion,
                codigoPostal,
                telefono,
                direccion
            )

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            val uid = userId ?: ""
            val database =
                FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
            val ref = database.getReference("Empresas")
            ref.child(uid).setValue(empresa)
            // Muestra un mensaje al usuario indicando que los datos se han guardado

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

    private fun validacionCif(cif: String): Boolean {

        // El CIF debe tener 9 caracteres (7 números y 1 letra)

        if (cif.length != 9) {
            return false
        }

        // Los primeros 7 caracteres deben ser números

        val numeros = cif.substring(0, 7)
        if (!TextUtils.isDigitsOnly(numeros)) {
            return false
        }

        // El último caracter debe ser una letra

        val letra = cif.substring(8, 9)
        if (!letra.matches(Regex("[A-Za-z]"))) {
            return false
        }

        return true
    }

    private fun camposRellenados(
        email: String, password: String, nombre: String, cif: String,
        pais: String, poblacion: String, codigoPostal: String, telefono: String, direccion: String
    ): Boolean {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
            nombre
        ) &&
                !TextUtils.isEmpty(cif) && !TextUtils.isEmpty(pais) && !TextUtils.isEmpty(poblacion) &&
                !TextUtils.isEmpty(codigoPostal) && !TextUtils.isEmpty(telefono) && !TextUtils.isEmpty(
            direccion
        )
    }

    private fun validacionEmail(email: String): Boolean {
        val emailPattern = Pattern.compile("^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}\$")
        val matcher = emailPattern.matcher(email)
        return matcher.matches()
    }

    private fun validacionPassword(password: String): Boolean {
        val passwordPattern =
            Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}\$")
        val matcher = passwordPattern.matcher(password)
        return matcher.matches()
    }

    private fun validacionCodigoPostal(codigoPostal: String): Boolean {
        return codigoPostal.matches("[0-9]{5}".toRegex())
    }

}
