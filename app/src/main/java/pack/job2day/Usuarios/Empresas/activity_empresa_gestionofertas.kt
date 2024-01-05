package pack.job2day.Usuarios.Empresas

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import pack.job2day.R

class activity_empresa_gestionofertas : AppCompatActivity() {

    lateinit var btnCrearOferta : Button
    lateinit var btnModOferta : Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_gestionofertas)

        btnCrearOferta = findViewById(R.id.btnCrearOferta)
        btnModOferta = findViewById(R.id.btnListadoOfertas)

        btnCrearOferta.setOnClickListener{
            val intent = Intent (this, activity_empresa_crearoferta::class.java)
            startActivity(intent)
        }

        btnModOferta.setOnClickListener{
            val intent = Intent (this, activity_empresa_modificaroferta::class.java )
            startActivity(intent)
        }

    }
}