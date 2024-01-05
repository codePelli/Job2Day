package pack.job2day.Usuarios.Candidatos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import pack.job2day.R

class activity_candidatos_menu_gestioncv : AppCompatActivity() {

    lateinit var btnCrearcv: Button
    lateinit var btnModificarcv: Button
    lateinit var btnEliminarcv: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidatos_menu_gestioncv)

        // Obtiene las referencias a los objetos de la interfaz de usuario
        btnCrearcv = findViewById(R.id.btnCrearcv)
        btnModificarcv = findViewById(R.id.btnModificarcv)
        btnEliminarcv = findViewById<Button>(R.id.btnEliminarcv)

        // Obtener la referencia al nodo del CV del usuario actual

        //Listeners botones
        btnCrearcv.setOnClickListener {
            val intent = Intent(this, activity_candidatos_crearcv::class.java)
            startActivity(intent)
        }
        btnModificarcv.setOnClickListener {
            val intent = Intent(this, activity_candidatos_modificarcv::class.java)
            startActivity(intent)
        }

        btnEliminarcv.setOnClickListener {
            eliminarCv()
        }

    }

    // Se aprovecha el código del activity de modificarcv

    private fun eliminarCv() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val database =
                FirebaseDatabase.getInstance("https://job2daympl-default-rtdb.europe-west1.firebasedatabase.app/")
            val nodoCandidatos = database.getReference("Candidatos")
            val nodoUsuario = nodoCandidatos.child(userId)

            nodoUsuario.get().addOnSuccessListener { dataSnapshot ->
                if (dataSnapshot.exists()) {
                    val cvRef = nodoUsuario.child("cv")
                    cvRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                cvRef.removeValue().addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            this@activity_candidatos_menu_gestioncv,
                                            "CV eliminado con éxito.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this@activity_candidatos_menu_gestioncv,
                                    "No tienes ningún CV creado.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Posible error con la BBDD
                            Toast.makeText(
                                this@activity_candidatos_menu_gestioncv,
                                "ERROR ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
        }
    }
}

