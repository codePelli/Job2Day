package pack.job2day.Usuarios.Empresas

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import pack.job2day.MainActivity
import pack.job2day.R

class activity_empresa_gestionarperfil : AppCompatActivity() {

    private lateinit var etEmailEmpresaPerfil: EditText
    private lateinit var etPasswordEmpresaPerfil: EditText
    private lateinit var etCifEmpresaPerfil: EditText
    private lateinit var etNombreEmpresaPerfil: EditText
    private lateinit var etPaisEmpresaPerfil: EditText
    private lateinit var etPoblacionEmpresaPerfil: EditText
    private lateinit var etCodigoEmpresaPerfil: EditText
    private lateinit var editTextTelefono: EditText
    private lateinit var editTextDireccion: EditText
    private lateinit var btn_confirmarCambios: Button
    private lateinit var btnEliminarPerfil: Button

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_gestionarperfil)

        etEmailEmpresaPerfil = findViewById(R.id.etEmailEmpresaPerfil)
        etPasswordEmpresaPerfil = findViewById(R.id.etPasswordEmpresaPerfil)
        etCifEmpresaPerfil = findViewById(R.id.etCifEmpresaPerfil)
        etNombreEmpresaPerfil = findViewById(R.id.etNombreEmpresaPerfil)
        etPaisEmpresaPerfil = findViewById(R.id.etPaisEmpresaPerfil)
        etPoblacionEmpresaPerfil = findViewById(R.id.etPoblacionEmpresaPerfil)
        etCodigoEmpresaPerfil = findViewById(R.id.editTextEmpresaCodigoPerfil)
        editTextTelefono = findViewById(R.id.editTextEmpresaTelefono)
        editTextDireccion = findViewById(R.id.editTextEmpresaDireccion)
        btn_confirmarCambios = findViewById(R.id.btn_confirmarCambios)
        btnEliminarPerfil = findViewById(R.id.btnEliminarPerfil)

        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        Log.d("Empresas", "User ID: $userId")
        database =
            FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
                .getReference("Empresas").child(userId!!)

        cargarDatosEmpresa()

        btn_confirmarCambios.setOnClickListener {
            actualizarDatos()
        }

        btnEliminarPerfil.setOnClickListener {
            eliminarPerfil()
        }
    }

    private fun actualizarDatos() {
        val email = etEmailEmpresaPerfil.text.toString()
        val password = etPasswordEmpresaPerfil.text.toString()
        val cif = etCifEmpresaPerfil.text.toString()
        val nombre = etNombreEmpresaPerfil.text.toString()
        val pais = etPaisEmpresaPerfil.text.toString()
        val poblacion = etPoblacionEmpresaPerfil.text.toString()
        val codigoPostal = etCodigoEmpresaPerfil.text.toString()
        val telefono = editTextTelefono.text.toString()
        val direccion = editTextDireccion.text.toString()

        if (!camposVacios(
                email,
                password,
                cif,
                nombre,
                pais,
                poblacion,
                codigoPostal,
                telefono,
                direccion
            )
        ) {
            Toast.makeText(
                this, "No puedes dejar campos en blanco", Toast.LENGTH_SHORT
            ).show()
        }

        val empresaMap = mapOf(
            "email" to email,
            "password" to password,
            "cif" to cif,
            "nombre" to nombre,
            "pais" to pais,
            "poblacion" to poblacion,
            "codigoPostal" to codigoPostal,
            "telefono" to telefono,
            "direccion" to direccion
        )

        for ((key, value) in empresaMap) {
            database.child(key).setValue(value)
        }

        Toast.makeText(this, "Datos actualizados con éxito", Toast.LENGTH_SHORT).show()
        actualizarEmailAuth(email)
        actualizarContrasenaAuth(password)

        }


    private fun cargarDatosEmpresa() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                etEmailEmpresaPerfil.setText(dataSnapshot.child("email").value as? String ?: "")
                etPasswordEmpresaPerfil.setText(
                    dataSnapshot.child("password").value as? String ?: ""
                )
                etNombreEmpresaPerfil.setText(dataSnapshot.child("nombre").value as? String ?: "")
                etCifEmpresaPerfil.setText(dataSnapshot.child("cif").value as? String ?: "")
                etPaisEmpresaPerfil.setText(dataSnapshot.child("pais").value as? String ?: "")
                etPoblacionEmpresaPerfil.setText(
                    dataSnapshot.child("poblacion").value as? String ?: ""
                )
                etCodigoEmpresaPerfil.setText(
                    dataSnapshot.child("codigoPostal").value as? String ?: ""
                )
                editTextTelefono.setText(dataSnapshot.child("telefono").value as? String ?: "")
                editTextDireccion.setText(dataSnapshot.child("direccion").value as? String ?: "")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(
                    this@activity_empresa_gestionarperfil,
                    "Error al cargar tus datos. ${databaseError.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun camposVacios(
        email: String,
        password: String,
        cif: String,
        nombre: String,
        pais: String,
        poblacion: String,
        codigoPostal: String,
        telefono: String,
        direccion: String,

    ): Boolean {
        return email.isNotBlank() &&
                password.isNotBlank() &&
                cif.isNotBlank() &&
                nombre.isNotBlank() &&
                pais.isNotBlank() &&
                poblacion.isNotBlank() &&
                codigoPostal.isNotBlank() &&
                telefono.isNotBlank() &&
                direccion.isNotBlank()

    }

    private fun actualizarEmailAuth(email: String) {
        auth.currentUser?.updateEmail(email)?.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(
                    this,
                    "Error al actualizar el correo electrónico", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun actualizarContrasenaAuth(password: String) {
        auth.currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(
                    this,
                    "Error al actualizar la contraseña", Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun eliminarPerfil() {
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        val database = FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
        val nodoCandidatos = database.getReference("Empresas")

        // Muestra un cuadro de diálogo para confirmar la eliminación

        AlertDialog.Builder(this)
            .setTitle("Eliminar perfil")
            .setMessage("¿Seguro que quieres eliminar el perfil?")
            .setPositiveButton("SI") { _, _ ->

                // Eliminar el nodo del usuario en la base de datos

                if (userId != null) {
                    nodoCandidatos.child(userId).removeValue().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Perfil eliminado", Toast.LENGTH_SHORT).show()

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


