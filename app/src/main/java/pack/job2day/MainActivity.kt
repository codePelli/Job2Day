package pack.job2day

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import pack.job2day.RegistroUsuarios.activity_alta_usuarios
import pack.job2day.RegistroUsuarios.activity_decide_tipo
import pack.job2day.Usuarios.Candidatos.activity_menu_candidatos
import pack.job2day.Usuarios.Empresas.activity_menu_empresa

class MainActivity : AppCompatActivity() {

    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var btnEntrarMenu: Button
    lateinit var btnAltaUsuario: Button

    lateinit var auth: FirebaseAuth


    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseApp.initializeApp(this)

        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        auth = Firebase.auth
        val database = FirebaseDatabase.getInstance()

        // Obtiene las referencias a los objetos de la interfaz de usuario
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        btnEntrarMenu = findViewById(R.id.btn_entrar)
        btnAltaUsuario = findViewById(R.id.btn_altaUsuario)

        btnAltaUsuario.setOnClickListener {
            val intent = Intent(
                this@MainActivity,
                activity_alta_usuarios::class.java
            )
            startActivity(intent)
        }

        btnEntrarMenu.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            if (email.isEmpty() && password.isEmpty()) { Toast.makeText(this,
                    "DEBES INTRODUCIR TUS DATOS", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (email.isEmpty()) { Toast.makeText(this,
                    "INTRODUCE EL EMAIL", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (password.isEmpty()) { Toast.makeText(this,
                    "INTRODUCE LA CONTRASEÑA", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

                // LOGIN POR PRIMERA VEZ
                Firebase.auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                                val empresasRef = FirebaseDatabase.getInstance(
                                    "https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app"
                                ).getReference("Empresas")
                                empresasRef.orderByChild("email").equalTo(email)
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            if (snapshot.exists()) {
                                                // Inicia sesión como empresa
                                                auth.signInWithEmailAndPassword(email, password)
                                                    .addOnCompleteListener(this@MainActivity) { task ->
                                                        if (task.isSuccessful) {
                                                            // Inicio de sesión exitoso, redirige al usuario a la actividad de empresas
                                                            val intent = Intent(
                                                                this@MainActivity,
                                                                activity_menu_empresa::class.java
                                                            )
                                                            startActivity(intent)
                                                            finish()
                                                        } else {
                                                            // Si el inicio de sesión falla, muestra un mensaje al usuario
                                                            Toast.makeText(
                                                                this@MainActivity,
                                                                "Error al iniciar sesión: ${task.exception?.localizedMessage}",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }

                                            } else {
                                                // Busca si el usuario es un candidato
                                                val candidatosRef =
                                                    FirebaseDatabase.getInstance(
                                                        "https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app"
                                                    ).getReference("Candidatos")
                                                candidatosRef.orderByChild("email").equalTo(email)
                                                    .addListenerForSingleValueEvent(object :
                                                        ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            if (snapshot.exists()) {
                                                                // Inicia sesión como candidato
                                                                auth.signInWithEmailAndPassword(
                                                                    email,
                                                                    password
                                                                )
                                                                    .addOnCompleteListener(this@MainActivity) { task ->
                                                                        if (task.isSuccessful) {
                                                                            // Inicio de sesión exitoso, redirige al usuario a la actividad de candidatos
                                                                            val intent = Intent(
                                                                                this@MainActivity,
                                                                                activity_menu_candidatos::class.java
                                                                            )
                                                                            startActivity(intent)
                                                                            finish()
                                                                        }
                                                                    }
                                                            }
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            TODO("Not yet implemented")
                                                        }
                                                    })
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }
                                    })
                            }
                        }
                    }
            }
        }



