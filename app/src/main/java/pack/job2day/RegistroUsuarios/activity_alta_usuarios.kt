package pack.job2day.RegistroUsuarios

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import pack.job2day.MainActivity
import pack.job2day.R
import java.util.regex.Pattern

class activity_alta_usuarios : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alta_usuarios)

        auth = FirebaseAuth.getInstance()

        email = findViewById(R.id.email_edit_text)
        password = findViewById(R.id.password_edit_text)

        val registerButton: Button = findViewById(R.id.register_button)

        registerButton.setOnClickListener {

            val userEmail = email.text.toString()
            val userPassword = password.text.toString()

            if (userEmail.isEmpty() && userPassword.isEmpty()) {
                showDialog("DEBES INTRODUCIR TUS DATOS")
                return@setOnClickListener
            } else if (userEmail.isEmpty()) {
                showDialog("INTRODUCE EL EMAIL")
                return@setOnClickListener
            } else if (userPassword.isEmpty()) {
                showDialog("INTRODUCE LA CONTRASEÑA")
                return@setOnClickListener
            }

            if (!validacionEmail(userEmail)) {
                showDialog("EMAIL INVÁLIDO")
                return@setOnClickListener
            }

            if (!validacionPassword(userPassword)) {
                showDialog("CONTRASEÑA INVÁLIDA: debe contener 8 caracteres, mínimo una mayúscula y un valor numérico")
                return@setOnClickListener
            }

            // https://firebase.google.com/docs/auth/android/password-auth
            auth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(ContentValues.TAG, "CREAR USUARIO CORRECTO")
                        val user = auth.currentUser
                        updateUI(user)

                        val intent = Intent(this, activity_decide_tipo::class.java)
                        startActivity(intent)

                    } else {
                        Log.w(ContentValues.TAG, "CREAR USUARIO FAIL", task.exception)
                        Toast.makeText(
                            baseContext, "Autenticación fallida.",
                            Toast.LENGTH_SHORT
                        ).show()
                        updateUI(null)
                    }
                }
        }

        val backButton: Button = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User signed in successfully
            Toast.makeText(
                baseContext, "El usuario se ha creado correctamente.",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        } else {
            // User signed out
        }
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

    private fun showDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }


}