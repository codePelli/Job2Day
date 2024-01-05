package pack.job2day.Usuarios.Candidatos

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pack.job2day.MainActivity
import pack.job2day.R

class activity_menu_candidatos : AppCompatActivity() {

    lateinit var btnGestionCv : Button
    lateinit var btnBusquedaEmpleo : Button
    lateinit var btnGestionCandidatura : Button
    lateinit var btnGestionPerfil : Button
    lateinit var btnLogout : Button

    lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_candidatos)

        FirebaseApp.initializeApp(this)
        auth = Firebase.auth
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        Log.d("MyApp", "User ID: $userId")

        // Obtiene las referencias a los objetos de la interfaz de usuario
        btnGestionCv = findViewById(R.id.btnGestionCv)
        btnBusquedaEmpleo = findViewById(R.id.btnBusquedaEmpleo)
        btnGestionCandidatura = findViewById(R.id.btnGestCandid)
        btnGestionPerfil = findViewById(R.id.btnGestPerfil)
        btnLogout = findViewById(R.id.btnLogout)

        //Listeners botones

        btnGestionCv.setOnClickListener{
            val intent = Intent (this, activity_candidatos_menu_gestioncv::class.java)
            startActivity(intent)
        }

        btnBusquedaEmpleo.setOnClickListener{
            val intent = Intent (this, activity_candidatos_busquedaempleo::class.java)
            startActivity(intent)
        }

        btnGestionCandidatura.setOnClickListener{
            val intent = Intent (this, activity_candidatos_gestioncandidaturas::class.java)
            startActivity(intent)
        }

        btnGestionPerfil.setOnClickListener{
            val intent = Intent (this, activity_candidatos_gestionarperfil::class.java )
            startActivity(intent)
        }

        btnLogout.setOnClickListener{
            Firebase.auth.signOut()

            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}