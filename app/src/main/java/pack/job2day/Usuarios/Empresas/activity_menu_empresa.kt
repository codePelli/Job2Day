package pack.job2day.Usuarios.Empresas

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import pack.job2day.MainActivity
import pack.job2day.R

class activity_menu_empresa : AppCompatActivity() {

    lateinit var btnGestionOfertas : Button
    lateinit var btnGestionCand : Button
    lateinit var BtnGestionPerfil : Button
    lateinit var btnLogout : Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_empresa)

        btnGestionOfertas = findViewById(R.id.btnGestionOfertas)
        btnGestionCand = findViewById(R.id.btnGestionCand)
        BtnGestionPerfil = findViewById(R.id.btnGestionPerfil)
        btnLogout = findViewById(R.id.btnLogout)

        btnGestionOfertas.setOnClickListener{
            val intent = Intent (this, activity_empresa_gestionofertas::class.java)
            startActivity(intent)
        }

        btnGestionCand.setOnClickListener{
            val intent = Intent (this, activity_empresa_gestioncandidaturas::class.java )
            startActivity(intent)
        }
        BtnGestionPerfil.setOnClickListener{
            val intent = Intent (this, activity_empresa_gestionarperfil::class.java )
            startActivity(intent)
        }

        btnLogout.setOnClickListener{
            Firebase.auth.signOut()

            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}