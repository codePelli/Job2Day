package pack.job2day.RegistroUsuarios

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import pack.job2day.R
import pack.job2day.RegistroUsuarios.Candidatos.activity_reg_candidato
import pack.job2day.RegistroUsuarios.Empresas.activity_reg_empresa

class activity_decide_tipo : AppCompatActivity() {

    lateinit var btnActivityRegistroCandidato: Button
    lateinit var btnActivityRegistroEmpresa: Button
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_decide_tipo)

        auth = FirebaseAuth.getInstance()

        btnActivityRegistroCandidato = findViewById(R.id.btn_registroCandidato)
        btnActivityRegistroEmpresa = findViewById(R.id.btn_registroEmpresa)

        //Listeners botones

        btnActivityRegistroCandidato.setOnClickListener {
            val intent = Intent(this, activity_reg_candidato::class.java)
            startActivity(intent)
        }
        btnActivityRegistroEmpresa.setOnClickListener {
            val intent = Intent(this, activity_reg_empresa::class.java)
            startActivity(intent)
        }

    }
}