package br.edu.ufrn.android_ecommerce

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpActivity: AppCompatActivity() {

    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonSignUp: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewSignIn: TextView

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()

        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        progressBar = findViewById(R.id.progressbar)
        textViewSignIn = findViewById(R.id.textViewSignIn)

        buttonSignUp.setOnClickListener{
            registerUser()
        }

        textViewSignIn.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    private fun registerUser(){

        val username = editTextName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val password = editTextPassword.text.toString().trim()

        if (username.isEmpty()){
            editTextName.error = "Nome de usuário é necessário"
            editTextName.requestFocus()
            return
        }

        if (email.isEmpty()){
            editTextEmail.error = "E-mail é necessário"
            editTextEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Por favor digite um email válido"
            editTextEmail.requestFocus()
            return
        }

        if (password.isEmpty()){
            editTextPassword.error = "Senha é necessária"
            editTextPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            editTextPassword.error = "A senha deve possuir pelo menos 6 caracteres"
            editTextPassword.requestFocus()
            return
        }

        finishRegister(username, email, password)

    }

    fun finishRegister(username: String, email: String, password: String) {
        progressBar.visibility = View.VISIBLE

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            when {
                task.isSuccessful -> {
                    val user = auth.currentUser

                    val profileUpdates = UserProfileChangeRequest.Builder().setDisplayName(username).build()

                    user!!.updateProfile(profileUpdates).addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            Log.d("UPDATE_PROFILE", "User profile updated.")
                        }
                    }

                    finish()
                }
                task.exception is FirebaseAuthUserCollisionException -> {
                    progressBar.visibility = View.GONE
                    editTextPassword.setText("")
                    Toast.makeText(applicationContext, "E-mail já cadastrado!", Toast.LENGTH_SHORT).show()

                }
                else -> {
                    progressBar.visibility = View.GONE
                    Log.w("createUser:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}