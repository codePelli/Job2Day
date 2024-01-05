package pack.job2day.Usuarios.Candidatos

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pack.job2day.MainActivity
import pack.job2day.R

class activity_candidatos_gestionarperfil : AppCompatActivity() {

    private lateinit var btnEliminarPerfil : Button
    private lateinit var btn_ConfirmarCambios: Button

    private lateinit var etNombre : EditText
    private lateinit var etApellido : EditText
    private lateinit var etEmail : EditText
    private lateinit var etTelefono : EditText
    private lateinit var etPais : EditText
    private lateinit var etPoblacion : EditText
    private lateinit var etContrasena : EditText

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidatos_modificarperfil)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        database =
            FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Candidatos").child(userId!!)

        etContrasena = findViewById<EditText>(R.id.editTextPasswordMod)
        etNombre = findViewById<EditText>(R.id.editTextNombreMod)
        etApellido = findViewById<EditText>(R.id.editTextApellido)
        etEmail = findViewById<EditText>(R.id.editTextEmailMod)
        etTelefono = findViewById<EditText>(R.id.editTextTelefonoMod)
        etPais = findViewById<EditText>(R.id.editTextPaisMod)
        etPoblacion = findViewById<EditText>(R.id.editTextPoblacionMod)
        btn_ConfirmarCambios = findViewById(R.id.btn_confirmarCambios)
        btnEliminarPerfil = findViewById(R.id.btnEliminarPerfilCandidato)

        cargarDatosUsuario()

        btn_ConfirmarCambios.setOnClickListener {
            actualizarDatos()
        }

        btnEliminarPerfil.setOnClickListener{
            eliminarPerfil()
        }
    }

    private fun actualizarDatos() {
        val email = etEmail.text.toString()
        val contrasena = etContrasena.text.toString()
        val nombre = etNombre.text.toString()
        val apellido = etApellido.text.toString()
        val telefono = etTelefono.text.toString()
        val pais = etPais.text.toString()
        val poblacion = etPoblacion.text.toString()

        // Comprobar campos
        if (!comprobarCampos(email, contrasena, nombre, apellido, telefono, pais, poblacion)){
            Toast.makeText(this, "No puedes dejar campos en blanco", Toast.LENGTH_SHORT).show()
        }


        // Actualizar datos del candidato en la base de datos de Firebase
        val candidatoMap = mapOf(
            "email" to email,
            "contrasena" to contrasena,
            "nombre" to nombre,
            "apellido" to apellido,
            "telefono" to telefono,
            "pais" to pais,
            "poblacion" to poblacion
        )
        database.updateChildren(candidatoMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Datos actualizados con éxito", Toast.LENGTH_SHORT).show()
                actualizarEmailAuth(email)
                actualizarContraseñaAuth(contrasena)
            } else {
                Toast.makeText(this, "Error al actualizar los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

        // Uso casting seguro as? y String? porque me daba error al ejecutar el activity

    private fun cargarDatosUsuario() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                etEmail.setText(dataSnapshot.child("email").value as? String?: "")
                etContrasena.setText(dataSnapshot.child("contrasena").value as? String?: "")
                etNombre.setText(dataSnapshot.child("nombre").value as? String?: "")
                etApellido.setText(dataSnapshot.child("apellido").value as? String?: "")
                etTelefono.setText(dataSnapshot.child("telefono").value as? String?: "")
                etPais.setText(dataSnapshot.child("pais").value as? String?: "")
                etPoblacion.setText(dataSnapshot.child("poblacion").value as? String?: "")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@activity_candidatos_gestionarperfil,
                    "Error al cargar tus datos. ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
    private fun comprobarCampos(
        email: String,
        contrasena: String,
        nombre: String,
        apellido: String,
        telefono: String,
        pais: String,
        poblacion: String
    ): Boolean {
        return email.isNotBlank() &&
                contrasena.isNotBlank() &&
                nombre.isNotBlank() &&
                apellido.isNotBlank() &&
                telefono.isNotBlank() &&
                pais.isNotBlank() &&
                poblacion.isNotBlank()
    }
    private fun actualizarEmailAuth(newEmail: String) {
        val user = auth.currentUser
        user?.updateEmail(newEmail)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Usuario de login cambiado.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun actualizarContraseñaAuth(newPassword: String) {
        val user = auth.currentUser
        user?.updatePassword(newPassword)?.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Contraseña actualizada", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun eliminarPerfil() {
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
        val nodoCandidatos = database.getReference("Candidatos")

        // Muestra un cuadro de diálogo para confirmar la eliminación

        AlertDialog.Builder(this)
            .setTitle("Eliminar perfil")
            .setMessage("¿Estás seguro de que deseas eliminar tu perfil?")
            .setPositiveButton("SI") { _, _ ->

                // Eliminar el nodo del usuario en la base de datos

                if (userId != null) {
                    nodoCandidatos.child(userId).removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Perfil eliminado de la base de datos", Toast.LENGTH_SHORT).show()

                            // Eliminar la autenticación del usuario

                            auth.currentUser?.delete()?.addOnCompleteListener { authTask ->
                                if (authTask.isSuccessful) {
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
            .setNegativeButton("NO", null)
            .show()
    }

}